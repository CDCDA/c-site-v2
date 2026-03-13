<template>
  <el-dropdown trigger="click" @command="changeLanguage" style="height: 1.3rem;color: inherit !important">
    <i class="svg-icon-wrap">
      <svg-icon iconName="commonSvg-语言切换" class="header-icon search" @click="searchClick" />
    </i>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item v-for="item in languageList" :key="item.value" :command="item.value"
          :disabled="language === item.value">
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
