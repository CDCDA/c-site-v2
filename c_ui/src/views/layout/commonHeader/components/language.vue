<template>
  <el-dropdown trigger="click" @command="changeLanguage" style="color: inherit !important">
    <i class="svg-icon-wrap" style="margin: 0px 1px">
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
// const { t:$t } = useI18n();
import { computed } from 'vue';
import useThemeStore from '@/store/modules/theme.ts';

const i18n = useI18n();
const themeStore = useThemeStore();
const language = computed(() => themeStore.language);

const languageList = [
  { label: '简体中文', value: 'zh_cn' },
  { label: '繁体中文', value: 'zh_hant' },
  { label: 'English', value: 'en_us' }
];

const changeLanguage = (lang: string) => {
  i18n.locale.value = lang;
  themeStore.language = lang;
  window.localStorage.setItem('language', lang);
};
</script>
