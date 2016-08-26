package com.shaw.mapper;

import com.shaw.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentMapper {

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    public int add(Comment comment);

    /**
     * 修改评论
     *
     * @param comment
     * @return
     */
    public int update(Comment comment);

    /**
     * 获取总记录数
     *
     * @param map
     * @return
     */
    public Long getTotal(Map<String, Object> map);

    /**
     * 删除评论信息
     *
     * @param id
     * @return
     */
    public Integer delete(Integer id);

    public List<Comment> listComments(Map<String, Object> map);


}
