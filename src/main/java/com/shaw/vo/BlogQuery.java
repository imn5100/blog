package com.shaw.vo;

import com.shaw.util.PageBean;

/**
 * Created by shaw on 2017/6/13.
 */
public class BlogQuery extends PageBean {
    private String title;
    private Integer typeId;
    private String releaseDateStr;
    private boolean noContent = false;

    public BlogQuery() {

    }

    public BlogQuery(int page, int pageSize) {
        super(page, pageSize);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDateStr() {
        return releaseDateStr;
    }

    public void setReleaseDateStr(String releaseDateStr) {
        this.releaseDateStr = releaseDateStr;
    }

    public boolean isNoContent() {
        return noContent;
    }

    public void setNoContent(boolean noContent) {
        this.noContent = noContent;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
