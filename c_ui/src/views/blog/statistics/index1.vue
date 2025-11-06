<!--
 * @Description: 数据统计
-->
<template>
  <div class="page-main statistic-main">
    <div class="total">
      <div class="total-col">
        <el-statistic :title="$t('文章数')" :value="blogNumValue" />
      </div>
      <div class="total-col">
        <el-statistic :title="$t('相册数')" :value="albumNumValue" />
      </div>
      <div class="total-col">
        <el-statistic :title="$t('分类数')" :value="typeNumValue" />
      </div>
      <div class="total-col">
        <el-statistic :title="$t('随笔数')" :value="essayNumValue" />
      </div>
      <div class="total-col">
        <el-statistic :title="$t('网站更新数')" :value="updateLogNumValue" />
      </div>
    </div>
    <div class="chart">
      <h3>{{ $t('博客统计图') }}</h3>
      <blogCountChart style="width: 100%; height: 100%" />
    </div>
    <div class="chart">
      <h3>{{ $t('博客统计图') }}</h3>
      <blogTypeRadar style="width: 100%; height: 100%" />
    </div>
    <div class="chart" style="height: 300px">
      <h3>{{ $t('博客统计图') }}</h3>
      <blogTypeChart style="width: 100%; height: 100%" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import blogCountChart from './components/blogCountLine.vue';
import blogTypeRadar from './components/assemblyCountRadar.vue';
import blogTypeChart from './components/blogTypePie.vue';
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
const musicNum = ref(0) as any;
const musicNumValue = useTransition(musicNum);
const typeNum = ref(0) as any;
const typeNumValue = useTransition(typeNum);
const updateLogNum = ref(0) as any;
const updateLogNumValue = useTransition(updateLogNum);

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

async function musicCount() {
  const { code, data } = (await countMusic()) as any;
  if (code === 200) {
    musicNum.value = data;
  }
}

async function typeCount() {
  const { code, data } = (await countTypes()) as any;
  if (code === 200) {
    typeNum.value = data;
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
  musicCount();
  typeCount();
  updateLogCount();
}

onMounted(() => {
  init();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .statistic-main {
    background: get('back');
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    border-radius: 8px;
  }
  .total {
    border: 2px solid get('bk');
  }
}
.total {
  width: calc(50%);
  height: 150px;
  margin-top: 80px;
  border-radius: 8px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 0 5%;
  .total-col {
    :deep(.el-statistic__head) {
      font-weight: bold;
      font-size: 1rem;
      margin-bottom: 30px;
    }
    :deep(.el-statistic__content) {
      font-weight: bold;
      font-size: 0.9rem;
    }
  }
}
.chart {
  height: 200px;
  width: 70%;
  margin: 30px;
}
</style>
