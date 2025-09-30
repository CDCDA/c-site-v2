package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.ImageRelation;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <图片关联接口>
 **/

public interface ImageRelationService extends IService<ImageRelation> {

    Integer insertEssayImageRelations(List<String> urls, Long essayId);
    Integer insertAlbumImageRelations(List<String> urls, Long albumId);

}
