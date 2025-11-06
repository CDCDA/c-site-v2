<template>
  <div class="other-main">
    <div class="setting-line">
      <div class="setting-item">
        <div class="setting-item-label">{{ $t('粒子效果(仅静态壁纸)') }}</div>
        <el-switch v-model="themeStore.options.isParticles" @change="setParticles()" />
      </div>
      <div class="setting-item">
        <div class="setting-item-label">{{ $t('樱花特效') }}</div>
        <el-switch v-model="themeStore.options.isSakura" @change="setIsSakura()" />
      </div>
    </div>
    <div class="setting-line">
      <div class="setting-item">
        <div class="setting-item-label">
          <el-tooltip :content="$t('每次切换路由页面壁纸跟着切换')" placement="top">
            <el-icon><QuestionFilled /></el-icon>
          </el-tooltip>
          {{ $t('随机壁纸') }}
        </div>
        <el-switch v-model="themeStore.options.isRandom" @change="setIsRandom()" />
      </div>
      <div class="setting-item">
        <div class="setting-item-label">
          <el-tooltip :content="$t('包含首页顶栏和首页打字机字体颜色')" placement="top">
            <el-icon><QuestionFilled /></el-icon>
          </el-tooltip>
          {{ $t('首页字体颜色') }}
        </div>
        <el-color-picker v-model="themeStore.options.mhFontColor" @change="setFontColor()" />
      </div>
    </div>
    <div class="setting-line">
      <div class="setting-item">
        <div class="setting-item-label">
          <el-tooltip
            :content="
              $t(
                '每个弹窗都有两种风格--常规颜色背景和透明磨砂背景，弹窗右上角的旋转按钮可以当次切换'
              )
            "
            placement="top"
          >
            <el-icon><QuestionFilled /></el-icon>
          </el-tooltip>
          {{ $t('弹窗风格') }}
        </div>
        <el-radio-group v-model="themeStore.options.dialogType" @change="setDialogType()">
          <el-radio value="normal">{{ $t('常规') }}</el-radio>
          <el-radio value="filter">{{ $t('磨砂') }}</el-radio>
        </el-radio-group>
      </div>
      <div class="setting-item">
        <div class="setting-item-label">{{ $t('字体') }}</div>
        <el-select
          v-model="themeStore.options.fontFamily"
          @change="setFontFamily()"
          append-to=".select-base"
        >
          <el-option v-for="item in fontFamilys" :value="item.value" :label="item.label" />
        </el-select>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref } from 'vue';
import { QuestionFilled } from '@element-plus/icons-vue';

import { themes, fontFamilys } from '../themeData.ts';
import useThemeStore from '@/store/modules/theme.ts';
var themeStore = useThemeStore();

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

// 选中弹窗风格
function setDialogType() {
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
</script>
<style scoped lang="scss">
@include theme() {
  .other-main {
    width: calc(100% - 0px);
    height: 100%;
    padding: 0;
    font-size: 0.8rem;
    .setting-line {
      display: flex;
      width: 100%;
      justify-content: space-between;
    }
    .setting-item {
      .setting-item-label {
        height: 1.5rem;
        display: flex;
        align-items: center;
      }
      .el-icon {
        margin-right: 5px;
      }
      .el-select {
        width: 250px;
      }
      display: flex;
      flex: 1;
      justify-content: space-between;
      align-items: center;
      position: relative;
      padding: 10px 20px;
      box-shadow: rgba(0, 0, 0, 0.225) 0 0 3px 0;
      border-radius: 8px;
      margin: 10px 5px;
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
}
</style>
