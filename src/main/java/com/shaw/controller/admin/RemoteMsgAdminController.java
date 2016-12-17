package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.RemoteMsg;
import com.shaw.bo.UploadFile;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.service.RemoteMsgService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.vo.RemoteMsgQuery;
import com.shaw.vo.WebFileQuery;
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
        HttpResponseUtil.write(response, result);
    }

    @RequestMapping("/addWhiteList")
    public void addWhiteList(String ip, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(ip)) {
            redisClient.sadd(CacheKey.WHITE_LIST_IP, ip);
            HttpResponseUtil.write(response, ResponseCode.SUCCESS.getCode());
            logger.info("add white list: " + ip);
            return;
        } else {
            HttpResponseUtil.write(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
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
                HttpResponseUtil.write(response, ResponseCode.SUCCESS.getCode());
            } else {
                HttpResponseUtil.write(response, ResponseCode.FAIL.getCode());
            }
        } else {
            HttpResponseUtil.write(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    @RequestMapping("/consumerMsg")
    public void consumerMsg(String topic, HttpServletResponse response) throws Exception {
        if (!StringUtils.isBlank(topic)) {
            RemoteMsg remoteMsg = remoteMsgService.consumerMsg(topic);
            remoteMsg.setStatus(Constants.MSG_START);
            remoteMsgService.updateByPrimaryKeySelective(remoteMsg);
            String result = JSONObject.toJSONString(remoteMsg);
            logger.info("consumerMsg:" + result);
            HttpResponseUtil.write(response, result);
        } else {
            HttpResponseUtil.write(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
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
                        HttpResponseUtil.write(response, ResponseCode.SUCCESS.getCode());
                    } else {
                        HttpResponseUtil.write(response, ResponseCode.FAIL.getCode());
                    }
                } else {
                    HttpResponseUtil.write(response, ResponseCode.MSG_OVER.getCode());
                }
            } else {
                HttpResponseUtil.write(response, ResponseCode.ID_WRONG.getCode());
            }
        } else {
            HttpResponseUtil.write(response, ResponseCode.PARAM_NOT_FORMAT.getCode());
        }
    }

    @RequestMapping("/batchDelete")
    public void batchDelete(String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        List<Integer> idList = StringUtil.parseToIntList(ids);
        if (idList.size() == 0) {
            result.put("success", true);
            HttpResponseUtil.write(response, result);
            return;
        } else {
            Integer count = remoteMsgService.batchDelete(idList);
            result.put("count", count);
            result.put("success", true);
            HttpResponseUtil.write(response, result);
        }
    }


}
