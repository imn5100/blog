package com.shaw.mapper;

import com.shaw.bo.Link;

import java.util.List;
import java.util.Map;

public interface LinkMapper {

    Integer add(Link link);

    Integer update(Link link);

    List<Link> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

    Integer delete(Integer id);
}
