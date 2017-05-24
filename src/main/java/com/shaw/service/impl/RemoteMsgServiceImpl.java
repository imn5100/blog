package com.shaw.service.impl;

import com.shaw.bo.RemoteMsg;
import com.shaw.mapper.RemoteMsgMapper;
import com.shaw.service.RemoteMsgService;
import com.shaw.vo.RemoteMsgQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        if (record != null) {
            record.setStatus((short) 1);
            record.setCreateTime(System.currentTimeMillis());
            record.setOpTime(record.getCreateTime());
        }
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
        if (record != null) {
            record.setOpTime(System.currentTimeMillis());
        }
        return remoteMsgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RemoteMsg record) {
        return remoteMsgMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<RemoteMsg> queryList(RemoteMsgQuery query) {
        return remoteMsgMapper.queryList(query);
    }

    @Override
    public int queryCount(RemoteMsgQuery query) {
        return remoteMsgMapper.queryCount(query);
    }

    @Override
    public RemoteMsg consumerMsg(String topic) {
        return remoteMsgMapper.consumerMsg(topic);
    }

    @Override
    public int batchDelete(List<Integer> id) {
        return remoteMsgMapper.batchDelete(id);
    }
}
