# RabbitMQ 多队列架构升级指南

## 📋 架构升级概述

### 原架构（单队列）的问题

```
┌─────────────┐      ┌──────────────────┐      ┌─────────────┐
│ 所有消息    │ ───► │ websocket.control│ ───► │ 竞争消费    │
│ (混合)      │      │ .queue           │      │ (问题点)    │
└─────────────┘      └──────────────────┘      └─────────────┘
```

**存在的问题：**
1. ❌ **广播消息无法真正广播**：一条消息只会被一个消费者处理，无法发送给所有在线用户
2. ❌ **消息互相干扰**：不同用户的消息在同一个队列，处理慢的用户会阻塞其他用户
3. ❌ **无法精准投递**：难以实现针对特定用户的消息推送
4. ❌ **监控困难**：无法区分哪些消息积压是哪个用户导致的

---

### 新架构（广播 + 个人队列）

```
┌─────────────────────────────────────────────────────────────┐
│                      RabbitMQ                               │
│                                                             │
│  ┌─────────────────┐      ┌─────────────────┐              │
│  │ broadcast.      │      │ control.        │              │
│  │ exchange        │      │ exchange        │              │
│  └────────┬────────┘      └────────┬────────┘              │
│           │                        │                        │
│           ├────────────┐          ├─────────────────┐      │
│           │            │          │                 │      │
│           ▼            ▼          ▼                 ▼      │
│  ┌─────────────┐ ┌──────────┐ ┌────────┐   ┌────────────┐ │
│  │ broadcast.  │ │ system_  │ │ user.1 │   │ user.2     │ │
│  │ queue       │ │ notice   │ │ queue  │   │ queue      │ │
│  │ (广播)      │ │ (系统)   │ │ (用户) │   │ (用户)     │ │
│  └─────────────┘ └──────────┘ └────────┘   └────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**架构优势：**
- ✅ **真正的广播**：广播消息可以被多个消费者同时接收
- ✅ **个人专属队列**：每个用户独立队列，互不干扰
- ✅ **精准投递**：可以精确发送给指定用户
- ✅ **易于监控**：清楚看到每个用户的消息处理情况
- ✅ **灵活扩展**：可以为不同用户设置不同优先级

---

## 🏗️ 架构设计详解

### 队列规划

#### 1. 广播队列（Broadcast Queue）
```java
// 队列名称
websocket.broadcast.queue

// 交换机
websocket.broadcast.exchange

// 路由键
broadcast.#

// 用途
- 系统通知广播
- 磁盘信息更新
- 公共频道消息
- 全局公告
```

#### 2. 用户专属队列（User Queue）
```java
// 队列名称格式
websocket.user.{userId}

// 示例
websocket.user.123  // 用户 ID=123 的队列
websocket.user.456  // 用户 ID=456 的队列

// 交换机
websocket.control.exchange

// 路由键格式
user.{userId}

// 用途
- 个人消息
- 待办事项通知
- 私聊消息
- 个性化推送
```

---

## 💻 核心代码改动

### 1. RabbitMQConfig.java

```java
@Configuration
public class RabbitMQConfig {
    
    // ========== 交换机配置 ==========
    
    // WebSocket 控制总线交换机（主交换机）
    public static final String WEBSOCKET_CONTROL_EXCHANGE = "websocket.control.exchange";
    
    // WebSocket 广播交换机
    public static final String WEBSOCKET_BROADCAST_EXCHANGE = "websocket.broadcast.exchange";
    
    // ========== 队列配置 ==========
    
    // WebSocket 广播队列
    public static final String WEBSOCKET_BROADCAST_QUEUE = "websocket.broadcast.queue";
    
    // 用户队列前缀（动态创建）
    public static final String USER_QUEUE_PREFIX = "websocket.user.";
    
    // ========== 路由键配置 ==========
    
    // WebSocket 广播路由键
    public static final String WEBSOCKET_BROADCAST_ROUTING_KEY = "broadcast.#";
    
    // 用户路由键模板
    public static final String USER_ROUTING_KEY_TEMPLATE = "user.%s";
    
    // Bean: 创建广播交换机、队列、绑定
    @Bean
    public DirectExchange websocketBroadcastExchange() {
        return new DirectExchange(WEBSOCKET_BROADCAST_EXCHANGE, true, false);
    }
    
    @Bean
    public Queue websocketBroadcastQueue() {
        return new Queue(WEBSOCKET_BROADCAST_QUEUE, true);
    }
    
    @Bean
    public Binding websocketBroadcastBinding(...) {
        return new Binding(...);
    }
    
    // 动态创建用户队列
    public Queue createUserQueue(Long userId) {
        String queueName = getUserQueueName(userId);
        return new Queue(queueName, true);
    }
    
    public static String getUserQueueName(Long userId) {
        return USER_QUEUE_PREFIX + userId;
    }
    
    public static String getUserRoutingKey(Long userId) {
        return String.format(USER_ROUTING_KEY_TEMPLATE, userId);
    }
}
```

### 2. WebSocketControlService.java

```java
@Service
public class WebSocketControlService {
    
    /**
     * 发送广播消息（所有订阅广播频道的用户都会收到）
     */
    public void sendBroadcastMessage(String channel, Object data) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "broadcast");
        message.put("channel", channel);
        message.put("data", data);
        
        // 发送到广播交换机
        sendMessageToRabbitMQ(message, 
                             RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE, 
                             RabbitMQConfig.WEBSOCKET_BROADCAST_ROUTING_KEY);
    }
    
    /**
     * 发送消息给特定用户（通过用户独立队列）
     */
    public void sendUserMessage(Long userId, Object messageData) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "user_message");
        message.put("userId", userId);
        message.put("message", messageData);
        
        // 发送到用户队列
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, 
                             RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, 
                             userRoutingKey);
    }
    
    /**
     * 发送待办事项通知（发送给特定用户）
     */
    public void sendTodoNotification(Long userId, Object todoInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "todo_notification");
        message.put("userId", userId);
        message.put("data", todoInfo);
        
        // 发送到用户队列
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, 
                             RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, 
                             userRoutingKey);
    }
}
```

### 3. UserQueueManager.java（新增）

```java
@Service
public class UserQueueManager {
    
    // 存储用户队列的监听容器
    private final Map<Long, SimpleMessageListenerContainer> userListenerContainers 
        = new ConcurrentHashMap<>();
    
    /**
     * 为用户创建专属队列并启动监听
     */
    public void createUserQueue(Long userId) {
        String queueName = RabbitMQConfig.getUserQueueName(userId);
        String routingKey = RabbitMQConfig.getUserRoutingKey(userId);
        
        // 1. 创建队列
        Queue userQueue = new Queue(queueName, true);
        
        // 2. 绑定队列到交换机
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE,
                                     RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, 
                                     routingKey, null);
        
        // 3. 创建监听容器
        SimpleMessageListenerContainer container = createListenerContainer(queueName);
        
        // 4. 存储容器
        userListenerContainers.put(userId, container);
    }
    
    /**
     * 移除用户队列（用户下线时调用）
     */
    public void removeUserQueue(Long userId) {
        SimpleMessageListenerContainer container = userListenerContainers.remove(userId);
        if (container != null) {
            container.stop();
        }
    }
}
```

---

## 🚀 使用示例

### 场景 1：发送系统广播通知

```java
@Autowired
private WebSocketControlService controlService;

// 向所有在线用户发送系统维护通知
controlService.sendSystemNotice(
    "系统维护通知",
    "今晚 22:00 进行系统维护，预计持续 2 小时",
    "warning"
);

// 或者使用通用广播方法
controlService.sendBroadcastMessage("system_notice", Map.of(
    "title", "放假通知",
    "content", "五一劳动节放假安排",
    "date", "2026-05-01"
));
```

### 场景 2：发送个人消息

```java
// 给用户 ID=123 发送私信
controlService.sendUserMessage(123L, Map.of(
    "type", "notification",
    "message", "你有新的待办事项",
    "priority", "high"
));
```

### 场景 3：待办事项通知

```java
// 创建待办事项后，通知对应用户
Todo todo = todoService.create(todoData);

// 发送给创建者
controlService.sendTodoNotification(todo.getUserId(), Map.of(
    "todoId", todo.getId(),
    "title", todo.getTitle(),
    "action", "create"
));
```

### 场景 4：磁盘监控广播

```java
// 定时任务检测磁盘空间
@Scheduled(fixedRate = 60000) // 每分钟检测一次
public void checkDiskSpace() {
    DiskInfo diskInfo = diskService.getDiskInfo();
    
    if (diskInfo.getUsagePercent() > 80) {
        // 广播警告给所有管理员
        controlService.sendDiskInfo(Map.of(
            "usage", diskInfo.getUsagePercent(),
            "free", diskInfo.getFreeSpace(),
            "level", "warning"
        ));
    }
}
```

---

## 🔧 配置参数

### application.yml

```yaml
spring:
  rabbitmq:
    host: 120.48.127.181
    port: 5672
    username: admin
    password: 123456
    virtual-host: /
    
    listener:
      simple:
        acknowledge-mode: manual    # 手动 ACK
        prefetch: 10                # 预取消息数
        concurrency: 3              # 初始并发消费者
        max-concurrency: 10         # 最大并发消费者
```

---

## 📊 监控和管理

### 1. 查看队列状态

```bash
# RabbitMQ Management UI
http://your-rabbitmq-host:15672

# 登录后查看：
- Queues 标签页：查看所有队列
- Exchanges 标签页：查看所有交换机
- Bindings 标签页：查看绑定关系
```

### 2. 监控指标

```java
@Autowired
private UserQueueManager queueManager;

// 获取活跃用户队列数量
int activeCount = queueManager.getActiveQueueCount();
log.info("当前活跃用户队列数：{}", activeCount);
```

### 3. 日志监控

```
关键日志标识：
📡 - 创建交换机
📬 - 创建队列
🔗 - 绑定关系
🎧 - 启动监听器
📤 - 发送消息
✅ - 发送成功
❌ - 发送失败
```

---

## ⚠️ 注意事项

### 1. 用户队列的生命周期

```java
// WebSocket 连接建立时创建队列
@OnOpen
public void onOpen(Session session) {
    Long userId = getCurrentUserId();
    userQueueManager.createUserQueue(userId);
}

// WebSocket 连接断开时删除队列
@OnClose
public void onClose() {
    Long userId = getCurrentUserId();
    userQueueManager.removeUserQueue(userId);
}
```

### 2. 消息确认机制

```java
// 手动 ACK - 确保消息不丢失
@Override
public void onMessage(Message message, Channel channel) {
    try {
        // 处理消息
        processMessage(message);
        
        // 处理成功后确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        
    } catch (Exception e) {
        // 处理失败，重新入队
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), 
                         false, true);
    }
}
```

### 3. 性能优化建议

```yaml
# 广播队列 - 可以多设置几个消费者
concurrency: 5
max-concurrency: 20

# 用户队列 - 每个用户一个消费者即可
concurrency: 1
max-concurrency: 1
prefetch: 5
```

---

## 🔄 迁移步骤

### 从单队列迁移到多队列

1. **备份现有数据**
   ```bash
   # 导出当前队列配置
   rabbitmqadmin export backup.json
   ```

2. **部署新代码**
   ```bash
   ./mvnw clean package
   java -jar target/c-server-1.0.0.jar
   ```

3. **验证新队列创建**
   ```bash
   # 访问 RabbitMQ Management UI
   # 检查以下队列是否存在：
   # - websocket.broadcast.queue
   # - websocket.user.* (动态创建)
   ```

4. **测试广播功能**
   ```bash
   curl -X POST http://localhost:7000/websocket/control/broadcast/test \
     -H "Content-Type: application/json" \
     -d '{"message":"测试广播"}'
   ```

5. **测试个人消息**
   ```bash
   curl -X POST http://localhost:7000/websocket/control/user/123 \
     -H "Content-Type: application/json" \
     -d '{"message":"测试个人消息"}'
   ```

---

## 📈 性能对比

| 指标 | 单队列架构 | 多队列架构 |
|------|-----------|-----------|
| 广播效率 | ❌ 低（只能发给一个消费者） | ✅ 高（可发给所有订阅者） |
| 个人消息隔离 | ❌ 无 | ✅ 完全隔离 |
| 消息积压处理 | ❌ 困难 | ✅ 容易定位 |
| 扩展性 | ❌ 差 | ✅ 好 |
| 监控难度 | ❌ 高 | ✅ 低 |
| 资源消耗 | ✅ 低 | ⚠️ 中等（可接受） |

---

## 🎯 总结

### 改进后的优势

1. **真正的广播能力**：支持一对多消息推送
2. **精准个人推送**：每个用户独立队列，互不干扰
3. **易于监控和调试**：清楚看到每个队列的状态
4. **灵活扩展**：可以为 VIP 用户设置高优先级队列
5. **故障隔离**：单个用户队列问题不影响其他用户

### 适用场景

- ✅ 需要全局通知的系统
- ✅ 用户量中等的平台（< 10 万在线）
- ✅ 需要精准推送的场景
- ✅ 对消息可靠性要求高的系统

### 未来进一步优化方向

1. **Topic Exchange**：更灵活的路由规则
2. **死信队列**：处理失败消息
3. **优先级队列**：VIP 用户优先处理
4. **消息过期**：自动清理过期消息
5. **延迟队列**：支持定时消息

---

## 📚 参考资料

- [Spring AMQP 官方文档](https://docs.spring.io/spring-amqp/reference/)
- [RabbitMQ 官方文档](https://www.rabbitmq.com/documentation.html)
- [WebSocket 控制总线设计思路](./WEBSOCKET_RABBITMQ_USAGE.md)
