package com.pw.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Blog;
import com.pw.dto.BlogPageDTO;
import com.pw.mapper.BlogMapper;
import com.pw.service.BlogService;
import com.pw.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
