package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pw.domain.Blog;
import com.pw.dto.BlogPageDTO;
import com.pw.vo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//代表持久层
public interface BlogMapper extends BaseMapper<Blog> {
    IPage<BlogVO> page(IPage<BlogPageDTO> page, BlogPageDTO param);

    List<BlogVO> list(BlogPageDTO blog);


    List<BlogTypeDataVO> listByType(Integer number);

    BlogVO getBlogById(Long blogId);

    Integer count(BlogPageDTO blog);

    List<BlogCountVO> countBlogByDateRange(Long userId, String startTime, String endTime);

    List<BlogTypeCountVO> countBlogByType(Long userId, String startTime, String endTime);

    List<BlogTagCountVO> countBlogByTag(Long userId, String startTime, String endTime);

    Long getRandomBlog();

    Long getPreBlog(Long blogId);

    Long getNextBlog(Long blogId);
}
