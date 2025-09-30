<!--
 * @Description: 用户管理
-->
<template>
  <div class="page-main user-main">
    <div class="user-left bounceInRight animated">
      <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        background="rgba(29, 32, 33, 0.6)"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <el-menu-item v-for="(item, i) in data" :key="i" @click="clickMenu(item)">
          <span class="first-menu-name" slot="title" :index="item.id">{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="user-right bounceInLeft animated">
      <KeepAlive> <router-view /></KeepAlive>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const data = ref([
  {
    id: '1',
    path: 'personalPage',
    label: '个人资料'
  },
  {
    id: '2',
    path: 'blogManage',
    label: '博客管理'
  },
  {
    id: '3',
    path: 'typeManage',
    label: '分类管理'
  }
  // {
  //   id: '4',
  //   path: 'myCollection',
  //   label: '我的收藏'
  // },
  // {
  //   id: '100',
  //   label: '测试'
  // }
]);

function clickMenu(item: any) {
  router.push({ name: item.path });
}
</script>
<style lang="scss">
@include theme() {
  .user-main {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    padding-top: 15px;
    .user-left {
      width: auto;
      min-width: 225px;
      margin-right: 15px;
    }
    .user-right {
      width: auto;
      min-width: 800px;
    }
    .user-left,
    .user-right {
      height: calc(100% - 15px);
      background: get('back');
      border-radius: 10px;
      box-shadow: get('box-shadow');
      color: get('font-color');
    }
    .el-menu {
      margin: 0;
    }
  }
}
</style>
