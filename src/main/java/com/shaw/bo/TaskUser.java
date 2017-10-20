package com.shaw.bo;

import com.shaw.vo.RemoteTaskPermission;

import java.io.Serializable;
import java.util.List;

/**
 * @author imn5100
 */
public class TaskUser implements Serializable {
    private String appKey;

    private String appSecret;

    private Byte permissions;

    private String name;

    private String intr;

    private Long activeTime;

    private String showActiveTime;

    private List<RemoteTaskPermission> remoteTaskPermissionList;

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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}