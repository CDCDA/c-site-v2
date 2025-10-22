<template>
  <div class="theme-main">
    <div
      v-for="item in themes"
      :class="['theme-item', activeTheme.key == item.key ? 'theme-item-active' : '']"
      @click="emit('changeTheme', item)"
    >
      <div class="theme-back" :style="{ background: item.background }"></div>
      <span class="theme-label">{{ item.label }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import useThemeStore from '@/store/modules/theme.ts';
import { themes } from '../themeData.ts';

const props = defineProps({
  activeTheme: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['changeTheme']);
var themeStore = useThemeStore();
</script>
<style lang="scss" scoped>
@include theme() {
  .theme-main {
    display: flex;
    flex-wrap: wrap;
    .theme-item {
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

      .theme-image,
      .theme-back {
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
    .theme-item:hover,
    .theme-item-active {
      background: get('border-color');

      // .theme-label {
      //   color: white;
      // }

      .theme-image,
      .theme-back {
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
  }
  .theme-label {
    height: 2rem;
    line-height: 2rem;
    font-size: 0.8rem;
    color: get('font-color');
    text-align: center;
  }
}
</style>
