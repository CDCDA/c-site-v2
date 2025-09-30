package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.ImageRelation;
import com.pw.mapper.ImageRelationMapper;
import com.pw.service.ImageRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageRelationServiceImpl extends ServiceImpl<ImageRelationMapper, ImageRelation> implements ImageRelationService {
    @Autowired
    ImageRelationMapper imageRelationMapper;

    @Override
    @Transactional
    public Integer insertEssayImageRelations(List<String> urls, Long essayId)
    {
        return imageRelationMapper.insertEssayImageRelations(urls,essayId);
    }

    @Override
    @Transactional
    public Integer insertAlbumImageRelations(List<String> urls, Long albumId)
    {
        return imageRelationMapper.insertAlbumImageRelations(urls,albumId);
    }
}
