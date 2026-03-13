package com.pw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Blog;
import com.pw.domain.BlogTag;
import com.pw.domain.BlogTagRealation;
import com.pw.dto.BlogPageDTO;
import com.pw.mapper.BlogMapper;
import com.pw.service.BlogService;
import com.pw.service.BlogTagRelationSerivce;
import com.pw.service.BlogTagService;
import com.pw.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pw.common.utils.SnowFlake;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {


    @Autowired
    BlogMapper blogMapper;

    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private BlogTagRelationSerivce blogTagRelationSerivce;

    @Override
    @Transactional
    public IPage<BlogVO> page(IPage<BlogPageDTO> page, BlogPageDTO param) {
        try {
            return blogMapper.page(page, param);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.info(String.valueOf(e));
        }
        return null;
    }

    @Override
    @Transactional
    public List<BlogVO> list(BlogPageDTO blog) {
        return blogMapper.list(blog);
    }

    @Override
    @Transactional
    public List<BlogTypeDataVO> listByType(Integer number) {
        return blogMapper.listByType(number);
    }

    @Override
    @Transactional
    public BlogVO getBlogById(Long blogId) {
        return blogMapper.getBlogById(blogId);
    }

    @Override
    @Transactional
    public Integer count(BlogPageDTO blog) {
        return blogMapper.count(blog);
    }

    @Override
    @Transactional
    public List<BlogCountVO> countBlogByDateRange(Long userId, String startTime, String endTime) {
        return blogMapper.countBlogByDateRange(userId, startTime, endTime);
    }

    @Override
    @Transactional
    public List<BlogTypeCountVO> countBlogByType(Long userId, String startTime, String endTime) {
        return blogMapper.countBlogByType(userId, startTime, endTime);
    }

    @Override
    @Transactional
    public List<BlogTagCountVO> countBlogByTag(Long userId, String startTime, String endTime) {
        return blogMapper.countBlogByTag(userId, startTime, endTime);
    }

    @Override
    @Transactional
    public Long getRandomBlog() {
        return blogMapper.getRandomBlog();
    }


    @Override
    public Long getPreBlog(Long blogId) {
        return blogMapper.getPreBlog(blogId);
    }

    @Override
    public Long getNextBlog(Long blogId) {
        return blogMapper.getNextBlog(blogId);
    }

    @Override
    @Transactional
    public Long createBlog(Blog blog) {
        blog.setBlogId(new SnowFlake(1, 0).nextId());
        this.save(blog);

        if (ObjectUtils.isNotEmpty(blog.getTags())) {
            List<Long> tagIds = new ArrayList<>();
            for (BlogTag blogTag : blog.getTags()) {
                if (!isEmpty(blogTag.getTagId())) {
                    tagIds.add(blogTag.getTagId());
                } else {
                    blogTag.setTagId(new SnowFlake(1, 0).nextId());
                    blogTagService.save(blogTag);
                    tagIds.add(blogTag.getTagId());
                }
            }
            blogTagRelationSerivce.insertTags(tagIds, blog.getBlogId());
        }

        return blog.getBlogId();
    }

    @Override
    @Transactional
    public Long updateBlog(Blog blog) {
        this.updateById(blog);

        if (ObjectUtils.isNotEmpty(blog.getTags())) {
            // 删除原有标签关联
            QueryWrapper<BlogTagRealation> wrapper = new QueryWrapper<>();
            wrapper.eq("blog_id", blog.getBlogId());
            blogTagRelationSerivce.remove(wrapper);

            // 重新插入标签关联
            List<Long> tagIds = new ArrayList<>();
            for (BlogTag blogTag : blog.getTags()) {
                if (!isEmpty(blogTag.getTagId())) {
                    tagIds.add(blogTag.getTagId());
                } else {
                    blogTag.setTagId(new SnowFlake(1, 0).nextId());
                    blogTagService.save(blogTag);
                    tagIds.add(blogTag.getTagId());
                }
            }
            blogTagRelationSerivce.insertTags(tagIds, blog.getBlogId());
        }

        return blog.getBlogId();
    }
}
