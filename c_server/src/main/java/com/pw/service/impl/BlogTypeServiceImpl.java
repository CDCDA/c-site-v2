package com.pw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.domain.BlogType;
import com.pw.dto.BlogPageDTO;
import com.pw.mapper.BlogTypeMapper;
import com.pw.service.BlogTypeService;
import com.pw.vo.BlogTypeVO;
import com.pw.vo.BlogVO;
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
public class BlogTypeServiceImpl extends ServiceImpl<BlogTypeMapper, BlogType> implements BlogTypeService {

    @Autowired
    private BlogTypeMapper blogTypeMapper;

    @Autowired
    private BlogServiceImpl blogService;

    @Override
    @Transactional
    public List<BlogTypeVO> listBlogType(Long userId) {
        return blogTypeMapper.listBlogType(userId);
    }

    @Override
    public List<BlogType> listTypeAndBlog() {
        Long userId = JwtTokenUtil.getLoginUser().getUserId();
        LambdaQueryWrapper<BlogType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogType::getUserId, userId);
        List<BlogType> blogTypeList = this.list(wrapper);
        BlogPageDTO blog = new BlogPageDTO();
        blog.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        blog.setPageNum(1);
        blog.setPageSize(6);
        blogTypeList.forEach(type -> {
            blog.setTypeId(type.getTypeId());
            List<BlogVO> result = blogService.list(blog);
            type.setBlogList(result);
        });
        return blogTypeList;
    }
}
