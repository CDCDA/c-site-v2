package com.pw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.Album;
import com.pw.dto.AlbumDTO;
import com.pw.vo.AlbumVO;
import com.pw.vo.BlogVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <相册接口>
 **/

public interface AlbumService extends IService<Album> {

    IPage<AlbumVO> page(IPage<AlbumDTO> page, @Param("param") AlbumDTO param);

    AlbumVO getAlbumById(Long albumId);

    Integer count(AlbumDTO albumDTO);
}
