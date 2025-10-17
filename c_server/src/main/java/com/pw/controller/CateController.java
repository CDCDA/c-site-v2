package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Cate;
import com.pw.service.CateService;
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
@Tag(name = "美食")
@RequestMapping("/cates")
public class CateController extends BaseController implements convertController {

    @Autowired
    private CateService cateService;

    @GetMapping
    @Operation(summary = "查询美食列表")
    public Result list(Cate cate) {
        return resultIPage(cateService.page(setPage(cate), convertWrap(cate)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询美食数")
    public Result count(Cate cate) {
        return resultData(cateService.count(convertWrap(cate)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据美食id查询美食")
    public Result get(@PathVariable Long id) {
        return resultData(cateService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增美食")
    public Result create(@RequestBody Cate cate) {
        cate.setId(new SnowFlake(1, 0).nextId());
        cateService.save(cate);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改美食")
    public Result update(@PathVariable Long id, @RequestBody Cate cate) {
        cate.setId(id);
        return resultExit(cateService.updateById(cate));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除美食")
    public Result delete(@PathVariable Long id) {
        return resultExit(cateService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除美食")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(cateService.removeByIds(ids));
    }
}
