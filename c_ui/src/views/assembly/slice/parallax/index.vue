<!--
 * @Description: 视差
-->
<template>
  <div id="container" class="container">
    <ul id="scene" class="scene">
      <li class="layer" data-depth="1.00">
        <div class="scene-item depth-100" />
      </li>
      <li class="layer" data-depth="0.80">
        <div class="scene-item depth-80" />
      </li>
      <li class="layer" data-depth="0.60">
        <div class="scene-item depth-60" />
      </li>
      <li class="layer" data-depth="0.40">
        <div class="scene-item depth-40" />
      </li>
      <li class="layer" data-depth="0.20">
        <div class="scene-item depth-20" />
      </li>
      <li class="layer" data-depth="0.00">
        <div class="scene-item depth-0" />
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
import { onMounted } from 'vue';
import { loadScript } from '@/utils/common.ts';
import Parallax from 'parallax-js';
function init() {
  setTimeout(() => {
    let scene = document.querySelector('#scene') as any;
    let parallax = new Parallax(scene);
  }, 2000);
}
onMounted(() => {
  init();
});
</script>

<style lang="scss" scoped>
@keyframes wave {
  0% {
    -webkit-transform: rotateZ(0deg) translate3d(0, 10%, 0) rotateZ(0deg);
    -moz-transform: rotateZ(0deg) translate3d(0, 10%, 0) rotateZ(0deg);
    transform: rotateZ(0deg) translate3d(0, 10%, 0) rotateZ(0deg);
  }
  100% {
    -webkit-transform: rotateZ(360deg) translate3d(0, 10%, 0) rotateZ(-360deg);
    -moz-transform: rotateZ(360deg) translate3d(0, 10%, 0) rotateZ(-360deg);
    transform: rotateZ(360deg) translate3d(0, 10%, 0) rotateZ(-360deg);
  }
}
.container {
  height: 100%;
  width: 100%;
  background: #000;
  position: relative;
  display: table;
  #scene {
    width: 100%;
    height: 100%;
    padding: 0;
    position: relative;
    overflow: hidden;
    li {
      position: absolute;
      display: block;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      //transform: translate3d(-17.5px, -12.495px, 0px);
      transform-style: preserve-3d;
      backface-visibility: hidden;
    }
  }
  .scene-item {
    margin: 0 0;
    background-image: url('/public/parallax/images/wave.png');
    //background: transparent repeat-x;
    background-size: auto 101%;
    width: 300%;
    left: -100%;
    position: absolute;
  }
  .depth-100 {
    bottom: 144px;
    height: 130px;
    animation: wave 4.22222s 0.1s infinite linear;
  }
  .depth-80 {
    bottom: 120px;
    height: 150px;
    animation: wave 3.22222s 0.1s infinite linear;
  }
  .depth-60 {
    bottom: 72px;
    height: 210px;
    animation: wave 4.22222s 0.1s infinite linear;
  }
  .depth-40 {
    bottom: 66px;
    height: 180px;
    animation: wave 6.22222s 0.1s infinite linear;
  }
  .depth-20 {
    bottom: 44px;
    height: 165px;
    animation: wave 4.5s 0.1s infinite linear;
  }
  .depth-0 {
    bottom: 20px;
    height: 210px;
    animation: wave 5.62s 0.1s infinite linear;
  }
}
</style>
