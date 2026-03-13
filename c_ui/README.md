# C-Site-V2 个人网站系统

## 项目简介

C-Site-V2 是一套完整的个人网站系统，采用前后端分离架构。后端基于 Spring Boot 构建，提供完善的 RESTful API；前端采用 Vue 3 + Vite 开发，提供现代化的用户界面。系统集成了博客、相册、游戏、影视等多种内容管理功能，并支持用户认证、WebSocket 实时通信、微信公众号接入等高级特性。

## 技术栈

### 后端技术

- **框架**: Spring Boot 3.x
- **数据库**: MyBatis Plus + MySQL
- **缓存**: Redis
- **认证**: JWT (JSON Web Token)
- **文档**: Swagger / OpenAPI
- **WebSocket**: Spring WebSocket
- **任务调度**: Spring Scheduling
- **其他**: Lombok, Hutool, FastJSON

### 前端技术

- **框架**: Vue 3 + Vite
- **UI 组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios
- **构建工具**: Vite

## 功能特性

### 内容管理

- **博客系统**: 支持博客分类、标签管理、评论功能，支持按时间、分类、标签统计
- **相册管理**: 图片相册管理，支持关联图片
- **游戏管理**: 游戏信息及分类统计
- **影视管理**: 影视资源及分类统计
- **随笔管理**: 随笔文章管理
- **待办事项**: Todo 清单管理
- **更新日志**: 版本更新记录

### 系统功能

- **用户管理**: 用户注册、登录、权限管理
- **字典数据**: 系统字典类型和数据管理
- **文件上传**: 支持文件上传功能
- **消息系统**: 站内消息通知
- **评论系统**: 树形评论结构

### 高级特性

- **JWT 认证**: 无状态用户认证，支持游客登录
- **WebSocket**: 实时消息推送，磁盘监控通知
- **微信公众号**: 接入微信公众平台，支持模板消息
- **Node-RED 集成**: 物联网设备对接
- **国际化支持**: 多语言前端界面

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

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 前端

- Node.js 16+
- npm 8+ 或 yarn 1.22+

## 快速开始

### 后端部署

1. **配置数据库和 Redis**

在 `application.yml` 中配置数据库连接和 Redis 连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
```

2. **构建项目**

```bash
cd c_server
./mvnw clean package -DskipTests
```

3. **运行项目**

```bash
java -jar target/p-server-v3.jar
```

或使用 Maven 直接运行：

```bash
./mvnw spring-boot:run
```

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

项目启动后，可通过以下地址访问 Swagger API 文档：

```
http://localhost:8080/swagger-ui/index.html
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
| 鉴权 | /auth                    | 用户登录、注册、验证码      |
| 用户 | /users                   | 用户管理                    |
| 文件 | /files                   | 文件上传                    |
| 消息 | /messages                | 站内消息                    |
| 待办 | /todos                   | 待办事项                    |
| 字典 | /dict-datas, /dict-types | 字典数据管理                |

## 许可证

本项目仅供学习交流使用。
