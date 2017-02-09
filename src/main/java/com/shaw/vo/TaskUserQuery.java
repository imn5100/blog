package com.shaw.vo;

import com.shaw.util.PageBean;

/**
 * Created by shaw on 2017/1/14 0014.
 */
public class TaskUserQuery extends PageBean {
    private String name;
    private String appKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
