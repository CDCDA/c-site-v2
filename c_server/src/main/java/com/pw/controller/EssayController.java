package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Essay;
import com.pw.domain.ImageRelation;
import com.pw.dto.EssayDTO;
import com.pw.service.EssayService;
import com.pw.service.ImageRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "随笔")
@RequestMapping("/essays")
public class EssayController extends BaseController implements convertController {

    @Autowired
    private EssayService essayService;

    @Autowired
    private ImageRelationService imageRelationService;

    @GetMapping
    @Operation(summary = "查询随笔列表")
    public Result list(EssayDTO essay) {
        essay.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        return resultIPage(essayService.page(setPage(essay), essay));
    }

    @GetMapping("/count")
    @Operation(summary = "查询随笔数")
    public Result count(EssayDTO essayDTO) {
        return resultData(essayService.count(essayDTO));
    }

    @GetMapping("/stats/date-range")
    @Operation(summary = "按时间范围查询随笔列表计数")
    public Result countByDateRange(String startTime, String endTime) throws ParseException {
        Long userId = JwtTokenUtil.getLoginUserId();
        return resultData(essayService.countEssayByDateRange(userId, startTime, endTime));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据随笔id查询随笔")
    public Result get(@PathVariable Long id) {
        return resultData(essayService.getEssayById(id));
    }

    @PostMapping
    @Operation(summary = "新增随笔")
    public Result create(@RequestBody Essay essay) {
        List<String> urls = new ArrayList<>();
        essay.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        essay.setId(new SnowFlake(1, 0).nextId());

        essayService.save(essay);

        List<ImageRelation> imageRelationList = essay.getImageRelations();
        if (ObjectUtils.isNotEmpty(imageRelationList)) {
            for (ImageRelation imageRelation : imageRelationList) {
                if (!isEmpty(imageRelation.getUrl())) {
                    urls.add(imageRelation.getUrl());
                }
            }
            if (!urls.isEmpty()) {
                imageRelationService.insertEssayImageRelations(urls, essay.getId());
            }
        }
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改随笔")
    public Result update(@PathVariable Long id, @RequestBody Essay essay) {
        if (isEmpty(essay.getId())) {
            return Result.error("随笔ID不能为空");
        }

        List<String> urls = new ArrayList<>();
        essay.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        List<ImageRelation> imageRelationList = essay.getImageRelations();

        // 更新随笔基本信息
        boolean updateResult = essayService.updateById(essay);

        // 删除旧的图片关联
        QueryWrapper<ImageRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("essay_id", essay.getId());
        imageRelationService.remove(wrapper);

        // 添加新的图片关联
        if (ObjectUtils.isNotEmpty(imageRelationList)) {
            for (ImageRelation imageRelation : imageRelationList) {
                if (!isEmpty(imageRelation.getUrl())) {
                    urls.add(imageRelation.getUrl());
                }
            }
            if (!urls.isEmpty()) {
                imageRelationService.insertEssayImageRelations(urls, essay.getId());
            }
        }

        return resultExit(updateResult);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除随笔")
    public Result delete(@PathVariable Long id) {
        return resultExit(essayService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除随笔")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(essayService.removeByIds(ids));
    }
}
