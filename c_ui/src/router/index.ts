import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import useThemeStore from '@/store/modules/theme.ts';

import { random } from 'lodash';
import { autoClearTimer } from '@/utils/timer.ts';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    meta: { title: '登录', isHidden: true, cache: false },
    component: () => import('/src/views/login/login.vue')
  },
  {
    path: '/home',
    name: 'home',
    meta: { title: '首页', preload: true },
    component: () => import(/* webpackChunkName: "about" */ '/src/views/home/index.vue')
  },
  {
    path: '/personalInfo',
    name: 'personalInfo',
    meta: {
      title: '个人信息',
      isHidden: true,
      svgIcon: '人员信息',
      src: '/images/manageImages/个人中心.png'
    },
    component: () => import('/src/views/manage/personalInfo/index.vue')
  },
  {
    path: '/manage',
    name: 'manage',
    meta: { title: '管理页', isHidden: true },
    component: () => import('/src/views/manage/manage.vue'),
    children: [
      {
        path: '/blogManage',
        name: 'blogManage',
        meta: {
          title: '博客管理',
          isHidden: true,
          svgIcon: 'commonSvg-博客',
          src: '/images/manageImages/博客.png',
          affix: true
        },
        component: () => import('/src/views/manage/blogManage/index.vue')
      },
      {
        path: '/blogManageEditor',
        name: 'blogManageEditor',
        meta: { title: '博客编辑' },
        component: () => import('/src/views/blog/blogEditor/editor.vue')
      },
      {
        path: '/typeManage',
        name: 'typeManage',
        meta: {
          title: '分类管理',
          isHidden: true,
          svgIcon: 'commonSvg-分类1',
          src: '/images/manageImages/分类.png'
        },
        component: () => import('/src/views/manage/typeManage/index.vue')
      },
      {
        path: '/tagManage',
        name: 'tagManage',
        meta: {
          title: '标签管理',
          isHidden: true,
          svgIcon: 'commonSvg-标签',
          src: '/images/manageImages/标签.png'
        },
        component: () => import('/src/views/manage/tagManage/index.vue')
      },
      {
        path: '/essayManage',
        name: 'essayManage',
        meta: {
          title: '随笔管理',
          isHidden: true,
          svgIcon: 'commonSvg-随笔',
          src: '/images/manageImages/随笔.png'
        },
        component: () => import('/src/views/manage/essayManage/index.vue')
      },
      {
        path: '/essayManageEditor',
        name: 'essayManageEditor',
        meta: { title: '随笔编辑' },
        component: () => import('/src/views/user/essay/components/editor.vue')
      },
      {
        path: '/albumManage',
        name: 'albumManage',
        meta: {
          title: '相册管理',
          isHidden: true,
          svgIcon: 'commonSvg-相册',
          src: '/images/manageImages/相册.png'
        },
        component: () => import('/src/views/manage/albumManage/index.vue')
      },
      {
        path: '/gameManage',
        name: 'gameManage',
        meta: {
          title: '游戏管理',
          isHidden: true,
          svgIcon: 'commonSvg-游戏',
          src: '/images/manageImages/游戏.png'
        },
        component: () => import('/src/views/manage/gameManage/index.vue')
      },
      {
        path: '/dramaManage',
        name: 'dramaManage',
        meta: {
          title: '影视管理',
          isHidden: true,
          svgIcon: 'audioSvg-影视',
          src: '/images/manageImages/影视.png'
        },
        component: () => import('/src/views/manage/dramaManage/index.vue')
      },
      // {
      //   path: '/gourmetManage',
      //   name: 'gourmetManage',
      //   meta: { title: '美食管理', isHidden: true, svgIcon: '美食', src: '/images/manageImages/美食.png' },
      //   component: () => import('/src/views/manage/gourmetManage/index1.vue')
      // },
      // {
      //   path: '/musicManage',
      //   name: 'musicManage',
      //   meta: {
      //     title: '音乐管理',
      //     isHidden: true,
      //     svgIcon: 'audioSvg-音乐',
      //     src: '/images/manageImages/音乐.png'
      //   },
      //   component: () => import('/src/views/manage/musicManage/index.vue')
      // },
      {
        path: '/wallpaperManage',
        name: 'wallpaperManage',
        meta: {
          title: '壁纸管理',
          isHidden: true,
          svgIcon: 'commonSvg-壁纸',
          src: '/images/manageImages/壁纸.png'
        },
        component: () => import('/src/views/manage/wallpaperManage/index.vue')
      },
      {
        path: '/dictManage',
        name: 'dictManage',
        meta: {
          title: '字典管理',
          isHidden: true,
          svgIcon: 'commonSvg-字典',
          src: '/images/manageImages/字典.png'
        },
        component: () => import('/src/views/manage/dictManage/index.vue')
      },
      {
        path: '/logManage',
        name: 'logManage',
        meta: {
          title: '更新日志管理',
          isHidden: true,
          svgIcon: 'commonSvg-日志',
          src: '/images/manageImages/日志.png'
        },
        component: () => import('/src/views/manage/logManage/index.vue')
      }
    ]
  },
  {
    path: '/profile',
    name: 'profile',
    meta: { title: '', isHidden: true },
    component: () => import('/src/views/profile/index.vue')
  },

  {
    path: '/blogEditor',
    name: 'blogEditor',
    meta: { title: '博客编辑' },
    component: () => import('/src/views/blog/blogEditor/index.vue')
  },
  {
    path: '/blogDisplay',
    name: 'blogDisplay',
    meta: { title: '博客展示' },
    component: () => import('/src/views/blog/blogDisplay.vue')
  },
  {
    path: '/blogEditor',
    name: 'blogEditor',
    meta: { title: '博客编辑' },
    component: () => import('/src/views/blog/blogEditor/index.vue')
  },
  {
    path: '/blogType',
    name: 'blogType',
    meta: { title: '分类', icon: 'FolderOpened', parent: 'blog' },
    component: () => import('/src/views/blog/blogType/index.vue')
  },
  {
    path: '/blogTypePage',
    name: 'blogTypePage',
    meta: { title: '博客分类详情' },
    component: () => import('/src/views/blog/blogType/blogTypePage.vue')
  },
  {
    path: '/blogTag',
    name: 'blogTag',
    meta: { title: '标签', icon: 'PriceTag', parent: 'blog' },
    component: () => import('/src/views/blog/blogTag/index.vue')
  },
  {
    path: '/blogTagPage',
    name: 'blogTagPage',
    meta: { title: '博客标签详情' },
    component: () => import('/src/views/blog/blogTag/blogTagPage.vue')
  },
  {
    path: '/statistics',
    name: 'statistics',
    meta: { title: '统计', icon: 'Histogram', parent: 'blog' },
    component: () => import('/src/views/blog/statistics/index.vue')
  },
  {
    path: '/album',
    name: 'album',
    meta: { title: '相册', icon: 'Camera', parent: 'user' },
    component: () => import('/src/views/user/album/index.vue')
  },
  {
    path: '/albumPage',
    name: 'albumPage',
    meta: { title: '相册详情' },
    component: () => import('/src/views/user/album/albumPage.vue')
  },
  {
    path: '/essay',
    name: 'essay',
    meta: { title: '随笔', icon: 'Notebook', parent: 'user' },
    component: () => import('/src/views/user/essay/index.vue')
  },
  {
    path: '/essayEditor',
    name: 'essayEditor',
    meta: { title: '随笔编辑' },
    component: () => import('/src/views/user/essay/essayEditor.vue')
  },
  // {
  //   path: '/equipment',
  //   name: 'equipment',
  //   meta: { title: '装备', icon: 'Suitcase', parent: 'user' },
  //   component: () => import('/src/views/user/equipment/index1.vue')
  // },
  // {
  //   path: '/music',
  //   name: 'music',
  //   meta: { title: '音乐', icon: 'Headset', parent: 'user' },
  //   component: () => import('/src/views/user/music/index.vue')
  // },
  {
    path: '/svg',
    name: 'svg',
    meta: { title: 'svg集合', svgIcon: 'svg图片', parent: 'assembly' },
    component: () => import('/src/views/assembly/svg/index.vue')
  },
  {
    path: '/ai',
    name: 'ai',
    meta: { title: 'AI', icon: 'Orange', parent: 'assembly' },
    component: () => import('/src/views/assembly/ai/chatAi.vue')
  },
  {
    path: '/slice',
    name: 'slice',
    meta: { title: '切片', icon: 'Orange', parent: 'assembly' },
    component: () => import('/src/views/assembly/slice/index.vue'),
    children: [
      {
        path: '/rubiks',
        name: 'rubiks',
        meta: {
          title: '魔方',
          introduction: '普通3x3动态魔方',
          url: 'http://120.48.127.181/file/slice/魔方.png'
        },
        component: () => import('/src/views/assembly/slice/rubiks/index.vue')
      },
      {
        path: '/rotatingRubik',
        name: 'rotatingRubik',
        meta: {
          title: '图片旋转魔方',
          introduction: '可旋转展示6面图片的魔方',
          url: 'http://120.48.127.181/file/slice/图片旋转魔方.png'
        },
        component: () => import('/src/views/assembly/slice/rotatingRubik/index.vue')
      },
      {
        path: '/3dMenu',
        name: '3dMenu',
        meta: {
          title: '3d菜单',
          introduction: '3d菜单',
          url: 'http://120.48.127.181/file/slice/3d菜单.png'
        },
        component: () => import('/src/views/assembly/slice/3dMenu/index.vue')
      },
      {
        path: '/cardFlip',
        name: 'cardFlip',
        meta: {
          title: '卡片翻转',
          introduction: '卡片翻转',
          url: 'http://120.48.127.181/file/slice/卡片翻转.png'
        },
        component: () => import('/src/views/assembly/slice/cardFlip/index.vue')
      },
      {
        path: '/heartLoading',
        name: 'heartLoading',
        meta: {
          title: '心型加载',
          introduction: '心型加载',
          url: 'http://120.48.127.181/file/slice/心型加载.png'
        },
        component: () => import('/src/views/assembly/slice/heartLoading/index.vue')
      },
      {
        path: '/neonRain',
        name: 'neonRain',
        meta: {
          title: '霓虹雨',
          introduction: '雨',
          url: 'http://120.48.127.181/file/slice/雨.png'
        },
        component: () => import('/src/views/assembly/slice/neonRain/index.vue')
      },
      {
        path: '/stackCard',
        name: 'stackCard',
        meta: {
          title: '堆叠卡',
          introduction: '堆叠卡',
          url: 'http://120.48.127.181/file/slice/堆叠卡.png'
        },
        component: () => import('/src/views/assembly/slice/stackCard/index.vue')
      },
      {
        path: '/rotateMenu',
        name: 'rotateMenu',
        meta: {
          title: '旋转菜单',
          introduction: '旋转菜单',
          url: 'http://120.48.127.181/file/slice/旋转菜单.png'
        },
        component: () => import('/src/views/assembly/slice/rotateMenu/index.vue')
      },
      {
        path: '/parallax',
        name: 'parallax',
        meta: {
          title: '视差',
          introduction: '视差',
          url: 'http://120.48.127.181/file/slice/视差.png'
        },
        component: () => import('/src/views/assembly/slice/parallax/index.vue')
      },
      {
        path: '/rain',
        name: 'rain',
        meta: {
          title: '雨',
          introduction: '雨',
          url: 'http://120.48.127.181/file/slice/雨.png'
        },
        component: () => import('/src/views/assembly/slice/rain/index.vue')
      },
      {
        path: '/swiper',
        name: 'swiper',
        meta: {
          title: '倾斜轮播',
          introduction: '倾斜轮播',
          url: 'http://120.48.127.181/file/slice/雨.png'
        },
        component: () => import('/src/views/assembly/slice/swiper/index.vue')
      }
    ]
  },
  {
    path: '/testField',
    name: 'testField',
    meta: { title: '试验田', icon: 'OfficeBuilding', parent: 'assembly' },
    component: () => import('/src/views/assembly/testField/index.vue'),
    children: [
      // {
      //   path: '/vForm',
      //   name: 'vForm',
      //   meta: {
      //     title: '低代码',
      //     introduction: '视差',
      //     url: 'http://120.48.127.181/file/testField/低代码.png'
      //   },
      //   component: () => import('/src/views/assembly/testField/vForm/index.vue')
      // },
      {
        path: '/draggle',
        name: 'draggle',
        meta: {
          title: '自由拖拽',
          introduction: '自由拖拽',
          url: 'http://120.48.127.181/file/testField/自由拖拽.png'
        },
        component: () => import('/src/views/assembly/testField/draggle/index.vue')
      },
      {
        path: '/ganttChart',
        name: 'ganttChart',
        meta: {
          title: '甘特图',
          introduction: '甘特图',
          url: 'http://120.48.127.181/file/testField/甘特图.png'
        },
        component: () => import('/src/views/assembly/testField/dhxGanttChart/index.vue')
      },
      {
        path: '/editor',
        name: 'editor',
        meta: {
          title: '富文本编辑器',
          introduction: '富文本编辑器',
          url: 'http://120.48.127.181/file/testField/富文本编辑器.png'
        },
        component: () => import('/src/views/assembly/testField/editor/index.vue')
      },
      {
        path: '/jsplumb',
        name: 'jsplumb',
        meta: {
          title: '连线绘图',
          introduction: '连线绘图',
          url: 'http://120.48.127.181/file/testField/连线绘图.png'
        },
        component: () => import('/src/views/assembly/testField/jsplumb/index.vue')
      },
      {
        path: '/gridLayout',
        name: 'gridLayout',
        meta: {
          title: '宫格',
          introduction: '可选宫格',
          url: 'http://120.48.127.181/file/testField/低代码.png'
        },
        component: () => import('/src/views/assembly/testField/gridLayout/index.vue')
      }
      // {
      //   path: '/canvas',
      //   name: 'canvas',
      //   meta: {
      //     title: 'canvas动画',
      //     introduction: 'canvas动画',
      //     url: 'http://120.48.127.181/file/testField/canvas动画.png'
      //   },
      //   component: () => import('/src/components/sakura/line.vue')
      // }
    ]
  },
  {
    path: '/personalProfile',
    name: 'personalProfile',
    meta: { title: '个人信息', icon: 'User', parent: 'intro' },
    component: () => import('/src/views/introduction/personalProfile/index.vue')
  },
  {
    path: '/projectExperience',
    name: 'projectExperience',
    meta: { title: '项目经历', icon: 'User', parent: 'intro' },
    component: () => import('/src/views/introduction/projectExperience/index.vue')
  },
  // {
  //   path: '/fitness',
  //   name: 'fitness',
  //   meta: { title: '运动', svgIcon: '运动', parent: 'other' },
  //   component: () => import('/src/views/others/fitness/index1.vue')
  // },
  {
    path: '/game',
    name: 'game',
    meta: { title: '游戏', svgIcon: '游戏', parent: 'other' },
    component: () => import('/src/views/others/game/index.vue')
  },
  {
    path: '/drama',
    name: 'drama',
    meta: { title: '影视', icon: 'VideoCamera', parent: 'other' },
    component: () => import('/src/views/others/dramaSeries/index.vue')
  },
  // {
  //   path: '/gourmet',
  //   name: 'gourmet',
  //   meta: { title: '美食', icon: 'KnifeFork', parent: 'other' },
  //   component: () => import('/src/views/others/gourmet/index1.vue')
  // },
  {
    path: '/website',
    name: 'website',
    meta: { title: '本站', svgIcon: '关于', parent: 'associate' },
    component: () => import('/src/views/associate/website/index.vue')
  },
  {
    path: '/updateLog',
    name: 'updateLog',
    meta: { title: '更新日志', svgIcon: '日志', parent: 'associate' },
    component: () => import('/src/views/associate/updateLog/index.vue')
  },
  {
    path: '/refresh',
    name: 'refresh',
    component: () => import('/src/components/blankTemplate.vue')
  },
  {
    path: '/:catchAll(.*)',
    name: '404',
    meta: { title: '', isHidden: true },
    component: () => import('/src/views/error/index.vue')
  },
  {
    path: '/error',
    name: 'error',
    meta: { title: '', isHidden: true },
    component: () => import('/src/views/error/index.vue')
  }
];
const router = createRouter({
  history: createWebHashHistory('/'),
  routes
});

const navShowRoute = ['login', 'register'];
const footerNotShowRoute = ['manage', 'statistics', 'personalInfo', 'blogEditor', 'ai'];
//切换路由后回到顶部
router.afterEach(() => {
  scrollToView();
});

router.beforeEach(async (to: any) => {
  // 记录路由
  console.log(to);
  window.localStorage.setItem('lastRouter', JSON.stringify(to));
  // 随机壁纸
  let themeStore = useThemeStore();
  if (!themeStore) return;
  if (themeStore.options && themeStore.options.isRandom) {
    let backUrl =
      themeStore.imgWallpaperList[random(0, themeStore.imgWallpaperList.length - 1)]?.url;
    if (backUrl) {
      let back = document.getElementById('tsparticles') as any;
      back.style.background = 'left/cover fixed no-repeat url(' + backUrl + ')';
    }
  }
  if (to.path.includes('login') || to.path.includes('anage')) {
    themeStore.isFooterShow = false;
    themeStore.isShow = false;
  } else {
    themeStore.isFooterShow = true;
    themeStore.isShow = true;
  }
});

// 滚动到指定的位置
function scrollToView() {
  let el = document.querySelector('.el-main') as any;
  el?.scrollTo({ top: 0, behavior: 'smooth' });
}

export default router;
