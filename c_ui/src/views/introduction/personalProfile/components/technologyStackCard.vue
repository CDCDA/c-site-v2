<!--
 * @Description: 技术栈卡片 
-->
<template>
  <div class="technology-stack-card">
    <cardFlip>
      <template #front>
        <div class="stack-item-list">
          <div class="stack-item-col" v-for="item in techStackData">
            <c-image class="stack-item" :src="item.upStack.url" />
            <c-image class="stack-item" :src="item.downStack.url" />
          </div>
        </div>
      </template>
      <template #back>
        <div class="stack-tag-list">
          <div class="stack-tag" v-for="item in techStackList">
            <c-image :src="item.url" /><span class="tag-text">{{ item.label }}</span>
          </div>
        </div>
      </template>
    </cardFlip>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { techStackData, techStackList } from '@/utils/techStackData.ts';
import cardFlip from '@/components/cardFlip/index.vue';
</script>

<style lang="scss" scoped>
.stack-item-col {
  margin: 0 5px;
  @include flex-column;
  .stack-item {
    width: 110px;
    height: 110px;
    padding: 5px;
    box-shadow: get('box-shadow');
    border-radius: 30px;
    margin: 10px 5px;
    background: white;
  }
  .stack-item:nth-child(2) {
    transform: translate(-80px);
  }
}
.stack-tag-list {
  @include flex;
  flex-wrap: wrap;
  justify-content: start;
  width: 100%;
  height: 100%;
  z-index: 1;
  padding: 0 20px;
  pointer-events: none;
  // background: get('bk');
  .stack-tag {
    border: 1px solid #d1d1d1;
    border-radius: 12px;
    padding: 5px 10px;
    pointer-events: none;
    margin: 5px;
    background: white;
    @include flex;
    .el-image {
      width: 25px;
      height: 25px;
      margin-right: 10px;
    }
    .tag-text {
    }
  }
}
.technology-stack-card {
  width: 100%;
  height: 300px;
  .stack-item-list {
    display: flex;
    animation: row-up 10s linear infinite !important;
  }
}
@keyframes row-up {
  0% {
    transform: translateX(0%);
  }

  100% {
    transform: translateX(-20%);
  }
}
</style>
