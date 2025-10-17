<!--
 * @Description: 随笔
-->
<template>
  <div class="page-main essay-main">
    <div class="essay-header animated bounceInDown">
      <video
        :src="'/video/video_2000212665.mp4'"
        class="essay-header-video"
        autoplay
        loop
        muted
      ></video>
      <span class="essay-header-center"
        >喝一壶清茶，写几行小篆，<br />看一剪流云，梦一回江南。愿与草木，随遇而安</span
      >
      <span class="essay-header-bottom">闲情逸致</span>
      <!-- <div class="essay-header-add">新增+</div> -->
    </div>
    <!-- <div class="essay-center"> -->
    <grid-layout
      class="essay-center"
      :responsive="false"
      :layout.sync="essayList"
      :col-num="3"
      :autoSize="true"
      :row-height="1"
      :is-draggable="true"
      :is-resizable="false"
      :is-mirrored="false"
      :vertical-compact="false"
      :margin="[15, 10]"
      :use-css-transforms="true"
      ref="grid"
    >
      <grid-item
        v-for="(item, i) in essayList"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :key="item.i"
      >
        <div class="essay-item c-left" :class="`essay-${item.i}`">
          <div class="essay-item-user">
            <c-image class="essay-item-user-avatar" :src="userData.avatar"></c-image>
            <div class="essay-item-info">
              <span class="essay-item-user-nickName">{{ userData.nickName }}</span>
              <span class="essay-item-date">
                {{ sformatDate(new Date(item.createTime)) }}
              </span>
            </div>
          </div>
          <span class="essay-item-content">{{ item.content }}</span>
          <div class="img-list" v-if="item.images && item.images.length > 0">
            <c-image class="img-list-item" v-for="img in item.images" :src="img" />
          </div>
          <div class="c-dotted-line"></div>
          <div class="essay-item-footer">
            <div class="essay-item-tag-list">
              <span class="essay-item-tag" v-for="tag in item.tags">{{ `# ${tag.tagName}` }}</span>
            </div>
            <svg-icon iconName="commonSvg-评论" @click="toComment(item)"></svg-icon>
          </div>
        </div>
      </grid-item>
    </grid-layout>
    <div class="tip">仅展示最近20条</div>
    <essayComment
      :visible="commentOpen"
      @close="close"
      :essay-data="selectEssay"
      :user-data="userData"
    />
  </div>
</template>

<script setup lang="ts" name="essay">
import { ref, onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer';
import { getUserById } from '@/api/system/user';
import { pageEssays } from '@/api/essay.ts';
import useUserStore from '@/store/modules/user';
import { sformatDate } from '@/utils/date.ts';
import essayComment from './components/essayComment.vue';
import { ElNotification } from 'element-plus';
const imgLoading = ref('rotate') as any;
const userStore = useUserStore();
const essayList = ref([]) as any;
const commentOpen = ref(false);
const grid = ref(null) as any;
const selectEssay = ref({}) as any;
function close() {
  commentOpen.value = false;
}

function handle() {
  imgLoading.value = false;
}

/**
 * @description: 动画执行时间
 * @param {*} i
 * @return {*}
 */

/**
 * @description: 获取随笔
 * @return {*}
 */
async function getEssayList() {
  const { code, rows, total } = (await pageEssays({ pageNum: 1, pageSize: 20 })) as any;
  if (code === 200) {
    essayList.value = [];
    let length = rows.length;
    let index = 0;
    for (let i = 0; i < length / 3; i++) {
      for (let j = 0; j < 3; j++) {
        if (rows[index])
          essayList.value.push({
            x: j,
            y: i,
            w: 1,
            h: 10,
            i: rows[index].id,
            images: rows[index].images,
            createTime: rows[index].createTime,
            content: rows[index].content,
            userId: rows[index].userId,
            tags: rows[index].tags ? JSON.parse(rows[index].tags) : []
          });
        index++;
      }
    }

    let column = 3;
    let colHeightList = [] as any;
    // 计算随笔初始位置
    autoClearTimer(() => {
      essayList.value.forEach((e: any, i: any) => {
        let localIndex = i % column;
        let el = document.querySelector(`.essay-${e.i}`) as any;
        let height = el.offsetHeight / 10;
        // 第一行
        if (i < column) {
          e.x = i;
          e.y = 0;
          e.h = height;
          // 高度数据更新
          if (!colHeightList[localIndex]) colHeightList[localIndex] = 0;
          colHeightList[localIndex] = colHeightList[localIndex] + height;
        } else {
          // 最低的高度，先默认为第一列高度
          let current = colHeightList[0];
          // 最低的列，先默认为第一个
          let col = 0;
          // 循环每一列进行比较
          colHeightList.forEach((h: any, i: any) => {
            if (h < current) {
              current = h;
              col = i;
            }
          });
          e.y = current;
          e.h = height;
          e.x = col;
          // 更新列高度数组
          colHeightList[col] = current + height;
        }
      });
      grid.value.layoutUpdate();
    }, 100);
  }
}

const userData = ref({}) as any;

function toComment(item: any) {
  // ElNotification.info('施工中...');
  selectEssay.value = item;
  commentOpen.value = true;
}

async function getUserData() {
  const { code, data } = (await getUserById(userStore.userId)) as any;
  if (code === 200) {
    userData.value = data;
  }
}

onMounted(() => {
  getUserData();
  getEssayList();
});
</script>
<style lang="scss">
@include theme() {
  .essay-main {
    @include flex-column;
    justify-content: start;
    background: transparent !important;
    backdrop-filter: none !important;
    // max-width: 1100px !important;
    .essay-header {
      height: 40vh;
      width: 100%;
      border-radius: 12px;
      box-shadow: get('box-shadow');
      position: relative;
      .essay-header-video {
        opacity: 0.9;
        height: 100%;
        width: 100%;
        border-radius: 12px;
        object-fit: cover;
      }
      margin-bottom: 30px;
      color: white;
      .essay-header-top {
        position: absolute;
        top: 10px;
        left: 15px;
      }
      .essay-header-center {
        position: absolute;
        top: 47px;
        left: 24px;
        font-size: 35px;
        font-weight: bold;
        text-align: left;
      }
      .essay-header-bottom {
        position: absolute;
        bottom: 14px;
        left: 16px;
      }
      .essay-header-add {
        width: 90px;
        height: 35px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 12px;
        position: absolute;
        right: 10px;
        bottom: 10px;
        cursor: pointer;
        background: #d3d3d3;
        color: black;
      }
    }
    .essay-center {
      width: calc(100% + 30px);
      min-height: 100vh;
      display: table;
      .essay-item {
        transition: all 0.2s ease-in-out;
        border-radius: 6px;
        position: relative;
        padding: 12px 11px 10px 12px;
        text-align: left;
        // box-shadow: get('box-shadow');
        background: get('back');
        color: get('font-color');
        .essay-item-user {
          display: flex;
          margin-bottom: 12px;
          .essay-item-user-avatar {
            width: 47px;
            height: 45px;
            border-radius: 6px;
          }
          .essay-item-info {
            width: calc(100% - 50px);
            padding: 3px 10px;
            @include flex-column;
            justify-content: space-between;
            .essay-item-user-nickName,
            .essay-item-date {
              text-align: left;
              width: 100%;
            }
            .essay-item-user-nickName {
              font-size: 16px;
              font-weight: 400;
              color: #6dbdc3;
            }
            .essay-item-date {
              font-size: 12px;
              color: #969394;
            }
          }
        }
        .essay-item-content {
          white-space: pre-wrap;
        }
        .img-list {
          overflow-x: auto;
          width: 100%;
          @include flex;
          min-height: 40px;
          flex-wrap: wrap;
          justify-content: start;
          margin: 12px 0;
          .img-list-item {
            // height: 100px;
            width: calc(33.33% - 10px);
            aspect-ratio: 3/2;
            margin: 5px;
            border-radius: 5px;
          }
        }
        .essay-item-footer {
          display: flex;
          justify-content: space-between;
          .essay-item-tag-list {
            width: calc(100% - 40px);
            display: flex;
            flex-wrap: nowrap;
            .essay-item-tag {
              margin-right: 10px;
              font-size: 14px;
              padding: 2px 5px;
              color: #999;
              background: #f2f2f2;
              border-radius: 4px;
            }
          }
          .svg-icon {
            width: 20px;
            height: 20px;
            cursor: pointer;
          }
        }
      }
      .essay-item:hover {
        transform: scale(1.02);
      }
    }
  }
}
.vue-grid-item {
  // height: auto !important;
  // width: calc(33% - 10px) !important;
}
</style>
