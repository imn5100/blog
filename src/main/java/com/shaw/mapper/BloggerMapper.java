package com.shaw.mapper;

import com.shaw.bo.Blogger;

public interface BloggerMapper {

    Blogger find();

    Blogger getByUserName(String userName);

    Integer update(Blogger blogger);
}
