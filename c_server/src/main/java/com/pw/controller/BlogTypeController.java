package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.EmptyJugeUtil;
import com.pw.domain.BlogType;
import com.pw.domain.Comment;
import com.pw.service.BlogTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "博客分类")
@RequestMapping("/blog-types")
public class BlogTypeController extends BaseController implements convertController {

    @Autowired
    private BlogTypeService blogTypeService;

    @GetMapping
    @Operation(summary = "查询分类分页")
    public Result list(BlogType blogType) {
        blogType.setUserId(JwtTokenUtil.getLoginUserId());
        return resultIPage(blogTypeService.page(setPage(blogType), convertWrap(blogType)));
    }

    @GetMapping("/with-blogs")
    @Operation(summary = "查询分类列表并列出指定数量的博客")
    public Result listWithBlogs() {
        return resultList(blogTypeService.listTypeAndBlog());
    }

    @GetMapping("/with-stats")
    @Operation(summary = "查询分类列表(带统计)")
    public Result listWithStats(BlogType blogType) {
        return resultList(blogTypeService.listBlogType(blogType.getUserId()));
    }

    @GetMapping("/count")
    @Operation(summary = "查询分类数")
    public Result count(Long userId) {
        if (ObjectUtils.isEmpty(userId)) {
            userId = JwtTokenUtil.getLoginUser().getUserId();
        }
        QueryWrapper<BlogType> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return resultData(blogTypeService.count(wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据分类id查询分类")
    public Result get(@PathVariable Long id) {
        BlogType blogType = blogTypeService.getById(id);
        return resultData(blogType);
    }

    @PostMapping
    @Operation(summary = "新增博客分类")
    public Result create(@RequestBody BlogType blogType) {
        blogType.setUserId(JwtTokenUtil.getLoginUserId());
        return resultExit(blogTypeService.save(blogType));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改博客分类")
    public Result update(@PathVariable Long id, @RequestBody BlogType blogType) {
        blogType.setTypeId(id);
        blogType.setUpdateTime(new Date());
        blogType.setUpdateBy(JwtTokenUtil.getLoginUserId());
        return resultExit(blogTypeService.updateById(blogType));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除博客分类")
    public Result delete(@PathVariable Long id) {
        return resultExit(blogTypeService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除博客分类")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(blogTypeService.removeByIds(ids));
    }
}
