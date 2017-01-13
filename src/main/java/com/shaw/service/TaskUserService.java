package com.shaw.service;

import com.shaw.bo.TaskUser;

public interface TaskUserService {
    int deleteByPrimaryKey(String appkey);

    int insert(TaskUser record);

    int insertSelective(TaskUser record);

    TaskUser selectByPrimaryKey(String appkey);

    int updateByPrimaryKeySelective(TaskUser record);

    int updateByPrimaryKey(TaskUser record);
}