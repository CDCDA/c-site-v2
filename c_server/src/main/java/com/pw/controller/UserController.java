package com.pw.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.User;
import com.pw.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pw.common.utils.PassWordUtil.encodePassword;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.pageUtil.setPage;

@Component
@RestController
@Tag(name = "用户")
@RequestMapping("/users")
public class UserController extends BaseController implements convertController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "用户列表分页查询")
    public Result list(User user) {
        return resultIPage(userService.page(setPage(user), convertWrap(user)));
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public Result create(@Valid @RequestBody User user) {
        user.setUserId(null);
        user.setPassword(encodePassword(user.getPassword()));
        return resultExit(userService.save(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "用户详情")
    public Result get(@PathVariable Long id) {
        return resultData(userService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改用户")
    public Result update(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setUserId(id);
        if (ObjectUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(encodePassword(user.getPassword()));
        }
        return resultExit(userService.updateById(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result delete(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return Result.error("用户ID不能为空");
        }
        return resultExit(userService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除用户")
    public Result batchDelete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return Result.error("用户ID列表不能为空");
        }
        return resultExit(userService.removeByIds(ids));
    }
}
