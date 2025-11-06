package com.pw.config;

import com.pw.common.handler.CustomWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author cyd
 * @create 2024/12/15 下午 08:11
 * @Description
 **/
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private CustomWebSocketHandler customWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("🔧 注册 WebSocket 端点: /ws");

        registry.addHandler(customWebSocketHandler, "/ws")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*"); // 同时设置两者确保兼容性
    }
}
