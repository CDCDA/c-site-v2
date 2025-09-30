<template>
  <div class="profile-main page-main bounceInLeft animated">
    <el-carousel trigger="click" type="card" class="profile-carousel" :autoplay="false">
      <el-carousel-item v-for="item in 6" :key="item" class="profile-carousel-item">
        <h3 text="2xl" justify="center">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
</script>
<style lang="scss">
.profile-main {
  height: 100%;
}
.profile-carousel {
  margin-top: 10px;
  width: 100%;
  height: calc(100% - 10px);
  .el-carousel__container {
    height: 100%;
  }
  .profile-carousel-item {
    height: calc(100%);
    background: left / cover fixed no-repeat url('http://120.48.127.181/file/background.jpg');
    box-shadow: get('box-shadow');
    // backdrop-filter: blur(20px);
    opacity: 0.9;
    width: 80%;
    left: -15%;
  }
}
</style>
