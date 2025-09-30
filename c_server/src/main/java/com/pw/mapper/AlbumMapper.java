package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.domain.Album;
import com.pw.dto.AlbumDTO;
import com.pw.vo.AlbumVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @author cyd
 * @date 2023/11/7 21:49
 * @description <相册>
 **/
@Repository
public interface AlbumMapper extends BaseMapper<Album> {

    IPage<AlbumVO> page(IPage<AlbumDTO> page, AlbumDTO param);


    AlbumVO getAlbumById(Long albumId);


    Integer count(AlbumDTO albumDTO);
}
