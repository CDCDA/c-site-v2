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
    title: i18n.global.t('MES系统 V3 (微前端企业版)'),
    intro: i18n.global.t(
      '采用 qiankun 微前端架构重构的下一代企业级制造执行系统。通过打破技术栈壁垒，实现了 Vue2 存量应用与 Vue3 新特性的无缝并行，集成了低代码引擎与实时数据可视化大屏，为大型制造企业提供一站式数字化解决方案。'
    ),
    date: ['2023-5', '2025-8'],
    tags: [
      'Java',
      'TypeScript',
      'Vue2',
      'Vue3',
      'Spring Boot',
      'PostgreSQL',
      'qiankun',
      'Low-Code'
    ],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('微前端架构'),
        intro: i18n.global.t(
          '基于 qiankun 实现了多技术栈（Vue2/Vue3/Vite）的深度集成。处理微前端大型应用在迭代过程中的样式污染与通信瓶颈，支撑子应用快速独立部署与热插拔。'
        )
      },
      {
        title: i18n.global.t('低代码集成'),
        intro: i18n.global.t(
          '修改开源低代码源码，统合主应用与低代码子应用的页面生成与发布，抽象出 API/SQL/静态数据等多种数据协议转换层。支持业务人员通过可视化配置快速生成复杂业务表单，实现动态路由映射与版本灰度发布。'
        )
      },
      {
        title: i18n.global.t('高负载大屏设计器'),
        intro: i18n.global.t(
          '集成可视化大屏配置系统，对接低代码数据源接口库。采用分层渲染优化技术，支持 50+ 种组件的拖拽配置，实现实时数据展示与交互。'
        )
      },
      {
        title: i18n.global.t('自定义控制台'),
        intro: i18n.global.t(
          '自研可视化控制台，支持自定义组件的网格布局配置。用户可以根据业务需求，快速搭建自定义的模块监控，例如贴片机运行相关指标的可视化与采集报错排查。'
        )
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
    title: i18n.global.t('个人全栈管理平台 V2'),
    intro: i18n.global.t(
      '基于 Java 21 与 Spring Boot 3 打造的高性能全栈生态系统。项目涵盖了从底层安全架构、多级缓存设计到前端工程化实践的完整闭环，是现代云原生开发模式的深度实践。'
    ),
    date: ['2024-3', '2026-2'],
    tags: [
      'TypeScript',
      'Vue3',
      'Spring Boot 3',
      'Java 21',
      'PostgreSQL',
      'Docker',
      'Redis',
      'MySQL'
    ],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('现代后端架构体系'),
        intro: i18n.global.t(
          '利用 Java 21 虚拟线程大幅提升并发处理能力；集成 Spring Security + JWT 权限体系；通过 Redis 缓存抽象层，构建了高响应、强一致的 RESTful 服务。'
        )
      },
      {
        title: i18n.global.t('前端性能极致优化'),
        intro: i18n.global.t(
          '基于 Vite 5 构建，实施了模块级代码分割（manualChunks）、图片压缩转换及针对首屏渲染的预加载策略。'
        )
      },
      {
        title: i18n.global.t('全链路 DevOps'),
        intro: i18n.global.t(
          '编写 Dockerfile 与 Docker Compose 脚本实现多环境一键拉起；配置 Nginx 反向代理与 Gzip 压缩，结合自动化日志监控方案，打造生产级的可靠运维流程。'
        )
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
    title: i18n.global.t('基于 Rust 的跨平台桌面工具集'),
    intro: i18n.global.t(
      '挑战传统 Electron 开发模式，利用 Rust + Tauri 打造的轻量级、原生性能工具箱。包体积从百兆级缩减至十兆，本体内存占用率降低 70% 以上。'
    ),
    date: ['2025-6', '2025-12'],
    tags: ['Rust', 'Tauri', 'TypeScript', 'Vue3', 'Vite'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('Rust 高性能后端逻辑'),
        intro: i18n.global.t(
          '利用 Rust 的内存安全特性封装底层系统 API，实现多线程高性能文件树获取(10w+文件仅需2s，electron 大概率直接卡死)和系统层的截屏、插入等操作。通过 Tauri 命令系统与前端进行二进制数据交互，消除性能瓶颈。'
        )
      },
      {
        title: i18n.global.t('原生交互功能集成'),
        intro: i18n.global.t(
          '深度调用操作系统特权接口，实现了全局热键绑定、托盘常驻控制、系统 CPU/内存实时监控以及分离窗口。'
        )
      },
      {
        title: i18n.global.t('开发者工具链生态'),
        intro: i18n.global.t(
          '内置 JSON 智能格式化、Regex 调试器、Docker 日志实时流解析、 Markdown 实时预览和快捷shell命令等功能，致力于打造最懂程序员的“瑞士军刀”。'
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
    title: i18n.global.t('IoT 贴片机通用数据采集系统'),
    intro: i18n.global.t(
      '工业级的跨设备数据采集平台。支持主流品牌贴片机协议适配，实现了高频数据的稳定采集、动态驱动加载与远程静默更新，是连接物理工厂与云端 MES 的关键桥梁。'
    ),
    date: ['2024-5', '2026-2'],
    tags: ['Java', 'Spring Boot', 'PostgreSQL', 'Industrial IoT', 'SFTP'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('多机型协议抽象层'),
        intro: i18n.global.t(
          '设计并实现了统一的驱动抽象模型，成功兼容雅马哈、西门子、松下等不同厂商的数据解析逻辑。支持在线热更新驱动 DLL/Jar，无需停机即可完成新机型适配。'
        )
      },
      {
        title: i18n.global.t('高可用连接策略优化'),
        intro: i18n.global.t(
          '针对车间网络波动的复杂环境，设计了动态短连接池方案，结合退避重试算法与心跳探活机制，有效解决了长连接容易导致的“假活”与死锁问题。'
        )
      },
      {
        title: i18n.global.t('并发线程安全管理'),
        intro: i18n.global.t(
          '处理自定义线程执行池环境下的多级嵌套线程问题，防止采集任务在极端高负载下因线程递归或堆积导致的 OOM 崩溃。'
        )
      }
    ]
  },
  {
    id: 'node-red',
    type: 'primary',
    coverUrl: new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href],
    title: i18n.global.t(' Node-RED 数据中枢'),
    intro: i18n.global.t(
      '基于 Node-RED 二次开发的低代码流式数据处理平台。通过PM2监控node-red子应用，实现了子应用的自动重启、日志监控、内存占用统计等功能。'
    ),
    date: ['2024-10', '2025-12'],
    tags: ['JavaScript', 'Node.js', 'Node-RED', 'Distributed Systems'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('工业级自定义节点组件'),
        intro: i18n.global.t(
          '针对工业文件传输协议封装了 SFTP 文件轮询、读写等全功能节点、增量监听与数据格式自动解析节点等，实现了采集流程的快速设计与部署。'
        )
      },
      {
        title: i18n.global.t('通用型解析方案'),
        intro: i18n.global.t(
          '设计通用型解析方案，使得非专业人员可以直接配置解析规则，无需编写代码，实现从采集流程到数据存储的全流程自动化。'
        )
      },
      {
        title: i18n.global.t('大批量文件操作处理'),
        intro: i18n.global.t(
          '通过缓存校验，队列窗口，任务防抖和自检机制和异步处理等方法，实现了在node.js单cpu环境下对大批量文件的高效稳定处理。'
        )
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
    title: i18n.global.t('SMT 智能生产管控系统'),
    intro: i18n.global.t(
      '聚焦于 SMT 生产全生命周期的管理系统，涵盖设备实时看板、生产报表分析、移动端巡检。通过双向通信机制实现了异常的秒级响应，显著提升了车间生产透明度。'
    ),
    date: ['2025-4', '2025-7'],
    isAll: false,
    tags: ['Java', 'Vue2', 'WebSocket', 'Uni-App', 'SSE', 'Redis'],
    modules: [
      {
        title: i18n.global.t('双向实时数据推送'),
        intro: i18n.global.t(
          '采用 WebSocket 消息通信机制，实现了服务器端对客户端的实时数据推送。针对移动端优化引入 SSE (Server-Sent Events) 机制，实现低功耗下的消息实时触达。'
        )
      },
      {
        title: i18n.global.t('Uni-App 跨端移动应用'),
        intro: i18n.global.t(
          '开发配套移动端微信网页，集成了扫码报工、故障提报、微信公众号模版消息推送等功能，实现了生产管理从 PC 端向移动端的完整延伸。'
        )
      },
      {
        title: i18n.global.t('全自动国际化脚本'),
        intro: i18n.global.t(
          '为了支撑全球化部署，自研了一套node.js编写的国际化自动提取与翻译脚本，结合翻译API自动生成多语言对照表，将系统的多语言适配周期从周级缩短至小时级，缩减95%的手动工作。外加配置远程动态加载语言文件，可在网页端直接查找对应的语句修改覆盖，无需重启服务。'
        )
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
    title: i18n.global.t('UTPS 数字化工时管理系统'),
    intro: i18n.global.t(
      '深耕产线效能分析，集 Web 后台、飞书微应用为一体。核心功能包括人员配置自动排班、生产效率实时计算与飞书协同审批流，全方位解决制造企业的人力成本追踪痛点。'
    ),
    date: ['2025-7', '2026-2'],
    tags: ['Java', 'TypeScript', 'Vue3', 'Spring Boot', 'SQLServer', 'Feishu/Lark'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('飞书生态深度集成'),
        intro: i18n.global.t(
          '独立开发飞书小程序端，打通了企业组织架构映射与单点登录（SSO），实现了在飞书工作台内直接进行工时填报与移动审批。'
        )
      },
      {
        title: i18n.global.t('复杂计算引擎设计'),
        intro: i18n.global.t(
          '实现了一套高灵活度的即时计算逻辑，支持根据操作工技能矩阵、产线产能配置实时生成效率报表，数据准确度提升至 99.9% 以上。'
        )
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
    title: i18n.global.t('全域价值流可视化平台'),
    intro: i18n.global.t(
      '企业级全链路数据流转可视化方案。打破数据孤岛，支持从原材料进场到成品出库的全生命周期可视化展示，适配 PC 监控墙与 iPad 移动端。'
    ),
    date: ['2025-7', '2025-11'],
    tags: ['Vue3', 'TypeScript', 'ECharts', 'Responsive Design'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('响应式数字孪生看板'),
        intro: i18n.global.t('实现了一套代码兼容大屏展示与平板交互，确保多终端视觉体验高度一致。')
      },
      {
        title: i18n.global.t('重构列表框架'),
        intro: i18n.global.t(
          '重构通用列表框架，使其除了基础的列表功能外，还支持子表，数据钩子定制等功能。'
        )
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
    title: i18n.global.t('MES系统 V5 (无界微前端版)'),
    intro: i18n.global.t(
      'MES 系统的又一次架构进化。弃用 Shadow DOM 限制较多的 qiankun，转向基于 WebComponent 容器的“无界”框架，实现了极速加载与更完美的子应用原生体验。'
    ),
    date: ['2025-8', '2025-9'],
    tags: ['Vue3', 'Wujie', 'Micro-frontends', 'Vite'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('高性能微前端演进'),
        intro: i18n.global.t(
          '设计并落地了无界（Wujie）架构方案，利用其预加载与多实例销毁机制，将复杂子系统的切换耗时降低了 40%，并彻底解决了微前端下的样式污染和全局弹窗定位难题。'
        )
      },
      {
        title: i18n.global.t('编写一键式打包脚本'),
        intro: i18n.global.t(
          '编写了一键式打包脚本，实现了多应用的快速打包部署，减少了手动操作的错误风险，提升了部署效率。'
        )
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
    title: i18n.global.t('SCM云标签打印系统'),
    intro: i18n.global.t(
      '针对多租户场景设计的云端标签设计与打印中台。支持租户数据库独立部署，提供可视化标签模板编辑器，能够与 MES 系统无缝对接，实现大规模并发下的流水号精准生成。'
    ),
    date: ['2024-5', '2024-10'],
    tags: ['Java', 'Spring Boot', 'Multi-tenancy', 'Label Design'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('复杂流水号设计支持'),
        intro: i18n.global.t(
          '设计了支持复杂逻辑（如日期、十六进制混排，各种表数据以及即时输入数据）的高性能流水号生成器。'
        )
      },
      {
        title: i18n.global.t('跨系统数据同步机制'),
        intro: i18n.global.t(
          '构建了标准的 REST API 与 Webhook 回调体系，实现了主系统（MES）订单指令下发与子系统（标签云）实时反馈的闭环通信。'
        )
      }
    ]
  }
];
