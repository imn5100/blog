package com.shaw.bo;

import com.shaw.vo.RemoteTaskPermission;

import java.io.Serializable;
import java.util.List;

public class TaskUser implements Serializable {
    private String appkey;

    private String appsecret;

    private Byte permissions;

    private String name;

    private String intr;

    private Long activeTime;

    private String showActiveTime;

    private List<RemoteTaskPermission> remoteTaskPermissionList;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey == null ? null : appkey.trim();
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret == null ? null : appsecret.trim();
    }

    public Byte getPermissions() {
        return permissions;
    }

    public void setPermissions(Byte permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIntr() {
        return intr;
    }

    public void setIntr(String intr) {
        this.intr = intr == null ? null : intr.trim();
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public String getShowActiveTime() {
        return showActiveTime;
    }

    public void setShowActiveTime(String showActiveTime) {
        this.showActiveTime = showActiveTime;
    }

    public List<RemoteTaskPermission> getRemoteTaskPermissionList() {
        return remoteTaskPermissionList;
    }

    public void setRemoteTaskPermissionList(List<RemoteTaskPermission> remoteTaskPermissionList) {
        this.remoteTaskPermissionList = remoteTaskPermissionList;
    }
}