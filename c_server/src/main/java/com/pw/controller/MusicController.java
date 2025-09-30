package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Music;
import com.pw.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "歌曲")
@RequestMapping("/musics")
public class MusicController extends BaseController implements convertController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    @Operation(summary = "查询歌曲列表")
    public Result list(Music music) {
        return resultIPage(musicService.page(setPage(music), convertWrap(music)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询歌曲数")
    public Result count(Music music) {
        return resultData(musicService.count(convertWrap(music)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据歌曲id查询歌曲")
    public Result get(@PathVariable String id) {
        return resultData(musicService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增歌曲")
    public Result create(@RequestBody Music music) {
        music.setId(new SnowFlake(1, 0).nextId());
        musicService.save(music);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改歌曲")
    public Result update(@PathVariable Long id, @RequestBody Music music) {
        music.setId(id);
        return resultExit(musicService.updateById(music));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除歌曲")
    public Result delete(@PathVariable String id) {
        return resultExit(musicService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除歌曲")
    public Result batchDelete(@RequestBody List<String> ids) {
        return resultExit(musicService.removeByIds(ids));
    }
}
