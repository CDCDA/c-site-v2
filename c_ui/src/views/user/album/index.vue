<!--
 * @Description: 相册
-->
<template>
  <div class="page-main album-main">
    <div class="album-header animated bounceInDown">
      <video :src="'/video/video_1783824280.mp4'" class="album-header-video" autoplay loop muted />
      <div class="album-header-top">相册集</div>
      <div class="album-header-center">唯有记忆，才是最完美的影像。</div>
      <div class="album-header-bottom">历历在目</div>
    </div>
    <div class="album-center">
      <div class="album-item" @click="routeTo(item)" v-for="(item, i) in list" :key="i">
        <el-image class="album-item-cover" :src="item.coverUrl" fit="cover">
          <template #placeholder>
            <div class="image-slot" v-cLoading="'rotate'" style="width: 100%; height: 100%" />
          </template>
        </el-image>
        <div class="album-item-name">{{ item.name }}</div>
        <div class="album-item-count">{{ item.images.length }}</div>
        <div class="album-item-date">{{ formatDate(new Date(item.createTime), 'YY-mm-dd') }}</div>
        <div class="album-item-instoction">{{ item.intro }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="album">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { pageAlbums } from '@/api/album.ts';
import { formatDate } from '@/utils/date';

const router = useRouter();
const list = ref([]) as any;

async function getList() {
  const { code, rows } = await pageAlbums({});
  if (code === 200) {
    list.value = rows;
  }
}

function routeTo(item: any) {
  router.push({ name: 'albumPage', query: { albumId: item.id } });
}

onMounted(() => {
  getList();
});
</script>

<style lang="scss">
@include theme() {
  .album-header {
    height: 40vh;
    width: calc(100%);
    border-radius: 12px;
    box-shadow: get('box-shadow');
    margin-bottom: 1rem;
    color: white;

    .album-header-video {
      opacity: 0.95;
      height: 100%;
      width: 100%;
      border-radius: 12px;
      object-fit: cover;
    }

    .album-header-top {
      position: absolute;
      top: 10px;
      left: 15px;
    }

    .album-header-center {
      position: absolute;
      top: 47px;
      left: 14px;
      font-size: 35px;
      font-weight: bold;
    }

    .album-header-bottom {
      position: absolute;
      bottom: 14px;
      left: 16px;
    }
  }

  .album-main {
    @include flex-column;
    justify-content: start;
    background: transparent !important;
    backdrop-filter: none !important;

    .album-center {
      display: flex;
      position: relative;
      flex-wrap: wrap;
      justify-content: start;
      align-items: center;
      width: calc(100% + 20px);

      .album-item {
        width: calc(25% - 20px);
        aspect-ratio: 5/7;
        border-radius: 12px;
        margin: 10px;
        transition: all 0.6s ease;
        opacity: 0.9;
        cursor: pointer;
        box-shadow: get('box-shadow');
        position: relative;
        color: white;

        .album-item-cover {
          width: 100%;
          height: 100%;
          border-radius: 12px;
          object-fit: cover;
          object-position: 70%;
          background-repeat: no-repeat;
        }

        .album-item-name {
          position: absolute;
          left: 24px;
          top: 20px;
          font-size: 30px;
          font-weight: bold;
        }

        .album-item-count {
          position: absolute;
          bottom: 20px;
          left: 25px;
          font-size: 30px;
        }

        .album-item-date {
          position: absolute;
          bottom: 27px;
          right: 25px;
          opacity: 0.8;
        }

        .album-item-instoction {
          position: absolute;
          left: 25px;
          top: 77px;
          font-size: 1rem;
          font-weight: bold;
          opacity: 0.9;
        }

        &:hover {
          filter: blur(0px);
          transform: scale(1.03);
        }
      }
    }
  }
}
</style>
