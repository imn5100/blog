package com.shaw.vo;


import com.qiniu.storage.model.FileInfo;
import com.shaw.constants.Constants;
import com.shaw.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaw on 2016/11/15 0015.
 */
public class WebFileInfoVo {
    private String key;
    private String hash;
    private String fsize;
    private String putTime;
    private String mimeType;
    private String endUser;
    private String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFsize() {
        return fsize;
    }

    public void setFsize(String fsize) {
        this.fsize = fsize;
    }

    public String getPutTime() {
        return putTime;
    }

    public void setPutTime(String putTime) {
        this.putTime = putTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public static WebFileInfoVo convert(FileInfo fileInfo) {
        WebFileInfoVo vo = new WebFileInfoVo();
        if (fileInfo == null) {
            return null;
        }
        vo.setHash(fileInfo.hash);
        vo.setEndUser(fileInfo.endUser);
        vo.setFsize((fileInfo.fsize / 1024) + "kb");
        vo.setKey(fileInfo.key);
        vo.setMimeType(fileInfo.mimeType);
        vo.setPutTime(TimeUtils.getFormatTime(fileInfo.putTime / 10000));
        vo.setUrl(Constants.QINIU_BASE_URL + fileInfo.key);
        return vo;
    }

    public static List<WebFileInfoVo> convertList(List<FileInfo> fileInfoList) {
        if (fileInfoList == null || fileInfoList.size() == 0) {
            return null;
        } else {
            List<WebFileInfoVo> list = new ArrayList<WebFileInfoVo>();
            for (FileInfo fileInfo : fileInfoList) {
                WebFileInfoVo vo = convert(fileInfo);
                if (vo != null) {
                    list.add(vo);
                }
            }
            return list;
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
