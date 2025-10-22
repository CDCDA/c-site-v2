<!--suppress ALL -->
<template>
  <div id="app-theme" data-theme="theme-white">
    <div class="dialog-base"></div>
    <div class="select-base"></div>
    <div class="messageBox-base"></div>
    <el-container class="container">
      <el-header class="el-header">
        <common-header v-if="themeStore.isShow" />
      </el-header>
      <el-main id="main">
        <sakura :new-options="sakuraOptions" v-if="themeStore.options?.isSakura" />
        <video
          id="tsparticles"
          class="tsparticles-video"
          autoplay
          loop
          muted
          :src="src"
          v-if="themeStore.backType == 'video'"
        />
        <div id="tsparticles" class="particles" v-else />
        <Particles
          v-if="themeStore.options && themeStore.options.isParticles"
          id="particles"
          :options="options"
        />
        <div class="router-container">
          <router-view v-slot="{ Component }">
            <component :is="Component" />
          </router-view>
        </div>
        <common-footer v-if="themeStore.isFooterShow" />
      </el-main>
    </el-container>
    <SideSetting :isHideen="false" v-if="themeStore.isShow"></SideSetting>
    <!--    <rightClickMenu :visible="visible" :left="left" :top="top" />-->
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import sakura from '@/components/sakura/index.vue';
import rightClickMenu from '@/components/rightClickMenu/index.vue';
import { useRouter } from 'vue-router';
import useThemeStore from '@/store/modules/theme.ts';
import { verifyTokenNoIntercept } from '@/api/system/auth.ts';
import useWebSocketStore from '@/store/modules/webSocket.js';
import useUserStore from './store/modules/user';
import CommonHeader from '@/views/layout/commonHeader/index.vue';
import CommonFooter from '@/views/layout/commonFooter/index.vue';
import { autoClearTimer } from './utils/timer';
import SideSetting from '@/views/layout/sideSetting/index.vue';
import { pageWallpapers } from '@/api/system/wallpaper.ts';
const sakuraOptions = ref({
  staticx: false,
  stop: null,
  num: 15,
  show: true,
  zIndex: -1
}) as any;

const src = ref(null) as any;
var userStore = useUserStore();
var themeStore = useThemeStore();
themeStore.isFooterShow = false;
themeStore.isShow = false;
const router = useRouter() as any;
const visible = ref(false);
const top = ref(0);
const left = ref(0);
const options = ref({
  background: {
    color: {
      // value: "#000", //粒子颜色
    }
  },
  fpsLimit: 60,
  interactivity: {
    events: {
      onClick: {
        enable: true,
        mode: 'repulse' //可用的click模式有: "push", "remove", "repulse", "bubble"。
      },
      onHover: {
        enable: true,
        mode: 'grab' //可用的hover模式有: "grab", "repulse", "bubble"。
      },
      resize: true
    },
    modes: {
      bubble: {
        distance: 400,
        duration: 2,
        opacity: 0.8,
        size: 40
      },
      push: {
        quantity: 4
      },
      repulse: {
        distance: 200,
        duration: 0.4
      }
    }
  },
  particles: {
    color: {
      value: '#ffffff'
    },
    links: {
      color: '#ffffff', //'#dedede'。线条颜色。
      distance: 150, //线条长度
      enable: true, //是否有线条
      opacity: 0.5, //线条透明度。
      width: 1 //线条宽度。
    },
    collisions: {
      enable: false
    },
    move: {
      direction: 'none',
      enable: true,
      outMode: 'bounce',
      random: false,
      speed: 2, //粒子运动速度。
      straight: false
    },
    number: {
      density: {
        enable: true,
        area: 800
      },
      value: 60 //粒子数量。
    },
    opacity: {
      value: 0.5 //粒子透明度。
    },
    shape: {
      type: 'circle' //可用的粒子外观类型有："circle","edge","triangle", "polygon","star"
    },
    size: {
      random: true,
      value: 5
    }
  },
  detectRetina: true
}) as any;

// watch(
//   () => visible.value,
//   newValue => {
//     if (newValue) {
//       //菜单显示的时候
//       // document.body.addEventListener，document.body.removeEventListener它们都接受3个参数
//       // ("事件名" , "事件处理函数" , "布尔值");
//       // 在body上添加事件处理程序
//       document.body.addEventListener('click', closeMenu);
//     } else {
//       //菜单隐藏的时候
//       // 移除body上添加的事件处理程序
//       document.body.removeEventListener('click', closeMenu);
//     }
//   }
// );

//右击
function openMenu(e: any) {
  var x = e.pageX; //这个应该是相对于整个浏览器页面的x坐标，左上角为坐标原点（0,0）
  var y = e.pageY; //这个应该是相对于整个浏览器页面的y坐标，左上角为坐标原点（0,0）
  top.value = y;
  left.value = x;
  visible.value = true; //显示菜单
}
//关闭菜单
function closeMenu() {
  visible.value = false; //关闭菜单
}
let userData = window.localStorage.getItem('userData') as any;
if (userData) {
  userData = JSON.parse(userData) as any;
  userStore.token = userData.token;
  userStore.userId = userData.userId;
  userStore.userName = userData.userName;
  userStore.permission = userData.permission;
  userStore.nickName = userData.nickName;
  userStore.email = userData.email;
  userStore.avatar = userData.avatar;
}

async function init() {
  let appTheme = document.querySelector('#app-theme') as any;
  // 获取缓存的主题数据
  let themeData = window.localStorage.getItem('themeData') as any;
  if (themeData) {
    themeData = JSON.parse(themeData) as any;
    themeStore.theme = themeData.theme ? themeData.theme : 'theme-white';
    themeStore.backUrl = themeData.backUrl ? themeData.backUrl : '@/assets/images/宇宙天际.jpg';
    themeStore.backType = themeData.backType ? themeData.backType : 'img';
    themeStore.options = themeData.options ? themeData.options : [];
  }
  var { theme, backUrl, options, backType } = themeStore;
  // 设置主题
  appTheme.setAttribute('data-theme', theme);
  // 设置壁纸
  let back = document.getElementById('tsparticles') as any;
  if (backType == 'img') {
    back.style.background = 'left/cover fixed no-repeat url(' + backUrl + ')';
  } else if (backType == 'color') {
    back.style.background = backUrl;
  } else if (backType == 'video') {
    src.value = backUrl;
  }
  // 设置字体
  appTheme.style.fontFamily = options.fontFamily;
  if (!userData) {
    router.push({ path: '/login' });
    return;
  }
  //查看是否有token
  if (userStore.token) {
    const res = await verifyTokenNoIntercept();
    if (res.data.code === 200) {
      // 上次访问的路由
      const routerStr = window.localStorage.getItem('lastRouter');
      if (routerStr) {
        const route = JSON.parse(routerStr) as any;
        let path = route?.path || '';
        if (!path) path = '/home';
        if (path === '/') path = '/home';
        router.push({ path, query: router?.query });
      }
    } else {
      router.push({ path: '/login' });
    }
  } else {
    userStore.token = '';
    router.push({ path: '/login' });
  }
}

onMounted(() => {
  themeStore.isShow = true;
  // console.log(window.location.href);
  if (!window.location.href.includes('/manage')) {
    init();
  } else {
    themeStore.isFooterShow = false;
    themeStore.isShow = false;
  }
  // getBackList(themeStore);
  // setTimeout(() => {
  //   const webSocketStore = useWebSocketStore();
  //   webSocketStore.connectWebSocket({
  //     // onsend: event => {
  //     //   console.log('订阅数据', 'AAAAAAAAA');
  //     //   event.currentTarget.send(`{"type": "subscribe", "data": "${'AAA'}"}`);
  //     // }
  //   });
  // }, 3000);
});
</script>

<style lang="scss">
:root {
  --el-color-primary: #00968c !important;
  --el-color-danger: #f76d6b !important;
  --el-color-info: #94929c !important;
  --el-color-warning: #e6a23c !important;
  --el-color-success: #63c339 !important;
  --el-color-primary-light-3: #4db6ac !important;
}
@font-face {
  font-family: 'DaoLiTi';
  src: url(/AlimamaDaoLoTi/AlimamaDaoLiTi.ttf);
}
@font-face {
  font-family: 'FangDaKai';
  src: url(/font-family/AlimamaDongFangDaKai-Regular/AlimamaDongFangDaKai-Regular.ttf);
}
@font-face {
  font-family: 'DingDing';
  src: url(/font-family/iuMg2dm8ifws/2sjeZLl8aCWp.woff2);
}
@font-face {
  font-family: 'Uranus';
  src: url(/font-family/Uranus-Pixel-main/Uranus_Pixel_11Px.ttf);
}
@font-face {
  font-family: 'Shark';
  src: url(/font-family/Shark/shark.woff2);
}
#app-theme,
#app {
  @include full();
}
#app-theme {
  animation: blur-to-clear 1s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
body,
html {
  height: 100%;
  width: 100% !important;
  margin: 0;
  padding: 0;
  overflow: hidden;
}
@keyframes blur-to-clear {
  0% {
    filter: blur(20px) brightness(0.4);
    transform: scale(1.5);
  }
  100% {
    filter: blur(0px) brightness(1);
    transform: scale(1);
  }
}
#tsparticles {
  height: 100%;
  width: 100%;
  object-fit: cover;
  position: absolute;
  z-index: -1;
  left: 0;
}
.particles {
  position: fixed;
  top: 0;
  left: 0;
  @include full();
  z-index: -1;
}
#app {
  @include getValue() {
    @import '@/assets/styles/common.scss';
    @import '@/assets/styles/element-ui.scss';
  }
}
.router-container {
  min-height: 100vh;
}
.el-header {
  text-align: center;
  line-height: 56px;
  padding: 0 0 0 0 !important;
  height: auto !important;
}

.el-aside {
  text-align: center;
}

.el-main {
  text-align: center;
  padding: 0;
  overflow: auto;
  padding: 0 0 0 0 !important;
}
.container {
  height: 100%;
  background: transparent;
  overflow: auto;
}
:deep(.el-menu.el-menu--horizontal) {
  border-bottom: solid #e6e6e6;
}
</style>
