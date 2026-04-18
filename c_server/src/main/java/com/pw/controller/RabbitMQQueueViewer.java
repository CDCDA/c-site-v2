package com.pw.controller;

import com.pw.config.RabbitMQConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RabbitMQ 队列查看工具
 * 用于查看队列信息和队列中的消息
 *
 * @author cyd
 * @create 2026/04/11
 */
@RestController
@Tag(name = "RabbitMQ队列查看", description = "RabbitMQ队列信息和消息查看工具")
@RequestMapping("/api/rabbitmq/queue")
@Slf4j
public class RabbitMQQueueViewer {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    /**
//     * 获取所有队列信息
//     */
//    @GetMapping("/list")
//    public Map<String, Object> listQueues() {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // 获取所有队列的名称
//            List<Queue> queues = rabbitAdmin.getQueues();
//
//            Map<String, Map<String, Object>> queueInfoMap = new HashMap<>();
//
//            for (Queue queue : queues) {
//                String queueName = queue.getName();
//                QueueInformation info = rabbitAdmin.getQueueInfo(queueName);
//
//                Map<String, Object> infoMap = new HashMap<>();
//                infoMap.put("name", queueName);
//                infoMap.put("messageCount", info.getMessageCount());
//                infoMap.put("consumerCount", info.getConsumerCount());
//
//                queueInfoMap.put(queueName, infoMap);
//            }
//
//            result.put("success", true);
//            result.put("queues", queueInfoMap);
//            result.put("total", queues.size());
//
//        } catch (Exception e) {
//            log.error("获取队列列表失败", e);
//            result.put("success", false);
//            result.put("error", e.getMessage());
//        }
//
//        return result;
//    }

    /**
     * 获取指定队列的详细信息
     */
    @GetMapping("/info/{queueName}")
    @Operation(summary = "获取队列详细信息")
    public Map<String, Object> getQueueInfo(@PathVariable String queueName) {
        Map<String, Object> result = new HashMap<>();

        try {
            QueueInformation info = rabbitAdmin.getQueueInfo(queueName);

            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("name", queueName);
            infoMap.put("messageCount", info.getMessageCount());
            infoMap.put("consumerCount", info.getConsumerCount());

            result.put("success", true);
            result.put("queueInfo", infoMap);

            log.info("队列 {} 信息: 消息数={}, 消费者数={}", 
                    queueName, info.getMessageCount(), info.getConsumerCount());

        } catch (Exception e) {
            log.error("获取队列 {} 信息失败", queueName, e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取队列中的消息（从队列中取出并返回）
     * 注意：这会从队列中移除消息！
     *
     * @param queueName 队列名称
     * @param count 获取消息数量，默认 1
     */
    @GetMapping("/get/{queueName}")
    @Operation(summary = "获取队列消息")
    public Map<String, Object> getMessages(@PathVariable String queueName,
                                           @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> messages = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                try {
                    Object message = rabbitTemplate.receiveAndConvert(queueName);
                    if (message != null) {
                        Map<String, Object> messageInfo = new HashMap<>();
                        messageInfo.put("index", i + 1);
                        messageInfo.put("message", message);
                        messageInfo.put("timestamp", System.currentTimeMillis());
                        messages.add(messageInfo);

                        log.info("从队列 {} 获取消息: {}", queueName, message);
                    } else {
                        log.info("队列 {} 中没有更多消息", queueName);
                        break;
                    }
                } catch (Exception e) {
                    log.error("获取消息失败", e);
                }
            }

            result.put("success", true);
            result.put("queueName", queueName);
            result.put("messageCount", messages.size());
            result.put("messages", messages);

            log.info("从队列 {} 获取了 {} 条消息", queueName, messages.size());

        } catch (Exception e) {
            log.error("从队列 {} 获取消息失败", queueName, e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 快捷查看 WebSocket 相关队列
     */
    @GetMapping("/websocket-status")
    @Operation(summary = "查看WebSocket队列状态")
    public Map<String, Object> getWebSocketQueueStatus() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Map<String, Object>> statusMap = new HashMap<>();

            // 控制队列
            QueueInformation controlInfo = rabbitAdmin.getQueueInfo(RabbitMQConfig.WEBSOCKET_CONTROL_QUEUE);
            Map<String, Object> controlMap = new HashMap<>();
            controlMap.put("name", RabbitMQConfig.WEBSOCKET_CONTROL_QUEUE);
            controlMap.put("messageCount", controlInfo.getMessageCount());
            controlMap.put("consumerCount", controlInfo.getConsumerCount());
            statusMap.put("控制队列", controlMap);

            // 广播队列
            QueueInformation broadcastInfo = rabbitAdmin.getQueueInfo(RabbitMQConfig.WEBSOCKET_BROADCAST_QUEUE);
            Map<String, Object> broadcastMap = new HashMap<>();
            broadcastMap.put("name", RabbitMQConfig.WEBSOCKET_BROADCAST_QUEUE);
            broadcastMap.put("messageCount", broadcastInfo.getMessageCount());
            broadcastMap.put("consumerCount", broadcastInfo.getConsumerCount());
            statusMap.put("广播队列", broadcastMap);

            // 控制队列死信队列
            QueueInformation controlDLQInfo = rabbitAdmin.getQueueInfo(RabbitMQConfig.WEBSOCKET_CONTROL_DLQ);
            Map<String, Object> controlDLQMap = new HashMap<>();
            controlDLQMap.put("name", RabbitMQConfig.WEBSOCKET_CONTROL_DLQ);
            controlDLQMap.put("messageCount", controlDLQInfo.getMessageCount());
            controlDLQMap.put("consumerCount", controlDLQInfo.getConsumerCount());
            statusMap.put("控制队列死信队列", controlDLQMap);

            // 广播队列死信队列
            QueueInformation broadcastDLQInfo = rabbitAdmin.getQueueInfo(RabbitMQConfig.WEBSOCKET_BROADCAST_DLQ);
            Map<String, Object> broadcastDLQMap = new HashMap<>();
            broadcastDLQMap.put("name", RabbitMQConfig.WEBSOCKET_BROADCAST_DLQ);
            broadcastDLQMap.put("messageCount", broadcastDLQInfo.getMessageCount());
            broadcastDLQMap.put("consumerCount", broadcastDLQInfo.getConsumerCount());
            statusMap.put("广播队列死信队列", broadcastDLQMap);

            result.put("success", true);
            result.put("queues", statusMap);

        } catch (Exception e) {
            log.error("获取 WebSocket 队列状态失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 查看死信队列中的消息
     */
    @GetMapping("/dlq/messages")
    @Operation(summary = "查看死信队列消息")
    public Map<String, Object> getDeadLetterMessages(@RequestParam(defaultValue = "5") int count) {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> dlqMessages = new HashMap<>();

            // 控制队列死信消息
            List<Map<String, Object>> controlDLQMsgs = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                try {
                    Object msg = rabbitTemplate.receiveAndConvert(RabbitMQConfig.WEBSOCKET_CONTROL_DLQ);
                    if (msg != null) {
                        Map<String, Object> msgMap = new HashMap<>();
                        msgMap.put("message", msg);
                        controlDLQMsgs.add(msgMap);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }

            // 广播队列死信消息
            List<Map<String, Object>> broadcastDLQMsgs = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                try {
                    Object msg = rabbitTemplate.receiveAndConvert(RabbitMQConfig.WEBSOCKET_BROADCAST_DLQ);
                    if (msg != null) {
                        Map<String, Object> msgMap = new HashMap<>();
                        msgMap.put("message", msg);
                        broadcastDLQMsgs.add(msgMap);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }

            dlqMessages.put("控制队列死信", controlDLQMsgs);
            dlqMessages.put("广播队列死信", broadcastDLQMsgs);

            result.put("success", true);
            result.put("messages", dlqMessages);

        } catch (Exception e) {
            log.error("获取死信队列消息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }
}
