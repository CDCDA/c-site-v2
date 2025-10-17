<!--
 * @Description: 数据统计
-->
<template>
  <div class="page-main statistic-main">
    <statisticBack class="statistic-back" />
    <div class="statistic-main-content">
      <div class="left-side">
        <div class="statistic-item">
          <div class="item-header">博客数量</div>
          <div class="item-body">
            <blogCountChart />
          </div>
        </div>
        <div class="statistic-item">
          <div class="item-header">博客分类</div>
          <div class="item-body">
            <blogTypePie />
          </div>
        </div>
        <div class="statistic-item">
          <div class="item-header">博客标签</div>
          <div class="item-body">
            <blogTagBar />
          </div>
        </div>
      </div>
      <div class="main-side">
        <div class="statistic-item" style="height: 124px">
          <clock />
        </div>
        <div class="statistic-item" style="height: calc(30%)">
          <div class="count-list">
            <div class="count-item">
              <el-statistic title="博客数" :value="blogNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="随笔数" :value="essayNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="相册数" :value="albumNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="分类数" :value="typeNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="标签数" :value="tagNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="切片数" :value="sliceNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="大模块数" :value="moduleNumValue" />
            </div>
            <div class="count-item">
              <el-statistic title="网站更新数" :value="updateLogNumValue" />
            </div>
          </div>
        </div>
        <div class="statistic-item" style="height: calc(70% - 134px)">
          <div class="item-header">网站更新日志</div>
          <div class="item-body">
            <updateCountLineBar />
          </div>
        </div>
      </div>
      <div class="right-side">
        <div class="statistic-item">
          <div class="item-header">随笔数量</div>
          <div class="item-body">
            <essayCountLine />
          </div>
        </div>
        <div class="statistic-item">
          <div class="item-header">组件统计</div>
          <div class="item-body">
            <assemblyCountRadar />
          </div>
        </div>
        <div class="statistic-item">
          <div class="item-header">游戏影视统计</div>
          <div class="item-body">
            <gameDramaPie />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import statisticBack from './components/statisticBack.vue';
import blogCountChart from './components/blogCountLine.vue';
import updateCountLineBar from './components/updateCountLineBar.vue';
import essayCountLine from './components/essayCountLine.vue';
import assemblyCountRadar from './components/assemblyCountRadar.vue';
import blogTagBar from './components/blogTagBar.vue';
import blogTypePie from './components/blogTypePie.vue';
import gameDramaPie from './components/gameDramaPie.vue';
import clock from './components/clock.vue';
import { countTags } from '@/api/tag.ts';
import { countBlogs } from '@/api/blog.ts';
import { countAlbum } from '@/api/album.ts';
import { countEssays } from '@/api/essay.ts';
import { countMusic } from '@/api/music.ts';
import { countTypes } from '@/api/type.ts';
import { countLogs } from '@/api/system/updateLog';
import { useTransition } from '@vueuse/core';
const blogNum = ref(0) as any;
const blogNumValue = useTransition(blogNum);
const albumNum = ref(0) as any;
const albumNumValue = useTransition(albumNum);
const essayNum = ref(0) as any;
const essayNumValue = useTransition(essayNum);
// const musicNum = ref(0) as any;
// const musicNumValue = useTransition(musicNum);
const typeNum = ref(0) as any;
const typeNumValue = useTransition(typeNum);
const updateLogNum = ref(0) as any;
const updateLogNumValue = useTransition(updateLogNum);
const tagNum = ref(0) as any;
const tagNumValue = useTransition(tagNum);
const sliceNum = ref(0) as any;
const sliceNumValue = useTransition(sliceNum);
const moduleNum = ref(0) as any;
const moduleNumValue = useTransition(moduleNum);
const router = useRouter();

async function blogCount() {
  const { code, data } = (await countBlogs({})) as any;
  if (code === 200) {
    blogNum.value = data;
  }
}

async function albumCount() {
  const { code, data } = (await countAlbum()) as any;
  if (code === 200) {
    albumNum.value = data;
  }
}

async function essayCount() {
  const { code, data } = (await countEssays()) as any;
  if (code === 200) {
    essayNum.value = data;
  }
}

function sliceCount() {
  sliceNum.value = (router.options.routes as any).find(
    (x: any) => x.name == 'slice'
  ).children.length;
}

function moduleCount() {
  moduleNum.value = (router.options.routes as any).find(
    (x: any) => x.name == 'testField'
  ).children.length;
}

// async function musicCount() {
//   const { code, data } = (await countMusic()) as any;
//   if (code === 200) {
//     musicNum.value = data;
//   }
// }

async function typeCount() {
  const { code, data } = (await countTypes()) as any;
  if (code === 200) {
    typeNum.value = data;
  }
}

async function tagCount() {
  const { code, data } = (await countTags()) as any;
  if (code === 200) {
    tagNum.value = data;
  }
}

async function updateLogCount() {
  const { code, data } = (await countLogs()) as any;
  if (code === 200) {
    updateLogNum.value = data;
  }
}

function init() {
  blogCount();
  albumCount();
  essayCount();
  tagCount();
  typeCount();
  sliceCount();
  moduleCount();
  updateLogCount();
}

onMounted(() => {
  init();
});
</script>
<style lang="scss" scoped>
.statistic-main.page-main {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  border-radius: 0 !important;
  position: fixed !important;
  width: 100vw !important;
  height: 100vh !important;
  margin: 0 !important;
  max-width: none !important;
  .statistic-main-content {
    width: calc(100% - 40px);
    height: calc(100% - 40px);
    padding: 70px 20px 20px 20px;
    z-index: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    .left-side,
    .right-side,
    .main-side {
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: start;
    }
    .left-side {
      width: calc(30% - 20px);
      height: 100%;
      margin-right: 20px;
    }
    .main-side {
      width: 40%;
      height: 100%;
    }
    .right-side {
      width: calc(30% - 20px);
      height: 100%;
      margin-left: 20px;
    }
    .statistic-item {
      width: calc(100% - 20px);
      height: calc(33.333% - 33.33px);
      padding: 10px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      .item-header {
        width: 100%;
        height: 2rem;
        font-size: 1rem;
        color: white;
      }
      .item-body {
        width: 100%;
        height: calc(100% - 2rem);
      }
    }
    .count-list {
      display: flex;
      flex-wrap: wrap;
      justify-content: start;
      align-items: center;
      width: 100%;
      height: 100%;
    }
    .count-item {
      color: white;
      width: calc(25% - 40px);
      margin: 10px 20px;
      :deep(.el-statistic__head) {
        color: white;
        font-size: 0.8rem;
        margin-bottom: 20px;
      }
      :deep(.el-statistic__number) {
        color: white;
        font-size: 1.2rem;
        font-weight: bold;
      }
    }
  }
}
@property --rotate {
  syntax: '<angle>';
  initial-value: 132deg;
  inherits: false;
}

.statistic-item {
  background: rgb(25 28 41 / 70%);
  margin: 10px;
  padding: 3px;
  position: relative;
  border-radius: 6px;
  justify-content: center;
  align-items: center;
  display: flex;
  font-size: 1.5em;
  color: rgb(88 199 250 / 0%);
  cursor: pointer;
}

.statistic-item:hover {
  color: rgb(88 199 250 / 100%);
  transition: color 1s;
  background: rgb(25 28 41 /100%);
}
.statistic-item:hover:before,
.statistic-item:hover:after {
  display: block;
}

.statistic-item::before {
  content: '';
  display: none;
  width: calc(100% + 10px);
  height: calc(100% + 10px);
  border-radius: 8px;
  background-image: linear-gradient(var(--rotate), #5ddcff, #3c67e3 43%, #4e00c2);
  position: absolute;
  z-index: -1;
  top: -5px;
  left: -5px;
  animation: spin 2.5s linear infinite;
}

@keyframes spin {
  0% {
    --rotate: 0deg;
  }
  100% {
    --rotate: 360deg;
  }
}
</style>
