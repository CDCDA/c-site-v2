<template>
  <div class="tv-frame">
    <div class="tv-screen">
      <div class="overlay"></div>
      <contact-dialog v-model="showContact" />

      <div class="person-fixed-button" @click="router.push({ name: 'personalProfile' })">
        <el-icon> <Right /> </el-icon>{{ $t('关于我') }}
      </div>
      <div class="person-fixed-button contact-button" @click="showContact = true">
        <el-icon>
          <Message />
        </el-icon>
        <span>{{ $t('联系我') }}</span>
      </div>
      <a href="https://github.com/CDCDA" target="_blank" class="person-fixed-button github-button">
        <svg-icon class="github-svg" iconName="techStackSvg-git-white" />
        <span>{{ $t('git') }}</span>
      </a>
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
                <el-icon style="margin-right: 0.5rem">
                  <Calendar />
                </el-icon>
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
      :slides-per-view="slidesPerView"
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
          <div v-if="item.type === 'primary'" class="primary-badge">
            {{ $t('重点') }}
          </div>
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
import { useRouter } from 'vue-router';
const router = useRouter();
const modules = [Navigation, Thumbs, Pagination, Mousewheel, Autoplay];
import 'swiper/css';
import ContactDialog from '@/components/contactDialog/index.vue';

const showContact = ref(false);
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

const slidesPerView = computed(() => {
  const count = props.itemList.length || 10;
  const maxSlides = Math.floor(count / 2);
  return Math.min(maxSlides, 9);
});

const swiperStyle = computed(() => ({
  '--perforation-size': '12px',
  '--film-gutter': '8px',
  '--slides-per-view': slidesPerView.value
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

onMounted(() => {
  // 确保swiper正确计算尺寸并居中显示第一个slide
  setTimeout(() => {
    if (thumbsSwiper.value) {
      thumbsSwiper.value.update();
      thumbsSwiper.value.slideToLoop(0, 0);
    }
  }, 500);
});
</script>
<style lang="scss" scoped>
@include theme() {
  .person-fixed-button {
    position: absolute;
    bottom: 3%;
    left: 32px;
    z-index: 99;

    background: #404040;

    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 25px;

    color: white;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100px;
    height: 2rem;
    cursor: pointer;
    transition: all 0.3s ease;

    .el-icon {
      font-size: 18px;
      margin-right: 5px;
    }
  }

  .person-fixed-button:hover {
    transform: translateY(-2px);
  }

  .github-button {
    left: 263px;

    .github-svg {
      width: 18px;
      height: 18px;
      margin-right: 8px;
      margin-bottom: -3px;
    }
  }

  .contact-button {
    left: 147px;
  }

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
      border-radius: 15px;
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
      top: 2rem;
      left: 42px;
    }

    .swiper-slide {
    }

    .project-item {
      aspect-ratio: 16/9;
      /* 改为16:9宽高比 */
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
        flex: 1;
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
        width: 46%;
        text-align: left;
        padding: 0rem 2rem 0 1rem;
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
  // padding: 20px 0; // 增加上下内边距，防止放大时被遮挡

  .film-slide {
    position: relative;
    // 默认状态：去色、变暗、透明度降低
    filter: grayscale(1) brightness(0.5);
    opacity: 0.6;
    transition: all 0.5s cubic-bezier(0.16, 1, 0.3, 1);
    cursor: pointer;

    // --- 选中态样式 (Swiper自带类名或你的active类) ---
    &.swiper-slide-thumb-active,
    &.active {
      filter: grayscale(0) brightness(1.2); // 恢复彩色并加亮
      opacity: 1;
      transform: scale(1.2); // 放大比例稍微加大
      z-index: 20;

      // 选中时的发光边框
      .film-frame {
        outline: 2px solid #fff; // 亮白色边框
        box-shadow: 0 0 20px rgba(255, 255, 255, 0.4), 0 0 2rem rgba(255, 255, 255, 0.2);

        // 选中时文字层变亮
        .back-filter {
          background: rgba(0, 0, 0, 0.2); // 遮罩变浅，让图片更清晰

          .title {
            color: black;
            text-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
            font-weight: bold;
          }
        }
      }

      // --- 新增：选中项上方的倒三角指示器 ---
      &::before {
        content: '';
        position: absolute;
        top: -15px; // 位于slide上方
        left: 50%;
        transform: translateX(-50%);
        border-left: 8px solid transparent;
        border-right: 8px solid transparent;
        border-top: 10px solid #fff; // 白色三角指向下方
        z-index: 30;
        animation: bounce 2s infinite; // 增加一个微弱的跳动动画
      }
    }

    // 悬停在非选中的上面时，稍微亮一点
    &:hover:not(.swiper-slide-thumb-active) {
      filter: grayscale(0.5) brightness(0.8);
      opacity: 0.8;
    }
  }
}

// 修正 film-frame 的样式，确保 border-radius 生效
.film-frame {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #000; // 底色设为黑

  .back-filter {
    background: rgba(0, 0, 0, 0.6); // 默认遮罩深一点
    transition: all 0.3s ease;

    .title {
      font-size: 12px; // 胶卷上的文字不宜过大
      text-align: center;
      padding: 0 5px;
    }
  }
}

// 动画效果
@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateX(-50%) translateY(0);
  }

  40% {
    transform: translateX(-50%) translateY(-5px);
  }

  60% {
    transform: translateX(-50%) translateY(-3px);
  }
}

// 重点标签在选中时才更亮
.primary-badge {
  opacity: 0.7;
  transition: opacity 0.3s;
}

.swiper-slide-thumb-active .primary-badge {
  opacity: 1;
  transform: scale(1.1);
}

.film-frame {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(45deg, rgba(255, 255, 255, 0.1) 0%, rgba(0, 0, 0, 0.4) 100%);
    opacity: 0.4;
    transition: opacity 0.3s;
  }

  .primary-badge {
    position: absolute;
    top: 6px;
    right: 10px;
    z-index: 20;
    padding: 2px 6px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    font-size: 10px;
    font-weight: 600;
    border-radius: 3px;
    box-shadow: 0 2px 6px rgba(102, 126, 234, 0.4);
  }
}

.swiper-slide-img {
  width: 100%;
  height: 90px;
  max-width: 220px;
  display: block;
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
