<template>
  <div class="tv-frame">
    <div class="tv-screen">
      <div class="overlay"></div>
      <swiper
        :loop="true"
        :spaceBetween="20"
        :pagination="{
          type: 'fraction'
        }"
        :thumbs="{ swiper: thumbsSwiper }"
        :modules="modules"
        @swiper="onProjectSwiper"
        class="project-swiper"
      >
        <swiper-slide v-for="(item, index) in itemList" class="flex-center" :key="item.id">
          <div class="project-item flex-between">
            <div class="project-img-list">
              <inside-swiper :item="item"></inside-swiper>
            </div>
            <div class="project-detail">
              <div class="title">
                {{ item.title }}
              </div>
              <div class="dateTime">
                <el-icon style="margin-right: 0.5rem"><Calendar /></el-icon>
                {{ `${item.date[0]} ~ ${item.date[1]}` }}
              </div>
              <div class="subTitle">{{ $t('项目简介') }}</div>
              <div class="intro">{{ item.intro }}</div>
              <div class="subTitle" style="margin-bottom: 0.5rem">{{ $t('关联技术栈') }}</div>
              <div class="tech">
                <el-tag
                  class="tech-tag"
                  effect="dark"
                  :type="getRandomType()"
                  v-for="tag in item.tags"
                  >{{ tag }}</el-tag
                >
              </div>
              <div class="subTitle" style="margin-top: 0.5rem">{{ $t('负责模块') }}</div>
              <ul class="module">
                <li class="module-item" v-for="module in item.modules">
                  <div class="module-title">{{ module.title }}</div>
                  <div class="intro">{{ module.intro }}</div>
                </li>
              </ul>
            </div>
          </div>
        </swiper-slide>
      </swiper>
    </div>
    <!-- 电视底部装饰 -->
    <div class="tv-base"></div>
  </div>
  <div class="film-container">
    <!--    @wheel.prevent="handleWheel"-->
    <swiper
      ref="swiperRef"
      :grab-cursor="true"
      :modules="modules"
      :loop="true"
      :slides-per-view="9"
      :space-between="5"
      class="top-swiper"
      :centered-slides="true"
      @swiper="onSwiper"
      :style="swiperStyle"
    >
      <swiper-slide
        v-for="(item, index) in itemList"
        :key="item.id"
        :class="['film-slide', { active: item.id === activeId }]"
        :data-swiper-slide-index="index"
        @click="handleSlideClick()"
      >
        <div class="film-frame">
          <div class="back-filter">
            <div class="title">{{ item.title }}</div>
          </div>
          <c-image class="swiper-slide-img" :lazy="false" :src="item.coverUrl" />
        </div>
      </swiper-slide>
    </swiper>
    <div class="film-back"></div>
  </div>
</template>

<script lang="ts" setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css/thumbs';
import 'swiper/css';
import { Pagination, Navigation, Thumbs, Mousewheel, Autoplay } from 'swiper/modules';
import { onMounted, ref, watch, computed } from 'vue';
const modules = [Navigation, Thumbs, Pagination, Mousewheel, Autoplay];
import 'swiper/css';

import 'swiper/css/pagination';
import 'swiper/css/navigation';
import InsideSwiper from '@/views/introduction/projectExperience/components/insideSwiper.vue';
function getRandomType() {
  const typeArr = ['default', 'primary', 'success', 'info', 'warning', 'danger'];
  return typeArr[Math.floor(Math.random() * typeArr.length)];
}

const activeId = ref(0);
const thumbsSwiper = ref(null) as any;
const props = defineProps({
  itemList: {
    default: [] as any
  }
});
// 新增胶片效果参数
const swiperStyle = computed(() => ({
  '--perforation-size': '12px',
  '--film-gutter': '8px'
}));
const emit = defineEmits(['itemClick']);

// 修改点击处理函数
const handleSlideClick = () => {
  if (!thumbsSwiper.value) return;
  const swiper = thumbsSwiper.value;
  // 获取被点击slide的真实索引
  const clickedIndex = parseInt(swiper.clickedSlide?.dataset.swiperSlideIndex, 10);
  // 更新激活ID并切换幻灯片
  activeId.value = props.itemList[clickedIndex]?.id || 0;
  swiper.slideToLoop(clickedIndex, 500); // 使用slideToLoop处理循环索引
};

// 修改slideChange监听
const onSwiper = (swiper: any) => {
  thumbsSwiper.value = swiper;
  swiper.on('slideChange', () => {
    activeId.value = props.itemList[swiper.realIndex]?.id;
  });
};
const onProjectSwiper = (swiper: any) => {
  swiper.on('slideChange', () => {
    activeId.value = props.itemList[swiper.realIndex]?.id;
    const clickedIndex = parseInt(swiper.clickedSlide?.dataset.swiperSlideIndex, 10);
    thumbsSwiper.value.slideToLoop(props.itemList[swiper.realIndex]?.id, 500); // 使用slideToLoop处理循环索引
  });
};

onMounted(() => {});
</script>
<style lang="scss" scoped>
@include theme() {
  /* 新增电视外壳样式 */
  .tv-frame {
    position: relative;
    width: 70%;
    margin: 0 auto 2.5rem auto;
    padding: 0.1rem;
    background: #2a2a2a;
    border-radius: 20px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.5), inset 0 -5px 15px rgba(0, 0, 0, 0.3);
    aspect-ratio: 15 / 7;
    height: calc(100% - 190px);

    /* 屏幕金属边框 */
    &::before {
      content: '';
      position: absolute;
      top: -10px;
      left: -10px;
      right: -10px;
      bottom: -10px;
      background: linear-gradient(145deg, #3a3a3a, #1a1a1a);
      border-radius: 25px;
      z-index: -1;
    }

    .tv-brand {
      position: absolute;
      top: 15px;
      left: 50%;
      transform: translateX(-50%);
      color: rgba(255, 255, 255, 0.3);
      font-size: 0.8rem;
      letter-spacing: 2px;
    }

    .tv-screen {
      position: relative;
      background: #000;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: inset 0 0 30px rgba(255, 255, 255, 0.1);
      height: 100%;
      .overlay {
        pointer-events: none;
        position: absolute;
        width: 100%;
        height: 100%;
        //background: repeating-linear-gradient(
        //  180deg,
        //  rgba(0, 0, 0, 0) 0,
        //  rgba(0, 0, 0, 0.3) 50%,
        //  rgba(0, 0, 0, 0) 100%
        //);
        background-size: auto 4px;
        z-index: 99;
      }
      .overlay::before {
        content: '';
        pointer-events: none;
        position: absolute;
        display: block;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: linear-gradient(
          0deg,
          transparent 0%,
          rgba(128, 128, 128, 0.2) 2%,
          rgba(128, 128, 128, 0.8) 3%,
          rgba(128, 128, 128, 0.2) 3%,
          transparent 100%
        );
        background-repeat: no-repeat;
        animation: scan 10s linear 20s infinite;
      }
      /* 添加屏幕玻璃反光效果 */
      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          45deg,
          rgba(255, 255, 255, 0) 60%,
          rgba(255, 255, 255, 0.05) 70%,
          rgba(255, 255, 255, 0) 80%
        );
        pointer-events: none;
      }
    }
  }
  .tv-base {
    width: 15%;
    position: absolute;
    left: 42.5%;
    height: 16px;
    background: #1e1e1e;
    border-radius: 0;
    bottom: -25px;

    &::before {
      content: '';
      position: absolute;
      top: 6px;
      left: -25%;
      height: 20px;
      margin: 10px auto 0;
      background: #1a1a1a;
      border-radius: 10px 10px 2px 2px;
      width: 150%;
    }
  }

  /* 修改原有swiper样式 */
  .project-swiper {
    height: calc(100% - 2rem);
    width: calc(100% - 2rem);
    margin: 0;
    padding: 1rem;
    background-color: #000000;
    background-image: radial-gradient(#333, #111);
    font-family: 'Inconsolata', Helvetica, sans-serif;
    color: rgba(128, 128, 128, 0.8);
    text-shadow: 0 0 1ex rgba(51, 51, 51, 1), 0 0 2px rgba(255, 255, 255, 0.8);
    :deep(.swiper-pagination) {
      width: fit-content;
      position: absolute;
      top: 40px;
      left: 42px;
    }
    .swiper-slide {
    }

    .project-item {
      aspect-ratio: 16/9; /* 改为16:9宽高比 */
      //background: rgba(0, 20, 0, 0.9); /* 深绿色调模拟老式屏幕 */
      //border: 2px solid rgba(255, 255, 255, 0.1);
      //box-shadow: inset 0 0 20px rgba(0, 80, 0, 0.5), 0 0 30px rgba(0, 80, 0, 0.3);
      height: 100%;
      width: 100%;
      .project-img-list {
        .c-image {
          width: 100%;
          height: 100%;
        }
        border: 2px solid rgba(255, 255, 255, 0.1);
        width: 40%;
        margin: 1rem;
        border-radius: 12px;
        overflow: hidden;
        aspect-ratio: 3/2;
        //&::before {
        //  content: '';
        //  position: absolute;
        //  width: 100%;
        //  height: 100%;
        //  box-shadow: inset 0 0 10px rgba(0, 255, 0, 0.2);
        //}
      }

      .project-detail {
        color: #fff;
        overflow: auto;
        text-shadow: 0 0 5px rgba(0, 255, 0, 0.3);
        width: calc(60% - 10rem);
        text-align: left;
        padding: 0rem 5rem;
        height: calc(100% - 0rem);
        flex-direction: column;
        align-items: start;
        justify-content: start;
        .title {
          font-size: 1.5rem;
          font-weight: bold;
          margin: 0.2rem 0 0.7rem 0;
        }
        .dateTime {
          font-size: 0.9rem;
          margin: 1rem 0;
          display: flex;
        }
        .subTitle {
          font-size: 1.1rem;
          font-weight: bold;
          margin: 1rem 0;
        }
        .module {
          .module-item {
            align-items: start;
            margin-bottom: 1rem;
            .module-title {
              color: #8f8;
              margin-bottom: 0.5rem;
            }
          }
        }
        .tech {
          .tech-tag {
            margin: 0.4rem;
          }
        }
      }
    }

    /* 调整导航按钮样式 */
    :deep(.swiper-button-next),
    :deep(.swiper-button-prev) {
      color: #8f8;
      &::after {
        text-shadow: 0 0 10px rgba(0, 255, 0, 0.5);
      }
    }
  }
}
@keyframes scan {
  0% {
    background-position: 0 -100vh;
  }
  35%,
  100% {
    background-position: 0 100vh;
  }
}
</style>
<style lang="scss" scoped>
@include theme() {
  .film-slide {
    //box-shadow: get('box-shadow');
  }
  .intro {
    white-space: pre-line;
  }
  .back-filter {
    background: rgba(0, 0, 0, 0.5);
    z-index: 10;
    position: absolute;
    width: 100%;
    height: 100%;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    .title {
    }
  }
}
.film-container {
  position: relative;
  padding: 1.2rem 0;
  background: linear-gradient(
      to right,
      rgba(0, 0, 0, 0.8) 0%,
      rgba(0, 0, 0, 0.4) 10%,
      rgba(0, 0, 0, 0) 50%,
      rgba(0, 0, 0, 0.4) 90%,
      rgba(0, 0, 0, 0.8) 100%
    ),
    repeating-linear-gradient(
      90deg,
      #171717 0px,
      #171717 10px,
      rgba(255, 255, 255, 0.5) 10px,
      rgba(255, 255, 255, 0.5) 20px
    );
  border-top: 10px solid #171717;
  border-bottom: 10px solid #171717;

  &::before,
  &::after {
    content: '';
    position: absolute;
    top: 0;
    height: 100%;
    width: 30px;
    background: linear-gradient(to right, rgba(0, 0, 0, 0.9) 0%, transparent 100%);
    z-index: 2;
  }

  &::before {
    left: 0;
  }

  &::after {
    right: 0;
    transform: rotate(180deg);
  }
  .film-back {
    width: 100%;
    height: calc(100% - 2.4rem);
    position: absolute;
    top: 0.7rem;
    background: #232323;
    border-top: 0.5rem solid #171717;
    border-bottom: 0.5rem solid #171717;
  }
}

.top-swiper {
  width: 100vw;
  overflow: visible;

  .film-slide {
    position: relative;
    transition: transform 1.6s cubic-bezier(0.16, 1, 0.3, 1);

    &.swiper-slide-thumb-active::before,
    &:hover::before {
      content: '';
      position: absolute;
      inset: -8px;
      background: #1a1a1a;
      z-index: -1;
      border-radius: 4px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
    }

    &.swiper-slide-thumb-active,
    &:hover {
      transform: scale(1.15);
      z-index: 10;
    }
    &:hover {
      z-index: 20;
    }
  }
}

.film-frame {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s ease;

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(45deg, rgba(255, 255, 255, 0.1) 0%, rgba(0, 0, 0, 0.4) 100%);
    opacity: 0.4;
    transition: opacity 0.3s;
  }
}

.swiper-slide-img {
  aspect-ratio: 16/9;
  width: calc((100vw / 9) - 10px);
  display: block;
  //border-radius: 2px;
}

.perforations {
  position: absolute;
  top: 50%;
  left: -8px;
  right: -8px;
  height: var(--perforation-size);
  transform: translateY(-50%);
  background: repeating-linear-gradient(
    to right,
    transparent 0,
    transparent 6px,
    #666 6px,
    #666 10px,
    transparent 10px,
    transparent 16px
  );
  opacity: 0.6;
}
</style>
