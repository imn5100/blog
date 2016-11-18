package com.shaw.mapper;

import com.shaw.bo.UploadFile;
import com.shaw.vo.WebFileQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UploadFile record);

    int insertSelective(UploadFile record);

    UploadFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UploadFile record);

    int updateByPrimaryKey(UploadFile record);

    int deleteQiniuByKey(List<String> key);

    List<UploadFile> queryList(WebFileQuery query);

    Integer countList(WebFileQuery query);

    Integer batchDelete(List<Integer> ids);

    Integer updateValid(@Param("list") List<Integer> ids, @Param("valid") boolean valid);
}