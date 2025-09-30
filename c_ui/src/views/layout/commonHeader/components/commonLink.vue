<!--
 * @Description: 标题外链
-->
<template>
  <div class="common-link">
    <!-- 标题 -->
    <el-icon class="header-name-icon"><Menu /></el-icon>
    <!-- 下拉菜单内容 -->
    <div class="link-content">
      <div class="link-title">常用网站</div>
      <div class="link-list">
        <div class="link-item" @click="clickLink(item)" v-for="item in linkList">
          <el-image class="link-icon" :src="item.iconUrl"></el-image>
          <span class="link-text"> {{ item.label }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';

const linkList = ref([
  {
    label: '渐变色',
    url: 'https://uigradients.com/#Timber',
    iconUrl: new URL('@/assets/svg/linkSvg/gredient.svg', import.meta.url).href
  },
  {
    label: '阿里图标库',
    url: 'https://www.iconfont.cn/',
    iconUrl: new URL('@/assets/svg/linkSvg/alibabaIcon.svg', import.meta.url).href
  },
  {
    label: 'Element UI',
    url: 'https://element-plus.gitee.io/zh-CN/component/button.html',
    iconUrl: new URL('@/assets/svg/linkSvg/elementUi.svg', import.meta.url).href
  },
  {
    label: 'AI百晓生',
    url: 'https://ai.hxkj.vip/',
    iconUrl: new URL('@/assets/svg/linkSvg/ai.ico', import.meta.url).href
  },
  {
    label: '据意查句',
    url: 'https://wantquotes.net/',
    iconUrl: 'https://wantquotes.net/favicon.ico'
  }
]);
function clickLink(item: any) {
  window.open(item.url);
}
</script>
<style scoped lang="scss">
@include theme() {
  .common-link {
    margin: 0 5px;
    position: relative;
    cursor: pointer;
    @include flex;
    .header-name-icon {
      margin-right: 10px;
      cursor: pointer;
      color: inherit;
      padding: 6px;
    }
    .header-name-icon:hover {
      margin-right: 10px;
      cursor: pointer;
      background: get('bk');
      color: white;
      border-radius: 25px;
    }
    .link-content {
      position: absolute;
      visibility: hidden;
      background: get('back');
      opacity: 1;
      transition: all 0.6s ease-in-out;
      top: 50px;
      z-index: 999;
      border-radius: 8px;
      overflow: hidden;
      width: 320px;
      left: -30px;
      box-shadow: get('box-shadow');
      .link-title {
        font-size: 14px;
        height: 22px;
        line-height: 22px;
        margin-top: 10px;
        text-align: left;
        padding-left: 27px;
        font-weight: normal;
        color: get('font-color');
      }
      .link-list {
        @include flex;
        flex-wrap: wrap;
        justify-content: start;
        padding: 10px 8px 15px;
        width: 100%;
        backdrop-filter: blur(20px);
        padding: 5px 8px;
        .link-item {
          white-space: nowrap;
          @include flex;
          justify-content: start;
          cursor: pointer;
          padding: 0 10px;
          width: 120px;
          height: 40px;
          margin: 5px;
          border-radius: 5px;
          .link-icon {
            width: 30px;
            height: 30px;
            margin-right: 10px;
          }
          .link-text {
            font-size: 0.8rem;
            color: get('font-color');
          }
          &:hover {
            background: get('border-color');
            border-radius: 8px;
            box-shadow: get('box-shadow');
            .link-text {
              color: get('re-font-color');
            }
          }
        }
      }
    }

    &:hover .link-content {
      visibility: visible;
      opacity: 1;
    }
  }
}
</style>
