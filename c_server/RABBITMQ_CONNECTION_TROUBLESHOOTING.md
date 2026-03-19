# RabbitMQ 连接超时问题解决方案

## ❌ **错误信息**

```
org.springframework.amqp.AmqpConnectException: java.net.ConnectException: Connection timed out: getsockopt
```

## 🔍 **问题原因**

这个错误表示应用无法连接到 RabbitMQ 服务器，常见原因：

1. **RabbitMQ 服务未启动**
2. **防火墙阻止了 5672 端口**
3. **网络不可达**
4. **配置的连接超时时间太短**
5. **RabbitMQ 服务器宕机或重启中**

---

## ✅ **解决方案（按优先级排序）**

### **方案 1：检查并启动 RabbitMQ 服务（最推荐）**

#### **1. SSH 登录服务器**
```bash
ssh root@120.48.127.181
```

#### **2. 检查 RabbitMQ 状态**
```bash
systemctl status rabbitmq-server
```

**正常输出示例：**
```
● rabbitmq-server.service - RabbitMQ broker
   Loaded: loaded (/usr/lib/systemd/system/rabbitmq-server.service; enabled)
   Active: active (running) since Thu 2026-03-19 10:00:00 CST
```

**异常输出（未运行）：**
```
● rabbitmq-server.service - RabbitMQ broker
   Loaded: loaded (/usr/lib/systemd/system/rabbitmq-server.service; enabled)
   Active: inactive (dead)
```

#### **3. 启动 RabbitMQ**
```bash
# 启动服务
systemctl start rabbitmq-server

# 设置开机自启
systemctl enable rabbitmq-server

# 查看日志确认启动成功
journalctl -u rabbitmq-server -n 50
```

#### **4. 验证端口监听**
```bash
# 检查 5672 端口是否正在监听
netstat -tlnp | grep 5672

# 应该看到类似输出：
# tcp        0      0 0.0.0.0:5672            0.0.0.0:*               LISTEN      1234/beam.smp
```

---

### **方案 2：配置防火墙规则**

#### **如果使用的是 firewalld（CentOS 7+）**
```bash
# 查看防火墙状态
firewall-cmd --state

# 如果返回 "running"，需要开放 5672 端口
firewall-cmd --permanent --add-port=5672/tcp

# 开放管理界面端口（可选）
firewall-cmd --permanent --add-port=15672/tcp

# 重新加载防火墙规则
firewall-cmd --reload

# 验证端口是否开放
firewall-cmd --list-ports
```

#### **如果使用的是 iptables**
```bash
# 添加规则允许 5672 端口
iptables -A INPUT -p tcp --dport 5672 -j ACCEPT

# 保存规则
service iptables save
```

#### **如果是云服务器（阿里云、腾讯云等）**
需要在云控制台的**安全组**中添加入站规则：
- 协议类型：TCP
- 端口范围：5672
- 授权对象：0.0.0.0/0（或指定 IP）

---

### **方案 3：测试网络连通性**

#### **从本地测试**
```bash
# ping 服务器
ping 120.48.127.181

# telnet 测试端口（如果安装了 telnet）
telnet 120.48.127.181 5672

# 或使用 nc 命令
nc -zv 120.48.127.181 5672
```

**成功标志：**
```
Connected to 120.48.127.181.
Escape character is '^]'.
```

**失败标志：**
```
Connection timed out
或
Connection refused
```

---

### **方案 4：优化应用配置（已完成）**

我已经优化了 `application.yml` 配置，增加了：

```yaml
spring:
  rabbitmq:
    # 连接超时配置
    connection-timeout: 60000  # 60 秒超时（更宽容）
    requested-heartbeat: 60    # 心跳间隔
    
    # 连接池配置
    cache:
      connection:
        mode: channel
        size: 10
      channel:
        size: 25
        checkout-timeout: 30000
    
    # 重试机制
    listener:
      simple:
        retry:
          enabled: true
          initial-interval: 1000
          max-attempts: 3
          max-interval: 10000
          multiplier: 2.0
```

**这些配置的作用：**
- ✅ 增加连接超时时间，避免短暂网络波动导致失败
- ✅ 自动重试机制，提高容错能力
- ✅ 连接池优化，提升性能

---

### **方案 5：临时禁用 RabbitMQ（开发环境）**

如果你暂时不需要 RabbitMQ 功能，可以切换到无 RabbitMQ 模式：

#### **方法 1：修改 application.yml**
```yaml
spring:
  profiles:
    active: no-rabbitmq  # 切换到无 RabbitMQ 模式
```

#### **方法 2：启动时指定**
```bash
java -jar target/c-server-1.0.0.jar --spring.profiles.active=no-rabbitmq
```

**注意：** 在无 RabbitMQ 模式下，以下功能将不可用：
- ❌ WebSocket 实时消息推送
- ❌ 广播通知
- ❌ 个人消息队列

但其他功能（数据库、Redis 等）仍可正常使用。

---

## 🛠️ **诊断脚本**

创建一个快速诊断脚本 `check-rabbitmq.sh`：

```bash
#!/bin/bash

RABBIT_HOST="120.48.127.181"
RABBIT_PORT="5672"

echo "======================================"
echo "RabbitMQ 连接诊断工具"
echo "======================================"

# 1. Ping 测试
echo -e "\n📡 1. Ping 测试..."
if ping -c 3 $RABBIT_HOST > /dev/null 2>&1; then
    echo "✅ Ping 成功"
else
    echo "❌ Ping 失败 - 网络不可达"
    exit 1
fi

# 2. 端口测试
echo -e "\n🔌 2. 端口测试 ($RABBIT_PORT)..."
if nc -zv $RABBIT_HOST $RABBIT_PORT 2>&1 | grep -q "succeeded"; then
    echo "✅ 端口 $RABBIT_PORT 可连接"
else
    echo "❌ 端口 $RABBIT_PORT 无法连接"
    echo "可能原因:"
    echo "  - RabbitMQ 服务未启动"
    echo "  - 防火墙阻止了端口"
    echo "  - 安全组未开放端口"
    exit 1
fi

# 3. 检查服务状态（需要 SSH 登录）
echo -e "\n🔧 3. 检查 RabbitMQ 服务状态..."
echo "请 SSH 登录服务器后执行："
echo "  systemctl status rabbitmq-server"

echo -e "\n======================================"
echo "诊断完成！"
echo "======================================"
```

使用方式：
```bash
chmod +x check-rabbitmq.sh
./check-rabbitmq.sh
```

---

## 📊 **常见问题速查表**

| 症状 | 可能原因 | 解决方案 |
|------|---------|---------|
| `Connection timed out` | 防火墙/安全组阻止 | 开放 5672 端口 |
| `Connection refused` | RabbitMQ 未运行 | 启动 RabbitMQ 服务 |
| `Authentication failed` | 用户名密码错误 | 检查配置 |
| `Unknown host` | DNS 解析失败 | 检查 hosts 或改用 IP |

---

## 🔐 **RabbitMQ 管理命令**

```bash
# 查看 RabbitMQ 状态
systemctl status rabbitmq-server

# 启动/停止/重启
systemctl start rabbitmq-server
systemctl stop rabbitmq-server
systemctl restart rabbitmq-server

# 查看日志
tail -f /var/log/rabbitmq/rabbit@localhost.log

# 查看已创建的用户
rabbitmqctl list_users

# 查看虚拟主机
rabbitmqctl list_vhosts

# 查看连接
rabbitmqctl list_connections

# 查看队列
rabbitmqctl list_queues name messages consumers
```

---

## 🎯 **推荐的修复流程**

### **步骤 1：快速诊断**
```bash
# 在本地执行
ping 120.48.127.181
nc -zv 120.48.127.181 5672
```

### **步骤 2：SSH 登录服务器**
```bash
ssh root@120.48.127.181
```

### **步骤 3：检查并启动服务**
```bash
systemctl status rabbitmq-server
systemctl start rabbitmq-server
systemctl enable rabbitmq-server
```

### **步骤 4：配置防火墙**
```bash
firewall-cmd --permanent --add-port=5672/tcp
firewall-cmd --reload
```

### **步骤 5：重启应用**
```bash
# 在你的 Java 项目中
./mvnw clean package
java -jar target/c-server-1.0.0.jar
```

### **步骤 6：验证连接**
查看应用日志，应该看到：
```
📡 创建 WebSocket 控制总线交换机：websocket.control.exchange
📬 创建 WebSocket 广播队列：websocket.broadcast.queue
🎧 创建 WebSocket 广播监听器
✅ RabbitAdmin 初始化成功
```

---

## 💡 **预防措施**

### **1. 监控 RabbitMQ 服务**

创建监控脚本 `/usr/local/bin/monitor-rabbitmq.sh`：

```bash
#!/bin/bash

if ! systemctl is-active --quiet rabbitmq-server; then
    echo "警告：RabbitMQ 服务未运行！" | mail -s "RabbitMQ 告警" admin@example.com
    systemctl start rabbitmq-server
fi
```

添加到 crontab：
```bash
*/5 * * * * /usr/local/bin/monitor-rabbitmq.sh
```

### **2. 配置日志轮转**

编辑 `/etc/logrotate.d/rabbitmq-server`：

```
/var/log/rabbitmq/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
}
```

### **3. 资源限制**

编辑 `/etc/security/limits.conf`，添加：

```
rabbitmq soft nofile 65536
rabbitmq hard nofile 65536
```

---

## 📞 **获取帮助**

如果以上方法都无法解决问题，可以：

1. **查看详细日志**
   ```bash
   journalctl -u rabbitmq-server -n 200 --no-pager
   ```

2. **检查系统资源**
   ```bash
   free -h     # 内存
   df -h       # 磁盘空间
   top         # CPU 使用率
   ```

3. **寻求社区帮助**
   - [RabbitMQ 官方文档](https://www.rabbitmq.com/documentation.html)
   - [Stack Overflow](https://stackoverflow.com/questions/tagged/rabbitmq)
   - [RabbitMQ GitHub Issues](https://github.com/rabbitmq/rabbitmq-server/issues)

---

## ✅ **验证修复成功**

修复后，重新启动应用应该看到：

```
🔧 初始化 RabbitAdmin...
✅ RabbitAdmin 初始化成功
📡 创建 WebSocket 控制总线交换机：websocket.control.exchange
📬 创建 WebSocket 控制总线队列：websocket.control.queue
🔗 绑定队列 websocket.control.queue 到交换机 websocket.control.exchange
📡 创建 WebSocket 广播交换机：websocket.broadcast.exchange
📬 创建 WebSocket 广播队列：websocket.broadcast.queue
🔗 绑定广播队列 websocket.broadcast.queue 到交换机 websocket.broadcast.exchange
🎧 创建 WebSocket 广播监听器
```

如果没有看到错误日志，说明连接成功！🎉
