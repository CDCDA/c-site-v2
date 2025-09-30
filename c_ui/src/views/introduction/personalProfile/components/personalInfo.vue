<!--
 * @Description: 角色卡 
-->
<template>
  <div class="personal-info">
    <span class="glossary">java练习生</span>
    <span class="glossary">学啊学啊学</span>
    <span class="glossary">全栈开发工程师</span>
    <span class="glossary">业余健身爱好者</span>
    <span class="glossary">没游戏玩了</span>
    <span class="glossary">日常找新歌</span>
    <el-avatar class="avatar" :src="'http://120.48.127.181/file/cat-star.jpg'"></el-avatar>
  </div>
</template>
<script setup lang="ts"></script>
<style lang="scss" scoped>
@include theme() {
  @keyframes up-down {
    0% {
      transform: translateY(0);
    }
    25% {
      transform: translateY(-5px);
    }
    50% {
      transform: translateY(0);
    }
    75% {
      transform: translateY(5px);
    }
    100% {
      transform: translateY(0);
    }
  }
  .personal-info {
    // background: get('bk');
    width: 400px;
    position: relative;
    .glossary {
      padding: 3px 15px;
      border-radius: 12px;
      // box-shadow: get('box-shadow');
      border: 1px solid #d1d1d1;
      background: white;
      position: absolute;
      font-weight: bold;
    }
    .glossary:nth-child(1) {
      left: -23px;
      top: 15px;
      animation: up-down 2.6s infinite linear;
    }
    .glossary:nth-child(2) {
      left: -27px;
      top: 150px;
      animation: up-down 3.1s infinite linear;
    }
    .glossary:nth-child(3) {
      left: -67px;
      top: 89px;
      animation: up-down 2.8s infinite linear;
    }
    .glossary:nth-child(4) {
      right: -67px;
      top: 86px;
      animation: up-down 2.9s infinite linear;
    }
    .glossary:nth-child(5) {
      right: -20px;
      top: 11px;
      animation: up-down 3s infinite linear;
    }
    .glossary:nth-child(6) {
      right: -23px;
      top: 146px;
      animation: up-down 2.8s infinite linear;
    }
  }

  .avatar {
    width: 200px;
    height: 200px;
    box-shadow: get('box-shadow');
  }
}
</style>
