export default [
  {
    id: 0,
    coverUrl: new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/pvs/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/pvs/img3.png', import.meta.url).href
    ],
    title: '贴片机文件采集驱动',
    intro: '轮询读取贴片机文件的Java程序，支持驱动在线上传和在线安装，命令行执行',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'Spring Boot', 'PostGreSQL', 'SFTP', 'FTP'],
    isAll: false,
    modules: [
      {
        title: '多个型号贴片机的适配',
        intro: '雅马哈，西门子，松下等贴片机的读取逻辑适配，包括多文件服务器读取情况视频'
      },
      {
        title: 'SFTP和FTP连接处理',
        intro:
          '贴片机型号与版本不同，文件访问方式也不一致，需要做相应处理，后将长连接方式改为短连接方式，保持长连接容易出现一些难以预测的问题，当然主要是这个程序本身面对的负载并不大'
      },
      {
        title: '嵌套线程处理',
        intro: '系统整体是一个主线程带多个子线程的嵌套方式,添加心跳包机制预防线程假死'
      }
    ]
  },
  {
    id: 1,
    coverUrl: new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href],
    title: 'node-red数采程序',
    intro: '贴片机文件采集驱动的升级版本，流程可视化，日志可视化，动态导入，网关监控',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Javascript', 'Jquery', 'Html', 'Css', 'Node'],
    isAll: false,
    modules: [
      {
        title: '新增多种节点',
        intro: '使用Jquery和JavaScript开发SFTP文件轮询，文件监听，文件解析等新节点'
      },
      {
        title: '贴片机文件采集流程设计',
        intro: '适配多机器的采集流程设计'
      },
      {
        title: '大批量文件采集处理',
        intro:
          '不同于贴片机文件采集，存在特殊的大批量文件采集过站情况，需从多个文件服务器的成百上千个目录下监听文件，需要预防某些服务器掉线、网络波动等问题，添加监听分段式重启机制，添加监听防抖，添加定时更新解析方案机制，添加加防止文件堆积机制等'
      }
    ]
  },
  {
    id: 2,
    coverUrl: new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt/img3.png', import.meta.url).href
    ],
    title: 'SMT精灵',
    intro: '集成到小主机的单体服务项目，用于工厂管理',
    date: ['2024-5-1', '2024-10-6'],
    isAll: false,
    tags: [
      'java',
      'javascript',
      'css',
      'vue2',
      'html',
      'spring boot',
      'postGreSQL',
      'websocket',
      'webpack',
      'redis'
    ],
    modules: [
      {
        title: 'pvs控制台',
        intro: 'spring boot搭配websocket实时刷新贴片机运行状态'
      },
      {
        title: '启动构建速度优化',
        intro: 'webpack loader缓存,图片资源,js解析等优化'
      },
      {
        title: '国际化',
        intro: 'node脚本+自动翻译api+人工调整'
      },
      {
        title: '看板',
        intro: '贴片机信息统计'
      }
    ]
  },

  {
    id: 3,
    coverUrl: new URL('@/assets/projectImage/smt-mobile/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/smt-mobile/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt-mobile/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt-mobile/img3.png', import.meta.url).href
    ],
    title: 'SMT精灵-手机端',
    intro: '使用uni-app开发，配合smt精灵使用的手机端',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'Uni-App', 'Spring Boot', 'PostGreSQL', 'sse'],
    isAll: false,
    modules: [
      {
        title: '整体架构',
        intro: '前端uni-app，后端通过sse连接与主smt精灵主工程连通'
      },
      {
        title: '微信对接',
        intro: '接入微信api，可通过手机端向微信发送模板信息'
      },
      {
        title: '动态导航',
        intro: '可动态设置下方导航菜单'
      },
      {
        title: '统计图表',
        intro: '适配手机端的图表'
      }
    ]
  },
  {
    id: 4,
    coverUrl: new URL('@/assets/projectImage/mow-v2/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v2/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v2/img2.png', import.meta.url).href
    ],
    title: 'MES系统-v2',
    intro: '多服务工厂管理系统，适配SQLServer和PG数据库，已经较为完整，偶尔处理一些bug和小需求',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'vue2', 'Spring Boot', 'PostGreSQL', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: '多功能甘特图',
        intro: '使用dhtmlx-gantt插件，补充了批量移动等功能'
      },
      {
        title: '国际化',
        intro: '脚本批量国际化+自动翻译api+人工调整'
      },
      {
        title: '维护',
        intro: '部分bug修复'
      }
    ]
  },
  {
    id: 5,
    coverUrl: new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/labelCloud/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/labelCloud/img3.png', import.meta.url).href
    ],
    title: '云标签打印系统',
    intro: '配合MES系统的子工程',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'vue2', 'Spring Boot', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: '与MES主工程的定时数据交互',
        intro: '通过定时任务交换订单、标签等数据的信息'
      },
      {
        title: '标签打印',
        intro: '按设计好的标签模板，处理标签数据逻辑'
      },
      {
        title: '大部分前端工作',
        intro: ''
      }
    ]
  },
  {
    id: 6,
    coverUrl: new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v3/img1.png', import.meta.url).href
    ],
    title: 'MES系统-v3',
    intro: '通过qiankun微前端框架整合多个不同技术栈项目，包含vue2和vue3项目',
    date: ['2024-5-1', '2024-10-6'],
    tags: [
      'Java',
      'JavaScript',
      'TypeScript',
      'Css',
      'Vue2',
      'Vue3',
      'Webpack',
      'vite',
      'Spring Boot',
      'PostGreSQL',
      'SQLServer'
    ],
    isAll: false,
    modules: [
      {
        title: 'qiankun框架项目整合',
        intro: '处理不同项目的公用信息，沙盒隔离和路由调度'
      },
      {
        title: '低代码',
        intro: '结合动态表单，实现快速生成常规列表页面并进行版本管理'
      },
      {
        title: '大屏报表',
        intro: '连通低代码接口库，可快速生成动态看板'
      }
    ]
  },
  {
    id: 7,
    coverUrl: new URL('@/assets/projectImage/lowCode/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/lowCode/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img4.png', import.meta.url).href
    ],
    title: '低代码',
    intro: 'MES系统-V3的子系统之一，以v-form表单为基础开发',
    date: ['2024-5-1', '2024-10-6'],
    tags: [
      'Java',
      'TypeScript',
      'Css',
      'Vue3',
      'Webpack',
      'Spring Boot',
      'PostGreSQL',
      'SQLServer'
    ],
    isAll: false,
    modules: [
      {
        title: '多种接口类型库',
        intro:
          '支持api接口(非本系统会有后端服务代理转发)，apiJson接口，静态数据接口，sql接口(mybatisPlus)'
      },
      {
        title: '常规页面生成',
        intro: '常规表单设计，支持表单嵌套，结合动态菜单实现在线发布和版本管理'
      },
      {
        title: '数据库自行配置模式pg和sqlserver双数据库适配',
        intro: '本质是拼接sql字符串，中转执行sql'
      }
    ]
  },
  {
    id: 8,
    coverUrl: new URL('@/assets/projectImage/bigScreen/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/bigScreen/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/bigScreen/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/bigScreen/img3.png', import.meta.url).href
    ],
    title: '大屏报表',
    intro: 'MES系统-V3的子系统之一，以一个较为完整的大屏设计器为基础进行开发',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack'],
    isAll: false,
    modules: [
      {
        title: '快速生成动态看板',
        intro: '连通低代码接口库，改造所有图表组件，实现快速生成动态看板'
      },
      {
        title: '组件补充',
        intro: '补充表格下拉框等组件'
      }
    ]
  },
  {
    id: 9,
    coverUrl: new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/kanban/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/kanban/img3.png', import.meta.url).href
    ],
    title: '看板',
    intro: '专门的看板项目，我只负责前端',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack', 'Spring Boot', 'Java'],
    isAll: false,
    modules: [
      {
        title: '所有前端工作'
      }
    ]
  },
  {
    id: 10,
    coverUrl: new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href],
    title: 'SMS备件管理系统',
    intro: '工厂管理的一个特殊模块，稍微改了几个接口，略过',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue3', 'Vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  },
  {
    id: 11,
    coverUrl: new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href],
    title: 'CMS样品管理系统',
    intro: '工厂管理的一个特殊模块，同样只是稍微改了几个接口，略过',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue3', 'Vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  },
  {
    id: 12,
    coverUrl: new URL('@/assets/projectImage/problemResolve/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/problemResolve/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/problemResolve/img2.png', import.meta.url).href
    ],
    title: '问题解决工具',
    intro: '一个比较老的项目，后端似乎用的.net，只改了几个前端问题，略过',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  }
];
