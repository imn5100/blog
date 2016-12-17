package com.shaw.service;

import com.shaw.bo.RemoteMsg;

/**
 * Created by shaw on 2016/12/17 0017.
 */
public interface RemoteMsgService {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteMsg record);

    int insertSelective(RemoteMsg record);

    RemoteMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteMsg record);

    int updateByPrimaryKey(RemoteMsg record);
}
