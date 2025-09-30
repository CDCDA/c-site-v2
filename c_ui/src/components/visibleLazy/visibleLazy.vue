<!--
 * @Description: 进入可视区域加载
-->
<template>
  <div class="lazy-wrap" :id="id" :style="{ height: height, display: display }"></div>
  <SyncComponent v-if="isVisible" class="lazy-main" :calss="animateName" />
</template>
<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
const props = defineProps({
  component: null as any,
  delay: {
    default: 500
  },
  height: {
    default: 300
  }
});
var height = '300px';
var SyncComponent = null as any;
const display = ref('block');
const animateName = ref('');
import { autoClearTimer } from '@/utils/timer';
const isVisible = ref(false); //定义一个dom元素显示与隐藏开关
const id = ref(`visible-lazy-${generateRandomId()}` as any);
function generateRandomId() {
  return Math.random().toString(16).substr(2, 16);
}
onMounted(() => {
  height = props.height ? props.height + 'px' : height;
  let el = (document as any).getElementById(id.value);
  //监听组件是否进入可视区域
  const io = new IntersectionObserver(entries => {
    if (entries[0].isIntersecting) {
      //解除监听
      io.disconnect();
      SyncComponent = props.component;
      //延时加载
      autoClearTimer(() => {
        display.value = 'none';
        isVisible.value = true;
        animateName.value = 'fade-in-normal';
      }, props.delay);
    }
  });
  //设置监听
  io.observe(el);
});
</script>
<style lang="scss" scoped>
// .lazy-main,
// .lazy-main > div {
//   transition: all 2.5s linear;
// }
</style>
