package com.pw.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类
 * 提供 Redisson 客户端，支持分布式锁、分布式集合等功能
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;

    /**
     * 配置 RedissonClient
     * 使用单节点模式连接 Redis
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 构建单节点 Redis 地址
        String address = "redis://" + redisHost + ":" + redisPort;

        // 使用单节点配置
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase)
                .setPassword(redisPassword.isEmpty() ? null : redisPassword)
                .setConnectionPoolSize(20)  // 连接池大小
                .setConnectionMinimumIdleSize(5)  // 最小空闲连接
                .setTimeout(3000)  // 连接超时时间（毫秒）
                .setRetryAttempts(3)  // 重试次数
                .setRetryInterval(1500);  // 重试间隔（毫秒）

        return Redisson.create(config);
    }
}
