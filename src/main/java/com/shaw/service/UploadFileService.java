package com.shaw.service;

import com.shaw.bo.UploadFile;
import com.shaw.vo.WebFileInfoVo;
import com.shaw.util.qiniu.QiNiuUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by shaw on 2016/11/17 0017.
 */
public interface UploadFileService {
    List<WebFileInfoVo> listQiniuWebFile(QiNiuUtils.QiniuFileQuery query) throws Exception;

    UploadFile uploadToQiniu(MultipartFile file, String filename) throws Exception;

    boolean batchDeleteQiniuFile(String[] idsStr) throws Exception;

    String uploadToSMMS(File file) throws Exception;
}
