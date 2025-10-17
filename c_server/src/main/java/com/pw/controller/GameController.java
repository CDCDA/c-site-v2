package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Game;
import com.pw.service.GameService;
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
@Tag(name = "游戏")
@RequestMapping("/games")
public class GameController extends BaseController implements convertController {

    @Autowired
    private GameService gameService;

    @GetMapping
    @Operation(summary = "查询游戏分页")
    public Result list(Game game) {
        return resultIPage(gameService.page(setPage(game), convertWrap(game)));
    }

    @GetMapping("/stats/type-count")
    @Operation(summary = "按分类查询游戏列表计数")
    public Result countByType() {
        return resultData(gameService.countGameByType(JwtTokenUtil.getLoginUser().getUserId()));
    }

    @GetMapping("/count")
    @Operation(summary = "查询游戏数")
    public Result count(Game game) {
        return resultData(gameService.count(convertWrap(game)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据游戏id查询游戏")
    public Result get(@PathVariable Long id) {
        return resultData(gameService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增游戏")
    public Result create(@RequestBody Game game) {
        game.setUserId(JwtTokenUtil.getLoginUser().getUserId());
        game.setId(new SnowFlake(1, 0).nextId());
        gameService.save(game);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改游戏")
    public Result update(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        return resultExit(gameService.updateById(game));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除游戏")
    public Result delete(@PathVariable Long id) {
        return resultExit(gameService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除游戏")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(gameService.removeByIds(ids));
    }
}
