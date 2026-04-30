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

    // ==================== 死信交换机配置 ====================

    // WebSocket 控制队列死信交换机
    public static final String WEBSOCKET_CONTROL_DLX = "websocket.control.dlx";

    // ==================== 队列配置 ====================

    // WebSocket 控制总线队列（处理所有用户消息和系统控制消息）
    public static final String WEBSOCKET_CONTROL_QUEUE = "websocket.control.queue";

    // WebSocket 控制队列死信队列
    public static final String WEBSOCKET_CONTROL_DLQ = "websocket.control.dlq";

    // ==================== 路由键配置 ====================

    // WebSocket 控制总线路由键（匹配所有用户消息：user.*）
    public static final String WEBSOCKET_CONTROL_ROUTING_KEY = "user.#";

    /**
     * 获取用户路由键
     */
    public static String getUserRoutingKey(Long userId) {
        return "user." + userId;
    }

    // ==================== 死信队列路由键配置 ====================

    // WebSocket 控制队列死信路由键
    public static final String WEBSOCKET_CONTROL_DLQ_ROUTING_KEY = "websocket.control.dlq";

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

    // ==================== 测试队列配置（模拟设备SFTP场景） ====================

    // 测试交换机
    public static final String TEST_DEVICE_EXCHANGE = "test.device.exchange";

    // 测试队列前缀（每个用户一个队列：test.device.queue.{userId}）
    public static final String TEST_DEVICE_QUEUE_PREFIX = "test.device.queue.";

    // 测试队列死信交换机
    public static final String TEST_DEVICE_DLX = "test.device.dlx";

    // 测试队列死信队列
    public static final String TEST_DEVICE_DLQ = "test.device.dlq";

    // 测试队列TTL（10分钟）
    public static final long TEST_DEVICE_QUEUE_TTL = 10 * 60 * 1000;

    // 测试路由键前缀
    public static final String TEST_DEVICE_ROUTING_KEY_PREFIX = "device.";

    /**
     * 创建延迟消息交换机（使用 RabbitMQ 延迟插件）
     */
    @Bean
    public CustomExchange websocketRetryExchange() {
        log.info("📡 创建 WebSocket 延迟消息交换机：{} (type: x-delayed-message)", WEBSOCKET_RETRY_EXCHANGE);

        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");

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
        args.put("x-dead-letter-exchange", WEBSOCKET_CONTROL_DLX);
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
        container.setConcurrentConsumers(3);
        container.setMaxConcurrentConsumers(10);
        container.setPrefetchCount(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

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

    // ==================== RabbitTemplate 配置 ====================

    /**
     * JSON 消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置 RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("🔧 开始配置 RabbitTemplate");

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("✅ 消息成功发送到 RabbitMQ 交换机，correlationId: {}",
                        correlationData != null ? correlationData.getId() : "null");
            } else {
                log.error("❌ 消息发送到 RabbitMQ 交换机失败，correlationId: {}, 原因: {}",
                        correlationData != null ? correlationData.getId() : "null", cause);
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("❌⚠️ 消息无法路由到队列 - 交换机: {}, 路由键: {}, 回复码: {}, 回复文本: {}",
                    returned.getExchange(), returned.getRoutingKey(),
                    returned.getReplyCode(), returned.getReplyText());
            log.error("❌⚠️ 失败的消息内容: {}", new String(returned.getMessage().getBody()));
            rabbitTemplate.convertAndSend(
                    WEBSOCKET_CONTROL_DLX,
                    WEBSOCKET_CONTROL_DLQ_ROUTING_KEY,
                    Map.of("message", returned.getMessage())
            );
        });

        log.info("✅ RabbitTemplate 配置完成");
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
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(3);
        container.setPrefetchCount(5);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

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
                websocketFinalDLX.getName(), WEBSOCKET_FINAL_DLQ_ROUTING_KEY, null);
    }

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
        container.setConcurrentConsumers(3);
        container.setMaxConcurrentConsumers(10);
        container.setPrefetchCount(10);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return container;
    }

    // ==================== 测试队列配置（模拟设备SFTP场景） ====================

    /**
     * 创建设备测试交换机（Direct类型）
     */
    @Bean
    public DirectExchange testDeviceExchange() {
        log.info("📡 创建设备测试交换机：{}", TEST_DEVICE_EXCHANGE);
        return new DirectExchange(TEST_DEVICE_EXCHANGE, true, false);
    }

    /**
     * 创建设备测试死信交换机
     */
    @Bean
    public DirectExchange testDeviceDLX() {
        log.info("📡 创建设备测试死信交换机：{}", TEST_DEVICE_DLX);
        return new DirectExchange(TEST_DEVICE_DLX, true, false);
    }

    /**
     * 创建设备测试死信队列
     */
    @Bean
    public Queue testDeviceDLQ() {
        log.info("📬 创建设备测试死信队列：{}", TEST_DEVICE_DLQ);
        return new Queue(TEST_DEVICE_DLQ, true);
    }

    /**
     * 绑定设备测试死信队列
     */
    @Bean
    public Binding testDeviceDLQBinding(DirectExchange testDeviceDLX, Queue testDeviceDLQ) {
        log.info("🔗 绑定设备测试死信队列 {} 到交换机 {}", TEST_DEVICE_DLQ, TEST_DEVICE_DLX);
        return new Binding(testDeviceDLQ.getName(), Binding.DestinationType.QUEUE,
                testDeviceDLX.getName(), "test.device.dlq", null);
    }

    /**
     * 动态创建设备测试队列（带TTL）
     */
    public Queue createTestDeviceQueue(String userId) {
        String queueName = TEST_DEVICE_QUEUE_PREFIX + userId;
        log.info("📬 动态创建设备测试队列：{}，TTL：{}ms", queueName, TEST_DEVICE_QUEUE_TTL);

        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", TEST_DEVICE_QUEUE_TTL);
        args.put("x-dead-letter-exchange", TEST_DEVICE_DLX);
        args.put("x-dead-letter-routing-key", "test.device.dlq");

        return new Queue(queueName, true, false, false, args);
    }

    /**
     * 获取设备测试路由键
     */
    public static String getTestDeviceRoutingKey(String userId) {
        return TEST_DEVICE_ROUTING_KEY_PREFIX + userId;
    }
}
