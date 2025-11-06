<!--
 * @Description: 相册
-->
<template>
  <div class="page-main equipment-main">
    <div class="equipment-header animated bounceInDown">
      <video
        :src="'/video/video_1800042575.mp4'"
        class="equipment-header-video"
        autoplay
        loop
        muted
      ></video>
      <span class="equipment-header-top">{{ $t('装备') }}</span>
      <span class="equipment-header-center">{{ $t('科技是第一生产力') }}</span>
      <span class="equipment-header-bottom">{{ $t('但是穷') }}</span>
    </div>

    <div class="equipment-center">
      <div class="equipment-item c-right" :class="getAnimate(i)" v-for="(item, i) in equipmentList">
        <c-image class="equipment-item-cover" :src="item.coverUrl" />
        <div class="equipment-item-info">
          <span class="equipment-item-name">{{ item.name }}</span>
          <span class="equipment-item-type">{{ item.model }}</span>
          <span class="equipment-item-instoction">{{ item.intro }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { pageEquips } from '@/api/equip.ts';
const router = useRouter();
const equipmentList = ref([]) as any;

function getAnimate(i: any) {
  if (i % 4 === 0) {
    return 'animated-0s5';
  }
  if (i % 4 === 1) {
    return 'animated';
  }
  if (i % 4 === 2) {
    return 'animated-1s5';
  }
  if (i % 4 === 3) {
    return 'animated-2s';
  }
  return 'animated';
}

async function getList() {
  const { code, rows } = (await pageEquips({})) as any;
  if (code === 200) {
    equipmentList.value = rows;
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss">
@include theme() {
  .equipment-main {
    @include flex-column;
    justify-content: start;
    background: transparent !important;
    backdrop-filter: none !important;
    .equipment-header {
      height: 40vh;
      width: 100%;
      border-radius: 12px;
      box-shadow: get('box-shadow');
      position: relative;
      .equipment-header-video {
        opacity: 0.95;
        height: 100%;
        width: 100%;
        border-radius: 12px;
        object-fit: cover;
      }
      margin-bottom: 1rem;
      color: white;
      .equipment-header-top {
        position: absolute;
        top: 10px;
        left: 15px;
      }
      .equipment-header-center {
        position: absolute;
        top: 47px;
        left: 14px;
        font-size: 35px;
        font-weight: bold;
      }
      .equipment-header-bottom {
        position: absolute;
        bottom: 14px;
        left: 16px;
      }
    }
    .equipment-center {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      @media screen and (max-width: 1200px) {
        .equipment-item-content-item {
          width: calc(50% - 12px);
        }
      }

      .equipment-item {
        width: calc(25% - 20px);
        border-radius: 10px;
        margin: 10px 0;
        box-shadow: get('box-shadow');
        background: get('back');
        position: relative;
        color: get('font-color');
        position: relative;
        overflow: hidden;
        @include flex-column;
        justify-content: start;
        .equipment-item-cover {
          width: 100%;
          aspect-ratio: 7/5;
          object-fit: cover;
          object-position: center;
          background-repeat: no-repeat;
          background: white;
          border-bottom: 1px solid get('border-color');
        }
        .equipment-item-info {
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          height: 120px;
          width: 100%;
          align-items: center;
          .equipment-item-name {
            font-weight: bold;
            font-size: 1rem;
            margin: 10px;
          }
          .equipment-item-type {
            margin-bottom: 0;
            opacity: 0.6;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            width: calc(100% - 30px);
          }
          .equipment-item-instoction {
            overflow: hidden;
            display: block;
            width: calc(100% - 20px);
            margin: 10px;
            display: -webkit-box;
            text-overflow: ellipsis;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;
          }
        }
      }
      .equipment-item:hover {
        transform: scale(1.03);
      }
    }
  }
}
</style>
