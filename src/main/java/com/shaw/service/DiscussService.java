package com.shaw.service;

import com.shaw.bo.Discuss;
import com.shaw.vo.CommentVo;

import java.util.List;

public interface DiscussService {
    int deleteByPrimaryKey(Integer id);

    int insert(Discuss record);

    int insertSelective(Discuss record);

    Discuss selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Discuss record);

    int updateByPrimaryKeyWithBLOBs(Discuss record);

    int updateByPrimaryKey(Discuss record);

    List<Discuss> getListByBlogId(Integer blogId);

    List<CommentVo> getCommentListByBlogId(Integer blogId);

    Discuss submitDiscuss(int blogId, int userId, String content);
}