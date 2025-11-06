package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Todo;
import com.pw.service.impl.TodoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "待办")
@RequestMapping("/todos")
public class TodoController extends BaseController implements convertController {

    @Autowired
    private TodoServiceImpl todoService;

    @GetMapping
    @Operation(summary = "查询待办分页")
    public Result list(Todo todo) {
        return resultIPage(todoService.page(setPage(todo), convertWrap(todo)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询待办数")
    public Result count(Todo todo) {
        return resultData(todoService.count(convertWrap(todo)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据待办id查询待办")
    public Result get(@PathVariable Long id) {
        return resultData(todoService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增待办")
    public Result create(@RequestBody Todo todo) {
        todo.setId(new SnowFlake(1, 0).nextId());
        todoService.save(todo);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改待办")
    public Result update(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        return resultExit(todoService.updateById(todo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除待办")
    public Result delete(@PathVariable Long id) {
        return resultExit(todoService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除待办")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(todoService.removeByIds(ids));
    }
}
