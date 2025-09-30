# 个人网站项目

## 项目简介

这是一个基于 Vue 3 和 TypeScript 的现代化个人网站，集成了多种功能模块和交互效果，采用 Vite 作为构建工具，具有良好的性能和开发体验。

## 技术栈

- **前端框架**: Vue 3
- **构建工具**: Vite
- **编程语言**: TypeScript
- **UI 组件库**: Element Plus
- **路由管理**: Vue Router
- **状态管理**: Pinia
- **HTTP 请求**: Axios
- **图表库**: ECharts, ECharts-gl
- **3D 效果**: Three.js
- **动画效果**: Particles.vue3, Parallax-js
- **编辑器**: WangEditor, MD-Editor-v3
- **样式预处理**: Sass, Less
- **代码规范**: ESLint, Prettier

## 项目结构

```
├── .env.development       # 开发环境变量
├── .env.production        # 生产环境变量
├── .eslintrc.cjs          # ESLint配置
├── .gitignore             # Git忽略文件
├── .prettierrc.cjs        # Prettier配置
├── index.html             # 入口HTML
├── package.json           # 项目依赖
├── public/                # 静态资源
├── src/                   # 源代码
│   ├── api/               # API请求
│   ├── assets/            # 资源文件
│   ├── components/        # 组件
│   ├── directives/        # 自定义指令
│   ├── main.ts            # 入口文件
│   ├── router/            # 路由配置
│   ├── store/             # 状态管理
│   ├── types/             # 类型定义
│   ├── utils/             # 工具函数
│   ├── views/             # 视图组件
│   └── vite-env.d.ts      # Vite环境类型
├── tsconfig.json          # TypeScript配置
├── tsconfig.node.json     # Node环境TypeScript配置
├── vite.config.ts         # Vite配置
└── visualizer/            # 打包分析
```

## 安装和运行

1. 克隆项目

```bash
git clone <项目地址>
cd personal-web/p_ui
```

2. 安装依赖

```bash
# 建议加上--force，否则可能出现依赖树错误
npm install --force
```

3. 运行开发服务器

```bash
npm run dev
```

## 构建和部署

1. 构建生产版本

```bash
npm run build
# 或使用yarn
# yarn build
```

2. 部署
   构建完成后，将`dist`目录下的文件部署到 Web 服务器或 CDN 即可。

## 功能特点

- 响应式设计
- 多种主题切换,可自定义主题色
- 动态粒子背景和视频背景
- 3D 效果和动画
- 博客、相册、音乐等多种内容展示
- 交互式图表
- 编辑器支持
- 文件上传和管理

## 项目配置

### 环境变量

- 开发环境: `.env.development`
- 生产环境: `.env.production`

### Vite 配置

主要配置文件: `vite.config.ts`

- 基础路径: `./`
- 插件配置: Vue, SVG 图标, 压缩, 打包分析
- CSS 预处理: SCSS
- 路径别名: `@` 指向 `src`
- 开发服务器: 端口 8088, 支持跨域
- 代理配置: 多个 API 代理

## 贡献指南

1.  Fork 项目
2.  创建特性分支 (`git checkout -b feature/fooBar`)
3.  提交更改 (`git commit -am 'Add some fooBar'`)
4.  推送到分支 (`git push origin feature/fooBar`)
5.  创建新的 Pull Request

## 相关链接

- [Vue 3 文档](https://v3.vuejs.org/)
- [Vite 文档](https://vitejs.dev/)
- [TypeScript 文档](https://www.typescriptlang.org/)
- [Element Plus 文档](https://element-plus.org/)

## 版权信息

© 2023 个人网站. 保留所有权利。
