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
        <div class="theme-main" v-if="activeName === 'theme'">
          <div
            v-for="item in themes"
            :class="['theme-item', activeTheme.key == item.key ? 'theme-item-active' : '']"
            @click="changeTheme(item)"
          >
            <div class="theme-back" :style="{ background: item.background }"></div>
            <span class="theme-label">{{ item.label }}</span>
          </div>
        </div></el-tab-pane
      >
      <el-tab-pane label="静态壁纸" name="static">
        <div class="back-main" v-loading="false" v-if="activeName === 'static'">
          <div
            v-for="item in backs.filter((e:any)=> e.type !== 'video')"
            :class="['back-item', item.active ? 'back-item-active' : '']"
            @click="changeBack(item)"
          >
            <c-image class="back-image" v-if="item.type == 'img'" :src="item.url" />
            <div v-if="item.type == 'color'" class="back-image" :style="{ background: item.url }" />
            <span class="back-label">{{ item.name }}</span>
          </div>
        </div></el-tab-pane
      >
      <el-tab-pane label="动态壁纸" name="dynamic">
        <div class="back-main" v-loading="false" v-if="activeName === 'dynamic'">
          <div
            v-for="item in backs.filter((e:any)=> e.type == 'video')"
            :class="['back-item', item.active ? 'back-item-active' : '']"
            @click="changeBack(item)"
          >
            <c-image class="back-image" :src="item.coverUrl"></c-image>
            <span class="back-label">{{ item.name }}</span>
          </div>
        </div></el-tab-pane
      >
      <el-tab-pane label="其他" name="other">
        <div class="other-main" v-if="activeName === 'other'">
          <div class="setting-item">
            <div class="setting-item-label">粒子效果(仅静态壁纸)</div>
            <el-switch
              v-model="themeStore.options.isParticles"
              @change="setParticles()"
            ></el-switch>
          </div>
          <div class="setting-item">
            <div class="setting-item-label">樱花特效</div>
            <el-switch v-model="themeStore.options.isSakura" @change="setIsSakura()"></el-switch>
          </div>
          <div class="setting-item">
            <div class="setting-item-label">随机壁纸</div>
            <el-switch v-model="themeStore.options.isRandom" @change="setIsRandom()"></el-switch>
          </div>
          <div class="setting-item">
            <div class="setting-item-label">首页字体颜色</div>
            <el-color-picker v-model="themeStore.options.mhFontColor" @change="setFontColor()" />
          </div>
          <div class="setting-item">
            <div class="setting-item-label">弹窗风格</div>
            <el-radio-group v-model="themeStore.options.dialogType" @change="setDialogType()">
              <el-radio value="normal">常规</el-radio>
              <el-radio value="filter">磨砂</el-radio>
            </el-radio-group>
          </div>
          <div class="setting-item">
            <div class="setting-item-label">字体</div>
            <el-select
              v-model="themeStore.options.fontFamily"
              @change="setFontFamily()"
              append-to=".select-base"
            >
              <el-option
                v-for="item in fontFamilys"
                :value="item.value"
                :label="item.label"
              ></el-option>
            </el-select>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </c-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import useThemeStore from '@/store/modules/theme.ts';
import { pageWallpapers } from '@/api/system/wallpaper.ts';
import { themes, fontFamilys } from './themeData.ts';
const emit = defineEmits(['closeThemeDialog']);
var themeStore = useThemeStore();
const dialogVisible = ref(true);
const loading = ref(false as any);
const activeName = ref('static');
var activeBack = {} as any;
const activeTheme = ref({}) as any;
//背景图
const backs = reactive([] as any);

var localStorage = window.localStorage as any;

function close() {
  emit('closeThemeDialog');
}

function open() {
  dialogVisible.value = true;
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

/**
 * @description: 设置背景
 * @return {*}
 */
const setBack = (): void => {
  let backUrl = activeBack.url as any;
  themeStore.backUrl = backUrl;
  themeStore.backType = activeBack.type;
  let back = document.getElementById('tsparticles') as any;
  switch (activeBack.type) {
    case 'img':
      back.style.background = 'left/cover fixed no-repeat url(' + backUrl + ')';
      break;
    case 'color':
      back.style.background = backUrl;
      break;
    case 'video':
      back.src = activeBack.url;
      break;
    default:
      break;
  }
  saveThemeData();
};

// 设置文字主题
function setFontFamily() {
  const { fontFamily } = themeStore.options;
  let appTheme = document.querySelector('#app-theme') as any;
  appTheme.style.fontFamily = fontFamily;
  saveThemeData();
}

// 设置粒子特效
function setParticles() {
  saveThemeData();
}

// 设置樱花特效
function setIsSakura() {
  saveThemeData();
}

// 开启随机壁纸
function setIsRandom() {
  saveThemeData();
}

// 设置首页字体颜色
function setFontColor() {
  const { mhFontColor } = themeStore.options;
  let header = document.querySelector('.common-header') as any;
  let homeTop = document.querySelector('.home-top') as any;
  let CycleUpDown = document.querySelector('.CycleUpDown') as any;
  if (header) {
    header.style.color = mhFontColor;
    let icons = header.querySelectorAll('.theme-icon');

    Object.keys(icons).forEach((e: any) => {
      icons[e].style.fill = mhFontColor;
    });
  }
  if (homeTop) homeTop.style.color = mhFontColor;
  if (CycleUpDown) {
    let themeIcon = CycleUpDown.querySelector('.theme-icon') as any;
    if (themeIcon) {
      themeIcon.style.fill = mhFontColor;
    }
  }
  saveThemeData();
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

// 选中背景
function changeBack(item: any) {
  backs.forEach((back: any) => {
    back.active = false;
  });
  activeBack.active = false;
  item.active = true;
  activeBack = JSON.parse(JSON.stringify(item));
  setBack();
}

// 选中弹窗风格
function setDialogType() {
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

/**
 * @description: 初始化主题背景数据
 * @return {*}
 */
function init() {
  // 获取缓存的主题数据
  const { theme, backUrl } = themeStore;
  themes.forEach((e: any) => {
    if (e.key == theme) {
      e.active = true;
      activeTheme.value = JSON.parse(JSON.stringify(e));
    } else {
      e.active = false;
    }
  });
  backs.forEach((e: any) => {
    if (e.url == backUrl || e.background == backUrl) {
      e.active = true;
      activeBack = JSON.parse(JSON.stringify(e));
    } else {
      e.active = false;
    }
  });
}

async function getBackList() {
  const { code, rows } = (await pageWallpapers({ pageSize: 9999 })) as any;
  if (code == 200) {
    Object.assign(backs, rows);
    init();
  }
}

onMounted(() => {
  getBackList();
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
      height: 100%;
      width: 100%;
    }

    .theme-main,
    .back-main {
      display: flex;
      flex-wrap: wrap;
      .theme-item,
      .back-item {
        cursor: pointer;
        width: calc(20% - 16px);
        display: flex;
        transition: all 0.2s linear;
        flex-direction: column;
        margin: 8px;
        border-radius: 5px;
        position: relative;
        background: get('modal');
        box-shadow: rgba(0, 0, 0, 0.225) 0 0 10px 0;
        .back-image,
        .theme-image,
        .theme-back {
          overflow: hidden;
          aspect-ratio: 11/7;
          width: calc(100% - 10px);
          margin: 5px 5px 0 5px;
          border-radius: 5px;
          object-fit: cover;
          img {
            transition: all 1s ease;
          }
        }
      }
      .back-item:hover,
      .theme-item:hover,
      .theme-item-active,
      .back-item-active {
        background: get('border-color');

        .theme-label,
        .back-label {
          color: white;
        }
        .back-image,
        .theme-image,
        .theme-back {
          img {
            transform: scale(1.2);
          }
        }
        &::before {
          content: '';
          position: absolute;
          border-radius: 9px;
          top: -5px;
          left: -5px;
          right: -5px;
          bottom: -5px;
          // border: 2px solid hsla(0, 0%, 100%, 0.4392156862745098);
          border: 3px solid get('border-color');
          transition: opacity 0.3s;
        }
      }
    }
    .theme-label,
    .back-label {
      height: 2rem;
      line-height: 2rem;
      font-size: 0.8rem;
      color: get('font-color');
      text-align: center;
    }
    .el-dialog__header {
      margin-bottom: 0 !important;
    }
    .other-main {
      width: calc(100% - 20px);
      height: 100%;
      padding: 0 10px;
      font-size: 0.8rem;
      .setting-item {
        .setting-item-label {
          height: 1.5rem;
          display: flex;
          align-items: center;
        }
        .el-select {
          width: 250px;
        }
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: relative;
        padding: 10px 20px;
        box-shadow: rgba(0, 0, 0, 0.225) 0 0 3px 0;
        border-radius: 8px;
        margin: 10px 0;
        color: get('font-color');
        background-color: rgba(255, 255, 255, 0.19);
      }
      .setting-item:hover {
        background: get('border-color');
        color: get('re-font-color');
        &::before {
          content: '';
          position: absolute;
          border-radius: 12px;
          top: -4px;
          left: -4px;
          right: -4px;
          bottom: -4px;
          border: 2px solid get('border-color');
          transition: opacity 0.3s;
        }
      }
    }
    .other-form {
      padding: 0 100px;
    }
  }
  .skin-change.filter {
    .theme-label,
    .back-label {
      color: white;
    }
    .back-item:hover,
    .theme-item:hover,
    .theme-item-active,
    .back-item-active {
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
