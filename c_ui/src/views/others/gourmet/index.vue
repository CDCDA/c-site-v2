<!--
 * @Description:  
-->
<!--
 * @Description: 美食
-->
<template>
  <div class="page-main gourmet-main">
    <div class="gourmet-top animated bounceInDown"><BubbleCollision /></div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BubbleCollision from './components/bubbleCollision.vue';
import RotatingBorder from '@/components/rotatingBorder.vue';
onMounted(() => {});
</script>

<style lang="scss" scoped>
@include theme() {
  .gourmet-main {
    background: transparent !important;
    backdrop-filter: none !important;
  }
  .gourmet-top {
    width: 100%;
    height: 40vh;
    border-radius: 8px;
    overflow: hidden;
  }
}
</style>
<style></style>
