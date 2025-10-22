<!--
 * @Description: 换肤
-->
<template>
  <c-dialog
    class="skin-change"
    v-model="dialogVisible"
    title="换肤"
    style="height: 75%"
    width="60%"
    :modal="true"
    align-center
    @close="emit('closeThemeDialog')"
  >
    <el-tabs v-model="activeName" class="skin-tabs" v-loading.fullscreen.lock="loading">
      <el-tab-pane label="主题" name="theme">
        <ThemeTab
          v-if="activeName === 'theme'"
          :themes="themes"
          :activeTheme="activeTheme"
          @changeTheme="changeTheme"
        />
      </el-tab-pane>
      <el-tab-pane label="静态壁纸" name="static">
        <StaticWallpaperTab
          v-if="activeName === 'static'"
          :activeUrl="activeUrl"
          @changeWallpaper="changeWallpaper"
        />
      </el-tab-pane>
      <el-tab-pane label="动态壁纸" name="dynamic">
        <DynamicWallpaperTab
          v-if="activeName === 'dynamic'"
          :activeUrl="activeUrl"
          @changeWallpaper="changeWallpaper"
        />
      </el-tab-pane>
      <el-tab-pane label="其他" name="other">
        <OtherSettingTab v-if="activeName === 'other'" />
      </el-tab-pane>
    </el-tabs>
    <!-- <div class="dialog-footer">
      <div>动态壁纸因为本身文件较大和服务器带宽的限制，所以可能会导致加载缓慢。</div>
    </div> -->
  </c-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import useThemeStore from '@/store/modules/theme.ts';
import { pageWallpapers } from '@/api/system/wallpaper.ts';
import { themes } from './themeData.ts';

import StaticWallpaperTab from './components/staticWallpaperTab.vue';
import DynamicWallpaperTab from './components/dynamicWallpaperTab.vue';
import OtherSettingTab from './components/otherSettingTab.vue';
import ThemeTab from './components/themeTab.vue';

const emit = defineEmits(['closeThemeDialog']);
const themeStore = useThemeStore();
const dialogVisible = ref(true);

const activeName = ref('static');

var activeWallpaper = {} as any;

const activeTheme = ref({}) as any;

var localStorage = window.localStorage as any;

function close() {
  emit('closeThemeDialog');
}

function open() {
  dialogVisible.value = true;
}

/**
 * @description: 设置背景
 * @return {*}
 */
const setBack = (): void => {
  let backUrl = activeWallpaper.url as any;

  themeStore.backUrl = backUrl;
  themeStore.backType = activeWallpaper.type;
  let back = document.getElementById('tsparticles') as any;
  switch (activeWallpaper.type) {
    case 'img':
      back.style.background = 'left/cover fixed no-repeat url(' + backUrl + ')';
      break;
    case 'color':
      back.style.background = backUrl;
      break;
    case 'video':
      back.src = activeWallpaper.url;
      break;
    default:
      break;
  }
  saveThemeData();
};

// 选中背景
function changeWallpaper(item: any) {
  activeWallpaper = item;
  activeUrl.value = item.url;
  setBack();
}

// 选中主题
function changeTheme(item: any) {
  themes.forEach((theme: any) => {
    theme.active = false;
  });
  item.active = true;
  activeTheme.value = JSON.parse(JSON.stringify(item));
  setTheme();
}

/**
 * @description: 设置主题
 * @return {*}
 */
function setTheme() {
  let theme = activeTheme.value.key as any;
  (document.getElementById('app-theme') as any).setAttribute('data-theme', theme);
  themeStore.theme = theme;
  saveThemeData();
}

// 缓存主题数据
function saveThemeData() {
  localStorage.setItem(
    'themeData',
    JSON.stringify({
      theme: themeStore.theme,
      backUrl: themeStore.backUrl,
      options: themeStore.options,
      backType: themeStore.backType
    })
  );
}

const activeUrl = ref('');

/**
 * @description: 初始化主题背景数据
 * @return {*}
 */
function init() {
  // 获取缓存的主题数据
  const { theme, backUrl } = themeStore;
  activeUrl.value = backUrl;
  themes.forEach((e: any) => {
    if (e.key == theme) {
      e.active = true;
      activeTheme.value = JSON.parse(JSON.stringify(e));
    } else {
      e.active = false;
    }
  });
}

onMounted(() => {
  init();
  // getBackList();
});
defineExpose({
  close,
  open
});
</script>
<!--suppress SassScssResolvedByNameOnly -->
<style lang="scss">
@include theme() {
  .skin-change {
    .el-dialog__body {
      overflow: hidden !important;
      padding-top: 5px !important;
      height: calc(100% - 2.5rem - 17px) !important;
    }
    .el-tabs__content {
      height: calc(100% - 55px);
      overflow: auto;
    }
    .el-tabs {
      height: calc(100% - 0px);
      width: 100%;
    }

    .el-dialog__header {
      margin-bottom: 0 !important;
    }
  }
  .skin-change.filter {
    .theme-label,
    .wallpaper-label {
      color: white;
    }
    .wallpaper-item:hover,
    .theme-item:hover,
    .theme-item-active,
    .wallpaper-item-active {
      background: hsla(0, 0%, 100%, 0.4392156862745098);
      &::before {
        border: 2px solid get('border-color');
      }
    }
    .setting-item {
      color: white;
      background-color: rgba(255, 255, 255, 0.19);
      box-shadow: rgba(0, 0, 0, 0.225) 0 0 10px 0;
    }
    .setting-item:hover {
      background: hsla(0, 0%, 100%, 0.4392156862745098);
      &::before {
        border: 2px solid hsla(0, 0%, 100%, 0.4392156862745098);
      }
    }
  }
}
</style>
