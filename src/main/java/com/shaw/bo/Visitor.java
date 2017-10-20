package com.shaw.bo;

import java.io.Serializable;

/**
 * @author imn5100
 */
public class Visitor implements Serializable {
    public static final int OAUTH_FROM_GITHUB = 1;

    private Integer id;

    private String account;

    private String name;

    private String thirdId;

    private String email;

    private Integer oauthFrom;

    private String avatarUrl;

    private Long createTime;

    private Long updateTime;

    private String moreInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOauthFrom() {
        return oauthFrom;
    }

    public void setOauthFrom(Integer oauthFrom) {
        this.oauthFrom = oauthFrom;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getHomePage() {
        if (oauthFrom == Visitor.OAUTH_FROM_GITHUB) {
            return "https://github.com/" + this.getAccount();
        } else {
            return "";
        }
    }
}