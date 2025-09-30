package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.handler.CustomWebSocketHandler;
import com.pw.common.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @author cyd
 * @date 2025/1/9 9:37
 * @description <>
 **/
@RestController
@Tag(name =  "WebSocket")
@RequestMapping("/websocket")
public class WebSocketController extends BaseController implements convertController {
    private final CustomWebSocketHandler webSocketHandler;
    public WebSocketController(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/broadcast")
    public Result broadcastMessage(@RequestBody String message) {
        webSocketHandler.broadcastMessage(message);
        return Result.ok().data("广播成功");
    }
}
