package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Album;
import com.pw.domain.ImageRelation;
import com.pw.dto.AlbumDTO;
import com.pw.service.AlbumService;
import com.pw.service.ImageRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "相册")
@RequestMapping("/albums")
public class AlbumController extends BaseController implements convertController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageRelationService imageRelationService;

    @GetMapping
    @Operation(summary = "查询相册分页")
    public Result list(Album album) {
        return resultIPage(albumService.page(setPage(album), convertWrap(album)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询相册数")
    public Result count(AlbumDTO albumDTO) {
        return resultData(albumService.count(albumDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据相册id查询相册")
    public Result get(@PathVariable Long id) {
        return resultData(albumService.getAlbumById(id));
    }

    @PostMapping
    @Operation(summary = "新增相册")
    public Result create(@RequestBody Album album) {
        List<String> urls = new ArrayList<>();
        album.setUserId(JwtTokenUtil.getLoginUserId());
        album.setId(new SnowFlake(1, 0).nextId());

        albumService.save(album);

        List<ImageRelation> imageRelationList = album.getImages();
        if (ObjectUtils.isNotEmpty(imageRelationList)) {
            for (ImageRelation imageRelation : imageRelationList) {
                if (!isEmpty(imageRelation.getUrl())) {
                    urls.add(imageRelation.getUrl());
                }
            }
            if (!urls.isEmpty()) {
                imageRelationService.insertAlbumImageRelations(urls, album.getId());
            }
        }
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改相册")
    public Result update(@PathVariable Long id, @RequestBody Album album) {
        List<String> urls = new ArrayList<>();
        album.setId(id);
        album.setUserId(JwtTokenUtil.getLoginUser().getUserId());

        boolean updateResult = albumService.updateById(album);

        QueryWrapper<ImageRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("album_id", album.getId());
        imageRelationService.remove(wrapper);

        List<ImageRelation> imageRelationList = album.getImages();
        if (ObjectUtils.isNotEmpty(imageRelationList)) {
            for (ImageRelation imageRelation : imageRelationList) {
                if (!isEmpty(imageRelation.getUrl())) {
                    urls.add(imageRelation.getUrl());
                }
            }
            if (!urls.isEmpty()) {
                imageRelationService.insertAlbumImageRelations(urls, album.getId());
            }
        }

        return resultExit(updateResult);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除相册")
    public Result delete(@PathVariable Long id) {
        return resultExit(albumService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除相册")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(albumService.removeByIds(ids));
    }
}
