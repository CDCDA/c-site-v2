# RabbitMQ 用户队列自动化管理方案

## 🎯 **核心设计理念**

### **问题：createUserQueue 需要手动调用吗？**

**答案：需要，但是可以自动化触发！**

---

## 📋 **两种方案对比**

### ❌ **方案一：完全被动创建（不推荐）**

```java
// 不预先创建队列，只在发送消息时依赖 RabbitMQ 自动创建
// 问题：
// 1. 消息可能丢失（如果队列不存在）
// 2. 无法控制监听器的启动时机
// 3. 调试困难
```

### ✅ **方案二：主动创建 + 自动化触发（推荐）**

```java
// 在 WebSocket 连接建立时自动创建用户队列
@OnOpen
public void onOpen(Session session) {
    Long userId = getUserIdFromSession(session);
    userQueueManager.createUserQueue(userId); // 自动触发
}

// 在 WebSocket 连接断开时删除用户队列
@OnClose
public void onClose() {
    Long userId = getUserId();
    userQueueManager.removeUserQueue(userId);
}
```

---

## 🔧 **实现细节**

### **1. RabbitMQConfig.java - 提供基础配置**

```java
@Configuration
public class RabbitMQConfig {
    
    // 交换机和队列常量定义
    public static final String WEBSOCKET_CONTROL_EXCHANGE = "websocket.control.exchange";
    public static final String USER_QUEUE_PREFIX = "websocket.user.";
    
    // 工具方法
    public static String getUserQueueName(Long userId) {
        return USER_QUEUE_PREFIX + userId;
    }
    
    public static String getUserRoutingKey(Long userId) {
        return String.format("user.%s", userId);
    }
    
    // 注意：不再提供 createUserQueue() Bean 方法
    // 因为用户队列是动态创建的，不是启动时就创建的
}
```

---

### **2. UserQueueManager.java - 自动化队列管理**

```java
@Service
public class UserQueueManager {
    
    @Autowired
    private RabbitAdmin rabbitAdmin; // 关键！用于自动声明
    
    /**
     * 为用户创建专属队列并启动监听（自动化）
     */
    public void createUserQueue(Long userId) {
        // 1. 检查是否已存在，避免重复创建
        if (hasUserQueue(userId)) {
            return;
        }
        
        // 2. 使用 RabbitAdmin 自动声明队列、交换机、绑定
        Queue queue = new Queue(getUserQueueName(userId), true);
        rabbitAdmin.declareQueue(queue);
        
        DirectExchange exchange = new DirectExchange(WEBSOCKET_CONTROL_EXCHANGE, true, false);
        rabbitAdmin.declareExchange(exchange);
        
        Binding binding = new Binding(...);
        rabbitAdmin.declareBinding(binding);
        
        // 3. 启动监听容器
        SimpleMessageListenerContainer container = createListenerContainer(queueName);
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

### **3. CustomWebSocketHandler.java - 集成点**

```java
@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {
    
    @Autowired
    private UserQueueManager userQueueManager;
    
    /**
     * WebSocket 连接建立时 - 自动创建用户队列
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserIdFromSession(session);
        
        // 🔥 关键：在这里自动创建用户队列
        userQueueManager.createUserQueue(userId);
        
        log.info("✅ WebSocket 连接建立，用户 {} 的队列已创建", userId);
    }
    
    /**
     * WebSocket 连接断开时 - 删除用户队列
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserIdFromSession(session);
        
        // 🔥 关键：在这里删除用户队列
        userQueueManager.removeUserQueue(userId);
        
        log.info("🗑️ WebSocket 连接关闭，用户 {} 的队列已删除", userId);
    }
    
    private Long getUserIdFromSession(WebSocketSession session) {
        // 从 session 中获取用户 ID
        Map<String, Object> attributes = session.getAttributes();
        return (Long) attributes.get("userId");
    }
}
```

---

## 🚀 **工作流程**

### **完整生命周期**

```
1. 用户登录 → 2. WebSocket 连接 → 3. 自动创建队列 → 4. 正常通信 → 5. 断开连接 → 6. 删除队列

┌─────────────┐      ┌──────────────┐      ┌─────────────┐      ┌─────────────┐
│ 用户打开网页 │ ──► │ 建立 WebSocket│ ──► │ createUser  │ ──► │ 接收个人消息│
│             │      │ 连接          │      │ Queue(userId)│      │             │
└─────────────┘      └──────────────┘      └─────────────┘      └─────────────┘
                                                                    │
                                                                    ▼
┌─────────────┐      ┌──────────────┐      ┌─────────────┐      ┌─────────────┐
│ 资源释放完成 │ ◄── │ removeUser  │ ◄── │ 用户关闭页面 │ ◄── │ 系统通知等  │
│             │      │ Queue(userId)│      │             │      │             │
└─────────────┘      └──────────────┘      └─────────────┘      └─────────────┘
```

---

## 💡 **为什么这样设计？**

### **优点：**

1. ✅ **按需创建**：只有用户真正连接时才创建队列，节省资源
2. ✅ **自动清理**：用户断开后立即删除队列，避免堆积
3. ✅ **易于管理**：每个用户的队列独立，互不干扰
4. ✅ **可追溯**：清楚知道哪些用户在线，哪些队列存在
5. ✅ **可靠性高**：使用 RabbitAdmin 确保队列一定存在

### **对比其他方案：**

| 方案 | 创建时机 | 删除时机 | 优点 | 缺点 |
|------|---------|---------|------|------|
| **启动时预创建** | 应用启动 | 永不删除 | 简单 | ❌ 浪费资源 |
| **发送消息时创建** | 第一次发消息 | 永不删除 | 延迟低 | ❌ 可能丢消息 |
| **连接时创建（推荐）** | WebSocket 连接 | WebSocket 断开 | ✅ 资源最优 | ⚠️ 需要集成 |

---

## 🔍 **RabbitAdmin 的作用**

### **什么是 RabbitAdmin？**

```java
@Bean
public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
}
```

**功能：**
- ✅ 自动向 RabbitMQ 服务器声明队列、交换机、绑定
- ✅ 如果已存在则忽略，不存在则创建
- ✅ 支持运行时动态声明

### **为什么要用 RabbitAdmin？**

```java
// ❌ 错误做法：只创建 Java 对象，不声明到 RabbitMQ
Queue queue = new Queue("my-queue", true);
// 这样只是创建了 Java 对象，RabbitMQ 服务器并不知道这个队列

// ✅ 正确做法：使用 RabbitAdmin 声明到服务器
rabbitAdmin.declareQueue(queue);
// RabbitMQ 服务器上现在有这个队列了
```

---

## 📝 **使用示例**

### **场景 1：用户 A 上线**

```java
// 1. 用户 A (userId=123) 打开网页
// 2. 前端建立 WebSocket 连接
// 3. afterConnectionEstablished() 被调用
// 4. userQueueManager.createUserQueue(123L)

// 日志输出：
🔧 为用户 123 创建专属队列：websocket.user.123, 路由键：user.123
📬 声明队列：websocket.user.123
📡 声明交换机：websocket.control.exchange
🔗 声明绑定：websocket.user.123 -> user.123
🎧 启动队列监听器：websocket.user.123
✅ 用户 123 的专属队列创建并启动成功
```

### **场景 2：发送个人消息给用户 A**

```java
// 发送消息（此时队列已经存在）
controlService.sendUserMessage(123L, Map.of(
    "message", "你有新的待办事项"
));

// 消息路由：
// 1. 发送到 websocket.control.exchange 交换机
// 2. 根据路由键 user.123 路由
// 3. 绑定到 websocket.user.123 队列
// 4. 监听器接收到消息
// 5. 通过 WebSocket 推送给用户 A
```

### **场景 3：用户 A 下线**

```java
// 1. 用户关闭浏览器
// 2. WebSocket 连接断开
// 3. afterConnectionClosed() 被调用
// 4. userQueueManager.removeUserQueue(123L)

// 日志输出：
🗑️ 已移除用户 123 的队列监听
🛑 停止队列：websocket.user.123
```

---

## ⚠️ **注意事项**

### **1. 必须在 WebSocket 处理器中集成**

```java
// ✅ 正确：在连接建立时创建
@OnOpen
public void onOpen() {
    userQueueManager.createUserQueue(userId);
}

// ❌ 错误：忘记创建队列
@OnOpen
public void onOpen() {
    // 什么都不做...等消息来了再说
    // 这样会导致第一条消息丢失！
}
```

### **2. 处理并发问题**

```java
// 使用 ConcurrentHashMap 保证线程安全
private final Map<Long, SimpleMessageListenerContainer> userListenerContainers 
    = new ConcurrentHashMap<>();

// 创建前检查，避免重复
if (hasUserQueue(userId)) {
    return; // 已存在，跳过
}
```

### **3. 异常处理**

```java
try {
    userQueueManager.createUserQueue(userId);
} catch (Exception e) {
    log.error("创建用户队列失败", e);
    // 可以选择拒绝 WebSocket 连接
    session.close();
}
```

---

## 🎯 **总结**

### **回答你的问题：createUserQueue 需要手动调用吗？**

**答案：需要在 WebSocket 连接事件中调用，但这是自动触发的。**

```java
// 你不需要记住每次发消息前都调用
// 只需要在 WebSocket 连接建立时调用一次即可

@OnOpen  // ← 这个注解会在连接建立时自动触发
public void onOpen() {
    userQueueManager.createUserQueue(userId); // ← 只需调用这一次
}
```

### **核心优势：**

1. ✅ **一次调用，终身受益**：连接建立时创建，之后所有消息都能正常接收
2. ✅ **自动化管理**：连接断开时自动删除，无需手动清理
3. ✅ **资源优化**：只为在线用户创建队列，离线用户不占用资源
4. ✅ **可靠性高**：使用 RabbitAdmin 确保队列一定存在于 RabbitMQ 服务器

---

## 📚 **相关文件**

- [`RabbitMQConfig.java`](./src/main/java/com/pw/config/RabbitMQConfig.java) - 基础配置
- [`UserQueueManager.java`](./src/main/java/com/pw/service/UserQueueManager.java) - 队列管理器
- [`WebSocketControlService.java`](./src/main/java/com/pw/service/WebSocketControlService.java) - 消息服务
- [`CustomWebSocketHandler.java`](./src/main/java/com/pw/common/handler/CustomWebSocketHandler.java) - WebSocket 处理器（需要集成）
