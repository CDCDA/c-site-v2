<template>
  <div class="wallpaper-main" v-cLoading="loading">
    <div
      v-for="item in wallPaperList"
      :class="['wallpaper-item', item.active ? 'wallpaper-item-active' : '']"
      @click="changeWallpaper(item)"
    >
      <c-image class="wallpaper-image" :src="item.url" />
      <span class="wallpaper-label">{{ item.name }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { pageWallpapers } from '@/api/system/wallpaper.ts';
const wallPaperList = ref([]) as any;
const loading = ref('rotate') as any;
const props = defineProps({
  activeUrl: {
    type: String,
    default: ''
  }
});
const emit = defineEmits(['changeWallpaper']);

watch(
  () => props.activeUrl,
  newVal => {
    if (!newVal) return;
    wallPaperList.value.forEach((item: any) => {
      item.active = item.url === newVal;
    });
  }
);

async function getStaticWallpapers() {
  loading.value = true;
  const { code, rows } = await pageWallpapers({
    pageSize: 9999,
    type: 'img'
  });
  if (code === 200) {
    wallPaperList.value = rows;
    if (!props.activeUrl) return;
    wallPaperList.value.forEach((item: any) => {
      item.active = item.url === props.activeUrl;
    });
  }
  loading.value = false;
}

function changeWallpaper(item: any) {
  emit('changeWallpaper', item);
}

onMounted(async () => {
  getStaticWallpapers();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .wallpaper-main {
    display: flex;
    flex-wrap: wrap;
    .wallpaper-item {
      cursor: pointer;
      width: calc(20% - 16px);
      display: flex;
      transition: all 0.2s linear;
      flex-direction: column;
      margin: 8px;
      border-radius: 5px;
      position: relative;
      background: get('modal');
      box-shadow: rgba(0, 0, 0, 0.225) 0 0 10px 0;
      .wallpaper-image {
        overflow: hidden;
        aspect-ratio: 11/7;
        width: calc(100% - 10px);
        margin: 5px 5px 0 5px;
        border-radius: 5px;
        object-fit: cover;
        img {
          transition: all 1s ease;
        }
      }
    }
    .wallpaper-item:hover,
    .wallpaper-item-active {
      background: get('border-color');
      .wallpaper-image {
        img {
          transform: scale(1.2);
        }
      }
      &::before {
        content: '';
        position: absolute;
        border-radius: 9px;
        top: -5px;
        left: -5px;
        right: -5px;
        bottom: -5px;
        // border: 2px solid hsla(0, 0%, 100%, 0.4392156862745098);
        border: 3px solid get('border-color');
        transition: opacity 0.3s;
      }
    }

    .wallpaper-label {
      height: 2rem;
      line-height: 2rem;
      font-size: 0.8rem;
      color: get('font-color');
      text-align: center;
    }
  }
}
</style>
