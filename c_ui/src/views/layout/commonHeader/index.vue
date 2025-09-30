<!--suppress ALL -->
<template>
  <div class="common-header" :class="isHideen ? 'is-hidden' : 'is-show'">
    <div class="header-name">
      <common-link></common-link>
      <a class="title" href="/home" title="返回主页"><span>记录</span></a>
      <!-- <v-mini-weather class="weather">
        <template #default="{ weather, icon }">
          <v-mini-weather-icon :icon="icon"></v-mini-weather-icon>
          <span
            >{{ weather.cityname }}/{{ weather.weather }}/{{ weather.temp
            }}{{ setWeatherData(weather) }}</span
          >
        </template>
      </v-mini-weather> -->
    </div>
    <div class="header-bar">
      <div class="header-bar-menu">
        <div class="header-bar-item" v-for="item in menuData">
          <div class="item-label">
            <svg-icon
              class="menu-icon"
              :iconName="item.iconName"
              style="font-size: 1.1rem; margin-right: 8px"
            />
            <span> {{ item.label }}</span>
          </div>
          <div class="item-content" v-if="item.children">
            <div class="item-menu">
              <div class="item" @click="clickMenu(e)" v-for="e in item.children">
                {{ e.label }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="header-bar-title">
        <div class="header-bar-title-text">{{ pageName }}</div>
      </div>
    </div>

    <div class="header-right">
      <el-tooltip content="随机文章" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon iconName="commonSvg-列车1" class="header-icon train" @click="toRandom" />
        </i>
      </el-tooltip>
      <el-tooltip content="搜索" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon iconName="commonSvg-搜索" class="header-icon search" @click="searchClick" />
        </i>
      </el-tooltip>
      <el-tooltip content="控制台" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon
            iconName="commonSvg-控制台"
            class="header-icon console"
            v-permission="'show'"
            @click="toManage"
          />
        </i>
      </el-tooltip>
      <el-tooltip content="登出" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon iconName="commonSvg-登出" class="header-icon logout" @click="logout" />
        </i>
      </el-tooltip>
      <c-image class="avatar" :src="userStore.avatar" @click="toPersonal"></c-image>
    </div>
  </div>

  <searchDialog v-if="searchVisible" @close="close"></searchDialog>
</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus';
import { ref, onMounted, watch } from 'vue';
import { getRandomBlog } from '@/api/blog';
import commonLink from './components/commonLink.vue';
import searchDialog from './components/blogSearchDialog.vue';
// import { vMiniWeather, vMiniWeatherIcon } from 'vue3-mini-weather';
import { debounce } from 'lodash';
import { autoClearTimer } from '@/utils/timer';
import { useRouter, onBeforeRouteUpdate } from 'vue-router';
import useUserStore from '@/store/modules/user';
import useThemeStore from '@/store/modules/theme.ts';
const themeStore = useThemeStore();
const userStore = useUserStore();

const router = useRouter() as any;
const articleElement = ref(null as any);
const progress = ref('0' as any);
const isHideen = ref(false);
const searchVisible = ref(false as any);
const weatherData = ref({});
const pageName = ref('首页');
const pageChange = ref(true);
const menuData = ref([] as any);

const menuHeader = [
  {
    label: '博客',
    value: 'blog',
    iconName: 'pixelSvg-刨冰',
    children: []
  },
  {
    label: '简介',
    children: [],
    iconName: 'pixelSvg-米饭',
    value: 'intro'
  },
  {
    label: '我的',
    children: [],
    iconName: 'pixelSvg-西瓜',
    value: 'user'
  },
  {
    label: '组件',
    children: [],
    iconName: 'pixelSvg-电池',
    value: 'assembly'
  },
  {
    label: '其他',
    children: [],
    iconName: 'pixelSvg-游戏机',
    value: 'other'
  },
  {
    label: '关于',
    children: [],
    iconName: 'pixelSvg-蜂蜜',
    value: 'associate'
  }
];

let headerBarMenu = document.querySelector('.header-bar-menu') as any;
let headerBarTitle = document.querySelector('.header-bar-title-text') as any;

function setWeatherData(weather: any) {
  weatherData.value = weather;
  (localStorage as any).setItem('weather', JSON.stringify(weather));
}

function toManage() {
  // 获取完整的路由地址
  const routeUrl = router.resolve({
    name: 'blogManage'
  });
  // 使用 window.open 打开新标签页
  window.open(routeUrl.href, '_blank');
  // window.open(item.url, '_blank');
  // router.push({ path: '/manage' });
}

const changeHeaderBar = debounce((newVal: any, oldVal: any) => {
  let difference = newVal - oldVal;
  if (headerBarMenu && headerBarTitle)
    if (difference > 0) {
      headerBarMenu.style.transform = 'translateY(-100px)';
      headerBarTitle.style.transform = 'translateY(0px)';
    } else {
      headerBarMenu.style.transform = 'translateY(0px)';
      headerBarTitle.style.transform = 'translateY(100px)';
    }
  isHideen.value = newVal === '0' || newVal === 'NaN';
}, 300); // 500ms 防抖时间

watch(
  () => progress.value,
  (newVal, oldVal) => {
    changeHeaderBar(newVal, oldVal);
  },
  { deep: true, immediate: true }
);

const isWaveShow = ref(false as any);

// 页面滚动时间(防抖)
const scrollEvent = debounce(() => {
  progress.value =
    Math.round(
      (articleElement.value.scrollTop /
        (articleElement.value.scrollHeight - articleElement.value.clientHeight)) *
        100
    ) + '';
}, 300); // 500ms 防抖时间

function close() {
  searchVisible.value = false;
}

function searchClick() {
  searchVisible.value = true;
}

//随机博客
async function toRandom() {
  const { data, code } = (await getRandomBlog()) as any;
  if (code == 200) {
    router.push({
      name: 'refresh'
    });
    autoClearTimer(() => {
      router.push({
        name: 'blogDisplay',
        query: { blogId: data }
      });
    }, 0);
  }
}

function toPersonal() {
  router.push({ path: '/personalInfo' });
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.userId = '';
      userStore.userName = '';
      userStore.token = '';
      userStore.permission = [];
      window.localStorage.setItem('userData', '');
      router.push({ name: 'login' });
    })
    .catch(() => {});
}

function clickMenu(item: any) {
  pageName.value = item.label;
  if (item.name === 'logOut') {
  } else router.push({ name: item.name });
}

router.options.routes.forEach((route: any) => {
  if (!route.meta) return;
  const { icon, svgIcon, remark, parent } = route.meta;
  if (icon || svgIcon) {
    let parentMenu = menuHeader.find((x: any) => x.value == parent) as any;
    parentMenu?.children.push({ label: remark, icon, svgIcon, name: route.name });
  }
});
menuData.value = menuHeader;

// 设置首页和顶栏颜色
function setHeaderFontColor() {
  if (!themeStore.options) return;
  const { mhFontColor } = themeStore.options;
  let header = document.querySelector('.common-header') as any;
  if (!header) return;
  header.style.color = mhFontColor;
}

onMounted(() => {
  headerBarMenu = document.querySelector('.header-bar-menu') as any;
  headerBarTitle = document.querySelector('.header-bar-title-text') as any;
  autoClearTimer(() => {
    // 监听滚动事件并更新样式
    articleElement.value = document.querySelector('.el-main');
    window.addEventListener('scroll', scrollEvent, true);
    setHeaderFontColor();
  }, 0);
});
</script>
<style lang="scss" scoped>
.avatar {
  height: 2rem;
  width: 2rem;
  border-radius: 2rem;
  margin-left: 0.5rem;
  border: 3px solid white;
  cursor: pointer;
}
.side-setting {
  opacity: 1;
}
.is-hidden.side-setting {
  opacity: 0;
}
@include theme() {
  @keyframes label-in {
    0% {
      left: -138px;
    }
    20% {
      left: -6px;
    }
    60% {
      left: -6px;
    }
    100% {
      left: -117px;
    }
  }

  @keyframes blur-to-clear {
    0% {
      -webkit-filter: blur(50px);
      transform: scale(1.5);
      filter: blur(50px);
    }
    50% {
      -webkit-filter: blur(30px);
      transform: scale(1.2);
      filter: blur(30px);
    }
    100% {
      -webkit-filter: blur(0);
      transform: scale(1);
      filter: blur(0);
    }
  }

  .common-header {
    z-index: 100;
    position: absolute;
    // animation: re-slide-in 1.5s forwards linear;
    top: 0;
    width: calc(100vw - 80px);
    padding: 0 40px;
    color: get('font-color');
    display: flex;
    justify-content: space-between;
    font-weight: bold;
    height: 2.8rem;
    margin-bottom: 10px;
    //background: get('back');
    background: get('back-tr');
    align-items: center;
    .page-name {
      width: 140px;
      height: 57px;
      display: flex;
      z-index: 999;
      justify-content: center;
      align-items: center;
      color: #363636;
      font-size: 60px;
      text-align: center;
      transform: perspective(0.5em) rotateX(5deg);
      top: 57px;
      left: -53px;
      border-radius: 4px;
      position: absolute;
      background: get('back');
      animation: label-in 2s forwards linear;
      .page-name-text {
        animation: scroll 0.5s forwards linear;
        transform: perspective(0.5em) rotateX(-9deg);
        content: attr(data-before);
        font-size: 24px;
        top: 3px;
        position: absolute;
        color: get('font-color');
      }
    }
    @keyframes name-in {
      0% {
        left: -117px;
      }
      100% {
        left: -6px;
      }
    }
    @keyframes name-out {
      0% {
        left: -6px;
      }
      100% {
        left: -117px;
      }
    }
    .page-name.is-change:hover {
      animation: name-in 0.5s forwards linear;
    }
    .page-name.is-change:not(:hover) {
      animation: name-out 0.5s forwards linear;
    }
    .page-name.is-change {
      @keyframes scroll {
        0% {
          transform: translateX(-100%);
        }
        70% {
          transform: translateX(0%);
        }
        80% {
          transform: translateX(15%);
        }
        100% {
          transform: translateX(0%);
        }
      }
      .page-name-text {
        animation: scroll 0.5s forwards linear;
      }
    }
    :deep(.el-input__wrapper) {
      border: none !important;
      height: 25px;
    }
    :deep(.el-input-group__append) {
      height: 27px;
      background: transparent;
      padding: 0 14px;
      .el-buttton {
        box-shadow: none !important;
      }
    }
    :deep(.el-button) {
      box-shadow: none !important;
    }
    .header-name,
    .header-bar,
    .header-right {
      color: inherit;
      display: flex;
      justify-content: center;
    }
    .header-name {
      width: 300px;
      font-weight: bold;
      font-size: 1.2rem;
      display: flex;
      justify-content: start;
      align-items: center;
      height: 100%;
      .header-name-icon {
        margin-right: 10px;
        cursor: pointer;
        color: inherit;
        padding: 6px;
      }
      .header-name-icon:hover {
        margin-right: 10px;
        cursor: pointer;
        background: get('bk');
        color: white;
        border-radius: 25px;
      }
      .title {
        cursor: pointer;
        text-decoration: none;
        color: inherit;
        position: relative;
        height: 100%;
        @include flex;
        &:before {
          content: ' ';
          background: url(../../../assets/svg/commonSvg/主页.svg) no-repeat;
          background-size: cover;
          height: 1.5rem;
          width: 1.5rem;
          display: inline-block;
          position: absolute;
          opacity: 0;
          z-index: 1;
        }
        &:after {
          background: get('bk');
          content: ' ';
          opacity: 0;
          width: 100%;
          height: 2rem;
          border-radius: 6px;
          display: inline-block;
          position: absolute;
          padding: 0 10px;
          box-shadow: get('box-shadow');
        }
      }
      .weather {
        @include flex;
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-left: 14px;
        .v-mini-weather-icon {
          display: flex;
          align-items: center;
          height: 42px;
        }
      }
      .title:hover {
        &:before {
          opacity: 1;
        }
        &:after {
          opacity: 1;
        }
      }
    }
    .header-bar {
      @include flex-column;
      height: 100%;
      width: calc(100% - 600px);
      font-size: 1rem;
    }
    .header-bar-menu {
      width: 100%;
      transform: translateY(0px);
      transition: all 0.2s linear;
      height: 100%;
      @include flex;
      .header-bar-item {
        padding: 0 20px;
        font-size: 1rem;
        cursor: pointer;
        position: relative;
        @include flex;
        &::before {
          content: ' ';
          background: #ffa500;
          height: 7px;
          width: 0%;
          display: inline-block;
          position: absolute;
          z-index: -1;
          left: 20px;
          bottom: 0;
          transition: all 0.3s ease;
        }
      }
      .header-bar-item:hover {
        .item-label {
          span {
            color: #ffa500;
          }
        }
        &::before {
          width: 65%;
        }
      }
      .item-label {
        display: inline-block;
        position: relative;
      }
      .item-content {
        position: absolute;
        visibility: hidden;
        opacity: 0;
        transition: all 0.5s ease;
        top: 49px;
        background: get('back-tr');
        backdrop-filter: blur(20px);
        border-radius: 6px;
        overflow: hidden;
        box-shadow: get('box-shadow');
        .item-menu {
          padding: 5px 10px;
          width: 8rem;
          @include flex-column;
          .item {
            @include flex;
            .menu-icon {
              margin: 0 10px 0 0;
            }
            text-align: center;
            height: 1.7rem;
            line-height: 1.7rem;
            cursor: pointer;
            width: calc(100% - 10px);
            background: hsla(0, 0%, 100%, 0.4392156862745098);
            border-radius: 4px;
            margin: 5px 10px;
            box-shadow: get('box-shadow');
            font-size: 0.9rem;
            padding: 3px 5px;
            color: #4a4a4a;
            &:hover {
              color: #ffa500;
            }
          }
        }
      }
      .header-bar-item:hover .item-content {
        visibility: visible;
        opacity: 1;
      }
      .header-bar-item:not(:hover) .item-content {
        visibility: hidden;
        opacity: 0;
      }
    }
    .header-bar-title {
      width: 150px;
      position: absolute;
      height: 100%;
      overflow: hidden;
      z-index: -1;
      cursor: pointer;
      @include flex;
      div {
        transform: translateY(100px);
        transition: all 0.2s linear;
        position: relative;
        // cursor: pointer;
        // &:after {
        //   content: '回到顶部';
        //   opacity: 1;
        //   z-index: 11;
        //   transition: all 0.2s linear;
        //   width: 100px;
        //   height: 35px;
        //   background: get('bk');
        //   border-radius: 12px;
        //   display: inline-block;
        //   position: absolute;
        //   left:0;
        //   top: 10px;
        //   padding:0 10px;
        // }
      }
      // div:hover {
      //   &:after {
      //     opacity: 1;
      //   }
      // }
    }
    .header-right {
      width: 300px;
      display: flex;
      justify-content: end;
      align-items: center;
      .svg-icon-wrap {
        position: relative;
        margin: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        &:active {
          transform: translateY(1px);
        }
      }
      .svg-icon-wrap::before {
        width: 1.5rem;
        height: 1.5rem;
        content: '';
        border-radius: 4px;
        background: get('bk');
        position: absolute;
        opacity: 0;
      }
      .svg-icon-wrap:hover {
        &::before {
          opacity: 1;
        }
        .header-icon {
          :deep(.theme-icon) {
            fill: white !important;
          }
        }
      }
      .header-icon {
        font-size: 1.3rem;
        @include flex;
        cursor: pointer;
        position: relative;
        outline: unset;
      }
    }
    .progress {
      margin: 0 10px;
      border-radius: 30px;
      font-size: 0.8rem;
      height: 30px;
      width: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: get('font-color');
      color: get('re-font-color');
      position: relative;
      transition: all 0.3s ease-in-out;
      .el-icon {
        visibility: hidden;
        position: absolute;
        height: 30px;
        width: 30px;
        border-radius: 30px;
        line-height: 30px;
        background: get('font-color');
        font-size: 26px;
        cursor: pointer;
        svg {
          margin-left: -1px;
        }
        // @include flex;
      }
    }
    .progress:hover {
      .el-icon {
        visibility: visible;
      }
    }

    .is-progress-full {
      width: 50px;
      border-radius: 15px;
      cursor: pointer;
    }
  }
  .header-search {
    position: relative;
    :deep(.el-select) {
      position: relative;
      left: -150px;
      width: 150px;
      // visibility: hidden;
      border: none;
    }
  }
  .dropdown {
    margin: 0 5px;
    height: 55px;
    position: relative;
    .dropdown-title {
      display: inline-block;
      position: relative;
      height: 60px;
      line-height: 60px;
      font-size: 1rem;
      color: #fff;
      background-color: #000;
      padding: 0 24px;
      border-radius: 4px;
      cursor: pointer;
      // transition: all 1s ease-in-out;
    }

    .dropdown-content {
      position: absolute;
      visibility: hidden;
      background: get('back');
      opacity: 0;
      transition: all 0.6s ease-in-out;
      right: -25px;
      top: 50px;
      z-index: 999;

      .dropdown-menu {
        margin-top: 4px; // 与title制造距离
        padding: 10px 8px 15px; // 给下面多留一点距离，抵消视觉差
        box-shadow: 0 0 8px;
        backdrop-filter: blur(20px);
        border-radius: 5px;
        padding: 0;
        .menuItem {
          white-space: nowrap;
          text-align: center;
          height: 35px;

          line-height: 35px;
          cursor: pointer;
          border-radius: 4px;
          padding: 0 20px;
          &:hover {
            box-shadow: get('box-shadow');
            border-radius: 5px;
          }
        }
      }
    }

    &:hover .dropdown-content {
      visibility: visible;
      opacity: 1;
    }
  }

  .is-hidden {
    box-shadow: none;
    background: transparent;
  }
  .is-show.common-header {
    color: get('font-color') !important;
    backdrop-filter: blur(5px);
    box-shadow: get('box-shadow');
    .svg-icon {
      color: get('font-color');
    }
    // .svg-icon-wrap:hover {
    //   .header-icon.svg-icon {
    //     :deep(.theme-icon) {
    //       fill: white !important;
    //     }
    //   }
    // }
  }

  .setting {
    position: fixed;
    box-shadow: 0 0 8px;
    backdrop-filter: blur(20px);
    border-radius: 5px;
    // right: 93px;
    padding: 0;
    .setting-item {
      list-style: none;
      text-align: center;
      height: 35px;
      line-height: 35px;
      padding: 0 20px;
    }
    .setting-item:hover {
      box-shadow: get('box-shadow');
      border-radius: 5px;
    }
  }
}
</style>
