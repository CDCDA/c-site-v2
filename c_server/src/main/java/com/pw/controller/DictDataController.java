package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.DictData;
import com.pw.service.impl.DictDataServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "字典数据")
@RequestMapping("/dict-datas")
public class DictDataController extends BaseController implements convertController {

    @Autowired
    private DictDataServiceImpl dictDataService;

    @GetMapping
    @Operation(summary = "查询字典数据列表")
    public Result list(DictData dictData) {
        return resultIPage(dictDataService.page(setPage(dictData), convertWrap(dictData)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询字典数据数")
    public Result count(DictData dictData) {
        return resultData((int) dictDataService.count(convertWrap(dictData)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据字典id查询字典数据")
    public Result get(@PathVariable Long id) {
        return resultData(dictDataService.getById(id));
    }

    @GetMapping("/by-type/{dictType}")
    @Operation(summary = "根据字典类型查询字典数据")
    public Result getByDictType(@PathVariable String dictType) {
        LambdaQueryWrapper<DictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictData::getDictType, dictType)
                .eq(DictData::getStatus, "1")
                .orderByAsc(DictData::getDictSort);
        return resultData(dictDataService.list(wrapper));
    }

    @GetMapping("/by-type/{dictType}/page")
    @Operation(summary = "根据字典类型查询字典数据分页")
    public Result getByDictTypePage(@PathVariable String dictType, DictData dictData) {
        dictData.setDictType(dictType);
        dictData.setOrderBy("dict_sort");
        dictData.setAsc("asc");
        return resultIPage(dictDataService.page(setPage(dictData), convertWrap(dictData)));
    }

    @PostMapping
    @Operation(summary = "新增字典数据")
    public Result create(@RequestBody DictData dictData) {
        DictData existDict = dictDataService.list(new LambdaQueryWrapper<DictData>()
                        .eq(DictData::getDictValue, dictData.getDictValue())
                        .eq(DictData::getDictType, dictData.getDictType()))
                .stream().findFirst().orElse(null);
        if (ObjectUtils.isNotEmpty(existDict)) {
            return Result.error("字典数据已存在");
        }

        dictDataService.save(dictData);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改字典数据")
    public Result update(@PathVariable Long id, @RequestBody DictData dictData) {
        dictData.setId(id);
        dictDataService.updateById(dictData);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典数据")
    public Result delete(@PathVariable Long id) {
        return resultExit(dictDataService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除字典数据")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(dictDataService.removeByIds(ids));
    }
}
