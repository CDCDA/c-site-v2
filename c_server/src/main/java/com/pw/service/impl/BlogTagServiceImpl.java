package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.BlogTag;
import com.pw.mapper.BlogTagMapper;
import com.pw.service.BlogTagService;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class BlogTagServiceImpl  extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

    @Override
    public List<BlogTag> listTypeAndBlog() {
        return null;
    }
}
