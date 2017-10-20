package com.shaw.controller;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.RemoteMsg;
import com.shaw.bo.TaskUser;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.service.RemoteMsgService;
import com.shaw.service.TaskUserService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.util.TimeUtils;
import com.shaw.vo.RemoteTaskPermission;
import com.shaw.vo.SocketMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author shaw
 * @date 2017/1/13 0013
 * 远程任务发送
 */
@Controller
@RequestMapping("/remoteTask")
public class RemoteTaskController {
    @Resource
    private TaskUserService taskUserService;
    @Resource
    private RemoteMsgService remoteMsgService;
    /**
     * 保证socket 服务端的redis序列化工具和 本站相同
     */
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 和socket server 相关的常量。
     */
    /**
     * web登录成功
     */
    public static final String LOGIN_SUCCESS_FLAG = "loginSuccess";
    /**
     * 客户端连接 key
     */
    public static final String USER_CLIENT_CONNECT = "user_client_connect";
    /**
     * socket task redis channel
     */
    public static final String SOCKET_TASK_REDIS_CHANNEL = "task_message";
    /**
     * socket连接状态key
     */
    public static final String SOCKET_CONNECT = "socket_connect";
    /**
     * web 登录状态key
     */
    public static final String USER_AUTH_KEY = "UserAuthKey:%s";
    /**
     * 接收socket推送任务消息的二层topic
     */
    public static final String TOPIC_TASK = "socketTask";

    /**
     * 主页及登录。如果是登录状态访问，则进入任务发送界面否则进入登录页面
     */
    @RequestMapping("/main")
    public ModelAndView remoteTask(String ak, String as, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/foreground/laboratory/remoteTask");
        request.setAttribute(LOGIN_SUCCESS_FLAG, false);
        if (!checkLogin(request)) {
            if (!StringUtils.isEmpty(ak) && !StringUtils.isEmpty(as)) {
                TaskUser taskUser = taskUserService.selectByPrimaryKey(ak);
                if (taskUser != null && taskUser.getAppSecret().equals(as)) {
                    setWebLoginStatus(taskUser);
                    taskUser.setActiveTime(System.currentTimeMillis());
                    //  0000 0000   0位为1，下载权限 1位为1 python脚本执行权限
                    List<RemoteTaskPermission> list = new ArrayList<>();
                    if ((taskUser.getPermissions() & 0x1) == 0x1) {
                        list.add(RemoteTaskPermission.DOWNLOAD);
                    }
                    if ((taskUser.getPermissions() & 0x2) == 0x2) {
                        list.add(RemoteTaskPermission.PYTHON);
                    }
                    taskUser.setRemoteTaskPermissionList(list);
                    taskUserService.updateByPrimaryKeySelective(taskUser);
                    setSocketStatus(request, taskUser.getAppKey());
                    taskUser.setShowActiveTime(TimeUtils.getFormatTime(taskUser.getActiveTime()));
                    request.getSession().setAttribute(CacheKey.TASK_USER_AUTH, taskUser);
                    request.setAttribute(LOGIN_SUCCESS_FLAG, true);
                }
            }
        }
        return mav;
    }

    /**
     * 退出登录
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser != null && !StringUtils.isEmpty(taskUser.getAppKey()))
            stringRedisTemplate.delete(String.format(USER_AUTH_KEY, taskUser.getAppKey()));
        request.getSession().removeAttribute(CacheKey.TASK_USER_AUTH);
        request.removeAttribute(LOGIN_SUCCESS_FLAG);
        HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
    }

    //quit表示退出，不进行记录入数据库
    public static final String QUIT = "quit";

    /**
     * 添加任务
     */
    @RequestMapping("/addTask")
    public void addTask(@RequestParam(value = "title") String title, @RequestParam(value = "contents") String contents, @RequestParam(value = "type") Integer type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser == null || !checkLogin(request)) {
            HttpResponseUtil.writeCode(response, ResponseCode.LOGIN_TIMEOUT);
        } else {
            if (StringUtils.isEmpty(title) || StringUtil.isEmpty(contents) || type == null) {
                HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
            } else {
                //客户端未连接
                if (!stringRedisTemplate.opsForSet().isMember(USER_CLIENT_CONNECT, taskUser.getAppKey())) {
                    HttpResponseUtil.writeCode(response, ResponseCode.SOCKET_NOT_CONNECT);
                    return;
                }
                RemoteMsg remoteMsg = new RemoteMsg();
                if (Objects.equals(type, RemoteTaskPermission.DOWNLOAD.getValue())) {
                    if (!((taskUser.getPermissions() & 0x1) == 0x1)) {
                        HttpResponseUtil.writeCode(response, ResponseCode.PERMISSION_WRONG);
                        return;
                    } else {
                        remoteMsg.setTopic(RemoteTaskPermission.DOWNLOAD.getName());
                    }
                } else if (Objects.equals(type, RemoteTaskPermission.PYTHON.getValue())) {
                    if (!((taskUser.getPermissions() & 0x2) == 0x2)) {
                        HttpResponseUtil.writeCode(response, ResponseCode.PERMISSION_WRONG);
                        return;
                    } else {
                        remoteMsg.setTopic(RemoteTaskPermission.PYTHON.getName());
                    }
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.PERMISSION_WRONG);
                    return;
                }
                remoteMsg.setAppKey(taskUser.getAppKey());
                remoteMsg.setContents(contents);
                //对于退出命令等操作，不记录在数据库中
                if (!remoteMsg.getContents().equalsIgnoreCase(QUIT)) {
                    remoteMsgService.insert(remoteMsg);
                }
                pubRedisMsg(remoteMsg);
                HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
            }
        }
    }

    /**
     * 更新远程任务用户基本信息 ，只允许更新名字和描述
     */
    @RequestMapping("/update")
    public void updateTaskUser(@RequestParam(value = "name") String name, @RequestParam(value = "intr") String intr, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser == null || !checkLogin(request)) {
            HttpResponseUtil.writeCode(response, ResponseCode.LOGIN_TIMEOUT);
        } else {
            if (StringUtils.isEmpty(name) || StringUtil.isEmpty(intr)) {
                HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
            } else {
                //检查数据是否需要修改，如果不需要直接返回成功
                boolean update = false;
                if (!name.equals(taskUser.getName())) {
                    taskUser.setName(name);
                    update = true;
                }
                if (!intr.equals(taskUser.getIntr())) {
                    taskUser.setIntr(intr);
                    update = true;
                }
                if (update) {
                    if (taskUserService.updateByPrimaryKeySelective(taskUser) > 0) {
                        //更新session中的数据
                        request.getSession().setAttribute(CacheKey.TASK_USER_AUTH, taskUser);
                        HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
                    } else {
                        HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
                    }
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
                }
            }
        }
    }

    /**
     * 检查登录状态，并更新超时时间
     */
    private boolean checkLogin(HttpServletRequest request) {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser == null) {
            return false;
        } else {
            String userRedisKey = String.format(USER_AUTH_KEY, taskUser.getAppKey());
            long expireTime = stringRedisTemplate.getExpire(userRedisKey, TimeUnit.SECONDS);
            if (expireTime > 0) {
                request.setAttribute(LOGIN_SUCCESS_FLAG, true);
                setSocketStatus(request, taskUser.getAppKey());
                if (expireTime < 60 * 10) {
                    stringRedisTemplate.expire(userRedisKey, Constants.SESSION_TIME, TimeUnit.SECONDS);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 设置 web站登录标示
     */
    private void setWebLoginStatus(TaskUser taskUser) {
        stringRedisTemplate.opsForValue().set(String.format(USER_AUTH_KEY, taskUser.getAppKey()), JSONObject.toJSONString(taskUser));
        stringRedisTemplate.expire(String.format(USER_AUTH_KEY, taskUser.getAppKey()), Constants.SESSION_TIME, TimeUnit.SECONDS);
    }


    /**
     * 获取socket客户端连接状态
     */
    private boolean setSocketStatus(HttpServletRequest request, String appkey) {
        boolean flag = stringRedisTemplate.opsForSet().isMember(USER_CLIENT_CONNECT, appkey);
        request.setAttribute(SOCKET_CONNECT, flag);
        return flag;
    }


    /**
     * 向redis发布 socket任务消息
     */
    private void pubRedisMsg(RemoteMsg remoteMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put("topic", TOPIC_TASK);
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setAppKey(remoteMsg.getAppKey());
        socketMessage.setContents(JSONObject.toJSONString(remoteMsg));
        socketMessage.setType(2);
        map.put("content", socketMessage);
        stringRedisTemplate.convertAndSend(SOCKET_TASK_REDIS_CHANNEL, JSONObject.toJSONString(map));
    }

}
