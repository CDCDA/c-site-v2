<!--
 * @Description: 侧边导航
-->
<template>
  <div class="sideNav">
    <div class="link" v-for="item in menuList">
      <div class="text">
        <svg-icon :iconName="`commonSvg-${item.meta.svgIcon}`"></svg-icon>
        <span>{{ item.meta.title }}</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const menuList = ref([]) as any;

onMounted(() => {
  menuList.value = (router.options.routes as any).find((x: any) => x.name == 'manage').children;
});
</script>

<style lang="scss" scoped>
@include theme() {
}
.sideNav {
  max-width: 200px;
  background: rgba(0, 0, 0, 0.75);
  margin: 40px auto;
  padding: 10px 0 20px 0;
  border: 1px solid #111;
  border-radius: 4px;
  box-shadow: 0 4px 5px rgba(0, 0, 0, 0.75);
}
.link {
  font-size: 16px;
  font-weight: 300;
  text-align: center;
  position: relative;
  height: 40px;
  line-height: 40px;
  margin-top: 10px;
  overflow: hidden;
  width: 90%;
  margin-left: 5%;
  cursor: pointer;
}
.link:after {
  content: '';
  position: absolute;
  width: 80%;
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  bottom: 50%;
  left: -100%;
  transition-delay: 0.5s;
  transition: all 0.5s;
}
.link:hover:after,
.link.hover:after {
  left: 100%;
}
.link .text {
  // text-shadow:0 -40px 0 rgba(255, 255, 255, 1);
  color: white;
  transition: all 0.75s;
  transform: translateY(100%) translateZ(0);
  transition-delay: 0.25s;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .svg-icon {
    width: 30px;
    height: 30px;
    margin-right: 5px;
    :deep(.theme-icon) {
      fill: white !important;
    }
  }
}
.link:hover .text,
.link.hover .text {
  // text-shadow:0 -40px 0 rgba(255, 255, 255, 1);
  transform: translateY(0%) translateZ(0) scale(1.1);
  font-weight: 600;
}
</style>
