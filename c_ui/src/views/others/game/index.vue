<!--
 * @Description: 游戏
-->
<template>
  <div class="page-main game-main">
    <div class="game-top">
      <div
        class="game-top-item bounceInDown"
        :class="getAnimateTime()"
        v-for="item in gameShowData"
      >
        <img :src="item.coverUrl" />
      </div>
    </div>
    <div class="game-center">
      <div class="divider c-left animated-0s5">
        <svg-icon iconName="commonSvg-手柄" /> {{ $t('单机游戏')
        }}<span> {{ $t('我不知道我是谁，不知道我在哪，我只知道我要大开杀戒了') }}</span>
      </div>
      <div class="game-list">
        <virtual-scroller
          :items="singlePlayerGames"
          :item-height="getItemHeight"
          content-tag="table"
        >
          <template #default="{ item }">
            <div class="game-list">
              <div class="game-item bounceInUp" @click="toOfficial(item)" v-for="game in item">
                <c-image :src="game.coverUrl" />
                <div class="game-info">
                  <div class="game-info-header">
                    <h3 class="no-wrap">{{ game.name }}</h3>
                    <el-rate
                      v-model="game.rate"
                      disabled
                      show-score
                      allow-half
                      text-color="#ff9900"
                      size="large"
                      :score-template="`${game.rate * 2}`"
                    />
                  </div>
                  <span class="no-wrap">{{ game.intro }}</span>
                </div>
              </div>
            </div>
          </template>
        </virtual-scroller>
      </div>
      <div class="divider c-left animated-0s5">
        <svg-icon iconName="commonSvg-手机" />
        {{ $t('手机游戏') }}<span>{{ $t('打发时间，不过现在已经不玩了') }}</span>
      </div>
      <div class="game-list">
        <div class="game-item" @click="toOfficial(item)" v-for="item in MobileGames">
          <c-image :src="item.coverUrl" />
          <div class="game-info">
            <div class="game-info-header">
              <h3 class="no-wrap">{{ item.name }}</h3>
              <el-rate
                v-model="item.rate"
                disabled
                show-score
                allow-half
                text-color="#ff9900"
                :score-template="`${item.rate * 2}`"
              />
            </div>
            <span class="no-wrap">{{ item.intro }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="game-bottom"></div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, computed } from 'vue';

import { VirtualScroller } from 'vue-virtual-scroller-classic';

import { pageGames } from '@/api/game.ts';
const gameList = ref([]) as any;
async function getList() {
  const { code, rows } = await pageGames({ pageSize: 9999 });
  if (code === 200) {
    gameShowData.value = JSON.parse(JSON.stringify(rows)).slice(0, 8);
    rows.forEach((game: any) => {
      game.rate = game.rate / 2;
    });

    const tempSinglePlayerGames = [];
    for (let i = 0; i < rows.filter((game: any) => game.type === '0').length; i += 4) {
      tempSinglePlayerGames.push(rows.filter((game: any) => game.type === '0').slice(i, i + 4));
    }
    singlePlayerGames.value = tempSinglePlayerGames;
    console.log(tempSinglePlayerGames);
    MobileGames.value = rows.filter((game: any) => game.type === '1');
  }
}

// 单机游戏
const singlePlayerGames = ref([]) as any;

//头部展示游戏
const gameShowData = ref([]) as any;

// 手机游戏
const MobileGames = ref([]) as any;

// 移除了动态 key，因为不需要重新渲染

const animateTimeList = ['0s5', '0s7', '1s', '1s2', '1s5', '1s7', '2s', '2s2', '2s5', '2s7', '3s'];

function getAnimateTime() {
  return `animated-${animateTimeList[Math.floor(Math.random() * 9) + 1]}`;
}

// 根据屏幕宽度计算 item-height（初始时确定，不动态监听）
const getItemHeight = computed(() => {
  const width = window.innerWidth;

  // 基于 CSS 中定义的 game-item 宽高比 (7/6.5) 和实际内容高度计算
  // 考虑不同屏幕下的布局差异
  if (width <= 480) {
    return 340; // 手机端：单列，更大的内容区域
  } else if (width <= 768) {
    return 320; // 平板端：双列
  } else if (width <= 1200) {
    return 300; // 小屏桌面：三列
  } else {
    return 280; // 大屏桌面：四列
  }
});

// 移除了动态监听，只在初始时计算一次

// 只在初始时计算，不监听窗口大小变化
onMounted(() => {
  getList();
});

function toOfficial(item: any) {
  if (!item.url) return;
  window.open(item.url, '_blank');
}
</script>

<style lang="scss" scoped>
@include theme() {
  .game-main {
    background: transparent !important;
    backdrop-filter: none !important;
  }

  .game-top {
    height: 40vh;
    @include flex;
    border-radius: 8px;
    justify-content: space-between;
    margin-bottom: 15px;

    .game-top-item:hover {
      // animation: slide-in 0.4s forwards linear;
      flex: 7;
      // .c-image {
      //   :deep(img) {
      //     object-fit: fill !important;
      //   }
      // }
    }

    .game-top-item {
      flex: 1;
      height: 100%;
      box-shadow: get('box-shadow');
      border-radius: 8px;
      overflow: hidden;
      margin-right: 10px;
      transition: all 0.8s ease;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      &:last-child {
        margin-right: 0;
      }
    }
  }

  .game-center {
    @include flex-column;
    margin: 0 -10px;

    .divider {
      font-size: 1.2rem;
      color: get('font-color');
      font-weight: bold;
      width: calc(100% - 2rem);
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

    .game-list {
      display: flex;
      justify-content: start;
      align-items: center;
      flex-wrap: wrap;
      width: calc(100%);
    }

    .game-item {
      box-shadow: get('box-shadow');
      background: get('back');
      position: relative;
      transition: all 0.2s linear;
      border-radius: 10px;
      width: calc(25% - 36px);
      margin: 15px;
      cursor: pointer;
      padding: 7px 3px;
      // height: 300px;
      aspect-ratio: 7/6.5;
      @include flex-column;
      justify-content: start;
      overflow: hidden;

      .el-image {
        width: calc(100% - 10px);
        border-radius: 8px;
        aspect-ratio: 7/4.6;
        object-fit: cover;
        background-position: center;
      }

      span {
        width: calc(100% - 20px);
        padding: 0 10px;
        transition: all 0.6s ease;
      }
    }

    .c-image {
      :deep(img) {
        transition: all 0.6s ease;
      }
    }

    .game-info {
      @include flex-column;
      justify-content: space-around; // 核心：垂直居中
      align-items: flex-start;
      // background: rgba(get('back'), 0.9); // 建议带一点透明度，效果更好
      // backdrop-filter: blur(4px);

      position: absolute;
      bottom: 0;
      left: 0;
      width: calc(100% - 0.8rem);

      /* 初始高度：仅容纳标题和一行简介 */
      height: 25%;
      padding: 8px 0.4rem;
      transition: all 0.4s ease;
      color: get('font-color');
      z-index: 2;
      background: get('back');

      .game-info-header {
        display: flex;
        width: 100%;
        box-sizing: border-box;
        padding: 0 6px;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 4px; // 紧凑间距

        h3 {
          margin: 0;
          text-align: left;
          font-size: 1rem;
          font-weight: bold;
          flex: 1;
          width: calc(100% - 150px);
          line-height: 1.3;
        }

        .no-wrap {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          max-width: 100%;
          margin: 0;
        }

        // 隐藏滚动条但保持滚动功能的工具类
        .hide-scrollbar {
          scrollbar-width: none;
          /* Firefox */
          -ms-overflow-style: none;

          /* IE/Edge */
          &::-webkit-scrollbar {
            display: none;
            /* Chrome/Safari/Opera */
          }
        }

        .el-rate {
          flex-shrink: 0;
          margin-left: 8px;
          height: auto;
          justify-content: flex-end;

          .el-rate__item {
            margin-right: 2px;
          }

          .el-rate__text {
            font-size: 12px;
          }
        }
      }

      span {
        width: 100%;
        box-sizing: border-box;
        padding: 0 6px;
        text-align: left;
        font-size: 0.85rem;
        opacity: 0.8;
        line-height: 1.4;
        overflow: hidden;
        /* 默认只显示一行 */
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
        white-space: normal; // 覆盖之前的 no-wrap
        transition: all 0.4s ease;
      }
    }

    .game-item:hover {
      .game-info {
        /* 悬浮时展开高度，显示更多简介 */
        height: 55%;
        background: get('back');
        justify-content: start;

        span {
          /* 悬浮时显示更多行，根据高度自动调整 */
          -webkit-line-clamp: none !important;
          opacity: 1;
          margin-top: 0.4rem;
          overflow: auto !important;
          /* 隐藏滚动条但保持滚动功能 */
          scrollbar-width: none;
          /* Firefox */
          -ms-overflow-style: none;

          /* IE/Edge */
          &::-webkit-scrollbar {
            display: none;
            /* Chrome/Safari/Opera */
          }
        }
      }

      .c-image {
        :deep(img) {
          filter: brightness(50%);
          transform: scale(1.1);
        }
      }
    }
  }

  // 响应式优化
  @media (max-width: 1200px) {
    .game-center .game-item {
      width: calc(33.33% - 30px);
    }
  }

  @media (max-width: 768px) {
    .game-center .game-item {
      width: calc(50% - 30px);
    }

    .game-info h3 {
      font-size: 1rem;
      width: calc(100% - 120px);
    }
  }

  @media (max-width: 480px) {
    .game-center .game-item {
      width: calc(100% - 20px);
      margin: 10px;
    }

    .game-info h3 {
      font-size: 0.9rem;
      width: calc(100% - 100px);
    }
  }
}
</style>
