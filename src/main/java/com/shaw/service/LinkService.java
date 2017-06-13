package com.shaw.service;

import com.shaw.bo.Link;
import com.shaw.util.PageBean;

import java.util.List;
import java.util.Map;


public interface LinkService {
    int add(Link link);

    int update(Link link);

    List<Link> list(PageBean pageBean);

    Long getTotal();

    Integer delete(Integer id);
}
