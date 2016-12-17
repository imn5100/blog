package com.shaw.mapper;

import com.shaw.bo.RemoteMsg;
import com.shaw.vo.RemoteMsgQuery;

import java.util.List;

public interface RemoteMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteMsg record);

    int insertSelective(RemoteMsg record);

    RemoteMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteMsg record);

    int updateByPrimaryKey(RemoteMsg record);

    List<RemoteMsg> queryList(RemoteMsgQuery query);

    int queryCount(RemoteMsgQuery query);

    RemoteMsg consumerMsg(String topic);
}