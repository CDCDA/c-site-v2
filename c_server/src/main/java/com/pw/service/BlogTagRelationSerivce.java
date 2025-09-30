package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.BlogTag;
import com.pw.domain.BlogTagRealation;
import com.pw.vo.BlogTagVO;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/18 17:50
 * @description <>
 **/
public interface BlogTagRelationSerivce extends IService<BlogTagRealation> {
    Integer insertTags(List<Long> tags, Long blogId);

    Integer countByIds(List<Long> ids,Long blogId);

    Integer countByUserId(Long userId);

    List<BlogTagVO> listBlogTag(BlogTag blogTag);

    Long isTagExit(String tagName);
}
