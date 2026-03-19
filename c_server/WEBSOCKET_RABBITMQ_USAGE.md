# WebSocket RabbitMQ 控制总线使用指南

## 概述

WebSocket 控制总线允许你通过 RabbitMQ 消息队列来控制 WebSocket 消息的发送，实现解耦和异步通信。

## 架构说明

```
REST API / 其他服务 → RabbitMQ → WebSocketControlMessageListener → CustomWebSocketHandler → WebSocket 客户端
```

## 配置说明

### 1. RabbitMQ 连接配置（application.yml）

```yaml
spring:
  rabbitmq:
    host: your-rabbitmq-host
    port: 5672
    username: your-username
    password: your-password
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual  # 手动 ACK
        prefetch: 10
        concurrency: 3
        max-concurrency: 10
```

### 2. RabbitMQ 拓扑结构

- **交换机**: `websocket.control.exchange` (直连交换机)
- **队列**: `websocket.control.queue` (持久化队列)
- **路由键**: `websocket.control.#`

## 使用方法

### 方式一：通过 REST API 发送（推荐用于测试）

#### 1. 广播消息到指定频道

```bash
POST http://localhost:7000/websocket/control/broadcast/{channel}
Content-Type: application/json

{
  "your": "data",
  "message": "hello"
}
```

**示例**：
```bash
curl -X POST http://localhost:7000/websocket/control/broadcast/system_notice \
  -H "Content-Type: application/json" \
  -d '{"title":"系统通知","content":"系统即将维护","status":"warning"}'
```

#### 2. 发送消息给特定用户

```bash
POST http://localhost:7000/websocket/control/user/{userId}
Content-Type: application/json

{
  "type": "message",
  "content": "你好"
}
```

**示例**：
```bash
curl -X POST http://localhost:7000/websocket/control/user/123 \
  -H "Content-Type: application/json" \
  -d '{"type":"notification","content":"你有新的待办事项"}'
```

#### 3. 发送系统通知

```bash
POST http://localhost:7000/websocket/control/system-notice?title=标题&content=内容&status=primary
```

**示例**：
```bash
curl -X POST "http://localhost:7000/websocket/control/system-notice?title=系统升级&content=今晚 23 点进行系统升级&status=warning"
```

#### 4. 发送磁盘信息更新

```bash
POST http://localhost:7000/websocket/control/disk-info
Content-Type: application/json

{
  "diskName": "C 盘",
  "totalSpace": 500000000000,
  "freeSpace": 200000000000,
  "usagePercent": 60.5
}
```

#### 5. 发送待办事项通知

```bash
POST http://localhost:7000/websocket/control/todo-notification
Content-Type: application/json

{
  "todoId": 1,
  "title": "完成报告",
  "description": "请在周五前提交",
  "priority": "high"
}
```

#### 6. 发送自定义控制消息

```bash
POST http://localhost:7000/websocket/control/custom?type=your_type
Content-Type: application/json

{
  "customField1": "value1",
  "customField2": "value2"
}
```

### 方式二：直接在代码中使用 WebSocketControlService

```java
@Autowired
private WebSocketControlService webSocketControlService;

// 1. 广播消息到频道
webSocketControlService.sendBroadcastMessage("disk_info", diskInfo);

// 2. 发送给特定用户
webSocketControlService.sendUserMessage(userId, message);

// 3. 发送系统通知
webSocketControlService.sendSystemNotice("系统通知", "通知内容", "warning");

// 4. 发送磁盘信息
webSocketControlService.sendDiskInfo(diskInfoMap);

// 5. 发送待办事项通知
webSocketControlService.sendTodoNotification(todoInfoMap);

// 6. 发送自定义消息
Map<String, Object> payload = new HashMap<>();
payload.put("field1", "value1");
webSocketControlService.sendControlMessage("custom_type", payload);
```

### 方式三：直接发送消息到 RabbitMQ

适用于其他微服务或外部系统：

```java
// 消息格式
{
  "type": "broadcast",  // 或 user_message, system_notice, disk_info, todo_notification
  "channel": "system_notice",  // 仅 broadcast 类型需要
  "userId": 123,  // 仅 user_message 类型需要
  "data": {  // 消息内容
    "your": "data"
  },
  "timestamp": 1710764800000
}
```

**示例代码**：
```java
@Autowired
private RabbitTemplate rabbitTemplate;

// 发送广播消息
Map<String, Object> message = new HashMap<>();
message.put("type", "broadcast");
message.put("channel", "system_notice");
message.put("data", Map.of("title", "通知", "content", "内容"));
message.put("timestamp", System.currentTimeMillis());

rabbitTemplate.convertAndSend(
    "websocket.control.exchange",
    "websocket.control.#",
    message
);
```

## 消息类型说明

| 消息类型 | 说明 | 必需字段 | 可选字段 |
|---------|------|---------|---------|
| broadcast | 广播到指定频道 | channel, data | - |
| user_message | 发送给特定用户 | userId, message | - |
| system_notice | 系统通知 | title, content | status |
| disk_info | 磁盘信息更新 | data | - |
| todo_notification | 待办事项通知 | data | - |

## WebSocket 客户端订阅

客户端连接到 WebSocket 后，可以订阅不同的频道：

```javascript
const ws = new WebSocket('ws://localhost:7000/ws');

ws.onopen = () => {
  // 订阅系统通知频道
  ws.send(JSON.stringify({
    type: 'subscribe',
    channel: 'system_notice'
  }));
  
  // 订阅磁盘信息频道
  ws.send(JSON.stringify({
    type: 'subscribe',
    channel: 'disk_info'
  }));
};

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log('收到消息:', message);
};
```

## 可用的频道

- `disk_info`: 磁盘信息
- `todo_list`: 待办事项
- `system_notice`: 系统通知
- `user_message`: 用户消息

## 完整示例

### Spring Boot 服务中发送通知

```java
@Service
public class NotificationService {
    
    @Autowired
    private WebSocketControlService webSocketControlService;
    
    public void notifyDiskWarning(String diskName, double usagePercent) {
        if (usagePercent > 90) {
            Map<String, Object> diskInfo = new HashMap<>();
            diskInfo.put("diskName", diskName);
            diskInfo.put("usagePercent", usagePercent);
            diskInfo.put("level", "critical");
            
            // 通过 RabbitMQ 发送到 WebSocket
            webSocketControlService.sendDiskInfo(diskInfo);
            
            // 同时发送系统通知
            webSocketControlService.sendSystemNotice(
                "磁盘空间警告",
                String.format("磁盘 %s 使用率已达 %.1f%%", diskName, usagePercent),
                "error"
            );
        }
    }
    
    public void notifyNewTodo(Long userId, String todoTitle) {
        Map<String, Object> todoInfo = new HashMap<>();
        todoInfo.put("title", todoTitle);
        todoInfo.put("createTime", System.currentTimeMillis());
        
        // 发送待办事项通知
        webSocketControlService.sendTodoNotification(todoInfo);
        
        // 发送给用户
        webSocketControlService.sendUserMessage(userId, Map.of(
            "type", "new_todo",
            "title", todoTitle
        ));
    }
}
```

## 注意事项

1. **RabbitMQ 连接**: 确保 RabbitMQ 服务正常运行
2. **手动 ACK**: 消息处理成功后会手动确认，失败会重新入队
3. **并发配置**: 可根据实际情况调整消费者数量
4. **消息格式**: 确保发送的消息符合 JSON 格式
5. **频道订阅**: 只有订阅了频道的用户才能收到对应消息

## 监控和调试

查看日志输出了解消息流转情况：
- 📨 收到 RabbitMQ 控制消息
- 📢 向频道广播消息
- 👤 向用户发送消息
- ✅ 控制消息发送成功
- ❌ 处理失败时的错误信息
