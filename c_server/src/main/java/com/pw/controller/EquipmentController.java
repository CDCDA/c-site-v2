package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Equipment;
import com.pw.service.EquipmentService;
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
@Tag(name = "设备")
@RequestMapping("/equipments")
public class EquipmentController extends BaseController implements convertController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    @Operation(summary = "查询设备列表")
    public Result list(Equipment equipment) {
        return resultIPage(equipmentService.page(setPage(equipment), convertWrap(equipment)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询设备数")
    public Result count(Equipment equipment) {
        return resultData(equipmentService.count(convertWrap(equipment)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据设备id查询设备")
    public Result get(@PathVariable Long id) {
        return resultData(equipmentService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增设备")
    public Result create(@RequestBody Equipment equipment) {
        equipment.setId(new SnowFlake(1, 0).nextId());
        equipmentService.save(equipment);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改设备")
    public Result update(@PathVariable Long id, @RequestBody Equipment equipment) {
        equipment.setId(id);
        return resultExit(equipmentService.updateById(equipment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备")
    public Result delete(@PathVariable Long id) {
        return resultExit(equipmentService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除设备")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(equipmentService.removeByIds(ids));
    }
}
