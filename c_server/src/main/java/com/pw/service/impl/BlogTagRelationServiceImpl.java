package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Blog;
import com.pw.domain.BlogTag;
import com.pw.domain.BlogTagRealation;
import com.pw.mapper.BlogMapper;
import com.pw.mapper.BlogTagMapper;
import com.pw.mapper.BlogTagRelationMapper;
import com.pw.service.BlogTagRelationSerivce;
import com.pw.vo.BlogTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRealation> implements BlogTagRelationSerivce {

    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogTagMapper blogTagMapper;

    @Override
    @Transactional
    public Long isTagExit(String tagName)
    {
        return blogTagRelationMapper.isTagExit(tagName);
    }
    @Override
    @Transactional
    public Integer insertTags(List<Long> tags, Long blogId)
    {
        return blogTagRelationMapper.insertTags(tags,blogId);
    }

    @Override
    public Integer countByIds(List<Long> ids, Long blogId) {
        return null;
    }

    @Override
    @Transactional
    public Integer countByUserId(Long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        List<Blog> blogList = blogMapper.selectByMap(map);
        List<Long> ids = new ArrayList<Long>();
        blogList.forEach(blog -> {
            ids.add(blog.getBlogId());
        });
        return blogTagRelationMapper.countByIds(ids,userId);
    }

    @Override
    @Transactional
    public List<BlogTagVO> listBlogTag(BlogTag blogTag){
        return  blogTagRelationMapper.listBlogTag(blogTag);
    }
}
