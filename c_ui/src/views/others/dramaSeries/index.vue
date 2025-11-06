<!--
 * @Description: 影视
-->
<template>
  <div class="page-main drama-main">
    <el-carousel
      trigger="click"
      type="card"
      class="drama-carousel animated bounceInDown"
      loop
      autoplay
      interval="5000"
    >
      <el-carousel-item v-for="item in coverImages" class="drama-carousel-item">
        <img class="carousel-item-img" :src="item" />
      </el-carousel-item>
    </el-carousel>
    <div class="divider animated-0s5 c-left">
      <svg-icon iconName="commonSvg-电影" /> {{ $t('电影') }}<span>{{ $t('没什么电影看啊') }}</span>
    </div>
    <div class="drama-list">
      <div class="drama-item" v-for="item in movies">
        <c-image :src="item.coverUrl" />
        <div class="drama-info">
          <h3>{{ item.name }}</h3>
          <el-rate
            class="drama-rate"
            v-model="item.rate"
            disabled
            show-score
            allow-half
            text-color="#ff9900"
            :score-template="`${item.rate * 2}`"
          />
          <span class="no-wrap drama-intro">{{ item.intro }}</span>
        </div>
      </div>
    </div>
    <div class="divider">
      <svg-icon iconName="commonSvg-电视播放" />{{ $t('电视剧')
      }}<span>{{ $t('没什么电视剧看啊') }}</span>
    </div>
    <div class="drama-list">
      <div class="drama-item" v-for="item in teleplays">
        <c-image :src="item.coverUrl" />
        <div class="drama-info">
          <h3>{{ item.name }}</h3>
          <el-rate
            class="drama-rate"
            v-model="item.rate"
            disabled
            show-score
            allow-half
            text-color="#ff9900"
            :score-template="`${item.rate * 2}`"
          />
          <span class="drama-intro no-wrap">{{ item.intro }}</span>
        </div>
      </div>
    </div>
    <div class="divider">
      <svg-icon iconName="commonSvg-动漫" />{{ $t('动漫') }}<span>{{ $t('没什么动漫看啊') }}</span>
    </div>
    <div class="drama-list">
      <div class="drama-item" v-for="item in animations">
        <c-image :src="item.coverUrl" />
        <div class="drama-info">
          <h3>{{ item.name }}</h3>
          <el-rate
            class="drama-rate"
            v-model="item.rate"
            disabled
            show-score
            allow-half
            text-color="#ff9900"
            :score-template="`${item.rate * 2}`"
          />
          <span class="drama-intro no-wrap">{{ item.intro }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import ParallaxWheelSeeding from './components/parallaxWheelSeeding.vue';
import { coverImages } from './dramaData.ts';
import { pageDramas } from '@/api/dramaSeries.ts';

const movies = ref([]) as any;

const teleplays = ref([]) as any;

const animations = ref([]) as any;

const animateTimeList = ['0s5', '0s7', '1s', '1s2', '1s5', '1s7', '2s', '2s2', '2s5', '2s7', '3s'];

function getAnimateTime() {
  return `animated-${animateTimeList[Math.floor(Math.random() * 9) + 1]}`;
}

async function getList() {
  const { code, rows, total } = (await pageDramas({})) as any;
  if (code === 200) {
    movies.value = rows.filter((e: any) => {
      return e.type == 0;
    });
    teleplays.value = rows.filter((e: any) => {
      return e.type == 1;
    });
    animations.value = rows.filter((e: any) => {
      return e.type == 2;
    });
  }
}

onMounted(() => {
  getList();
});
</script>

<style lang="scss" scoped>
@include theme() {
  .drama-main {
    @include flex-column;
    background: transparent !important;
    backdrop-filter: none !important;
  }
  .drama-carousel {
    margin-top: 10px;
    margin-bottom: 10px;
    width: 100%;
    height: 45vh !important;
    :deep(.el-carousel__container) {
      height: 100%;
    }
    .drama-carousel-item {
      height: calc(100%);
      box-shadow: get('box-shadow');
      // opacity: 0.9;
      border-radius: 8px;
    }
    .carousel-item-img {
      width: 100%;
      height: 100%;
      z-index: 1;
      object-fit: cover;
      background-position: center;
      border-radius: 8px;
    }
  }
  .drama-list {
    display: flex;
    justify-content: start;
    align-items: center;
    flex-wrap: wrap;
    width: calc(100% + 25px);
  }
  .drama-item {
    box-shadow: get('box-shadow');
    background: get('back');
    position: relative;
    transition: all 0.2s linear;
    border-radius: 10px;
    width: calc(20% - 30px);
    aspect-ratio: 5/8;
    margin: 15px;
    @include flex-column;
    justify-content: start;
    overflow: hidden;
    .c-image {
      width: 100%;
      border-radius: 8px;
      height: 100%;
    }
  }
  .c-image {
    :deep(img) {
      filter: brightness(90%);
    }
  }
  .drama-info {
    @include flex-column;
    justify-content: start;
    align-items: start;
    transition: all 0.6s ease;
    bottom: -60%;
    width: calc(100% - 40px);
    padding: 0 20px;
    position: absolute;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(0, 0, 0, 0.5) 25%);
    height: 100%;
    color: white;
    h3 {
      margin: 30px 3px 10px 3px;
    }
    .drama-rate {
      margin-bottom: 10px;
      // width: calc(100% - 20px);
    }
    .drama-intro {
      text-align: left;
      margin-left: 3px;
    }
  }
  .drama-item:hover {
    .drama-info {
      bottom: 0%;
    }
    .drama-intro {
      -webkit-line-clamp: 20 !important;
    }
    // .item-cover {
    //   opacity: 1;
    //   top: 30%;
    //   filter: brightness(100%);
    // }
  }
  .divider {
    font-size: 1.2rem;
    font-weight: bold;
    width: calc(100% - 20px);
    text-align: left;
    margin: 15px 0;
    background: get('back');
    box-shadow: get('box-shadow');
    padding: 10px;
    border-radius: 10px;
    @include flex;
    justify-content: start;
    .svg-icon {
      height: 30px;
      width: 30px;
      margin-right: 5px;
    }
    span {
      font-size: 0.9rem;
      font-weight: 300;
      margin-left: 10px;
    }
  }
  .item-cover {
    width: 100%;
    height: 70%;
    position: absolute;
    top: 320px;
    transition: all 0.5s linear;
    opacity: 0;
    background: white;
    @include flex;
  }
}
</style>
