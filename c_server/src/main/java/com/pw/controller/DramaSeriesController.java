package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.DramaSeries;
import com.pw.service.DramaSeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "影视")
@RequestMapping("/drama-series")
public class DramaSeriesController extends BaseController implements convertController {

    @Autowired
    private DramaSeriesService dramaSeriesService;

    @GetMapping
    @Operation(summary = "查询影视分页")
    public Result list(DramaSeries dramaSeries) {
        return resultIPage(dramaSeriesService.page(setPage(dramaSeries), convertWrap(dramaSeries)));
    }

    @GetMapping("/stats/type-count")
    @Operation(summary = "按分类查询影视列表计数")
    public Result countByType() {
        return resultData(dramaSeriesService.countDramaByType(JwtTokenUtil.getLoginUser().getUserId()));
    }

    @GetMapping("/count")
    @Operation(summary = "查询影视数")
    public Result count(DramaSeries dramaSeries) {
        return resultData(dramaSeriesService.count(convertWrap(dramaSeries)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据影视id查询影视")
    public Result get(@PathVariable Long id) {
        return resultData(dramaSeriesService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增影视")
    public Result create(@RequestBody DramaSeries dramaSeries) {
        dramaSeries.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        dramaSeries.setId(new SnowFlake(1, 0).nextId());
        dramaSeriesService.save(dramaSeries);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改影视")
    public Result update(@PathVariable Long id, @RequestBody DramaSeries dramaSeries) {
        dramaSeries.setId(id);
        return resultExit(dramaSeriesService.updateById(dramaSeries));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除影视")
    public Result delete(@PathVariable Long id) {
        return resultExit(dramaSeriesService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除影视")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(dramaSeriesService.removeByIds(ids));
    }
}
