<!--
 * @Description: 进入可视区域加载
-->
<template>
  <div class="visible-lazy-container" :id="id">
    <div class="visible-lazy fade-in-normal" style="width: 100%" v-if="isVisible">
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const isVisible = ref(false);
import { autoClearTimer } from '@/utils/timer.ts';

const id = ref(`visible-lazy-${generateRandomId()}`) as any;
function generateRandomId() {
  return Math.random().toString(16).substr(2, 16);
}
const io = new IntersectionObserver(entries => {
  if (entries[0].isIntersecting) {
    console.log('元素进入视野');
    io.disconnect();
    autoClearTimer(() => {
      isVisible.value = true;
    }, 500);
  }
});
io.observe((document as any).getElementById(id.value));
</script>
<style lang="scss" scoped></style>
