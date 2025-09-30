package com.pw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.Blog;
import com.pw.dto.BlogPageDTO;
import com.pw.vo.*;
import io.lettuce.core.dynamic.annotation.Param;


import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <博客接口>
 **/
public interface BlogService extends IService<Blog> {
    IPage<BlogVO> page(IPage<BlogPageDTO> page, @Param("param") BlogPageDTO param);

    List<BlogVO> list(BlogPageDTO param);

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
