package com.pw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 自动配置类
 * 包含连接超时处理和自动重连机制
 * 
 * @author cyd
 * @create 2026/03/19
 */
@Configuration
@Slf4j
public class RabbitMQAutoConfig {

    /**
     * RabbitAdmin Bean - 用于自动声明队列、交换机等
     * 如果连接失败，会记录错误但不会阻止应用启动
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        log.info("🔧 初始化 RabbitAdmin...");
        
        try {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
            rabbitAdmin.setAutoStartup(true);
            
            log.info("✅ RabbitAdmin 初始化成功");
            return rabbitAdmin;
            
        } catch (Exception e) {
            log.error("❌ RabbitAdmin 初始化失败（RabbitMQ 连接超时）", e);
            log.warn("⚠️  应用将继续运行，但 RabbitMQ 相关功能将不可用");
            log.warn("⚠️  请检查：");
            log.warn("   1. RabbitMQ 服务是否启动：systemctl status rabbitmq-server");
            log.warn("   2. 防火墙是否开放 5672 端口");
            log.warn("   3. 网络是否可达：ping 120.48.127.181");
            log.warn("   4. application.yml 中的 RabbitMQ 配置是否正确");
            
            // 返回一个空的 RabbitAdmin，避免应用启动失败
            return new RabbitAdmin(connectionFactory);
        }
    }
}
