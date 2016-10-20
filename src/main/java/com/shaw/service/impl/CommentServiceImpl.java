//package com.shaw.service.impl;
//
//import com.shaw.entity.Comment;
//import com.shaw.mapper.CommentMapper;
//import com.shaw.service.CommentService;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
///**
// * 评论Service实现类
// *
// * @author Administrator
// */
//@Service("commentService")
//public class CommentServiceImpl implements CommentService {
//
//    @Resource
//    private CommentMapper commentMapper;
//
//    public int add(Comment comment) {
//        return commentMapper.add(comment);
//    }
//
//    public List<Comment> list(Map<String, Object> map) {
//        return commentMapper.listComments(map);
//    }
//
//    public Long getTotal(Map<String, Object> map) {
//        return commentMapper.getTotal(map);
//    }
//
//    public Integer delete(Integer id) {
//        return commentMapper.delete(id);
//    }
//
//    public int update(Comment comment) {
//        return commentMapper.update(comment);
//    }
//
//}
