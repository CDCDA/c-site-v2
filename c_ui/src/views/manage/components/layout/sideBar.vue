<!--
 * @Description: 管理页侧边栏
-->
<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-menu"
      active-text-color="#ffd04b"
      background-color="#545c64"
      :default-active="defaultActive"
      text-color="#fff"
      :collapse="themeStore.isCollapse"
    >
      <div class="logo flex-center">后台管理</div>
      <el-menu-item v-for="(item, i) in menuList" @click="menuClick(item)" :index="item.path">
        <svg-icon
          class="menu-icon"
          :iconName="item.meta.svgIcon"
          style="font-size: 1.1rem; margin-right: 8px"
        />
        <template #title>
          <span>{{ item.meta.remark }}</span>
        </template>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import useThemeStore from '@/store/modules/theme.ts';
const router = useRouter();
const menuList = ref([] as any);
const themeStore = useThemeStore() as any;
const defaultActive = ref(0);
function menuClick(item: any) {
  router.push({ name: item.name });
}

watch(
  () => router,
  (val: any) => {
    const existRouter = menuList.value.filter((x: any) => x.name === val.currentRoute.value.name);
    if (existRouter.length !== 0) {
      defaultActive.value = existRouter[0].path;
    }
  },
  {
    deep: true
  }
);

onMounted(() => {
  menuList.value = (router.options.routes as any).find((x: any) => x.name == 'manage').children;
  menuList.value = menuList.value.filter(
    (x: any) => x.name !== 'blogManageEditor' && x.name !== 'essayManageEditor'
  );
  defaultActive.value = window.localStorage.getItem('lastRouter') || menuList.value[0].path;
});
</script>
<style lang="scss" scoped>
@include theme() {
}
.sidebar {
  //min-width: 220px;
}
.sidebar-menu {
  height: 100vh;
  //min-width: 180px;
}
.sidebar-menu.el-menu--collapse {
  min-width: initial;
}
.logo {
  height: 2.8rem;
  color: white;
}
</style>
