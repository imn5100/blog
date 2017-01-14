package com.shaw.service;

import com.shaw.bo.TaskUser;
import com.shaw.vo.TaskUserQuery;

import java.util.List;

public interface TaskUserService {
    int deleteByPrimaryKey(String appkey);

    int insert(TaskUser record);

    int insertSelective(TaskUser record);

    TaskUser selectByPrimaryKey(String appkey);

    int updateByPrimaryKeySelective(TaskUser record);

    int updateByPrimaryKey(TaskUser record);

    List<TaskUser> queryList(TaskUserQuery query);

    Integer queryCount(TaskUserQuery query);
}