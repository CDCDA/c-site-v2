<template>
  <div class="recommend-left">
    <!-- <div class="recommend-left-bottom">
      <div
        class="bottom-item"
        @click="router.push({ name: 'blogTypePage', query: { typeId: '1' } })"
      >
        <span>前端小记</span><svg-icon iconName="commonSvg-书籍" />
      </div>
      <div
        class="bottom-item"
        @click="router.push({ name: 'blogTypePage', query: { typeId: '2' } })"
      >
        <span>后端总结</span><svg-icon iconName="commonSvg-火焰" />
      </div>
      <div class="bottom-item" @click="router.push({ name: 'essay' })">
        <span>生活随笔</span><svg-icon iconName="commonSvg-编辑" />
      </div>
    </div> -->
    <div class="recommend-left-top">
      <div class="recommd-left-title">
        <span>我的技术栈</span>
      </div>
      <TechnologyStackCard class="recommd-left-stack" :isHoverShow="false" />
      <div class="recommend-left-top-cover" @click="toRange()">
        <div>随便逛逛</div>
        <svg-icon iconName="commonSvg-右"></svg-icon>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from 'vue-router';
import { getRandomBlog } from '@/api/blog';
import TechnologyStackCard from '../../introduction/personalProfile/components/technologyStackCard.vue';
const router = useRouter() as any;
async function toRange() {
  const { code, data } = (await getRandomBlog()) as any;
  if (code === 200) {
    router.push({
      name: 'blogDisplay',
      query: {
        blogId: data
      }
    });
  }
}
</script>
<style lang="scss" scoped>
@include theme() {
  .recommend-left {
    height: 100%;
    width: calc(50%);
    margin-right: 10px;
    display: flex;
    justify-content: space-between;
    cursor: pointer;
    .recommend-left-top {
      height: 100%;
      background: get('back');
      border-radius: 8px;
      width: calc(100%);
      box-shadow: get('box-shadow');
      overflow: hidden;
      position: relative;
      .recommd-left-title {
        position: absolute;
        top: 28px;
        left: 46px;
        font-size: 44px;
        font-weight: bold;
      }
      .recommd-left-stack {
        transform: rotate(-35deg);
        position: absolute;
        animation: none;
        right: -100px;
        top: 20px;
      }
      .recommend-left-top-cover {
        opacity: 0;
        z-index: 1;
        width: 100%;
        position: absolute;
        height: calc(100%);
        background: get('bk');
        font-size: 85px;
        font-weight: bold;
        color: white;
        @include flex-column;
        align-items: flex-start;
        padding-left: 50px;
        transition: cubic-bezier(0.71, 0.15, 0.16, 1.15) 0.6s;
        .svg-icon {
          width: 120px;
          height: 120px;
        }
      }
      &:hover {
        .recommend-left-top-cover {
          opacity: 1;
        }
      }
    }
    .recommend-left-bottom {
      @include flex-column;
      margin-top: 5px;
      width: 25%;
      height: 100%;
      margin-right: 20px;
      .bottom-item {
        height: calc(33.33% - 10px);
        cursor: pointer;
        box-shadow: get('box-shadow');
        width: calc(100%);
        background: get('back');
        border-radius: 8px;
        @include flex;
        position: relative;
        justify-content: start;
        span {
          color: white;
          font-weight: bold;
          font-size: 1rem;
          padding-left: 25px;
        }
        .svg-icon {
          opacity: 0.2;
          position: absolute;
          right: 0%;
          top: 20%;
          transition: 0.3s;
          width: 90px;
          text-align: center;
          filter: blur(2px);
          transform: rotate(15deg);
          .theme-icon {
            color: white !important;
            fill: white !important;
          }
        }
      }
      .bottom-item:not(:hover) {
        // animation: re-slide-in 0.3s forwards linear;
        img {
          animation: re-rotate-img 0.3s forwards linear;
        }
      }
      .bottom-item:hover {
        // animation: slide-in 0.3s forwards linear;
        .svg-icon {
          animation: rotate-img 0.3s forwards linear;
        }
      }

      .bottom-item:nth-child(1) {
        background: get('bk');
        margin-bottom: 10px;
      }
      .bottom-item:nth-child(2) {
        margin-bottom: 10px;
        background: linear-gradient(to right, #ff6655, #ffbf37);
      }
      .bottom-item:nth-child(3) {
        background: linear-gradient(to right, #18e7ae, #1eebeb);
      }
    }
  }
}
@keyframes slide-in {
  0% {
    width: 33%;
  }
  100% {
    width: 50%;
  }
}
@keyframes rotate-img {
  0% {
    opacity: 0.2;
    transform: rotate(15deg);
  }
  100% {
    opacity: 1;
    right: 5%;
    top: -5%;
    filter: blur(0px);
    transform: rotate(0deg);
  }
}
@keyframes re-slide-in {
  0% {
    width: 50%;
  }
  100% {
    width: 33%;
  }
}
@keyframes re-rotate-img {
  0% {
    opacity: 1;
    right: 5%;
    top: -5%;
    filter: blur(0px);
    transform: rotate(0deg);
  }
  100% {
    opacity: 0.2;
    transform: rotate(15deg);
  }
}
</style>
