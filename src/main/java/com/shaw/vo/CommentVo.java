package com.shaw.vo;

import com.shaw.bo.Discuss;
import com.shaw.bo.Visitor;
import com.shaw.util.TimeUtils;

public class CommentVo {
    private int userId;
    private String content;
    private String name;
    private String account;
    private String homePage;
    private String avatarUrl;
    private String discussTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDiscussTime() {
        return discussTime;
    }

    public void setDiscussTime(String discussTime) {
        this.discussTime = discussTime;
    }

    public static CommentVo build(Discuss discuss, Visitor visitor) {
        CommentVo commentVo = new CommentVo();
        commentVo.setUserId(visitor.getId());
        commentVo.setName(visitor.getName());
        commentVo.setAccount(visitor.getAccount());
        commentVo.setAvatarUrl(visitor.getAvatarUrl());
        commentVo.setHomePage(visitor.getHomePage());

        commentVo.setDiscussTime(TimeUtils.getFormatTime(discuss.getCreateTime()));
        commentVo.setContent(discuss.getContent());
        return commentVo;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
}
