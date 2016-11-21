package com.shaw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.storage.model.FileInfo;
import com.shaw.bo.UploadFile;
import com.shaw.constants.Constants;
import com.shaw.mapper.UploadFileMapper;
import com.shaw.service.UploadFileService;
import com.shaw.util.StringUtil;
import com.shaw.util.qiniu.QiNiuUtils;
import com.shaw.util.smms.SMMSUtils;
import com.shaw.vo.SMMSUploadResponseVo;
import com.shaw.vo.WebFileInfoVo;
import com.shaw.vo.WebFileQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Resource
    private UploadFileMapper uploadFileMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public List<WebFileInfoVo> listQiniuWebFile(QiNiuUtils.QiniuFileQuery query) {
        try {
            List<FileInfo> fileInfoList = QiNiuUtils.listFile(query);
            List<WebFileInfoVo> list = WebFileInfoVo.convertList(fileInfoList);
            return list;
        } catch (Exception e) {
            logger.error("select Qiniu file fail Execption:" + e.getMessage());
            return null;
        }
    }

    @Override
    public UploadFile uploadToQiniu(MultipartFile file, String filename) {
        try {
            String responseBody = QiNiuUtils.upload(file.getBytes(), filename);
            if (StringUtil.isNotEmpty(responseBody)) {
                JSONObject responseObj = JSONObject.parseObject(responseBody);
                String key = responseObj.getString("key");
                String hash = responseObj.getString("hash");
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFilename(filename);
                uploadFile.setUrl(Constants.QINIU_BASE_URL + key);
                uploadFile.setType((byte) 1);
                uploadFile.setSize(file.getSize());
                uploadFile.setStorename(key);
                uploadFile.setPath(key);
                uploadFile.setUploadTime(System.currentTimeMillis());
                uploadFile.setHash(hash);
                String suffix = StringUtil.getSuffix(file.getOriginalFilename());
                if (suffix != null) {
                    uploadFile.setMimetype(suffix);
                } else {
                    uploadFile.setMimetype(file.getContentType());
                }
                uploadFile.setIsValid((byte) 1);
                uploadFileMapper.insert(uploadFile);
                return uploadFile;
            }
            return null;
        } catch (Exception e) {
            logger.error("uploadToQiniu  fail Execption:" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean batchDeleteQiniuFile(String[] idsStr) {
        try {
            if (QiNiuUtils.batchDelete(null, idsStr)) {
                List<String> storenameList = Arrays.asList(idsStr);
                uploadFileMapper.deleteQiniuByKey(storenameList);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("batchDeleteQiniuFile  fail Execption:" + e.getMessage());
            return false;
        }
    }

    @Override
    public UploadFile uploadToSMMS(MultipartFile mfile) throws Exception {
        File file = new File("tmp" + mfile.getOriginalFilename());
        try {
            mfile.transferTo(file);
            String response = SMMSUtils.uploadFile(file);
            if (response != null) {
                SMMSUploadResponseVo vo = JSONObject.parseObject(response, SMMSUploadResponseVo.class);
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFilename(mfile.getOriginalFilename());
                uploadFile.setUrl(vo.getData().getUrl());
                uploadFile.setType((byte) 2);
                uploadFile.setSize(vo.getData().getSize());
                uploadFile.setStorename(vo.getData().getStorename());
                uploadFile.setPath(vo.getData().getPath());
                uploadFile.setUploadTime(System.currentTimeMillis());
                uploadFile.setHash(vo.getData().getHash());
                uploadFile.setDetail(vo.getData().getDetail());
                String suffix = StringUtil.getSuffix(mfile.getOriginalFilename());
                if (suffix != null) {
                    uploadFile.setMimetype(suffix);
                } else {
                    uploadFile.setMimetype(mfile.getContentType());
                }
                uploadFile.setIsValid((byte) 1);
                uploadFileMapper.insert(uploadFile);
                return uploadFile;
            }
        } catch (Exception e) {
            logger.error("Upload SMMS file fail Exception:" + e.getMessage());
            return null;
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
        return null;
    }

    @Override
    public List<UploadFile> queryList(WebFileQuery query) {
        return this.uploadFileMapper.queryList(query);
    }

    @Override
    public Integer countList(WebFileQuery query) {
        return this.uploadFileMapper.countList(query);
    }

    @Override
    public Integer batchDelete(List<Integer> ids) {
        return uploadFileMapper.batchDelete(ids);
    }

    @Override
    public Integer updateValid(List<Integer> ids, boolean valid) {
        return uploadFileMapper.updateValid(ids, valid);
    }
}
