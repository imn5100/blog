package com.shaw.service;

import com.shaw.bo.Blogger;

public interface BloggerService {

    Blogger find();

    Blogger getByUserName(String userName);

    Integer update(Blogger blogger);
}
