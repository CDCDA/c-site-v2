<!--
 * @Description: 管理页侧边栏
-->
<template>
  <div class="footerMenu" :class="isShowMenu ? 'is-show' : ''">
    <div class="menu-back"></div>
    <div v-for="item in menuList" class="menu-item" @click="menuClick(item)">
      <svg-icon class="menu-icon" :iconName="`commonSvg-${item.meta.svgIcon}`"></svg-icon>
      <span class="menu-title">{{ item.meta.title.replace($t('管理'), '') }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const menuList = ref([]) as any;
const isShowMenu = ref(false) as any;
const activeMenu = ref({}) as any;

function menuClick(item: any) {
  router.push({ name: item.name });
  activeMenu.value = item;
}

onMounted(() => {
  menuList.value = (router.options.routes as any).find((x: any) => x.name == 'manage').children;
  if (menuList.value.length > 0) {
    activeMenu.value = menuList.value[0];
    router.push({ name: activeMenu.value.name });
  }
  console.log(menuList.value);
});
</script>
<style lang="scss" scoped>
@include theme() {
  .footerMenu {
    position: absolute;
    bottom: -35px;
    height: 150px;
    width: 100vw;
    overflow-x: auto;
    background: transparent;
    display: flex;
    justify-content: center;
    align-items: end;
    .menu-back {
      background: orange;
      position: absolute;
      bottom: 0;
      height: 60px;
      width: 100%;
      box-shadow: get('box-shadow');
    }
    .menu-item:hover,
    .menu-item:active {
      // transform: scale(1.3);
      transform: translateY(-35px);
    }
    .menu-item {
      width: 70px;
      height: 70px;
      z-index: 100;
      cursor: pointer;
      border-radius: 8px;
      transition: all 0.2s ease-in-out;
      background: white;
      box-shadow: get('box-shadow');
      margin: 10px 15px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .menu-icon {
        width: 35px;
        height: 35px;
        margin-bottom: 5px;
        margin-top: 5px;
      }
      .menu-title {
        height: 20px;
        text-align: center;
        width: 100%;
        font-size: 14px;
      }
    }
  }
}
</style>
