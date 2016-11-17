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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shaw on 2016/11/17 0017.
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Resource
    private UploadFileMapper uploadFileMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public List<WebFileInfoVo> listQiniuWebFile(QiNiuUtils.QiniuFileQuery query) {
        List<FileInfo> fileInfoList = QiNiuUtils.listFile(query);
        List<WebFileInfoVo> list = WebFileInfoVo.convertList(fileInfoList);
        return list;
    }

    @Override
    public UploadFile uploadToQiniu(MultipartFile file, String filename) throws Exception {
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
    }

    @Override
    public boolean batchDeleteQiniuFile(String[] idsStr) {
        if (QiNiuUtils.batchDelete(null, idsStr)) {
            List<String> storenameList = Arrays.asList(idsStr);
            uploadFileMapper.deleteQiniuByKey(storenameList);
            return true;
        }
        return false;
    }

    @Override
    public UploadFile uploadToSMMS(MultipartFile mfile) throws Exception {
        File file = new File(mfile.getOriginalFilename());
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
}
