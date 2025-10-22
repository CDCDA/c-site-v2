package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.DictType;
import com.pw.service.impl.DictTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "字典类型")
@RequestMapping("/dict-types")
public class DictTypeController extends BaseController implements convertController {

    @Autowired
    private DictTypeServiceImpl dictTypeService;

    @GetMapping
    @Operation(summary = "查询字典类型列表")
    public Result list(DictType dictType) {
        return resultIPage(dictTypeService.page(setPage(dictType), convertWrap(dictType)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询字典类型数")
    public Result count(DictType dictType) {
        return resultData((int) dictTypeService.count(convertWrap(dictType)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据字典类型id查询字典类型")
    public Result get(@PathVariable Long id) {
        return resultData(dictTypeService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增字典类型")
    public Result create(@RequestBody DictType dictType) {
        DictType existDict = dictTypeService.list(new LambdaQueryWrapper<DictType>()
                        .eq(DictType::getDictType, dictType.getDictType()))
                .stream().findFirst().orElse(null);
        if (ObjectUtils.isNotEmpty(existDict)) {
            return Result.error("字典类型已存在");
        }

        dictTypeService.save(dictType);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改字典类型")
    public Result update(@PathVariable Long id, @RequestBody DictType dictType) {
        dictType.setId(id);
        dictTypeService.updateById(dictType);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典类型")
    public Result delete(@PathVariable Long id) {
        return resultExit(dictTypeService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除字典类型")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(dictTypeService.removeByIds(ids));
    }
}
