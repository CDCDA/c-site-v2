<!--
 * @Description:  目标时间轴
-->
<template>
  <div class="timeline">
    <div class="timeline-main">
      <div class="entries">
        <div class="entry" v-for="item in goalList">
          <div class="title">
            <div class="title-text">{{ item.date }}</div>
          </div>
          <div class="body" v-if="item.goal">
            <p>{{ item.goal }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const goalList = ref([
  {
    date: '2023-8',
    goal: '完成网站基础框架'
  },
  {
    date: '2023-9',
    goal: '完成“博客”模块'
  },
  {
    date: '2023-10',
    goal: '完成“其他”模块的3个页面'
  },
  {
    date: '2023-11',
    goal: '完成“我的”模块的4个页面'
  },
  {
    date: '2023-12',
    goal: '大体完成个人网站前端修改'
  },
  {
    date: '2024-1',
    goal: '网站引用资源和数据都改到服务器'
  }
]) as any;
</script>
<style lang="scss" scoped>
@include theme() {
  .timeline {
    width: 450px;
    height: 100vw;
    transform: rotate(-90deg);
    overflow-y: auto;
    display: flex;
    // scrollbar-width: thin;
    // scrollbar-color: transparent transparent;
    justify-content: center;
    align-items: center;
    color: get('font-color');
    position: relative;
    background: get('back');
    .timeline-main {
      height: auto;
      margin: 0;
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      top: 9%;
      background: transparent;
      &:before {
        content: '';
        position: absolute;
        top: 0;
        left: 50%;
        width: 4px;
        background: get('font-color');
        height: 120%;
      }
      .entries {
        width: calc(100% - 80px);
        max-width: 800px;
        padding: 0;
        flex-direction: column;
        justify-content: center;
        height: auto;
        top: 0%;
        left: -3px;
        margin-top: 1000px;
        position: relative;
        .entry {
          width: calc(50% - 80px);
          float: left;
          padding: 20px;
          clear: both;
          text-align: right;
          position: relative;
          // margin-left: -5px;
          &:not(:first-child) {
            margin-top: -60px;
          }
          .body {
            transform: rotate(90deg);
            width: 260px;
            height: 100px;
            position: absolute;
            top: 213px;
            left: -47px;
            border: 1px solid;
            border-radius: 10px;
            text-align: left;
            padding: 0 10px;
          }
          .title {
            font-size: 32px;
            margin-bottom: 320px;
            position: relative;
            .title-text {
              transform: rotate(90deg);
              position: absolute;
              left: 32px;
              top: -29px;
              width: 150px;
            }
            &:before {
              content: '';
              position: absolute;
              width: 8px;
              height: 8px;
              border: 4px solid get('font-color');
              background: get('back');
              border-radius: 100%;
              top: 50%;
              // transform: translateY(-50%);
              right: -73px;
              z-index: 1000;
            }
          }

          &:nth-child(2n) {
            text-align: left;
            float: right;
            .title {
              &:before {
                left: -63px;
              }
              .title-text {
                left: -71px;
                top: 0;
              }
            }
            .body {
              top: 211px;
              left: -85px;
            }
          }
        }
        .title:hover {
          &:before {
            transform: scale(1.5);
            top: 30%;
            transition: all 0.2s ease-in-out;
          }
        }
      }
    }
  }
  .timeline::-webkit-scrollbar {
    width: 0;
  }
}
</style>
