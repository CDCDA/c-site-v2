import i18n from '@/locales/i18n';
export default [
  {
    id: 'pvs-client',
    coverUrl: new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/pvs/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/pvs/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/pvs/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('贴片机文件采集驱动'),
    intro: i18n.global.t('贴片机信息采集程序，支持驱动在线上传和在线安装，打包成exe文件运行'),
    date: ['2024-5-1', '2025-10-6'],
    tags: ['Java', 'Spring Boot', 'PostGreSQL', 'SFTP', 'FTP'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('多个型号贴片机的适配'),
        intro: i18n.global.t(
          '雅马哈，西门子，松下等贴片机的读取和计算逻辑适配，包括分布式服务器读取情况适配'
        )
      },
      {
        title: 'SFTP和FTP连接处理',
        intro: i18n.global.t(
          '贴片机型号与版本不同，文件访问方式也不一致，需要做相应处理，后将长连接方式改为短连接方式，保持长连接容易出现一些难以预测的问题，当然主要是这个程序本身面对的负载并不大'
        )
      },
      {
        title: i18n.global.t('嵌套线程处理'),
        intro:
          '系统整体是一个主线程带多个子线程的嵌套方式,其实有很多冗余，但是积重难返。添加心跳包监测机制预防线程假死'
      },
      {
        title: i18n.global.t('现场问题的长期支持'),
        intro: i18n.global.t(
          '处理实施人员在使用过程中遇到的各种问题,包括但不限于：系统崩溃、数据丢失、性能问题等'
        )
      }
    ]
  },
  {
    id: 'utps',
    coverUrl: new URL('@/assets/projectImage/utps/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/utps/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/utps/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/utps/image3.png', import.meta.url).href
    ],
    title: i18n.global.t('utps工时管理'),
    intro: i18n.global.t(
      'utps工时管理系统，产线工作人员配置，效率计算等，亏到姥姥家的一个项目，钱少事多，要重构他们的jsp老系统，甚至附赠一个飞书版本，无法理解为什么会接这么一个项目'
    ),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'vue3', 'Spring Boot', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('部分接口'),
        intro: i18n.global.t('常规增删改查+部分计算接口')
      },
      {
        title: i18n.global.t('部分前端'),
        intro: i18n.global.t('有个挺恶心的互联tab，需要处理好数据交互')
      }
    ]
  },
  {
    id: 'labelCloud',
    coverUrl: new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/labelCloud/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/labelCloud/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/labelCloud/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('云标签打印系统'),
    intro: i18n.global.t('配合MES系统的子工程'),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'vue2', 'Spring Boot', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('与MES主工程的定时数据交互'),
        intro: i18n.global.t('通过定时任务交换订单、标签等数据的信息')
      },
      {
        title: i18n.global.t('标签打印'),
        intro: i18n.global.t('按设计好的标签模板，处理标签数据逻辑')
      },
      {
        title: i18n.global.t('部分前端工作'),
        intro: ''
      }
    ]
  },
  {
    id: 'node-red',
    coverUrl: new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/node-red/img1.png', import.meta.url).href],
    title: 'node-red数采程序',
    intro: i18n.global.t(
      '贴片机文件采集驱动的升级版本，流程可视化，日志可视化，动态导入，网关监控'
    ),
    date: ['2024-10-1', '2025-12-6'],
    tags: ['Javascript', 'Jquery', 'Html', 'Css', 'Node'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('新增多种节点'),
        intro: i18n.global.t('使用Jquery和nodeJs开发SFTP文件轮询，文件监听，文件解析等新节点')
      },
      {
        title: i18n.global.t('贴片机文件采集流程设计'),
        intro: i18n.global.t('适配多机器的采集流程设计')
      },
      {
        title: i18n.global.t('大批量文件采集处理'),
        intro:
          i18n.global.t(
            '不同于常规贴片机文件采集，存在特殊的大批量文件采集过站情况，需从多个文件服务器的成百上千个目录下监听文件，'
          ) +
          i18n.global.t(
            '需要预防某些服务器掉线、网络波动等问题，添加监听分段式重启机制，添加监听防抖，添加定时更新解析方案机制，'
          ) +
          '添加加防止文件堆积机制等。\n实际上应该用其他高性能语言来写，nodejs的性能受限于单cpu，但是当初没有和我说清楚要求，最后不得不花费一些时间来优化'
      }
    ]
  },
  {
    id: 'mes-v5',
    coverUrl: new URL('@/assets/projectImage/mow-v5/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v5/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v5/img2.png', import.meta.url).href
    ],
    title: 'mes系统-v5',
    intro: i18n.global.t(
      '需求是像微服务一样，每个模块都可以独立部署，采用无界框架实现，完成框架后移交给其他部门开发'
    ),
    date: ['2025-8-30', '2025-9-15'],
    tags: ['vue2', 'vue3'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('前端架构'),
        intro: i18n.global.t(
          '前端整体架构，包括技术栈选型，不同模块项目间的通信和组合，通用模块的抽取，以及快捷启动方式等'
        )
      }
    ]
  },
  {
    id: 'big-ics',
    coverUrl: new URL('@/assets/projectImage/big-ics/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/big-ics/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/big-ics/image3.png', import.meta.url).href,
      new URL('@/assets/projectImage/big-ics/image3.png', import.meta.url).href
    ],
    title: i18n.global.t('全域价值流'),
    intro: i18n.global.t(
      '应客户要求使用Sz-Admin框架，接口由客户方提供，比较奇葩的是还需要给客户代码说明文档，客户以后要将这个框架自用'
    ),
    date: ['2025-7-30', '2025-11-05'],
    tags: ['vue3'],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('全部前端工作'),
        intro: i18n.global.t('全部前端工作')
      },
      {
        title: i18n.global.t('代码说明文档'),
        intro: i18n.global.t('客户方需要根据Sz-Admin框架的使用说明，编写代码说明文档')
      }
    ]
  },
  {
    id: 'smt',
    coverUrl: new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/smt/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('SMT精灵'),
    intro: i18n.global.t(
      '集成到小主机的单体服务项目，用于工厂管理，其实是mes微服务项目的单体精简版'
    ),
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
        title: i18n.global.t('各种模块控制台'),
        intro: 'spring boot搭配websocket实时刷新贴片机运行状态'
      },
      {
        title: i18n.global.t('启动构建速度优化'),
        intro: 'webpack loader缓存,图片资源,js解析等优化'
      },
      {
        title: i18n.global.t('国际化'),
        intro: 'node脚本+自动翻译api+人工调整'
      },
      {
        title: i18n.global.t('看板'),
        intro: i18n.global.t('贴片机信息统计')
      }
    ]
  },
  {
    id: 'smt-mobile',
    coverUrl: new URL('@/assets/projectImage/smt-mobile/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/smt-mobile/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt-mobile/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/smt-mobile/img3.png', import.meta.url).href
    ],
    title: 'SMT精灵-手机端',
    intro: i18n.global.t('使用uni-app开发，配合smt精灵使用的手机端'),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['Java', 'JavaScript', 'Css', 'Uni-App', 'Spring Boot', 'PostGreSQL', 'sse'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('整体架构'),
        intro: i18n.global.t('前端uni-app，后端通过sse连接与主smt精灵主工程连通')
      },
      {
        title: i18n.global.t('微信对接'),
        intro: i18n.global.t('接入微信api，可通过手机端向微信发送模板信息')
      },
      {
        title: i18n.global.t('动态导航'),
        intro: i18n.global.t('可动态设置下方导航菜单')
      },
      {
        title: i18n.global.t('统计图表'),
        intro: i18n.global.t('适配手机端的图表')
      }
    ]
  },
  {
    id: 'mow-v2',
    coverUrl: new URL('@/assets/projectImage/mow-v2/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v2/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v2/img2.png', import.meta.url).href
    ],
    title: 'MES系统-v2',
    intro: i18n.global.t('多服务工厂管理系统，适配SQLServer和PG数据库，偶尔处理一些bug和需求'),
    date: ['2023-6-1', '2025-3-6'],
    tags: ['Java', 'JavaScript', 'Css', 'vue2', 'Spring Boot', 'PostGreSQL', 'SQLServer'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('多功能甘特图'),
        intro: i18n.global.t('使用dhtmlx-gantt插件，补充了批量移动等功能')
      },
      {
        title: i18n.global.t('国际化'),
        intro: '脚本批量国际化+自动翻译api+人工调整'
      },
      {
        title: i18n.global.t('维护'),
        intro: i18n.global.t('部分bug修复')
      }
    ]
  },

  {
    id: 'mow-v3',
    coverUrl: new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/mow-v3/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/mow-v3/img1.png', import.meta.url).href
    ],
    title: 'MES系统-v3',
    intro: i18n.global.t('通过qiankun微前端框架整合多个不同技术栈项目，包含vue2和vue3项目'),
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
        title: i18n.global.t('qiankun框架项目整合'),
        intro: i18n.global.t('处理不同项目的公用信息，沙盒隔离和路由调度')
      },
      {
        title: i18n.global.t('低代码'),
        intro: i18n.global.t('结合动态表单，实现快速生成常规列表页面并进行版本管理')
      },
      {
        title: i18n.global.t('大屏报表'),
        intro: i18n.global.t('连通低代码接口库，可快速生成动态看板')
      }
    ]
  },
  {
    id: 'lowCode',
    coverUrl: new URL('@/assets/projectImage/lowCode/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/lowCode/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img3.png', import.meta.url).href,
      new URL('@/assets/projectImage/lowCode/img4.png', import.meta.url).href
    ],
    title: i18n.global.t('低代码'),
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
        title: i18n.global.t('多种接口类型库'),
        intro: i18n.global.t(
          '支持api接口(非本系统会有后端服务代理转发)，apiJson接口，静态数据接口，sql接口(mybatisPlus)'
        )
      },
      {
        title: i18n.global.t('常规页面生成'),
        intro: i18n.global.t('常规表单设计，支持表单嵌套，结合动态菜单实现在线发布和版本管理')
      },
      {
        title: i18n.global.t('数据库自行配置模式pg和sqlserver双数据库适配'),
        intro: i18n.global.t('本质是拼接sql字符串，中转执行sql')
      }
    ]
  },
  {
    id: 'bigScreen',
    coverUrl: new URL('@/assets/projectImage/bigScreen/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/bigScreen/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/bigScreen/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/bigScreen/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('大屏报表'),
    intro: 'MES系统-V3的子系统之一，以一个较为完整的大屏设计器为基础进行开发',
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('快速生成动态看板'),
        intro: i18n.global.t('连通低代码接口库，改造所有图表组件，实现快速生成动态看板')
      },
      {
        title: i18n.global.t('组件补充'),
        intro: i18n.global.t('补充表格下拉框等组件')
      }
    ]
  },
  {
    id: 'kanban',
    coverUrl: new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/kanban/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/kanban/img2.png', import.meta.url).href,
      new URL('@/assets/projectImage/kanban/img3.png', import.meta.url).href
    ],
    title: i18n.global.t('看板'),
    intro: i18n.global.t('专门的看板项目，我只负责前端'),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack', 'Spring Boot', 'Java'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('所有前端工作')
      }
    ]
  },
  {
    id: 'smsSparePart',
    coverUrl: new URL('@/assets/images/404.png', import.meta.url).href,
    images: [new URL('@/assets/images/404.png', import.meta.url).href],
    title: i18n.global.t('SMS备件管理系统'),
    intro: i18n.global.t('工厂管理的一个特殊模块，稍微改了几个接口，略过'),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue3', 'Vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  },
  {
    id: 'cmsSparePart',
    coverUrl: new URL('@/assets/images/404.png', import.meta.url).href,
    images: [new URL('@/assets/images/404.png', import.meta.url).href],
    title: i18n.global.t('CMS样品管理系统'),
    intro: i18n.global.t('工厂管理的一个特殊模块，同样只是稍微改了几个接口，略过'),
    date: ['2024-5-1', '2024-10-6'],
    tags: ['JavaScript', 'Css', 'Vue3', 'Vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  },
  {
    id: 'problemResolve',
    coverUrl: new URL('@/assets/projectImage/problemResolve/img1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/problemResolve/img1.png', import.meta.url).href,
      new URL('@/assets/projectImage/problemResolve/img2.png', import.meta.url).href
    ],
    title: i18n.global.t('问题解决工具'),
    intro: i18n.global.t('一个比较老的项目，后端似乎用的.net，只改了几个前端问题，略过'),
    date: ['2024-3-1', '2024-4-1'],
    tags: ['JavaScript', 'Css', 'Vue2', 'Webpack', 'Spring Boot', 'Java'],
    isAll: false,
    modules: []
  },
  {
    id: 'c-site',
    coverUrl: new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href],
    title: i18n.global.t('个人网站'),
    intro: '初版个人网站，springboot2+vue3+jdk8+mysql+redis的单体服务',
    date: ['2024-3-1', '2024-10-15'],
    tags: ['JavaScript', 'Css', 'Vue3', 'vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('请查看c-site-v2的项目介绍')
      }
    ]
  },
  {
    id: 'c-site-micro',
    coverUrl: new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
    images: [new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href],
    title: i18n.global.t('个人网站-微服务版'),
    intro: i18n.global.t(
      '微服务测试版本，本来是拿来测试集群和微服务，但是轻量服务器实在是在不堪大用，开了两个nacos和几个基础服务就爆了，最后只能强行限制资源占用，勉勉强强测试一下'
    ),
    date: ['2024-3-1', '2024-10-15'],
    tags: ['JavaScript', 'Css', 'Vue3', 'vite', 'Spring Boot', 'Java'],
    isAll: false,
    modules: [
      {
        title: i18n.global.t('请查看c-site-v2的项目介绍')
      }
    ]
  },
  {
    id: 'c-site-v2',
    coverUrl: new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/c-site-v2/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image3.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image4.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-site-v2/image5.png', import.meta.url).href
    ],
    title: i18n.global.t('个人网站-v2'),
    intro:
      'springboot3 + vue3 + jdk21 + postgres + redis 的单体服务版本。\n' +
      '全面升级，重构代码，接口改为符合RESTful API的格式，更换jwt鉴权库和加密库,部署方式改为docker-compose批量部署。\n' +
      '前端首屏加载优化与打包优化，重构后台管理\n',
    date: ['2025-7-20', '2025-10-27'],
    tags: [
      'TypeScript',
      'Css',
      'Vue3',
      'vite',
      'Spring Boot3',
      'Java',
      'Postgres',
      'Docker',
      'Redis'
    ],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('都在这个网站了')
      }
    ]
  },
  {
    id: 'c-tools',
    coverUrl: new URL('@/assets/projectImage/c-tools/image1.png', import.meta.url).href,
    images: [
      new URL('@/assets/projectImage/c-tools/image1.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image2.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image3.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image4.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image5.png', import.meta.url).href,
      new URL('@/assets/projectImage/c-tools/image6.png', import.meta.url).href
    ],
    title: i18n.global.t('桌面端工具集'),
    intro:
      'tauri+rust+vue3 桌面端工具集,包含:\n' +
      '1. json处理\n' +
      '2. 哈希生成\n' +
      '3. 文件树\n' +
      '4. 文件整理\n' +
      '5. nginx格式化\n' +
      '6. docker日志转化\n' +
      '7. 系统信息\n' +
      '8. 批量正则\n' +
      'rust速度快，但难写，tauri跨平台能力强，但权限问题比较麻烦',
    date: ['2025-4-20', '2025-5-27'],
    tags: [
      'TypeScript',
      'Css',
      'Vue3',
      'vite',
      'Spring Boot3',
      'Java',
      'Postgres',
      'Docker',
      'Redis'
    ],
    isAll: true,
    modules: [
      {
        title: i18n.global.t('全部')
      }
    ]
  }
];
