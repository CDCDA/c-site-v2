package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.BlogType;
import com.pw.vo.BlogTypeVO;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:51
 * @description <博客分类接口>
 **/
public interface BlogTypeService  extends IService<BlogType> {
    List<BlogTypeVO> listBlogType(Long userId);

    List<BlogType> listTypeAndBlog();
}
