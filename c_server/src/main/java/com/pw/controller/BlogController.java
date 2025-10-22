package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.domain.Blog;
import com.pw.domain.BlogTag;
import com.pw.domain.BlogTagRealation;
import com.pw.dto.BlogPageDTO;
import com.pw.service.BlogService;
import com.pw.service.BlogTagRelationSerivce;
import com.pw.service.BlogTagService;
import com.pw.vo.BlogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@Tag(name = "博客")
@RequestMapping("/blogs")
public class BlogController extends BaseController implements convertController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private BlogTagRelationSerivce blogTagRelationSerivce;

    @GetMapping
    @Operation(summary = "查询博客列表")
    public Result list(BlogPageDTO blog) {
        blog.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        return resultIPage(blogService.page(setPage(blog), blog));
    }

    @GetMapping("/by-type")
    @Operation(summary = "根据分类查询博客列表")
    public Result listByType(Integer number) {
        return resultData(blogService.listByType(number));
    }

    @GetMapping("/count")
    @Operation(summary = "查询博客列表计数")
    public Result count(BlogPageDTO blog) {
        blog.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        return Result.ok().data(blogService.count(blog));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询博客")
    public Result get(@PathVariable Long id) {
        BlogVO blog = blogService.getBlogById(id);
        return resultData(blog);
    }

    @GetMapping("/random")
    @Operation(summary = "获取随机博客")
    public Result getRandom() {
        Long blogId = blogService.getRandomBlog();
        return resultData(String.valueOf(blogId));
    }

    @GetMapping("/neighbors/{id}")
    @Operation(summary = "获取博客的前一个博客和后一个博客")
    public Result getNeighbors(@PathVariable Long id) {
        Map<String, BlogVO> map = new HashMap<>();
        Long preBlogId = blogService.getPreBlog(id);
        Long nextBlogId = blogService.getNextBlog(id);
        map.put("preBlog", ObjectUtils.isNotEmpty(preBlogId) ? blogService.getBlogById(preBlogId) : null);
        map.put("nextBlog", ObjectUtils.isNotEmpty(nextBlogId) ? blogService.getBlogById(nextBlogId) : null);
        return Result.ok().data(map);
    }

    @GetMapping("/stats/date-range")
    @Operation(summary = "按时间范围查询博客列表计数")
    public Result countByDateRange(Long userId, String startTime, String endTime) throws ParseException {
        if (ObjectUtils.isEmpty(userId)) {
            userId = JwtTokenUtil.getLoginUser().getUserId();
        }
        return resultData(blogService.countBlogByDateRange(userId, startTime, endTime));
    }

    @GetMapping("/stats/type")
    @Operation(summary = "按分类查询博客列表计数")
    public Result countByType(Long userId, String startTime, String endTime) {
        return resultData(blogService.countBlogByType(userId, startTime, endTime));
    }

    @GetMapping("/stats/tag")
    @Operation(summary = "按标签查询博客列表计数")
    public Result countByTag(Long userId, String startTime, String endTime) {
        return resultData(blogService.countBlogByTag(userId, startTime, endTime));
    }

    @PostMapping
    @Operation(summary = "新增博客")
    public Result create(@RequestBody Blog blog) {
        blog.setBlogId(new SnowFlake(1, 0).nextId());
        blog.setUserId(JwtTokenUtil.getLoginUser().getUserId());

        blogService.save(blog);

        if (ObjectUtils.isNotEmpty(blog.getTags())) {
            List<Long> tagIds = new ArrayList<>();
            for (BlogTag blogTag : blog.getTags()) {
                if (!isEmpty(blogTag.getTagId())) {
                    tagIds.add(blogTag.getTagId());
                } else {
                    blogTag.setTagId(new SnowFlake(1, 0).nextId());
                    blogTagService.save(blogTag);
                    tagIds.add(blogTag.getTagId());
                }
            }
            blogTagRelationSerivce.insertTags(tagIds, blog.getBlogId());
        }

        return Result.ok().data(blog.getBlogId().toString());
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改博客")
    public Result update(@PathVariable Long id, @RequestBody Blog blog) {
        blog.setBlogId(id);

        blogService.updateById(blog);

        if (ObjectUtils.isNotEmpty(blog.getTags())) {
            QueryWrapper<BlogTagRealation> wrapper = new QueryWrapper<>();
            wrapper.eq("blog_id", blog.getBlogId());
            blogTagRelationSerivce.remove(wrapper);

            List<Long> ids = new ArrayList();
            for (BlogTag blogTag : blog.getTags()) {
                if (!isEmpty(blogTag.getTagId())) {
                    ids.add(blogTag.getTagId());
                } else {
                    blogTag.setTagId(new SnowFlake(1, 0).nextId());
                    blogTagService.save(blogTag);
                    ids.add(blogTag.getTagId());
                }
            }
            blogTagRelationSerivce.insertTags(ids, blog.getBlogId());
        }

        return Result.ok().data(blog.getBlogId().toString());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除博客")
    public Result delete(@PathVariable Long id) {
        return resultExit(blogService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除博客")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(blogService.removeByIds(ids));
    }
}
