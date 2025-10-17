<!--
 * @Description: 相册详情
-->
<template>
  <div class="album-page page-main">
    <div class="album-img-list fade-in" v-cLoading="loading">
      <div class="album-img-item" v-for="image in albumData.images">
        <c-image
          class="album-img"
          :preview-src-list="[image.url]"
          :preview-teleported="true"
          :src="image.url"
          fit="cover"
        />
        <span class="album-img-time">{{ image.createTime }}</span>
      </div>
      <template>
        <el-empty description="description" />
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAlbumById } from '@/api/album.ts';
import useUserStore from '@/store/modules/user';
import waterFall from '@/components/waterFall/index.vue';
const router = useRouter();
const userStore = useUserStore();
const loading = ref(false) as any;
const albumData = ref({}) as any;
const isShow = ref(false);
async function getAlbumData() {
  loading.value = true;
  const { code, data } = await getAlbumById(router.currentRoute.value.query.albumId);
  if (code === 200) {
    albumData.value = data;
    loading.value = false;
    isShow.value = true;
  }
}

onMounted(() => {
  getAlbumData();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .album-page {
    background: get('back');
    border-radius: 8px;
    padding: 30px;
    height: auto !important;
    width: calc(80% - 60px) !important;
  }
}
@keyframes toUp {
  0% {
    bottom: -40px;
  }
  100% {
    bottom: 0;
  }
}
@keyframes toDown {
  0% {
    bottom: 0;
  }
  100% {
    bottom: -40px;
  }
}
.album-img-list {
  @include flex;
  height: 600px;
  justify-content: start;
  flex-wrap: wrap;
  height: auto;
  // animation: fade-in 1s linear forwards;
  .album-img-item {
    width: calc(33.33% - 20px);
    aspect-ratio: 7/5;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    margin: 10px;
    cursor: pointer;
    .album-img {
      height: 100%;
      width: 100%;
      transition: all 0.25s linear;
    }
    .album-img-time {
      position: absolute;
      bottom: -30px;
      right: 0;
      width: calc(100%);
      line-height: 1.8;
      position: absolute;
      left: 0;
      font-size: 14px;
      background: rgba(0, 0, 0, 0.3);
      padding: 4px 8px;
      color: #fff;
      transition: all 0.3s ease-in-out;
    }
  }
  .album-img-item:hover {
    .album-img-time {
      animation: toUp linear forwards 0.3s;
    }
    .album-img {
      transform: scale(1.1);
    }
  }
  .album-img-item:not(:hover) {
    .album-img-time {
      animation: toDown ease-in-out forwards 0.3s;
    }
  }
}
</style>
