package com.shaw.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体
 *
 * @author Administrator
 */
public class Comment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -9057348009667176961L;
    private Integer id; // 编号
    private String userIp; // 用户IP
    private String content; // 评论内容
    private Blog blog; // 被评论的博客
    private Date commentDate; // 评论日期
    private Integer state; // 审核状态 0 待审核 1 审核通过 2 审核未通过

    private String blogTitle;
    private Integer blogId;

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
        if (blog == null) {
            blog = new Blog();
        }
        blog.setId(blogId);
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
        if (blog == null) {
            blog = new Blog();
        }
        blog.setTitle(blogTitle);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


}
