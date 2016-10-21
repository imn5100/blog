package com.shaw.test;

import com.shaw.mapper.BloggerMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by imn5100 on 2016/8/26 0026.
 */
public class MapperTest extends SpringTestCase {

    @Autowired
    BloggerMapper bloggerMapper;

    @org.junit.Test
    public void testCommentList() {
    }

}
