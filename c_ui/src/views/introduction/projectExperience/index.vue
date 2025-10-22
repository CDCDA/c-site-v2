<!--
 * @Description: 项目经历
-->
<template>
  <div class="project-main">
    <projectSwiper
      :item-list="projectList"
      :thumbs-swiper="thumbsSwiper"
      @setSwiper="setSwiper"
    ></projectSwiper>
    <!--    <bottomSwiper-->
    <!--      ref="bottomSwiperRef"-->
    <!--      :item-list="projectList"-->
    <!--      :thumbs-swiper="thumbsSwiper"-->
    <!--      @itemClick="itemClick"-->
    <!--    ></bottomSwiper>-->
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import projectList from './projectData.ts';
import { useRouter } from 'vue-router';
import bottomSwiper from './components/insideSwiper.vue';
import projectSwiper from './components/projectSwiper.vue';
import useThemeStore from '@/store/modules/theme.ts';
const themeStore = useThemeStore();
const bottomSwiperRef = ref(null) as any;
themeStore.isFooterShow = false;
const thumbsSwiper = ref(null) as any;
const visible = ref(false);
function open() {
  visible.value = true;
}
function close() {
  visible.value = false;
}
function itemClick(swiper: any) {
  thumbsSwiper.value = swiper;
}
function setSwiper(swiper: any) {
  bottomSwiperRef.value.setSwiper(swiper);
}
onMounted(() => {});
defineExpose({
  open,
  close
});
</script>
<style lang="scss">
.project-main {
  height: calc(100% - 5.5rem);
  width: 100%;
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 3rem 0 0 0;
}
</style>
