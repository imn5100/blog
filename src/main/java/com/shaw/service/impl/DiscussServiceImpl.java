package com.shaw.service.impl;

import com.shaw.bo.Discuss;
import com.shaw.bo.Visitor;
import com.shaw.mapper.DiscussMapper;
import com.shaw.mapper.VisitorMapper;
import com.shaw.service.DiscussService;
import com.shaw.util.StringUtil;
import com.shaw.vo.CommentVo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private VisitorMapper visitorMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return discussMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Discuss record) {
        return discussMapper.insert(record);
    }

    @Override
    public int insertSelective(Discuss record) {
        return discussMapper.insertSelective(record);
    }

    @Override
    public Discuss selectByPrimaryKey(Integer id) {
        return discussMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Discuss record) {
        return discussMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Discuss record) {
        return discussMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Discuss record) {
        return discussMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Discuss> getListByBlogId(Integer blogId) {
        if (blogId == null || blogId == 0) {
            return Collections.emptyList();
        }
        return discussMapper.getListByBlogId(blogId);
    }

    @Override
    public List<CommentVo> getCommentListByBlogId(Integer blogId) {
        List<Discuss> discussList = this.getListByBlogId(blogId);
        if (discussList.size() != 0) {
            Set<Integer> userId = discussList.stream().map(Discuss::getUserId).collect(Collectors.toSet());
            List<Visitor> visitors = visitorMapper.getListByIds(userId);
            Map<Integer, Visitor> visitorMap = new HashMap<>();
            for (Visitor visitor : visitors) {
                visitorMap.put(visitor.getId(), visitor);
            }
            List<CommentVo> commentVoList = new ArrayList<>();
            for (Discuss discuss : discussList) {
                commentVoList.add(CommentVo.build(discuss, visitorMap.get(discuss.getUserId())));
            }
            return commentVoList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public int submitDiscuss(int blogId, int userId, String content) {
        if (blogId == 0 || userId == 0 || StringUtil.isEmpty(content)) {
            return 0;
        }
        Discuss discuss = new Discuss();
        discuss.setBlogId(blogId);
        discuss.setUserId(userId);
        discuss.setContent(StringEscapeUtils.escapeHtml4(content));
        discuss.setStatus(1);
        discuss.setCreateTime(System.currentTimeMillis());
        discuss.setUpdateTime(System.currentTimeMillis());
        return this.insert(discuss);
    }
}
