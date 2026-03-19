package com.pw.config;

import com.pw.common.listener.WebSocketControlMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
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

    // WebSocket 控制总线交换机
    public static final String WEBSOCKET_CONTROL_EXCHANGE = "websocket.control.exchange";
    
    // WebSocket 控制总线队列
    public static final String WEBSOCKET_CONTROL_QUEUE = "websocket.control.queue";
    
    // WebSocket 控制总线路由键
    public static final String WEBSOCKET_CONTROL_ROUTING_KEY = "websocket.control.#";

    /**
     * 创建直连交换机
     */
    @Bean
    public DirectExchange websocketControlExchange() {
        log.info("📡 创建 WebSocket 控制总线交换机：{}", WEBSOCKET_CONTROL_EXCHANGE);
        return ExchangeBuilder.directExchange(WEBSOCKET_CONTROL_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 创建控制总线队列
     */
    @Bean
    public Queue websocketControlQueue() {
        log.info("📬 创建 WebSocket 控制总线队列：{}", WEBSOCKET_CONTROL_QUEUE);
        return QueueBuilder.durable(WEBSOCKET_CONTROL_QUEUE)
                .build();
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding websocketControlBinding(DirectExchange websocketControlExchange, Queue websocketControlQueue) {
        log.info("🔗 绑定队列 {} 到交换机 {}", WEBSOCKET_CONTROL_QUEUE, WEBSOCKET_CONTROL_EXCHANGE);
        return BindingBuilder.bind(websocketControlQueue)
                .to(websocketControlExchange)
                .with(WEBSOCKET_CONTROL_ROUTING_KEY)
                .build();
    }

    /**
     * 创建消息监听容器
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
        
        return container;
    }
}
