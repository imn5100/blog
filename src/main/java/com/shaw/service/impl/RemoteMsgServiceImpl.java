package com.shaw.service.impl;

import com.shaw.bo.RemoteMsg;
import com.shaw.mapper.RemoteMsgMapper;
import com.shaw.service.RemoteMsgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by shaw on 2016/12/17 0017.
 */
@Service
public class RemoteMsgServiceImpl implements RemoteMsgService {
    @Resource
    private RemoteMsgMapper remoteMsgMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return remoteMsgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RemoteMsg record) {
        return remoteMsgMapper.insert(record);
    }

    @Override
    public int insertSelective(RemoteMsg record) {
        return remoteMsgMapper.insertSelective(record);
    }

    @Override
    public RemoteMsg selectByPrimaryKey(Integer id) {
        return remoteMsgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RemoteMsg record) {
        return remoteMsgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RemoteMsg record) {
        return remoteMsgMapper.updateByPrimaryKey(record);
    }
}
