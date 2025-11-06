package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.Message;
import com.pw.service.GameService;
import com.pw.service.impl.MessageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;
import static com.pw.common.utils.ResultUtil.*;
import static com.pw.common.utils.pageUtil.setPage;

@RestController
@Tag(name = "消息")
@RequestMapping("/messages")
public class MessageController extends BaseController implements convertController {

    @Autowired
    private MessageServiceImpl messageService;

    @GetMapping
    @Operation(summary = "查询消息分页")
    public Result list(Message message) {
        return resultIPage(messageService.page(setPage(message), convertWrap(message)));
    }

    @GetMapping("/listUserMessages")
    public Result listUnRead(Message message) {
        message.setPageSize(100);
        message.setReceiverId(JwtTokenUtil.getLoginUserId());
        return resultIPage(messageService.page(setPage(message), convertWrap(message)));
    }

    @GetMapping("/count")
    @Operation(summary = "查询消息数")
    public Result count(Message message) {
        return resultData(messageService.count(convertWrap(message)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据消息id查询消息")
    public Result get(@PathVariable Long id) {
        return resultData(messageService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增消息")
    public Result create(@RequestBody Message message) {
        message.setId(new SnowFlake(1, 0).nextId());
        messageService.save(message);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改消息")
    public Result update(@PathVariable Long id, @RequestBody Message message) {
        message.setId(id);
        return resultExit(messageService.updateById(message));
    }

    @PutMapping("/batch-read")
    @Operation(summary = "批量修改已读状态")
    public Result read(@RequestBody List<Long> ids) {
        List<Message> messageList = new ArrayList<>();
        ids.forEach(id -> {
            Message message = new Message();
            message.setId(id);
            message.setIsRead("1");
            messageList.add(message);
        });
        return resultExit(messageService.updateBatchById(messageList));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除消息")
    public Result delete(@PathVariable Long id) {
        return resultExit(messageService.removeById(id));
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除消息")
    public Result batchDelete(@RequestBody List<Long> ids) {
        return resultExit(messageService.removeByIds(ids));
    }
}
