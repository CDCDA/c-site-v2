package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.token.UserLoginToken;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Wallpaper;
import com.pw.service.WallpaperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "壁纸")
@RequestMapping("/wallpapers")
public class WallpaperController extends BaseController implements convertController {

    @Autowired
    private WallpaperService wallpaperService;

    @GetMapping
    @Operation(summary = "查询壁纸分页")
    public Result page(Wallpaper wallpaper) {
        IPage<Wallpaper> result = wallpaperService.page(setPage(wallpaper), convertWrap(wallpaper));
        return resultIPage(result);
    }

    @GetMapping("/count")
    @Operation(summary = "查询壁纸数")
    public Result count(Wallpaper Wallpaper) {
        return resultData(wallpaperService.count(convertWrap(Wallpaper)));
    }

    @GetMapping("/getRandomWallpaper")
    @Operation(summary = "获取随机壁纸")
    public Result getRandomWallpaper() {
        LambdaQueryWrapper<Wallpaper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Wallpaper::getType, "img"); // 添加条件
        List<Wallpaper> wallpaperList = wallpaperService.list(wrapper); // 查询所有数据
        Random random = new Random();
        Wallpaper wallpaper = wallpaperList.get(random.nextInt(wallpaperList.size()));
        return resultData(wallpaper);
    }


    @GetMapping("/{id}")
    @Operation(summary = "根据壁纸id查询壁纸")
    public Result getById(@PathVariable Long id) {
        return resultData(wallpaperService.getById(id));
    }

    @PostMapping

    @Operation(summary = "新增壁纸")
    public Result save(@RequestBody Wallpaper wallpaper) {
        wallpaper.setId(new SnowFlake(1, 0).nextId());
        wallpaperService.save(wallpaper);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改壁纸")
    public Result update(@PathVariable Long id, @RequestBody Wallpaper wallpaper) {
        wallpaper.setId(id);
        return resultExit(wallpaperService.updateById(wallpaper));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除壁纸")
    public Result delete(@PathVariable Long id) {
        return resultExit(wallpaperService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除壁纸")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(wallpaperService.removeByIds(ids));
    }
}
