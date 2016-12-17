package com.shaw.vo;

import com.shaw.util.PageBean;

/**
 * Created by shaw on 2016/12/17 0017.
 */
public class RemoteMsgQuery extends PageBean {
    private String topic;
    private Short status;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

}
