package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.RemoteMsg;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.service.RemoteMsgService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.vo.RemoteMsgQuery;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by shaw on 2016/12/17 0017.
 */
@Controller
@RequestMapping("/admin/remote")
public class RemoteMsgAdminController {
    @Autowired
    private RemoteMsgService remoteMsgService;
    @Autowired
    private RedisClient redisClient;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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

    /////内部接口完毕，以下为对外接口///////////////////

    /**
     *
     * */
    @RequestMapping("/consumerMsg")
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

    @RequestMapping("/callBackMsg")
    public void callBackMsg(Integer id, @RequestParam(value = "type", required = false, defaultValue = "0") Integer type, HttpServletResponse response) throws Exception {
        if (id != null) {
            RemoteMsg remoteMsg = remoteMsgService.selectByPrimaryKey(id);
            if (remoteMsg != null) {
                if (remoteMsg.getStatus() != Constants.MSG_OVER) {
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
