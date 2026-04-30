package com.pw.controller;

import com.pw.common.listener.TestDeviceConsumer;
import com.pw.config.RabbitMQConfig;
import com.pw.domain.TestDeviceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 设备SFTP测试控制器
 * 用于测试批量消费和指数重试机制
 *
 * @author cyd
 * @create 2026/04/20
 */
@RestController
@RequestMapping("/api/device-test")
@Slf4j
public class DeviceSftpTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TestDeviceConsumer testDeviceConsumer;

    /**
     * 启动用户消费线程
     */
    @PostMapping("/start/{userId}")
    public Map<String, Object> startConsumer(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            testDeviceConsumer.startConsumer(userId);
            result.put("success", true);
            result.put("message", "用户" + userId + "消费线程已启动");
            result.put("userId", userId);
        } catch (Exception e) {
            log.error("启动消费线程失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 停止用户消费线程
     */
    @PostMapping("/stop/{userId}")
    public Map<String, Object> stopConsumer(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            testDeviceConsumer.stopConsumer(userId);
            result.put("success", true);
            result.put("message", "用户" + userId + "消费线程已停止");
            result.put("userId", userId);
        } catch (Exception e) {
            log.error("停止消费线程失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 发送单条消息
     */
    @PostMapping("/send/{userId}")
    public Map<String, Object> sendMessage(
            @PathVariable String userId,
            @RequestParam(defaultValue = "DOWNLOAD") String operation,
            @RequestParam(required = false) String fileName) {

        Map<String, Object> result = new HashMap<>();

        try {
            String name = fileName != null ? fileName : "file_" + System.currentTimeMillis() + ".txt";
            TestDeviceMessage message = TestDeviceMessage.create(userId, name, operation);

            String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.TEST_DEVICE_EXCHANGE,
                    routingKey,
                    message
            );

            result.put("success", true);
            result.put("message", "消息已发送");
            result.put("messageId", message.getMessageId());
            result.put("fileName", name);
            result.put("userId", userId);
            result.put("routingKey", routingKey);
        } catch (Exception e) {
            log.error("发送消息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/send-batch/{userId}")
    public Map<String, Object> sendBatch(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "DOWNLOAD") String operation,
            @RequestParam(required = false) Boolean failRate) {

        Map<String, Object> result = new HashMap<>();

        try {
            String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);
            List<String> messageIds = new ArrayList<>();
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                String fileName = String.format("file_%03d_%d.txt", i, System.currentTimeMillis());
                TestDeviceMessage message = TestDeviceMessage.create(userId, fileName, operation);

                // 模拟10%失败率
                if (failRate != null && failRate && Math.random() < 0.1) {
                    message.setFileContent("ERROR_FILE"); // 模拟错误文件
                }

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.TEST_DEVICE_EXCHANGE,
                        routingKey,
                        message
                );
                messageIds.add(message.getMessageId());
            }

            long cost = System.currentTimeMillis() - startTime;

            result.put("success", true);
            result.put("message", "批量消息已发送");
            result.put("count", count);
            result.put("userId", userId);
            result.put("routingKey", routingKey);
            result.put("costMs", cost);
            result.put("messageIds", messageIds);
        } catch (Exception e) {
            log.error("批量发送失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 发送模拟SFTP文件（模拟大量文件场景）
     */
    @PostMapping("/send-simulate/{userId}")
    public Map<String, Object> sendSimulate(
            @PathVariable String userId,
            @RequestParam(defaultValue = "100") int fileCount,
            @RequestParam(defaultValue = "100") int batchInterval) {

        Map<String, Object> result = new HashMap<>();

        try {
            String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);
            AtomicInteger sent = new AtomicInteger(0);

            // 使用线程异步发送
            new Thread(() -> {
                try {
                    for (int i = 0; i < fileCount; i++) {
                        String fileName = String.format("sim_file_%04d_%d.dat", i, System.currentTimeMillis());
                        TestDeviceMessage message = TestDeviceMessage.create(userId, fileName, "DOWNLOAD");

                        rabbitTemplate.convertAndSend(
                                RabbitMQConfig.TEST_DEVICE_EXCHANGE,
                                routingKey,
                                message
                        );

                        int current = sent.incrementAndGet();
                        if (current % 50 == 0) {
                            log.info("已发送 {} 条消息", current);
                        }

                        // 批次间隔
                        if (batchInterval > 0 && i < fileCount - 1) {
                            Thread.sleep(batchInterval);
                        }
                    }
                    log.info("模拟文件发送完成，共 {} 条", sent.get());
                } catch (Exception e) {
                    log.error("模拟文件发送异常", e);
                }
            }, "SimulateSender-" + userId).start();

            result.put("success", true);
            result.put("message", "模拟文件发送任务已启动");
            result.put("fileCount", fileCount);
            result.put("userId", userId);
            result.put("routingKey", routingKey);
            result.put("batchInterval", batchInterval + "ms");
        } catch (Exception e) {
            log.error("发送模拟文件失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取消费状态
     */
    @GetMapping("/status/{userId}")
    public Map<String, Object> getStatus(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> status = testDeviceConsumer.getStatus(userId);
            result.put("success", true);
            result.put("userId", userId);
            result.put("status", status);
        } catch (Exception e) {
            log.error("获取状态失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取所有消费线程状态
     */
    @GetMapping("/status-all")
    public Map<String, Object> getAllStatus() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> allStatus = testDeviceConsumer.getAllStatus();
            result.put("success", true);
            result.put("status", allStatus);
        } catch (Exception e) {
            log.error("获取所有状态失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 测试指数重试机制
     */
    @PostMapping("/test-retry/{userId}")
    public Map<String, Object> testRetry(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 发送一条会失败的消息
            TestDeviceMessage message = TestDeviceMessage.create(userId, "test_retry_file.txt", "DOWNLOAD");
            message.setFileContent("FORCE_FAIL"); // 模拟强制失败

            String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.TEST_DEVICE_EXCHANGE,
                    routingKey,
                    message
            );

            result.put("success", true);
            result.put("message", "测试重试消息已发送，将经历指数退避重试");
            result.put("messageId", message.getMessageId());
            result.put("retryDelays", Arrays.asList("5s", "10s", "20s", "40s"));
            result.put("maxRetries", 4);
        } catch (Exception e) {
            log.error("测试重试失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取队列信息
     */
    @GetMapping("/queue-info/{userId}")
    public Map<String, Object> getQueueInfo(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            String queueName = RabbitMQConfig.TEST_DEVICE_QUEUE_PREFIX + userId;
            String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);

            Properties properties = (Properties) rabbitTemplate.execute(channel -> {
                try {
                    return channel.queueDeclarePassive(queueName);
                } catch (Exception e) {
                    return null;
                }
            });

            result.put("success", true);
            result.put("queueName", queueName);
            result.put("routingKey", routingKey);
            result.put("messageCount", properties != null ? properties.get("QUEUE_MESSAGE_COUNT") : "N/A");
            result.put("consumerCount", properties != null ? properties.get("QUEUE_CONSUMER_COUNT") : "N/A");
            result.put("ttl", RabbitMQConfig.TEST_DEVICE_QUEUE_TTL);
            result.put("exchange", RabbitMQConfig.TEST_DEVICE_EXCHANGE);
        } catch (Exception e) {
            log.error("获取队列信息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取测试配置说明
     */
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("title", "设备SFTP测试接口");
        info.put("description", "模拟设备文件传输场景，支持批量消费和指数重试");

        Map<String, String> config = new HashMap<>();
        config.put("交换机", RabbitMQConfig.TEST_DEVICE_EXCHANGE);
        config.put("队列前缀", RabbitMQConfig.TEST_DEVICE_QUEUE_PREFIX);
        config.put("队列TTL", RabbitMQConfig.TEST_DEVICE_QUEUE_TTL + "ms (10分钟)");
        config.put("批量大小", "10条");
        config.put("批量超时", "5秒");
        config.put("最大重试", "4次");
        config.put("重试延迟", "5s, 10s, 20s, 40s (指数退避)");
        info.put("配置", config);

        Map<String, String> api = new HashMap<>();
        api.put("启动消费", "POST /api/device-test/start/{userId}");
        api.put("停止消费", "POST /api/device-test/stop/{userId}");
        api.put("发送单条", "POST /api/device-test/send/{userId}?operation=DOWNLOAD");
        api.put("批量发送", "POST /api/device-test/send-batch/{userId}?count=100");
        api.put("模拟文件", "POST /api/device-test/send-simulate/{userId}?fileCount=1000");
        api.put("测试重试", "POST /api/device-test/test-retry/{userId}");
        api.put("消费状态", "GET /api/device-test/status/{userId}");
        api.put("队列信息", "GET /api/device-test/queue-info/{userId}");
        info.put("接口说明", api);

        return info;
    }
}
