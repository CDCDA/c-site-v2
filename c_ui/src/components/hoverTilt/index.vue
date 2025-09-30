<!--
 * @Description: 悬停倾斜组件 
-->
<template>
  <div class="hover-tilt" :id="id">
    <slot name="default"></slot>
  </div>
</template>
<script setup lang="ts">
import { onMounted } from 'vue';

function range(value: any, percent: any) {
  if (percent >= 0.5) {
    const p = (percent - 0.5) / 0.5;

    return -1 * value * p;
  }
  const p = (0.5 - percent) / 0.5;
  return value * p;
}

const id = Math.random().toString(36).slice(-8);

onMounted(() => {
  var el = document.getElementById(id) as any;
  var onMouseUpdate = function onMouseUpdate(e: any) {
    const rect = el.getBoundingClientRect();
    const x = e.clientX - rect.left,
      y = e.clientY - rect.top;
    const xPercent = x / el.clientWidth,
      yPercent = y / el.clientHeight;
    const xDeg = range(-10, yPercent),
      yDeg = range(10, xPercent);
    el.style.transform = `perspective(1200px) rotateX(${xDeg}deg) rotateY(${yDeg}deg)`;
  };
  el.addEventListener('mousemove', onMouseUpdate, false);
  el.addEventListener('mouseenter', onMouseUpdate, false);
  el.addEventListener('mouseleave', function () {
    el.style.transform = `perspective(1200px) rotateX(0deg) rotateY(0deg)`;
  });
});
</script>
<style lang="scss">
.hover-tilt {
  transform: perspective(525px) translateZ(0) rotateX(var(--dx)) rotateY(var(--dy));
  transition: all 150ms linear 0s;
  // transition: 0.8s cubic-bezier(0.445, 0.05, 0.55, 0.95);
}
</style>
