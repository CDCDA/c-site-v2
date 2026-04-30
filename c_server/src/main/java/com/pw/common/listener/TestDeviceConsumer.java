package com.pw.common.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.config.RabbitMQConfig;
import com.pw.domain.TestDeviceMessage;
import com.pw.service.WebSocketControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 测试设备消费者（模拟SFTP场景）
 * 每个用户一个消费线程，支持批量消费和指数重试
 *
 * @author cyd
 * @create 2026/04/20
 */
@Slf4j
@Component
public class TestDeviceConsumer {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebSocketControlService webSocketControlService;

    // 存储每个用户的消费容器
    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    // 模拟处理中的任务（用于批量处理）
    private final Map<String, BlockingQueue<TestDeviceMessage>> batchBuffers = new ConcurrentHashMap<>();

    // 批量处理大小
    private static final int BATCH_SIZE = 10;

    // 批量处理超时（毫秒）
    private static final long BATCH_TIMEOUT = 5000;

    /**
     * 启动用户消费线程
     */
    public void startConsumer(String userId) {
        if (containers.containsKey(userId)) {
            log.warn("用户{}的消费线程已存在", userId);
            return;
        }

        String queueName = RabbitMQConfig.TEST_DEVICE_QUEUE_PREFIX + userId;
        String routingKey = RabbitMQConfig.getTestDeviceRoutingKey(userId);

        // 声明队列
        rabbitTemplate.execute(channel -> {
            Map<String, Object> args = new java.util.HashMap<>();
            args.put("x-message-ttl", RabbitMQConfig.TEST_DEVICE_QUEUE_TTL);
            args.put("x-dead-letter-exchange", RabbitMQConfig.TEST_DEVICE_DLX);
            args.put("x-dead-letter-routing-key", "test.device.dlq");
            channel.queueDeclare(queueName, true, false, false, args);
            channel.queueBind(queueName, RabbitMQConfig.TEST_DEVICE_EXCHANGE, routingKey);
            return null;
        });

        // 创建批量缓冲区
        batchBuffers.put(userId, new LinkedBlockingQueue<>());

        // 创建消费容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(BATCH_SIZE);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        // 设置消息监听器
        container.setMessageListener(message -> {
            try {
                processMessage(message, userId);
            } catch (Exception e) {
                log.error("用户{}消息处理异常", userId, e);
                handleError(message, userId, e);
            }
        });

        container.start();
        containers.put(userId, container);

        // 启动批量处理线程
        startBatchProcessor(userId);

        log.info("✅ 用户{}的消费线程已启动，队列：{}，批量大小：{}，TTL：{}ms",
                userId, queueName, BATCH_SIZE, RabbitMQConfig.TEST_DEVICE_QUEUE_TTL);

        // WebSocket通知
        notifyWebSocket(userId, "消费者启动", "success");
    }

    /**
     * 停止用户消费线程
     */
    public void stopConsumer(String userId) {
        SimpleMessageListenerContainer container = containers.remove(userId);
        if (container != null) {
            container.stop();
            log.info("🛑 用户{}的消费线程已停止", userId);
            notifyWebSocket(userId, "消费者停止", "info");
        }

        BlockingQueue<TestDeviceMessage> buffer = batchBuffers.remove(userId);
        if (buffer != null && !buffer.isEmpty()) {
            log.warn("用户{}缓冲区还有{}条未处理消息", userId, buffer.size());
        }
    }

    /**
     * 处理单条消息
     */
    private void processMessage(Message message, String userId) throws Exception {
        TestDeviceMessage task = parseMessage(message);

        log.info("📥 用户{}收到消息：{}，操作：{}", userId, task.getFileName(), task.getOperation());

        // 模拟处理：下载 -> 读取 -> 删除
        boolean success = processTask(task);

        if (success) {
            // 确认消息
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            rabbitTemplate.execute(channel -> {
                channel.basicAck(deliveryTag, false);
                return null;
            });

            log.info("✅ 用户{}消息处理成功：{}", userId, task.getFileName());
            notifyWebSocket(userId, "处理成功: " + task.getFileName(), "success");
        } else {
            throw new RuntimeException("处理失败");
        }
    }

    /**
     * 处理任务（模拟SFTP操作）
     */
    private boolean processTask(TestDeviceMessage task) {
        try {
            // 模拟网络延迟
            Thread.sleep(100);

            // 模拟随机失败（10%概率）
            if (Math.random() < 0.1) {
                log.warn("⚠️ 模拟处理失败：{}", task.getFileName());
                return false;
            }

            // 模拟操作
            switch (task.getOperation()) {
                case "DOWNLOAD":
                    log.debug("📥 下载文件：{}", task.getFileName());
                    break;
                case "UPLOAD":
                    log.debug("📤 上传文件：{}", task.getFileName());
                    break;
                case "DELETE":
                    log.debug("🗑️ 删除文件：{}", task.getFileName());
                    break;
                default:
                    log.debug("📄 处理文件：{}", task.getFileName());
            }

            return true;
        } catch (Exception e) {
            log.error("处理任务异常", e);
            return false;
        }
    }

    /**
     * 处理错误（发送到重试队列）
     */
    private void handleError(Message message, String userId, Exception e) {
        try {
            TestDeviceMessage task = parseMessage(message);
            task.incrementRetry();

            long deliveryTag = message.getMessageProperties().getDeliveryTag();

            // 拒绝原消息（不重入队）
            rabbitTemplate.execute(channel -> {
                channel.basicReject(deliveryTag, false);
                return null;
            });

            // 检查重试次数
            if (task.getRetryCount() >= 4) {
                // 进入最终死信队列
                log.error("💀 用户{}消息达到最大重试次数，进入死信队列：{}", userId, task.getFileName());
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.WEBSOCKET_FINAL_DLX,
                        RabbitMQConfig.WEBSOCKET_FINAL_DLQ_ROUTING_KEY,
                        task
                );
                notifyWebSocket(userId, "消息进入死信队列: " + task.getFileName(), "error");
            } else {
                // 发送到延迟重试队列
                long delay = getRetryDelay(task.getRetryCount());
                log.warn("⏳ 用户{}消息第{}次重试，延迟{}ms：{}",
                        userId, task.getRetryCount(), delay, task.getFileName());

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.WEBSOCKET_RETRY_EXCHANGE,
                        RabbitMQConfig.WEBSOCKET_RETRY_ROUTING_KEY,
                        task,
                        msg -> {
                            msg.getMessageProperties().setDelayLong(delay);
                            return msg;
                        }
                );
                notifyWebSocket(userId, "消息重试(" + task.getRetryCount() + "): " + task.getFileName(), "warning");
            }
        } catch (Exception ex) {
            log.error("处理错误时发生异常", ex);
        }
    }

    /**
     * 获取重试延迟（指数退避）
     */
    private long getRetryDelay(int retryCount) {
        long[] delays = {5000, 10000, 20000, 40000}; // 5s, 10s, 20s, 40s
        return delays[Math.min(retryCount - 1, delays.length - 1)];
    }

    /**
     * 解析消息
     */
    private TestDeviceMessage parseMessage(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), TestDeviceMessage.class);
    }

    /**
     * 启动批量处理器
     */
    private void startBatchProcessor(String userId) {
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "BatchProcessor-" + userId);
            t.setDaemon(true);
            return t;
        });

        executor.submit(() -> {
            BlockingQueue<TestDeviceMessage> buffer = batchBuffers.get(userId);
            if (buffer == null) return;

            while (containers.containsKey(userId)) {
                try {
                    List<TestDeviceMessage> batch = new ArrayList<>();

                    // 等待第一条消息
                    TestDeviceMessage first = buffer.poll(BATCH_TIMEOUT, TimeUnit.MILLISECONDS);
                    if (first == null) continue;
                    batch.add(first);

                    // 收集更多消息（非阻塞）
                    long deadline = System.currentTimeMillis() + BATCH_TIMEOUT;
                    while (batch.size() < BATCH_SIZE && System.currentTimeMillis() < deadline) {
                        TestDeviceMessage msg = buffer.poll(100, TimeUnit.MILLISECONDS);
                        if (msg != null) batch.add(msg);
                    }

                    // 批量处理
                    if (!batch.isEmpty()) {
                        processBatch(batch, userId);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    /**
     * 批量处理
     */
    private void processBatch(List<TestDeviceMessage> batch, String userId) {
        log.info("📦 用户{}批量处理{}条消息", userId, batch.size());

        int success = 0;
        int failed = 0;

        for (TestDeviceMessage task : batch) {
            if (processTask(task)) {
                success++;
            } else {
                failed++;
            }
        }

        log.info("✅ 用户{}批量处理完成：成功{}，失败{}", userId, success, failed);
        notifyWebSocket(userId, String.format("批量处理: %d成功, %d失败", success, failed), "info");
    }

    /**
     * WebSocket通知
     */
    private void notifyWebSocket(String userId, String message, String type) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "userId", userId,
                    "message", message,
                    "type", type,
                    "timestamp", System.currentTimeMillis()
            ));
            webSocketControlService.sendUserMessage(Long.valueOf(userId), payload);
        } catch (Exception e) {
            log.error("WebSocket通知失败", e);
        }
    }

    /**
     * 获取消费状态
     */
    public Map<String, Object> getStatus(String userId) {
        Map<String, Object> status = new HashMap<>();
        SimpleMessageListenerContainer container = containers.get(userId);

        if (container != null && container.isRunning()) {
            status.put("running", true);
            status.put("queueNames", container.getQueueNames());
            status.put("activeConsumerCount", container.getActiveConsumerCount());
        } else {
            status.put("running", false);
        }

        BlockingQueue<TestDeviceMessage> buffer = batchBuffers.get(userId);
        status.put("bufferSize", buffer != null ? buffer.size() : 0);

        return status;
    }

    /**
     * 获取所有消费线程状态
     */
    public Map<String, Object> getAllStatus() {
        Map<String, Object> allStatus = new HashMap<>();
        allStatus.put("totalConsumers", containers.size());
        allStatus.put("consumers", new HashMap<String, Object>());

        @SuppressWarnings("unchecked")
        Map<String, Object> consumerStatus = (Map<String, Object>) allStatus.get("consumers");

        for (String userId : containers.keySet()) {
            consumerStatus.put(userId, getStatus(userId));
        }

        return allStatus;
    }

    public void shutdown() {
        log.info("🛑 关闭所有设备消费者");
        new ArrayList<>(containers.keySet()).forEach(this::stopConsumer);
    }
}
