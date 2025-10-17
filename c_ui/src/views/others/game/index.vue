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
        <svg-icon iconName="commonSvg-手柄" /> 单机游戏
        <span> 我不知道我是谁，不知道我在哪，我只知道我要大开杀戒了</span>
      </div>
      <div class="game-list">
        <virtual-scroller :items="singlePlayerGames" item-height="340" content-tag="table">
          <template #default="{ item }">
            <div class="game-list">
              <div class="game-item bounceInUp" @click="toOfficial(item)" v-for="game in item">
                <c-image :src="game.coverUrl" />
                <div class="game-info">
                  <div class="game-info-header">
                    <h3>{{ game.name }}</h3>
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
        手机游戏<span>打发时间，不过现在已经不玩了</span>
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
import { ref, onMounted } from 'vue';

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

const animateTimeList = ['0s5', '0s7', '1s', '1s2', '1s5', '1s7', '2s', '2s2', '2s5', '2s7', '3s'];

function getAnimateTime() {
  return `animated-${animateTimeList[Math.floor(Math.random() * 9) + 1]}`;
}

function toOfficial(item: any) {
  if (!item.url) return;
  window.open(item.url, '_blank');
}

onMounted(() => {
  getList();
});
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
      width: calc(100% - 40px);
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
      padding: 5px 3px;
      // height: 300px;
      aspect-ratio: 7/6.5;
      @include flex-column;
      justify-content: start;
      overflow: hidden;
      .el-image {
        width: calc(100% - 6px);
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
      justify-content: start;
      background: get('back');
      transition: all 0.6s ease;
      bottom: -30%;
      width: 100%;
      position: absolute;
      height: 60%;
      color: get('font-color');
      span {
        padding: 0 12px;
        text-align: left;
      }
      .game-info-header {
        display: flex;
        width: calc(100% - 24px);
        padding: 0 12px;
        justify-content: space-between;
        align-items: center;
      }
      h3 {
        margin: 15px 0;
        text-align: left;
        width: calc(100% - 150px);
      }
    }
    .game-item:hover {
      span {
        -webkit-line-clamp: 6 !important;
      }
      .game-info {
        bottom: 0;
      }
      .c-image {
        :deep(img) {
          filter: brightness(60%);
          transform: scale(1.2);
        }
      }
    }
  }
}
</style>
