<!--
 * @Description:  时间轴
-->
<template>
  <div class="timeline">
    <div class="timeline-main">
      <div class="entries">
        <div class="entry">
          <div class="title">2011</div>
        </div>
        <div class="entry">
          <div class="title">2012</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
</script>
<style lang="scss" scoped>
.timeline {
  width: 100%;
  height: 100%;
  overflow-x: auto;
  .timeline-main {
    width: 2000px;
    height: 100%;
    background-color: #1d1d1d;
    position: relative;
    &:before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      bottom: 0;
      transform: translateY(50%);
      width: 100%;
      background-color: #fff;
      height: 4px;
    }
    .entries {
      width: calc(100% - 80px);
      margin: auto;
      position: relative;
      top: 2px;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      .entry {
        width: calc(50% - 80px);
        float: left;
        padding: 20px;
        clear: both;
        text-align: right;
        &:not(:first-child) {
          // margin-top: -60px;
        }
        .title {
          font-size: 32px;
          margin-bottom: 12px;
          position: relative;
          color: #fff;
          &:before {
            content: '';
            position: absolute;
            width: 8px;
            height: 8px;
            border: 4px solid #ffffff;
            background-color: #1d1d1d;
            border-radius: 100%;
            top: 50%;
            /* transform: translateY(-50%); */
            right: 0;
            z-index: 1000;
          }
          &.big:before {
            width: 24px;
            height: 24px;
            transform: translate(8px, -50%);
          }
        }
        &:nth-child(2n) {
          text-align: left;
          float: right;
          .title {
            &:before {
              // left: -63px;
            }
            &.big:before {
              transform: translate(-8px, -50%);
            }
          }
        }
      }
      .title:hover {
        &:before {
          transform: scale(1.5);
        }
      }
    }
  }
}
</style>
