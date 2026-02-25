import i18n from '@/locales/i18n';
export default [
  {
    id: 'mow-v3',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v3/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/bigScreen/img1.png', import.meta.url).href
    ],
    title: 'MES系统 V3',
    intro: i18n.global.t(
      '基于qiankun微前端架构的企业级制造执行系统，整合Vue2/Vue3多技术栈子应用，集成低代码平台与可视化大屏设计器'
    ),
    date: ['2023-5', '2025-8'],
    tags: ['Java', 'TypeScript', 'Vue2', 'Vue3', 'Spring Boot', 'PostgreSQL', 'qiankun'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('微前端架构设计'),
        intro: i18n.global.t(
          '解决多技术栈共存下的样式冲突与应用间通信瓶颈，实现子应用秒级热插拔，显著降低了大型系统的维护成本。'
        )
      },
      {
        title: i18n.global.t('低代码平台'),
        intro: i18n.global.t(
          '深度集成表单设计器，添加API/SQL/静态数据等多数据源接入，实现在线发布与版本管理，大幅缩短业务上线周期。'
        )
      },
      {
        title: i18n.global.t('大屏报表系统'),
        intro: i18n.global.t('可视化大屏设计器，对接低代码接口库，支持拖拽式图表配置与实时数据刷新')
      }
    ]
  },
  {
    id: 'c-site-v2',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image3.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image4.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image5.png', import.meta.url).href
    ],
    title: i18n.global.t('个人全栈管理平台'),
    intro: i18n.global.t(
      '基于 Java 21 (Virtual Threads) 与 Spring Boot 3 构建的高性能全栈闭环项目，实践了现代化的 RESTful 设计范式、严谨的 JWT 安全架构以及基于 Docker 的 CI/CD 全流程自动化部署。'
    ),
    date: ['2024-3', '2026-2'],
    tags: ['TypeScript', 'Vue3', 'Spring Boot 3', 'Java 21', 'PostgreSQL', 'Docker', 'Redis'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('后端架构设计'),
        intro: i18n.global.t(
          '基于Spring Boot 3构建RESTful API，集成JWT无状态认证、Redis缓存优化、PostgreSQL数据持久化'
        )
      },
      {
        title: i18n.global.t('前端性能优化'),
        intro: i18n.global.t('实现首屏加载优化、代码分割、资源懒加载，重构后台管理系统')
      },
      {
        title: i18n.global.t('DevOps实践'),
        intro: i18n.global.t('使用Docker Compose实现一键部署，配置Nginx反向代理')
      }
    ]
  },
  {
    id: 'c-tools',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/c-tools/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/c-tools/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image3.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image4.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image5.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image6.png', import.meta.url).href
    ],
    title: i18n.global.t('跨平台桌面工具集'),
    intro: i18n.global.t(
      '利用 Rust + Tauri 打造轻量级高效率桌面工具箱，包体积较传统 Electron 方案减小 90%，内存占用降低 70%。'
    ),
    date: ['2025-6', '2025-12'],
    tags: ['Rust', 'Tauri', 'TypeScript', 'Vue3', 'Vite'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('Rust核心模块'),
        intro: i18n.global.t(
          '通过 Rust FFI 调用系统级 API 实现高性能图像采集与底层文件流监控，确保复杂逻辑在毫秒级响应。'
        )
      },
      {
        title: i18n.global.t('系统集成功能'),
        intro: i18n.global.t('实现截图悬浮、取色器、系统信息监控等需要调用操作系统API的功能')
      },
      {
        title: i18n.global.t('开发工具集成'),
        intro: i18n.global.t(
          '集成JSON格式化、代码生成、Markdown编辑器、Docker日志解析等开发常用工具'
        )
      }
    ]
  },
  {
    id: 'pvs-client',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/pvs/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('贴片机数据采集系统'),
    intro: i18n.global.t(
      '多型号贴片机统一数据采集程序，支持驱动在线更新与安装，打包为exe可执行程序'
    ),
    date: ['2024-5', '2026-2'],
    tags: ['Java', 'Spring Boot', 'PostgreSQL', 'SFTP'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('多设备协议适配'),
        intro: i18n.global.t('适配雅马哈、西门子、松下等多品牌贴片机的数据读取逻辑')
      },
      {
        title: i18n.global.t('连接管理优化'),
        intro: i18n.global.t('设计短连接池方案替代长连接，解决网络波动导致的连接失效问题')
      },
      {
        title: i18n.global.t('处理线程嵌套'),
        intro: i18n.global.t('处理线程嵌套问题，添加心跳校验机制，防止线程假活')
      }
    ]
  },
  {
    id: 'node-red',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href],
    title: i18n.global.t('Node-RED数据采集平台（分布式部署）'),
    intro: i18n.global.t(
      '基于Node-RED的可视化数据采集平台，支持流程可视化设计、日志监控与动态节点扩展，后添加分布式部署功能，由主程序统一控制各个服务器节点'
    ),
    date: ['2024-10', '2025-12'],
    tags: ['JavaScript', 'Node.js', 'jQuery'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('自定义节点开发'),
        intro: i18n.global.t('开发SFTP文件轮询、文件监听、数据解析等自定义节点')
      },
      {
        title: i18n.global.t('大批量文件处理'),
        intro: i18n.global.t('优化多服务器并发监听方案，实现分段重启、防抖处理、堆积预防等机制')
      }
    ]
  },
  {
    id: 'smt',
    type: 'secondary',
    coverUrl: new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt-mobile/img1.png', import.meta.url).href
    ],
    title: i18n.global.t('SMT生产管理系统'),
    intro: i18n.global.t(
      '面向SMT生产线的综合管理系统，集成设备状态监控、生产数据统计、移动端应用，支持WebSocket实时通信'
    ),
    date: ['2025-4', '2025-7'],
    isAll: false,
    tags: [
      'Java',
      'JavaScript',
      'Vue2',
      'Spring Boot',
      'PostgreSQL',
      'WebSocket',
      'Uni-App',
      'Redis'
    ],
    modules: [
      {
        title: i18n.global.t('实时数据推送'),
        intro: i18n.global.t(
          '基于WebSocket实现贴片机运行状态实时推送，支持多客户端同时连接与断线重连'
        )
      },
      {
        title: i18n.global.t('移动端应用'),
        intro: i18n.global.t(
          '基于Uni-App开发配套移动端，后端通过SSE与主系统实时通信，集成微信公众号消息推送'
        )
      },
      {
        title: i18n.global.t('构建性能优化'),
        intro: i18n.global.t(
          '优化Webpack构建配置，启用Loader缓存、代码压缩、资源分离，显著提升打包速度'
        )
      },
      {
        title: i18n.global.t('国际化方案'),
        intro: i18n.global.t('开发自动化国际化脚本，结合翻译API与人工校对，实现多语言快速适配')
      }
    ]
  },

  {
    id: 'utps',
    type: 'secondary',
    coverUrl: new URL('@/assets/projectImage/utps/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/utps/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/utps/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/utps-feishu/image1.png', import.meta.url).href
    ],
    title: i18n.global.t('UTPS工时管理系统'),
    intro: i18n.global.t(
      '产线工时管理系统，包含Web端与飞书小程序，支持人员配置、效率计算与移动端审批'
    ),
    date: ['2025-7', '2026-2'],
    tags: ['Java', 'TypeScript', 'Vue3', 'Spring Boot', 'SQLServer', '飞书小程序'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('部分后端接口开发'),
        intro: i18n.global.t('olp相关接口开发')
      },
      {
        title: i18n.global.t('飞书小程序'),
        intro: i18n.global.t('开发飞书小程序端，实现移动端便捷操作')
      },
      {
        title: i18n.global.t('操作工配置计划'),
        intro: i18n.global.t('实现各种配置的即时关联计算和数据统计')
      }
    ]
  },
  {
    id: 'big-ics',
    type: 'secondary',
    coverUrl: new URL('@/assets/projectImage/big-ics/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/big-ics/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/big-ics/image3.png', import.meta.url).href
    ],
    title: i18n.global.t('全域价值流可视化'),
    intro: i18n.global.t('企业价值流数据可视化平台，支持PC端与平板端自适应展示'),
    date: ['2025-7', '2025-11'],
    tags: ['Vue3', 'TypeScript'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('前端开发'),
        intro: i18n.global.t('完成全部前端页面开发，实现响应式布局适配多终端')
      }
    ]
  },
  {
    id: 'mes-v5',
    type: 'secondary',
    coverUrl: new URL('@/assets/projectImage/mow-v5/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v5/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v5/img2.png', import.meta.url).href
    ],
    title: 'MES系统 V5',
    intro: i18n.global.t('基于无界框架的微前端MES系统，支持模块独立部署与热更新'),
    date: ['2025-8', '2025-9'],
    tags: ['Vue2', 'Vue3', '无界'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('前端架构设计'),
        intro: i18n.global.t('设计微前端架构方案，实现模块独立部署与应用通信')
      }
    ]
  },
  {
    id: 'labelCloud',
    type: 'secondary',
    coverUrl: new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/labelCloud/img2.png', import.meta.url).href
    ],
    title: i18n.global.t('云标签打印系统'),
    intro: i18n.global.t(
      'MES系统配套标签打印子系统，支持多租户（租户独立数据库）支持标签模板设计与批量打印'
    ),
    date: ['2024-5', '2024-10'],
    tags: ['Java', 'Vue2', 'Spring Boot', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('标签打印相关功能补充'),
        intro: i18n.global.t('复杂流水号SN支持，支持动态数据绑定与批量打印')
      },
      {
        title: i18n.global.t('主子系统的数据交互'),
        intro: i18n.global.t(
          '实现主系统与子系统之间的数据交互（订单，标签数据等），支持数据同步与通信'
        )
      }
    ]
  }
];
