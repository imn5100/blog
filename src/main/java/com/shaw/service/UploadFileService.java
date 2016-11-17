package com.shaw.service;

import com.shaw.WebFileInfoVo;
import com.shaw.util.qiniu.QiNiuUtils;
import com.sun.tools.javac.util.List;

import java.io.File;

/**
 * Created by shaw on 2016/11/17 0017.
 */
public interface UploadFileService {
    List<WebFileInfoVo> listQiniuWebFile(QiNiuUtils.QiniuFileQuery query);

    String uploadToQiniu(byte[] file, String filename);

    boolean deleteQiniuFileBatch(String[] idsStr);

    String uploadToSMMS(File file);
}
