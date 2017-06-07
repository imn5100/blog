package com.shaw.util;

import java.io.Serializable;

public class PageBean implements Serializable {

    private static final long serialVersionUID = 4146055498237132023L;
    private int page = 1; // 第几页
    private int pageSize = 15; // 每页记录数

    public PageBean() {
        
    }

    public PageBean(int page, int pageSize) {
        super();
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return page > 0 ? (page - 1) * pageSize : 0;
    }

}
