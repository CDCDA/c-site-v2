<!--
 * @Description: 视差管理页
-->
<template>
  <div class="manage-container flex-start">
    <side-bar></side-bar>
    <div class="manage-wrap">
      <HeaderBar />
      <div class="router-main">
        <KeepAlive> <router-view /></KeepAlive>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted } from 'vue';
import SideBar from '@/views/manage/components/layout/sideBar.vue';
import HeaderBar from '@/views/manage/components/layout/headerBar.vue';
import useThemeStore from '@/store/modules/theme.ts';
import { useRouter } from 'vue-router';
const router = useRouter();
const themeStore = useThemeStore();
themeStore.isShow = false;
themeStore.isFooterShow = false;
onMounted(() => {});
</script>

<style lang="scss" scoped>
.manage-container {
  width: 100vw;
  height: 100vh;
  background: white;
  position: absolute;
  font-family: 'shark';
  .manage-wrap {
    height: 100%;
    width: inherit;
    background: #f0f3f4;
  }
  .router-main {
    width: 100%;
    height: calc(100% - 2.8rem);
    overflow: auto;
    .essay-manage-editor {
      width: calc(100% - 60px);
      box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
      min-height: calc(100% - 60px);
    }
    .blog-manage-editor {
      width: calc(100% - 60px);
      box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
    }
  }
}
</style>
