package com.shaw.vo;

import com.shaw.util.PageBean;

/**
 * Created by shaw on 2017/1/14 0014.
 */
public class TaskUserQuery extends PageBean {
    private String name;
    private String appkey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
