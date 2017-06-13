package com.shaw.mapper;

import com.shaw.bo.Link;
import com.shaw.util.PageBean;

import java.util.List;
import java.util.Map;

public interface LinkMapper {

    Integer add(Link link);

    Integer update(Link link);

    List<Link> list(PageBean pageBean);

    Long getTotal();

    Integer delete(Integer id);
}
