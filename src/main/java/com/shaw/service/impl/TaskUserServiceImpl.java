package com.shaw.service.impl;

import com.shaw.bo.TaskUser;
import com.shaw.mapper.TaskUserMapper;
import com.shaw.service.TaskUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by shaw 2017/1/13 0013.
 */
@Service
public class TaskUserServiceImpl implements TaskUserService {
    @Resource
    private TaskUserMapper taskUserMapper;

    @Override
    public int deleteByPrimaryKey(String appkey) {
        return taskUserMapper.deleteByPrimaryKey(appkey);
    }

    @Override
    public int insert(TaskUser record) {
        return taskUserMapper.insert(record);
    }

    @Override
    public int insertSelective(TaskUser record) {
        return taskUserMapper.insertSelective(record);
    }

    @Override
    public TaskUser selectByPrimaryKey(String appkey) {
        return taskUserMapper.selectByPrimaryKey(appkey);
    }

    @Override
    public int updateByPrimaryKeySelective(TaskUser record) {
        return taskUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TaskUser record) {
        return taskUserMapper.updateByPrimaryKey(record);
    }
}
