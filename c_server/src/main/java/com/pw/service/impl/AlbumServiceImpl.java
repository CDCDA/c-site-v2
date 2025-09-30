package com.pw.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Album;
import com.pw.dto.AlbumDTO;
import com.pw.mapper.AlbumMapper;
import com.pw.service.AlbumService;
import com.pw.vo.AlbumVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {
    @Autowired
    AlbumMapper albumMapper;

    @Override
    public IPage<AlbumVO> page(IPage<AlbumDTO> page, AlbumDTO param) {
        return albumMapper.page(page, param);
    }

    @Override
    public AlbumVO getAlbumById(Long albumId) {
        return albumMapper.getAlbumById(albumId);
    }

    ;

    @Override
    public Integer count(AlbumDTO albumDTO) {
        return albumMapper.count(albumDTO);
    }

    ;
}
