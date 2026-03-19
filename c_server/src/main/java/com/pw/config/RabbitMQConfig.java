package com.pw.config;

import com.pw.common.listener.WebSocketControlMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * @author cyd
 * @create 2026/03/18
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    // ==================== WebSocket 控制总线交换机 ====================

    // WebSocket 控制总线交换机（主交换机）
    public static final String WEBSOCKET_CONTROL_EXCHANGE = "websocket.control.exchange";

    // WebSocket 广播交换机
    public static final String WEBSOCKET_BROADCAST_EXCHANGE = "websocket.broadcast.exchange";

    // ==================== 队列配置 ====================

    // WebSocket 控制总线队列（保留用于向后兼容）
    public static final String WEBSOCKET_CONTROL_QUEUE = "websocket.control.queue";

    // WebSocket 广播队列
    public static final String WEBSOCKET_BROADCAST_QUEUE = "websocket.broadcast.queue";

    // 用户队列前缀（动态创建）
    public static final String USER_QUEUE_PREFIX = "websocket.user.";

    // ==================== 路由键配置 ====================

    // WebSocket 控制总线路由键
    public static final String WEBSOCKET_CONTROL_ROUTING_KEY = "websocket.control.#";

    // WebSocket 广播路由键
    public static final String WEBSOCKET_BROADCAST_ROUTING_KEY = "broadcast.#";

    // 用户路由键模板
    public static final String USER_ROUTING_KEY_TEMPLATE = "user.%s";

    /**
     * 创建直连交换机
     */
    @Bean
    public DirectExchange websocketControlExchange() {
        log.info("📡 创建 WebSocket 控制总线交换机：{}", WEBSOCKET_CONTROL_EXCHANGE);
        return new DirectExchange(WEBSOCKET_CONTROL_EXCHANGE, true, false);
    }

    /**
     * 创建控制总线队列
     */
    @Bean
    public Queue websocketControlQueue() {
        log.info("📬 创建 WebSocket 控制总线队列：{}", WEBSOCKET_CONTROL_QUEUE);
        return new Queue(WEBSOCKET_CONTROL_QUEUE, true);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding websocketControlBinding(DirectExchange websocketControlExchange, Queue websocketControlQueue) {
        log.info("🔗 绑定队列 {} 到交换机 {}", WEBSOCKET_CONTROL_QUEUE, WEBSOCKET_CONTROL_EXCHANGE);
        return new Binding(websocketControlQueue.getName(), Binding.DestinationType.QUEUE,
                          websocketControlExchange.getName(), WEBSOCKET_CONTROL_ROUTING_KEY, null);
    }

    /**
     * 创建广播交换机
     */
    @Bean
    public DirectExchange websocketBroadcastExchange() {
        log.info("📡 创建 WebSocket 广播交换机：{}", WEBSOCKET_BROADCAST_EXCHANGE);
        return new DirectExchange(WEBSOCKET_BROADCAST_EXCHANGE, true, false);
    }

    /**
     * 创建广播队列
     */
    @Bean
    public Queue websocketBroadcastQueue() {
        log.info("📬 创建 WebSocket 广播队列：{}", WEBSOCKET_BROADCAST_QUEUE);
        return new Queue(WEBSOCKET_BROADCAST_QUEUE, true);
    }

    /**
     * 绑定广播队列到交换机
     */
    @Bean
    public Binding websocketBroadcastBinding(DirectExchange websocketBroadcastExchange, Queue websocketBroadcastQueue) {
        log.info("🔗 绑定广播队列 {} 到交换机 {}", WEBSOCKET_BROADCAST_QUEUE, WEBSOCKET_BROADCAST_EXCHANGE);
        return new Binding(websocketBroadcastQueue.getName(), Binding.DestinationType.QUEUE,
                          websocketBroadcastExchange.getName(), WEBSOCKET_BROADCAST_ROUTING_KEY, null);
    }

    /**
     * 创建广播监听容器
     */
    @Bean
    public SimpleMessageListenerContainer websocketBroadcastListenerContainer(
            ConnectionFactory connectionFactory,
            WebSocketControlMessageListener messageListener) {

        log.info("🎧 创建 WebSocket 广播监听器");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(WEBSOCKET_BROADCAST_QUEUE);
        container.setMessageListener(messageListener);
        container.setConcurrentConsumers(3); // 并发消费者数量
        container.setMaxConcurrentConsumers(10); // 最大消费者数量
        container.setPrefetchCount(10); // 预取消息数量

        return container;
    }

    /**
     * 获取用户队列名称
     */
    public static String getUserQueueName(Long userId) {
        return USER_QUEUE_PREFIX + userId;
    }

    /**
     * 获取用户路由键
     */
    public static String getUserRoutingKey(Long userId) {
        return String.format(USER_ROUTING_KEY_TEMPLATE, userId);
    }
}
