package com.shaw.service;

import com.shaw.bo.UploadFile;
import com.shaw.util.qiniu.QiNiuUtils;
import com.shaw.vo.WebFileInfoVo;
import com.shaw.vo.WebFileQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by shaw on 2016/11/17 0017.
 */
public interface UploadFileService {
    List<WebFileInfoVo> listQiniuWebFile(QiNiuUtils.QiniuFileQuery query) throws Exception;

    UploadFile uploadToQiniu(MultipartFile file, String filename) throws Exception;

    boolean batchDeleteQiniuFile(String[] idsStr) throws Exception;

    UploadFile uploadToSMMS(MultipartFile file) throws Exception;

    List<UploadFile> queryList(WebFileQuery query);

    Integer countList(WebFileQuery query);

    Integer batchDelete(List<Integer> ids);

    Integer updateValid(List<Integer> ids, boolean valid);
}
