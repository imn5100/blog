package com.shaw.test;

import com.shaw.entity.Comment;
import com.shaw.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by imn5100 on 2016/8/26 0026.
 */
public class MapperTest extends SpringTestCase {

    @Autowired
    CommentMapper commentMapper;

    @org.junit.Test
    public void testCommentList() {
        List<Comment> list = commentMapper.listComments(null);
        for (Comment c : list) {
            System.out.println(c.getBlogId() + c.getBlogTitle());
        }
    }

}
