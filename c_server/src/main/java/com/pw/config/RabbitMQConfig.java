package com.pw.config;

import com.pw.common.listener.DeadLetterQueueListener;
import com.pw.common.listener.RetryMessageListener;
import com.pw.common.listener.WebSocketControlMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 配置类
 *
 * @author cyd
 * @create 2026/03/18
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    // ==================== WebSocket 控制总线交换机 ====================

    // WebSocket 控制总线交换机（主交换机）- 使用 Topic 类型支持通配符
    public static final String WEBSOCKET_CONTROL_EXCHANGE = "websocket.control.exchange";

    // WebSocket 广播交换机
    public static final String WEBSOCKET_BROADCAST_EXCHANGE = "websocket.broadcast.exchange";

    // ==================== 死信交换机配置 ====================

    // WebSocket 控制队列死信交换机
    public static final String WEBSOCKET_CONTROL_DLX = "websocket.control.dlx";

    // WebSocket 广播队列死信交换机
    public static final String WEBSOCKET_BROADCAST_DLX = "websocket.broadcast.dlx";

    // ==================== 队列配置 ====================

    // WebSocket 控制总线队列（处理所有用户消息和系统控制消息）
    public static final String WEBSOCKET_CONTROL_QUEUE = "websocket.control.queue";

    // WebSocket 广播队列
    public static final String WEBSOCKET_BROADCAST_QUEUE = "websocket.broadcast.queue";

    // WebSocket 控制队列死信队列
    public static final String WEBSOCKET_CONTROL_DLQ = "websocket.control.dlq";

    // WebSocket 广播队列死信队列
    public static final String WEBSOCKET_BROADCAST_DLQ = "websocket.broadcast.dlq";

    // ==================== 路由键配置 ====================

    // WebSocket 控制总线路由键（匹配所有用户消息：user.*）
    public static final String WEBSOCKET_CONTROL_ROUTING_KEY = "user.#";

    // WebSocket 广播路由键
    public static final String WEBSOCKET_BROADCAST_ROUTING_KEY = "broadcast.#";

    /**
     * 获取用户路由键
     */
    public static String getUserRoutingKey(Long userId) {
        return "user." + userId;
    }

    // ==================== 死信队列路由键配置 ====================

    // WebSocket 控制队列死信路由键
    public static final String WEBSOCKET_CONTROL_DLQ_ROUTING_KEY = "websocket.control.dlq";

    // WebSocket 广播队列死信路由键
    public static final String WEBSOCKET_BROADCAST_DLQ_ROUTING_KEY = "websocket.broadcast.dlq";

    // ==================== 重试机制配置（延迟消息插件） ====================

    // 延迟消息交换机（使用 RabbitMQ 延迟插件）
    public static final String WEBSOCKET_RETRY_EXCHANGE = "websocket.retry.delayed.exchange";

    // 重试队列（接收延迟交换机到期后的消息）
    public static final String WEBSOCKET_RETRY_QUEUE = "websocket.retry.queue";

    // 最终失败死信交换机
    public static final String WEBSOCKET_FINAL_DLX = "websocket.final.dlx";

    // 最终失败死信队列（达到最大重试次数后）
    public static final String WEBSOCKET_FINAL_DLQ = "websocket.final.dlq";

    // 重试队列路由键
    public static final String WEBSOCKET_RETRY_ROUTING_KEY = "websocket.retry";

    // 最终失败死信队列路由键
    public static final String WEBSOCKET_FINAL_DLQ_ROUTING_KEY = "websocket.final.dlq";

    /**
     * 创建延迟消息交换机（使用 RabbitMQ 延迟插件）
     */
    @Bean
    public CustomExchange websocketRetryExchange() {
        log.info("📡 创建 WebSocket 延迟消息交换机：{} (type: x-delayed-message)", WEBSOCKET_RETRY_EXCHANGE);

        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");  // 延迟交换机的实际类型为 direct

        return new CustomExchange(WEBSOCKET_RETRY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 创建 Topic 交换机（支持通配符路由）
     */
    @Bean
    public TopicExchange websocketControlExchange() {
        log.info("📡 创建 WebSocket 控制总线 Topic 交换机：{}", WEBSOCKET_CONTROL_EXCHANGE);
        return new TopicExchange(WEBSOCKET_CONTROL_EXCHANGE, true, false);
    }

    /**
     * 创建控制总线队列（配置死信队列）
     */
    @Bean
    public Queue websocketControlQueue() {
        log.info("📬 创建 WebSocket 控制总线队列：{}", WEBSOCKET_CONTROL_QUEUE);
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", WEBSOCKET_CONTROL_DLX);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", WEBSOCKET_CONTROL_DLQ_ROUTING_KEY);

        return new Queue(WEBSOCKET_CONTROL_QUEUE, true, false, false, args);
    }

    /**
     * 绑定队列到 Topic 交换机
     */
    @Bean
    public Binding websocketControlBinding(@Qualifier("websocketControlExchange") TopicExchange websocketControlExchange,
                                          Queue websocketControlQueue) {
        log.info("🔗 绑定队列 {} 到 Topic 交换机 {}，绑定键：{}",
                WEBSOCKET_CONTROL_QUEUE, WEBSOCKET_CONTROL_EXCHANGE, WEBSOCKET_CONTROL_ROUTING_KEY);
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
     * 创建广播队列（配置死信队列）
     */
    @Bean
    public Queue websocketBroadcastQueue() {
        log.info("📬 创建 WebSocket 广播队列：{}", WEBSOCKET_BROADCAST_QUEUE);
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", WEBSOCKET_BROADCAST_DLX);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", WEBSOCKET_BROADCAST_DLQ_ROUTING_KEY);

        return new Queue(WEBSOCKET_BROADCAST_QUEUE, true, false, false, args);
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
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认

        return container;
    }

    /**
     * 创建控制总线监听容器（处理所有用户消息）
     */
    @Bean
    public SimpleMessageListenerContainer websocketControlListenerContainer(
            ConnectionFactory connectionFactory,
            WebSocketControlMessageListener messageListener) {

        log.info("🎧 创建 WebSocket 控制总线监听器");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(WEBSOCKET_CONTROL_QUEUE);
        container.setMessageListener(messageListener);
        container.setConcurrentConsumers(3); // 并发消费者数量
        container.setMaxConcurrentConsumers(10); // 最大消费者数量
        container.setPrefetchCount(10); // 预取消息数量
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认

        return container;
    }

    // ==================== 死信队列配置 ====================

    /**
     * 创建控制队列死信交换机
     */
    @Bean
    public DirectExchange websocketControlDLX() {
        log.info("📡 创建 WebSocket 控制队列死信交换机：{}", WEBSOCKET_CONTROL_DLX);
        return new DirectExchange(WEBSOCKET_CONTROL_DLX, true, false);
    }

    /**
     * 创建控制队列死信队列
     */
    @Bean
    public Queue websocketControlDLQ() {
        log.info("📬 创建 WebSocket 控制队列死信队列：{}", WEBSOCKET_CONTROL_DLQ);
        return new Queue(WEBSOCKET_CONTROL_DLQ, true);
    }

    /**
     * 绑定控制队列死信队列到死信交换机
     */
    @Bean
    public Binding websocketControlDLQBinding(DirectExchange websocketControlDLX, Queue websocketControlDLQ) {
        log.info("🔗 绑定死信队列 {} 到死信交换机 {}", WEBSOCKET_CONTROL_DLQ, WEBSOCKET_CONTROL_DLX);
        return new Binding(websocketControlDLQ.getName(), Binding.DestinationType.QUEUE,
                websocketControlDLX.getName(), WEBSOCKET_CONTROL_DLQ_ROUTING_KEY, null);
    }

    /**
     * 创建广播队列死信交换机
     */
    @Bean
    public DirectExchange websocketBroadcastDLX() {
        log.info("📡 创建 WebSocket 广播队列死信交换机：{}", WEBSOCKET_BROADCAST_DLX);
        return new DirectExchange(WEBSOCKET_BROADCAST_DLX, true, false);
    }

    /**
     * 创建广播队列死信队列
     */
    @Bean
    public Queue websocketBroadcastDLQ() {
        log.info("📬 创建 WebSocket 广播队列死信队列：{}", WEBSOCKET_BROADCAST_DLQ);
        return new Queue(WEBSOCKET_BROADCAST_DLQ, true);
    }

    /**
     * 绑定广播队列死信队列到死信交换机
     */
    @Bean
    public Binding websocketBroadcastDLQBinding(DirectExchange websocketBroadcastDLX, Queue websocketBroadcastDLQ) {
        log.info("🔗 绑定死信队列 {} 到死信交换机 {}", WEBSOCKET_BROADCAST_DLQ, WEBSOCKET_BROADCAST_DLX);
        return new Binding(websocketBroadcastDLQ.getName(), Binding.DestinationType.QUEUE,
                websocketBroadcastDLX.getName(), WEBSOCKET_BROADCAST_DLQ_ROUTING_KEY, null);
    }

    // ==================== RabbitTemplate 配置 ====================

    /**
     * JSON 消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置 RabbitTemplate（避免循环依赖）
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("🔧 开始配置 RabbitTemplate");

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // ⚠️ 关键设置：强制返回无法路由的消息
        // 如果消息无法路由到任何队列，必须返回给生产者，否则 ReturnsCallback 不会被触发
        rabbitTemplate.setMandatory(true);

        // 启用发送确认（生产者确认）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("✅ 消息成功发送到 RabbitMQ 交换机，correlationId: {}",
                        correlationData != null ? correlationData.getId() : "null");
            } else {
                log.error("❌ 消息发送到 RabbitMQ 交换机失败，correlationId: {}, 原因: {}",
                        correlationData != null ? correlationData.getId() : "null", cause);
            }
        });

        // 启用返回确认（消息无法路由时回调）
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("❌⚠️ 消息无法路由到队列 - 交换机: {}, 路由键: {}, 回复码: {}, 回复文本: {}",
                    returned.getExchange(), returned.getRoutingKey(),
                    returned.getReplyCode(), returned.getReplyText());

            // 记录失败的消息
            log.error("❌⚠️ 失败的消息内容: {}", new String(returned.getMessage().getBody()));
            //发送到死信队列
            rabbitTemplate.convertAndSend(
                    WEBSOCKET_CONTROL_DLX,
                    WEBSOCKET_CONTROL_DLQ_ROUTING_KEY,
                    Map.of("message", returned.getMessage()
                    ));
        });

        log.info("✅ RabbitTemplate 配置完成 - 已启用 ConfirmCallback 和 ReturnsCallback，Mandatory: true");

        return rabbitTemplate;
    }

    // ==================== 死信队列监听器配置 ====================

    /**
     * 创建控制队列死信队列监听容器
     */
    @Bean
    public SimpleMessageListenerContainer websocketControlDLQListenerContainer(
            ConnectionFactory connectionFactory,
            DeadLetterQueueListener deadLetterQueueListener) {

        log.info("🎧 创建 WebSocket 控制队列死信监听器");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(WEBSOCKET_CONTROL_DLQ);
        container.setMessageListener(deadLetterQueueListener);
        container.setConcurrentConsumers(1); // 死信队列消费者数量不宜过多
        container.setMaxConcurrentConsumers(3);
        container.setPrefetchCount(5); // 预取消息数量
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认

        return container;
    }

    /**
     * 创建广播队列死信队列监听容器
     */
    @Bean
    public SimpleMessageListenerContainer websocketBroadcastDLQListenerContainer(
            ConnectionFactory connectionFactory,
            DeadLetterQueueListener deadLetterQueueListener) {

        log.info("🎧 创建 WebSocket 广播队列死信监听器");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(WEBSOCKET_BROADCAST_DLQ);
        container.setMessageListener(deadLetterQueueListener);
        container.setConcurrentConsumers(1); // 死信队列消费者数量不宜过多
        container.setMaxConcurrentConsumers(3);
        container.setPrefetchCount(5); // 预取消息数量
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认

        return container;
    }

    // ==================== 重试机制配置（延迟消息插件） ====================

    /**
     * 创建最终失败死信交换机
     */
    @Bean
    public DirectExchange websocketFinalDLX() {
        log.info("📡 创建 WebSocket 最终失败死信交换机：{}", WEBSOCKET_FINAL_DLX);
        return new DirectExchange(WEBSOCKET_FINAL_DLX, true, false);
    }

    /**
     * 创建重试队列
     * 此队列接收延迟交换机到期后的消息，由监听器处理重试逻辑
     */
    @Bean
    public Queue websocketRetryQueue() {
        log.info("📬 创建 WebSocket 重试队列：{}", WEBSOCKET_RETRY_QUEUE);
        return new Queue(WEBSOCKET_RETRY_QUEUE, true);
    }

    /**
     * 绑定重试队列到延迟交换机
     */
    @Bean
    public Binding websocketRetryQueueBinding(@Qualifier("websocketRetryExchange") CustomExchange websocketRetryExchange,
                                         Queue websocketRetryQueue) {
        log.info("🔗 绑定重试队列 {} 到延迟交换机 {}，路由键：{}",
                WEBSOCKET_RETRY_QUEUE, WEBSOCKET_RETRY_EXCHANGE, WEBSOCKET_RETRY_ROUTING_KEY);
        return new Binding(WEBSOCKET_RETRY_QUEUE, Binding.DestinationType.QUEUE,
                WEBSOCKET_RETRY_EXCHANGE, WEBSOCKET_RETRY_ROUTING_KEY, null);
    }

    /**
     * 创建最终失败死信队列
     * 达到最大重试次数后的消息放入此队列
     */
    @Bean
    public Queue websocketFinalDLQ() {
        log.info("📬 创建 WebSocket 最终失败死信队列：{}", WEBSOCKET_FINAL_DLQ);
        return new Queue(WEBSOCKET_FINAL_DLQ, true);
    }

    /**
     * 绑定最终失败死信队列到死信交换机
     */
    @Bean
    public Binding websocketFinalDLQBinding(DirectExchange websocketFinalDLX, Queue websocketFinalDLQ) {
        log.info("🔗 绑定最终失败死信队列 {} 到死信交换机 {}，路由键：{}",
                WEBSOCKET_FINAL_DLQ, WEBSOCKET_FINAL_DLX, WEBSOCKET_FINAL_DLQ_ROUTING_KEY);
        return new Binding(WEBSOCKET_FINAL_DLQ, Binding.DestinationType.QUEUE,
                WEBSOCKET_FINAL_DLX, WEBSOCKET_FINAL_DLQ_ROUTING_KEY, null);
    }

    // ==================== 重试队列监听容器配置 ====================

    /**
     * 创建重试队列监听容器
     */
    @Bean
    public SimpleMessageListenerContainer websocketRetryQueueListenerContainer(
            ConnectionFactory connectionFactory,
            RetryMessageListener retryMessageListener) {

        log.info("🎧 创建 WebSocket 重试队列监听器");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(WEBSOCKET_RETRY_QUEUE);
        container.setMessageListener(retryMessageListener::handleMessage);
        container.setConcurrentConsumers(3); // 并发消费者数量
        container.setMaxConcurrentConsumers(10); // 最大消费者数量
        container.setPrefetchCount(10); // 预取消息数量
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 自动确认（处理成功自动ACK）

        return container;
    }
}
