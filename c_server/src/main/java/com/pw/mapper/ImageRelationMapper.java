package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.ImageRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @author cyd
 * @date 2023/11/7 21:50
 * @description <图片关联>
 **/
@Repository
public interface ImageRelationMapper extends BaseMapper<ImageRelation> {
    Integer insertEssayImageRelations(List<String> urls, Long essayId);
    Integer insertAlbumImageRelations(List<String> urls, Long albumId);
}
