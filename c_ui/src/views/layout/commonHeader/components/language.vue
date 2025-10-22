<template>
  <el-dropdown trigger="click" @command="changeLanguage">
    <i class="svg-icon-wrap">
      <svg-icon
        iconName="commonSvg-语言切换"
        style="font-size: 1.2rem; margin: 0 6px; cursor: pointer"
        class="header-icon bell"
      />
    </i>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="item in languageList"
          :key="item.value"
          :command="item.value"
          :disabled="language === item.value"
        >
          {{ item.label }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { computed } from 'vue';
import useThemeStore from '@/store/modules/theme.ts';

const i18n = useI18n();
const themeStore = useThemeStore();
const language = computed(() => themeStore.language);

const languageList = [
  { label: '简体中文', value: 'zh' },
  { label: 'English', value: 'en' }
];

const changeLanguage = (lang: string) => {
  i18n.locale.value = lang;
  themeStore.language = lang;
};
</script>
<style lang="scss" scoped>
@include theme() {
  .svg-icon-wrap::before {
    width: 1.5rem;
    height: 1.5rem;
    content: '';
    left: 3px;
    top: -3px;
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
    color: get('re-font-color');
  }
  .svg-icon {
  }
}
</style>
