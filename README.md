# C-Site-V2 个人管理平台

## 项目简介

使用java21 + springboot3 + Vue 3 + Vite构建的个人管理平台。支持jwt认证、rabbitmq管理的WebSocket实时通信、微信公众号接入等。

## 技术栈

### 后端技术

- **框架**: Spring Boot 3.2.3
- **数据库**: MyBatis Plus 3.5.7 + MySQL 8.0+ (支持 PostgreSQL)
- **缓存**: Redis (Lettuce 连接池)
- **认证**: JWT (io.jsonwebtoken 0.11.5) + Spring Security
- **中间件**: RabbitMQ (AMQP)
- **文档**: knife4j 4.5.0 (OpenAPI 3)
- **WebSocket**: Spring WebSocket + RabbitMQ 消息队列
- **任务调度**: Spring Scheduling
- **其他**: Lombok, FastJSON 2.0.43, Mail, Validation, Actuator

### 前端技术

- **框架**: Vue 3 + Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios
- **构建工具**: Vite

## 功能特性

### 内容模块

- **博客**: 支持博客分类、AI总结、标签管理、评论功能等
- **相册**: 图片相册管理，支持关联图片
- **游戏**: 游戏信息及分类统计
- **影视**: 影视资源及分类统计
- **随笔**: 随笔文章管理
- **更新日志**: 版本更新记录
- **统计看板**: 数据统计看板
- **组件实验田**: 各种实验性组件
- **个人简介|项目经历**: 个人介绍及部分历史项目经历

### 系统功能

- **用户管理**: 用户注册、登录、权限管理
- **后台管理**: 系统字典类型和数据管理
- **文件上传**: 支持文件上传功能
- **消息系统**: 站内消息通知
- **评论系统**: 树形评论结构

### 高级特性

- **JWT 认证**: 无状态用户认证，支持游客登录
- **WebSocket + RabbitMQ**: 实时消息推送、磁盘监控通知、控制总线
- **微信公众号**: 接入微信公众平台，支持模板消息
- **国际化支持**: 多语言前端界面
- **安全加密**: Spring Security 密码加密
- **邮件服务**: 支持邮件发送（如验证码、通知等）
- **参数验证**: 统一的参数校验机制
- **健康监控**: Actuator 健康检查和指标监控

## 项目结构

```
c-site-v2/
├── c_server/                    # Spring Boot 后端
│   ├── src/main/java/com/pw/
│   │   ├── PServerV3Application.java    # 应用入口
│   │   ├── common/                      # 公共组件
│   │   │   ├── aspect/                  # AOP 切面
│   │   │   ├── constants/               # 常量定义
│   │   │   ├── controller/              # 基础控制器
│   │   │   ├── entity/                  # 基础实体
│   │   │   ├── exception/               # 异常处理
│   │   │   ├── filter/                  # 过滤器
│   │   │   ├── handler/                 # 处理器
│   │   │   ├── mybatisPlus/             # MyBatis Plus 配置
│   │   │   ├── token/                  # 认证注解
│   │   │   └── utils/                   # 工具类
│   │   ├── config/                     # 配置类
│   │   ├── controller/                 # 控制器
│   │   ├── domain/                     # 实体类
│   │   ├── dto/                        # 数据传输对象
│   │   ├── mapper/                     # 数据访问层
│   │   ├── monitor/                    # 监控系统
│   │   ├── service/                    # 业务服务
│   │   └── vo/                         # 视图对象
│   └── src/main/resources/
│       ├── application.yml             # 应用配置
│       └── mapper/                     # MyBatis XML 映射
│
└── c_ui/                         # Vue 3 前端
    ├── src/                      # 源代码
    ├── public/                   # 静态资源
    ├── dist-pagination/          # 打包组件
    ├── lib/                      # 第三方库
    └── i18nScripts/             # 国际化脚本
```

## 环境要求

### 后端

- **JDK 21+** (必须)
- Maven 3.8+
- MySQL 8.0+ **或** PostgreSQL 13+ (支持双数据库)
- Redis 6.0+
- RabbitMQ 3.8+ (用于 WebSocket 消息队列)

### 前端

- Node.js 18+
- npm 8+ 或 yarn 1.22+

## 快速开始

### 后端部署

1. **配置数据库和 Redis**

在 `application-dev.yml` 或 `application-prod.yml` 中配置：

**MySQL 配置示例：**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

**Redis 配置：**

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 123456 # Redis 服务器密码，如果没有则不配置
      database: 0 # Redis 数据库索引 (默认0)
      # 连接池配置 (强烈推荐生产环境使用)
      lettuce:
        pool:
          max-active: 20 # 连接池最大连接数（使用负值表示没有限制）
          max-idle: 10 # 连接池中的最大空闲连接
          min-idle: 0 # 连接池中的最小空闲连接
          max-wait: 2000ms # 连接池最大阻塞等待时间（使用负值表示没有限制）
      # 连接超时配置
      timeout: 2000ms # 连接超时时间
```

**RabbitMQ 配置：**

```yaml
spring: rabbitmq:
  host: localhost
  port: 5672
  username: your_username
  password: your_password
  virtual-host: /
  listener:
  simple:
    acknowledge-mode: manual  # 手动 ACK
    prefetch: 10              # 预取消息数量
    concurrency: 3            # 初始并发消费者
    max-concurrency: 10       # 最大并发消费者

```

2. **构建项目**

```bash
cd c_server ./mvnw clean package -DskipTests
```

3. **运行项目**

```bash
java -jar target/c-server-1.0.0.jar
```

或使用 Maven 直接运行：

```bash
./mvnw spring-boot:run
```

**默认访问地址：** http://localhost:7000

### 前端部署

1. **安装依赖**

```bash
cd c_ui
npm install
# 或
yarn install
```

2. **开发模式**

```bash
npm run dev
```

3. **构建生产版本**

```bash
npm run build
```

## API 文档

项目启动后，可通过以下地址访问 knife4j API 文档（基于 OpenAPI 3）：

```
http://localhost:7000/doc.html
http://localhost:7000/swagger-ui.html
http://localhost:7000/v3/api-docs
```

## 主要模块说明

| 模块 | 路径                     | 说明                        |
| ---- | ------------------------ | --------------------------- |
| 博客 | /blogs                   | 博客 CRUD、分类、标签、统计 |
| 相册 | /albums                  | 相册及图片管理              |
| 随笔 | /essays                  | 随笔文章管理                |
| 标签 | /blog-tags               | 博客标签管理                |
| 分类 | /blog-types              | 博客分类管理                |
| 评论 | /comments                | 评论管理，支持树形结构      |
| 鉴权 | /auth                    | 用户登录、注册              |
| 用户 | /users                   | 用户管理                    |
| 文件 | /files                   | 文件上传                    |
| 消息 | /messages                | 站内消息                    |
| 待办 | /todos                   | 待办事项                    |
| 字典 | /dict-datas, /dict-types | 字典数据管理                |
