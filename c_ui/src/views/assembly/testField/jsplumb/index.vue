<!--
 * @Description: 连线绘图
 * @Author: cyd 1205489124@qq.com
 * @Date: 2023-06-26 16:56:13
 * @LastEditTime: 2023-07-24 09:25:59
-->
<template>
  <div class="jsplumb">
    <line1 />
    <line1 />
    <line1 />
    <line1 />
    <line1 />
    <!--    <line />-->
    <!--    <line />-->
    <!--    <line />-->
    <!--    <line />-->
    <!--    <line />-->
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import line1 from './components/line.vue';
</script>
<style lang="scss">
#wrapper {
  margin-bottom: 10px;
}
</style>
