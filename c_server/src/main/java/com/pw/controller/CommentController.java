package com.pw.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.pw.common.utils.ArraysToTreeUtil;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.Comment;
import com.pw.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "评论")
@RequestMapping("/comments")
public class CommentController extends BaseController implements convertController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    @Operation(summary = "查询评论列表")
    public Result list(Comment comment) {
        IPage<Comment> result = commentService.page(setPage(comment), convertWrap(comment));
        return resultIPage(result);
    }

    @GetMapping("/tree")
    @Operation(summary = "查询树形评论列表")
    public Result listTree(Comment comment) {
        IPage<Comment> result = commentService.page(setPage(comment), convertWrap(comment));
        List<Comment> list = result.getRecords();
        list.forEach(x -> {
            x.setChildren(new ArrayList<>());
        });
        ArraysToTreeUtil<Comment> arraysToTreeUtil = new ArraysToTreeUtil<>();
        arraysToTreeUtil.parent(Comment::getParentId).children(Comment::getChildren).code(Comment::getId);
        result.setRecords(arraysToTreeUtil.tree(list));
        return resultIPage(result);
    }

    @GetMapping("/count")
    @Operation(summary = "查询评论数")
    public Result count(Comment comment) {
        return resultData((int) commentService.count(convertWrap(comment)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询评论")
    public Result get(@PathVariable Long id) {
        return resultData(commentService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增评论")
    public Result create(@RequestBody Comment comment) {
        commentService.save(comment);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改评论")
    public Result update(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        commentService.updateById(comment);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public Result delete(@PathVariable Long id) {
        return resultExit(commentService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除评论")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(commentService.removeByIds(ids));
    }
}
