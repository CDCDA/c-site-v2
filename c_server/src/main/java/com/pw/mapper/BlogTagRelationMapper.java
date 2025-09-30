package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.BlogTag;
import com.pw.domain.BlogTagRealation;
import com.pw.vo.BlogTagVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//代表持久层
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRealation> {
    Integer insertTags(List<Long> tags, Long blogId);

    Integer countByIds(List<Long> ids, Long blogId);

    List<BlogTagVO> listBlogTag(BlogTag blogTag);

    Long isTagExit(String tagName);

    Integer countBlogTag(BlogTag blogTag);
}
