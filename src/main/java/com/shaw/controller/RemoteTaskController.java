package com.shaw.controller;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.RemoteMsg;
import com.shaw.bo.TaskUser;
import com.shaw.constants.CacheKey;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by shaw on 2017/1/13 0013.
 */
@Controller
@RequestMapping("/remoteTask")
public class RemoteTaskController {
    @Resource
    private TaskUserService taskUserService;
    @Resource
    private RemoteMsgService remoteMsgService;
    //保证socket 服务端的redis序列化工具和 本站相同
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;
    //登录成功
    public static final String LOGIN_SUCCESS_FLAG = "loginSuccess";
    //客户端连接 key
    public static final String USER_CLIENT_CONNECT = "user_client_connect";
    //socket task redis channel
    public static final String SOCKET_TASK_REDIS_CHANNEL = "task_message";
    //socket连接状态
    public static final String SOCKET_CONNECT = "socket_connect";
    //web 登录状态
    public static final String USER_AUTH_KEY = "UserAuthKey:%s";
    //redis 消息解析topic
    public static final String TOPIC_TASK = "socketTask";

    @RequestMapping("/main")
    public ModelAndView remoteTask(String ak, String as, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/foreground/laboratory/remoteTask");
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        request.setAttribute(LOGIN_SUCCESS_FLAG, false);
        if (taskUser != null) {
            request.setAttribute(LOGIN_SUCCESS_FLAG, true);
            setSocketStatus(request, taskUser.getAppkey());
        } else {
            if (!StringUtils.isEmpty(ak) && !StringUtils.isEmpty(as) && ak.length() == 32 && as.length() == 32) {
                taskUser = taskUserService.selectByPrimaryKey(ak);
                if (taskUser != null && taskUser.getAppsecret().equals(as)) {
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
                    setSocketStatus(request, taskUser.getAppkey());
                    taskUser.setShowActiveTime(TimeUtils.getFormatTime(taskUser.getActiveTime()));
                    request.getSession().setAttribute(CacheKey.TASK_USER_AUTH, taskUser);
                    request.setAttribute(LOGIN_SUCCESS_FLAG, true);
                }
            }
        }
        return mav;
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser != null && !StringUtils.isEmpty(taskUser.getAppkey()))
            stringRedisTemplate.delete(String.format(USER_AUTH_KEY, taskUser.getAppkey()));
        request.getSession().removeAttribute(CacheKey.TASK_USER_AUTH);
        request.removeAttribute(LOGIN_SUCCESS_FLAG);
        HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
    }

    @RequestMapping("/addTask")
    public void addTask(@RequestParam(value = "title") String title, @RequestParam(value = "contents") String contents, @RequestParam(value = "type") Integer type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        if (taskUser == null) {
            HttpResponseUtil.writeCode(response, ResponseCode.LOGIN_TIMEOUT);
        } else {
            if (StringUtils.isEmpty(title) || StringUtil.isEmpty(contents) || type == null) {
                HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
            } else {
                //客户端未连接
                if (!stringRedisTemplate.opsForSet().isMember(USER_CLIENT_CONNECT, taskUser.getAppkey())) {
                    HttpResponseUtil.writeCode(response, ResponseCode.SOCKET_NOT_CONNECT);
                    return;
                }
                RemoteMsg remoteMsg = new RemoteMsg();
                if (type == RemoteTaskPermission.DOWNLOAD.getValue()) {
                    if (!((taskUser.getPermissions() & 0x1) == 0x1)) {
                        HttpResponseUtil.writeCode(response, ResponseCode.PERMISSION_WRONG);
                        return;
                    } else {
                        remoteMsg.setTopic(RemoteTaskPermission.DOWNLOAD.getName());
                    }
                } else if (type == RemoteTaskPermission.PYTHON.getValue()) {
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
                remoteMsg.setAppkey(taskUser.getAppkey());
                remoteMsg.setContents(contents);
                remoteMsgService.insert(remoteMsg);
                pubRedisMsg(remoteMsg);
                HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
            }
        }
    }


    //设置 web站登录标示
    private void setWebLoginStatus(TaskUser taskUser) {
        stringRedisTemplate.opsForValue().set(String.format(USER_AUTH_KEY, taskUser.getAppkey()), JSONObject.toJSONString(taskUser));
        stringRedisTemplate.expire(String.format(USER_AUTH_KEY, taskUser.getAppkey()), 20L, TimeUnit.MINUTES);
    }


    //获取socket客户端连接状态
    private boolean setSocketStatus(HttpServletRequest request, String appkey) {
        boolean flag = stringRedisTemplate.opsForSet().isMember(USER_CLIENT_CONNECT, appkey);
        request.setAttribute(SOCKET_CONNECT, flag);
        return flag;
    }


    //向redis发布 socket任务消息
    private void pubRedisMsg(RemoteMsg remoteMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put("topic", TOPIC_TASK);
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setAppKey(remoteMsg.getAppkey());
        socketMessage.setContents(JSONObject.toJSONString(remoteMsg));
        socketMessage.setType(2);
        map.put("content", socketMessage);
        stringRedisTemplate.convertAndSend(SOCKET_TASK_REDIS_CHANNEL, JSONObject.toJSONString(map));
    }

}
