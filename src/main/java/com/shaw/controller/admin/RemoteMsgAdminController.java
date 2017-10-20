package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.annotation.IpPassport;
import com.shaw.bo.RemoteMsg;
import com.shaw.bo.TaskUser;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.service.RemoteMsgService;
import com.shaw.service.TaskUserService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.MD5Util;
import com.shaw.util.StringUtil;
import com.shaw.vo.RemoteMsgQuery;
import com.shaw.vo.TaskUserQuery;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author shaw
 * @date 2016/12/17 0017
 */
@Controller
@RequestMapping("/admin/remote")
public class RemoteMsgAdminController {
    @Autowired
    private RemoteMsgService remoteMsgService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TaskUserService taskUserService;
    Logger logger = LoggerFactory.getLogger(RemoteMsgAdminController.class);

    /**
     * 列出远程任务 (管理员页面使用 easyUi格式)
     */
    @RequestMapping("/listMsg")
    public void listMsg(RemoteMsgQuery query, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (query != null) {
            if (StringUtil.isEmpty(query.getTopic())) {
                query.setTopic(null);
            }
            List<RemoteMsg> list = remoteMsgService.queryList(query);
            Integer count = remoteMsgService.queryCount(query);
            result.put("success", true);
            result.put("rows", list);
            result.put("total", count);
        }
        HttpResponseUtil.writeJsonStr(response, result);
    }

    /**
     * 添加任务调用白名单
     */
    @RequestMapping("/addWhiteList")
    public void addWhiteList(String ip, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(ip)) {
            redisClient.sadd(CacheKey.WHITE_LIST_IP, ip);
            HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
            logger.info("add white list: " + ip);
            return;
        } else {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    /**
     * 移除任务调用白名单
     */
    @RequestMapping("/removeWhiteList")
    public void removeWhiteList(String ip, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(ip)) {
            redisClient.srem(CacheKey.WHITE_LIST_IP, ip);
            HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
            logger.info("remove white list: " + ip);
            return;
        } else {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    /**
     * 白名单列表
     */
    @RequestMapping("/whiteList")
    public void getWhiteList(HttpServletResponse response) throws Exception {
        Set<Object> wipList = redisClient.smembers(CacheKey.WHITE_LIST_IP);
        HttpResponseUtil.writeUseData(response, wipList, ResponseCode.SUCCESS);
    }

    /**
     * 推送|插入消息
     */
    @RequestMapping("/pushMsg")
    public void pushMsg(String contents, String topic, String other, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(contents) && !StringUtils.isBlank(topic)) {
            RemoteMsg remoteMsg = new RemoteMsg();
            remoteMsg.setContents(contents);
            remoteMsg.setTopic(topic);
            if (other != null)
                remoteMsg.setOther(other);
            if (remoteMsgService.insert(remoteMsg) > 0) {
                HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
            } else {
                HttpResponseUtil.writeJsonStr(response, ResponseCode.FAIL.getCode());
            }
        } else {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    /**
     * 更新消息
     */
    @RequestMapping("/updateMsg")
    public void updateMsg(RemoteMsg remoteMsg, HttpServletResponse response) throws Exception {
        if (remoteMsg != null && remoteMsg.getId() != null && StringUtil.isNotEmpty(remoteMsg.getContents()) && StringUtil.isNotEmpty(remoteMsg.getTopic())) {
            if (remoteMsgService.updateByPrimaryKeySelective(remoteMsg) > 0) {
                HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
            } else {
                HttpResponseUtil.writeJsonStr(response, ResponseCode.FAIL.getCode());
            }
        } else {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    /**
     * 批量删除消息
     */
    @RequestMapping("/batchDelete")
    public void batchDelete(String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        List<Integer> idList = StringUtil.parseToIntList(ids);
        if (idList.size() == 0) {
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
            return;
        } else {
            Integer count = remoteMsgService.batchDelete(idList);
            result.put("count", count);
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
        }
    }

    //////////////////////////////////////////////////////////socket远程任务用户维护接口

    /**
     * socket 远程任务 ，列出用户
     */
    @RequestMapping("/listUser")
    public void listUser(TaskUserQuery query, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (query != null) {
            if (StringUtil.isEmpty(query.getAppKey())) {
                query.setAppKey(null);
            }
            if (StringUtil.isEmpty(query.getName())) {
                query.setName(null);
            }
            List<TaskUser> list = taskUserService.queryList(query);
            Integer count = taskUserService.queryCount(query);
            result.put("success", true);
            result.put("rows", list);
            result.put("total", count);
        }
        HttpResponseUtil.writeJsonStr(response, result);
    }

    /**
     * 添加用户，并生成相关ak as
     */
    @RequestMapping("/addUser")
    public void addUser(TaskUser taskUser, String salt, HttpServletResponse response) throws Exception {
        if (taskUser != null) {
            if (StringUtil.isEmpty(taskUser.getName()) || StringUtil.isEmpty(salt) || taskUser.getPermissions() == null) {
                HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
            } else {
                String appkey = MD5Util.MD5(taskUser.getName() + salt);
                if (taskUserService.selectByPrimaryKey(appkey) != null) {
                    HttpResponseUtil.writeCode(response, ResponseCode.USER_REPEAT);
                    return;
                }
                String appsecret = MD5Util.MD5(salt + appkey);
                taskUser.setAppKey(appkey);
                taskUser.setAppSecret(appsecret);
                if (taskUserService.insert(taskUser) > 0) {
                    HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
                }
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
        }
    }

    /**
     * 删除socket远程任务用户
     */
    @RequestMapping("/deleteUser")
    public void deleteUser(String appkey, HttpServletResponse response) throws Exception {
        if (!StringUtil.isEmpty(appkey)) {
            if (taskUserService.deleteByPrimaryKey(appkey) > 0) {
                HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
            } else {
                HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
        }
    }

    /**
     * 更新远程任务用户
     */
    @RequestMapping("/updateUser")
    public void updateUser(TaskUser taskUser, HttpServletResponse response) throws Exception {
        if (taskUser != null && StringUtil.isNotEmpty(taskUser.getAppKey()) &&
                taskUser.getPermissions() != null &&
                taskUser.getAppKey().length() == 32) {
            if (taskUserService.updateByPrimaryKeySelective(taskUser) > 0) {
                HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
            } else {
                HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
        }

    }


    /////内部接口完毕，以下为对外接口///////////////////

    /**
     * 拉取任务消息接口，需要登录且验证ip
     */
    @IpPassport
    @RequestMapping("/script/consumerMsg")
    public void consumerMsg(String topic, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(topic)) {
            try {
                RemoteMsg remoteMsg = remoteMsgService.consumerMsg(topic);
                if (remoteMsg == null) {
                    HttpResponseUtil.writeCode(response, ResponseCode.FIND_NULL);
                } else {
                    remoteMsg.setStatus(Constants.MSG_START);
                    remoteMsgService.updateByPrimaryKeySelective(remoteMsg);
                    logger.info("consumerMsg:" + remoteMsg);
                    HttpResponseUtil.writeUseData(response, remoteMsg, ResponseCode.SUCCESS);
                }
            } catch (Exception e) {
                HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
            }
        } else {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    /**
     * 任务执行后的回调，用于更新任务状态
     */
    @IpPassport
    @RequestMapping("/script/callbackMsg")
    public void callBackMsg(Integer id, @RequestParam(value = "type", required = false, defaultValue = "0") Integer type, HttpServletResponse response) throws Exception {
        if (id != null) {
            RemoteMsg remoteMsg = remoteMsgService.selectByPrimaryKey(id);
            if (remoteMsg != null) {
                if (!Objects.equals(remoteMsg.getStatus(), Constants.MSG_OVER)) {
                    switch (type) {
                        case 1:
                            //重新启用任务
                            remoteMsg.setStatus(Constants.MSG_REUSE);
                        case 2:
                            //开启任务
                            remoteMsg.setStatus(Constants.MSG_START);
                            break;
                        case 3:
                            //完成任务
                            remoteMsg.setStatus(Constants.MSG_OVER);
                            break;
                        case 4:
                            //任务失败
                            remoteMsg.setStatus(Constants.MSG_FAIL);
                            break;
                        default:
                            //默认开启任务
                            remoteMsg.setStatus(Constants.MSG_START);
                            break;
                    }
                    logger.info("consumerMsg id:" + id + " type:" + type);
                    if (remoteMsgService.updateByPrimaryKeySelective(remoteMsg) > 0) {
                        HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
                    } else {
                        HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
                    }
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.MSG_OVER);
                }
            } else {
                HttpResponseUtil.writeCode(response, ResponseCode.ID_WRONG);
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
        }
    }
}
