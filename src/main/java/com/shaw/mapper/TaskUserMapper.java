package com.shaw.mapper;

import com.shaw.bo.TaskUser;

public interface TaskUserMapper {
    int deleteByPrimaryKey(String appkey);

    int insert(TaskUser record);

    int insertSelective(TaskUser record);

    TaskUser selectByPrimaryKey(String appkey);

    int updateByPrimaryKeySelective(TaskUser record);

    int updateByPrimaryKey(TaskUser record);
}