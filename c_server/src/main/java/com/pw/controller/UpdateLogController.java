package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.UpdateLog;
import com.pw.service.UpdateLogService;
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
@Tag(name = "更新日志")
@RequestMapping("/update-logs")
public class UpdateLogController extends BaseController implements convertController {
    @Autowired
    private UpdateLogService updateLogService;

    @GetMapping
    @Operation(summary = "查询更新日志列表")
    public Result list(UpdateLog updateLog) {
        updateLog.setOrderBy("operate_time");
        return resultIPage(updateLogService.page(setPage(updateLog), convertWrap(updateLog)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询更新日志数量")
    public Result count(UpdateLog updateLog) {
        LambdaQueryWrapper<UpdateLog> wrapper = new LambdaQueryWrapper<>();
        return Result.ok().data(updateLogService.count(wrapper));
    }

    @GetMapping("/stats/date-range")
    @Operation(summary = "按时间范围查询更新日志计数")
    public Result countByDateRange(String startTime, String endTime) {
        return resultData(updateLogService.countUpdateLogByDateRange(startTime, endTime));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询更新日志")
    public Result get(@PathVariable Long id) {
        return resultData(updateLogService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增更新日志")
    public Result create(@RequestBody UpdateLog updateLog) {
        return resultExit(updateLogService.save(updateLog));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改更新日志")
    public Result update(@PathVariable Long id, @RequestBody UpdateLog updateLog) {
        updateLog.setId(id);
        return resultExit(updateLogService.updateById(updateLog));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除更新日志")
    public Result delete(@PathVariable Long id) {
        return resultExit(updateLogService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除更新日志")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(updateLogService.removeByIds(ids));
    }
}
