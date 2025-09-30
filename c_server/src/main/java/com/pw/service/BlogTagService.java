package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.BlogTag;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:40
 * @description <博客标签>
 **/
public interface BlogTagService extends IService<BlogTag> {
    List<BlogTag> listTypeAndBlog();
}
