package com.shaw.service;

import com.shaw.bo.Link;

import java.util.List;
import java.util.Map;


public interface LinkService {
    int add(Link link);

    int update(Link link);

    List<Link> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

    Integer delete(Integer id);
}
