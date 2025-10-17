package com.pw.controller;

import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.ResultUtil;
import com.pw.common.utils.SnowFlake;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.domain.BlogTag;
import com.pw.mapper.BlogTagRelationMapper;
import com.pw.service.BlogTagRelationSerivce;
import com.pw.service.BlogTagService;
import com.pw.vo.BlogTagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "博客标签")
@RequestMapping("/blog-tags")
public class BlogTagController extends BaseController implements convertController {
    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private BlogTagRelationSerivce blogTagRelationSerivce;

    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;

    @GetMapping
    @Operation(summary = "查询标签分页")
    public Result list(BlogTag blogTag) {
        List<BlogTagVO> result = blogTagRelationSerivce.listBlogTag(blogTag);
        return ResultUtil.resultPage(result, blogTagRelationMapper.countBlogTag(blogTag));
    }

    @GetMapping("/with-blogs")
    @Operation(summary = "查询标签列表并列出指定数量的博客")
    public Result listWithBlogs() {
        return resultList(blogTagService.listTypeAndBlog());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询标签")
    public Result get(@PathVariable Long id) {
        return resultData(blogTagService.getById(id));
    }

    @GetMapping("/count")
    @Operation(summary = "根据用户id查询标签数")
    public Result count() {
        Long userId = JwtTokenUtil.getLoginUser().getUserId();
        return resultData(blogTagRelationSerivce.countByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "新增标签")
    public Result create(@RequestBody BlogTag blogTag) {
        Long tagId = blogTagRelationSerivce.isTagExit(blogTag.getTagName());
        if (isEmpty(tagId)) {
            blogTag.setTagId(new SnowFlake(1, 0).nextId());
            blogTagService.save(blogTag);
            return Result.ok().data(blogTag.getTagId());
        } else {
            return Result.ok().data(tagId);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改标签")
    public Result update(@PathVariable Long id, @RequestBody BlogTag blogTag) {
        blogTag.setTagId(id);
        blogTagService.updateById(blogTag);
        return Result.ok().data(blogTag.getTagId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public Result delete(@PathVariable Long id) {
        return resultExit(blogTagService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除标签")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(blogTagService.removeByIds(ids));
    }
}
