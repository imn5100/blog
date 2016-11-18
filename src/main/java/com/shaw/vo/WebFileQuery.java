package com.shaw.vo;

import com.shaw.util.PageBean;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class WebFileQuery extends PageBean {
    public WebFileQuery() {

    }

    public WebFileQuery(int page, int pageSize) {
        super(page, pageSize);
    }

    private String filename;
    private Byte type;
    private String mimetype;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}
