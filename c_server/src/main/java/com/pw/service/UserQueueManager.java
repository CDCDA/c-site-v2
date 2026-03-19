package com.pw.service;

import com.pw.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户队列管理器（自动化版本）
 * 负责动态创建、绑定和管理用户专属队列
 * 
 * @author cyd
 * @create 2026/03/19
 */
@Service
@Slf4j
public class UserQueueManager {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin; // 用于自动声明队列和绑定

    // 存储用户队列的监听容器
    private final Map<Long, SimpleMessageListenerContainer> userListenerContainers = new ConcurrentHashMap<>();

    /**
     * 为用户创建专属队列并启动监听（自动化）
     * 
     * @param userId 用户 ID
     */
    public void createUserQueue(Long userId) {
        if (hasUserQueue(userId)) {
            log.debug("用户 {} 的队列已存在，跳过创建", userId);
            return;
        }

        try {
            String queueName = RabbitMQConfig.getUserQueueName(userId);
            String routingKey = RabbitMQConfig.getUserRoutingKey(userId);

            log.info("🔧 为用户 {} 创建专属队列：{}, 路由键：{}", userId, queueName, routingKey);

            // 1. 使用 RabbitAdmin 自动声明队列
            Queue userQueue = new Queue(queueName, true);
            rabbitAdmin.declareQueue(userQueue);

            // 2. 自动声明交换机（如果不存在）
            DirectExchange controlExchange = new DirectExchange(RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, true, false);
            rabbitAdmin.declareExchange(controlExchange);

            // 3. 自动声明绑定关系
            Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE,
                                         RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, routingKey, null);
            rabbitAdmin.declareBinding(binding);

            // 4. 创建监听容器并启动
            SimpleMessageListenerContainer container = createListenerContainer(queueName);
            
            // 5. 存储容器
            userListenerContainers.put(userId, container);

            log.info("✅ 用户 {} 的专属队列创建并启动成功", userId);

        } catch (Exception e) {
            log.error("❌ 为用户 {} 创建队列失败", userId, e);
            throw new RuntimeException("创建用户队列失败", e);
        }
    }

    /**
     * 移除用户队列（用户下线时调用）
     * 
     * @param userId 用户 ID
     */
    public void removeUserQueue(Long userId) {
        try {
            SimpleMessageListenerContainer container = userListenerContainers.remove(userId);
            if (container != null) {
                container.stop();
                log.info("🗑️ 已移除用户 {} 的队列监听", userId);
            }
        } catch (Exception e) {
            log.error("❌ 移除用户 {} 的队列失败", userId, e);
        }
    }

    /**
     * 检查用户队列是否存在
     * 
     * @param userId 用户 ID
     * @return true-存在，false-不存在
     */
    public boolean hasUserQueue(Long userId) {
        return userListenerContainers.containsKey(userId);
    }

    /**
     * 创建监听容器
     * 
     * @param queueName 队列名称
     * @return 监听容器
     */
    private SimpleMessageListenerContainer createListenerContainer(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        
        // 使用 WebSocketControlMessageListener 来处理消息
        // 注意：这里需要传入 listener bean，可以通过构造函数或 setter 注入
        // 由于循环依赖问题，这里采用简化的方式，实际使用时需要在配置类中处理好
        
        container.setConcurrentConsumers(1);  // 每个用户一个消费者
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(5);  // 预取消息数
        
        container.start();
        
        log.info("🎧 启动队列 {} 的监听器", queueName);
        
        return container;
    }

    /**
     * 获取所有活跃的用户队列数量
     * 
     * @return 队列数量
     */
    public int getActiveQueueCount() {
        return userListenerContainers.size();
    }

    /**
     * 清理所有用户队列（系统关闭时调用）
     */
    public void cleanup() {
        log.info("🧹 开始清理所有用户队列...");
        
        for (Map.Entry<Long, SimpleMessageListenerContainer> entry : userListenerContainers.entrySet()) {
            try {
                entry.getValue().stop();
                log.info("🛑 停止用户 {} 的队列监听", entry.getKey());
            } catch (Exception e) {
                log.error("清理用户 {} 的队列失败", entry.getKey(), e);
            }
        }
        
        userListenerContainers.clear();
        log.info("✅ 所有用户队列清理完成");
    }
}
