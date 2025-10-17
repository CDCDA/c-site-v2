<template>
  <div class="drag-main">
    <!-- <el-button class="bt" @click="changeLayout()">切换</el-button> -->
    <div class="drag-tools">
      <el-tooltip
        :content="draggableOptions.lineVisible ? '关闭对齐线' : '开启对齐线'"
        placement="top"
      >
        <el-icon @click="draggableOptions.lineVisible = !draggableOptions.lineVisible"
          ><Grid
        /></el-icon>
      </el-tooltip>
      <!-- <el-tooltip content="恢复初始位置" placement="top">
        <el-icon @click="refresh(item)"><RefreshRight></RefreshRight></el-icon>
      </el-tooltip> -->
    </div>
    <DraggableContainer
      :referenceLineColor="'white'"
      class="draggableContainer"
      :referenceLineVisible="draggableOptions.lineVisible"
      v-if="isGrid"
    >
      <Vue3DraggableResizable
        v-for="item in draggableList"
        :initW="item.initW"
        :initH="item.initH"
        v-model:x="item.x"
        v-model:y="item.y"
        v-model:w="item.width"
        v-model:h="item.height"
        v-model:active="item.active"
        :draggable="!item.isLock"
        :resizable="!item.isLock"
        :parent="true"
        :class="item.type"
        @resize-end="resizeEndHandle(item)"
      >
        <component
          :is="item.type"
          :style="{ width: item.width, height: item.height }"
          v-if="!item.hidden"
        ></component>
        <div class="show-tools" @click="showTools(item)">
          <el-icon v-if="!item.toolsShow"><DArrowRight></DArrowRight></el-icon>
          <el-icon v-else><DArrowLeft></DArrowLeft></el-icon>
        </div>
        <div class="vdr-tools" v-if="item.toolsShow">
          <div class="vdr-tools-left">
            <el-tooltip :content="item.isLock ? '解锁' : '锁定'" placement="top">
              <el-icon v-if="item.isLock" @click="unLock(item)"><Lock></Lock></el-icon>
              <el-icon v-else @click="lock(item)"><Unlock></Unlock></el-icon>
            </el-tooltip>
            <el-tooltip content="恢复初始位置" placement="top">
              <el-icon @click="refresh(item)"><RefreshRight></RefreshRight></el-icon>
            </el-tooltip>
          </div>
          <div class="vdr-tools-right">
            <el-divider direction="vertical" />
            <el-tooltip content="上移1px" placement="top">
              <el-icon @click="move(item, 'top')"><CaretTop></CaretTop></el-icon>
            </el-tooltip>
            <el-tooltip content="下移1px" placement="top">
              <el-icon @click="move(item, 'bottom')"><CaretBottom></CaretBottom></el-icon>
            </el-tooltip>
            <el-tooltip content="左移1px" placement="top">
              <el-icon @click="move(item, 'left')"><CaretLeft></CaretLeft></el-icon>
            </el-tooltip>
            <el-tooltip content="右移1px" placement="top">
              <el-icon @click="move(item, 'right')"><CaretRight></CaretRight></el-icon>
            </el-tooltip>
          </div>
        </div>
      </Vue3DraggableResizable>
    </DraggableContainer>
    <!-- <grid-layout
      v-else
      :responsive="false"
      :layout.sync="layout"
      :col-num="70"
      :row-height="8"
      :is-draggable="true"
      :is-resizable="true"
      :is-mirrored="true"
      :style="{ height: '100%' }"
      :vertical-compact="false"
      :margin="[10, 10]"
      :use-css-transforms="true"
      ><grid-item
        v-for="item in layout"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :key="item.i"
      >
        <component
          :is="item.type"
          :style="{ width: item.w, height: item.h }"
          v-if="!item.hidden"
        ></component>
      </grid-item>
    </grid-layout> -->
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue';
import weather from './components/weather.vue';
import BlogTagCard from '@/views/blog/components/blogTagCard.vue';
import BlogTypeCard from '@/views/blog/components/blogTypeCard.vue';
import BlogUserCard from '@/views/blog/components/blogUserCard.vue';
import audioPlayer from './components/audioPlayer.vue';
import earth3d from './components/earth3d.vue';
import { DraggableContainer } from 'vue3-draggable-resizable';
import Vue3DraggableResizable from 'vue3-draggable-resizable';
import { autoClearTimer } from '@/utils/timer';
//default styles
import 'vue3-draggable-resizable/dist/Vue3DraggableResizable.css';

const isGrid = ref(true);
const draggableList = reactive([
  {
    name: '天气预报',
    i: '1',
    isLock: false,
    type: weather,
    active: false,
    w: 6,
    h: 10,
    width: 300,
    height: 400,
    minWidth: 300,
    minHeight: 400,
    x: 0,
    y: 0,
    initW: 300,
    initH: 400,
    initX: 0,
    initY: 0,
    toolsShow: false
  },
  {
    name: '标签云',
    i: '2',
    active: false,
    isLock: false,
    type: BlogTagCard,
    w: 6,
    h: 10,
    width: 300,
    height: 400,
    minWidth: 300,
    minHeight: 400,
    x: 185,
    y: 0,
    initW: 300,
    initH: 400,
    initX: 185,
    initY: 0,
    toolsShow: false
  },
  {
    name: '分类',
    i: '3',
    active: false,
    isLock: false,
    type: BlogTypeCard,
    w: 6,
    h: 10,
    width: 300,
    height: 400,
    minWidth: 300,
    minHeight: 400,
    x: 185,
    y: 245,
    initW: 300,
    initH: 400,
    initX: 185,
    initY: 245,
    toolsShow: false
  },
  {
    name: '用户信息',
    i: '4',
    type: BlogUserCard,
    active: false,
    isLock: false,
    w: 6,
    h: 10,
    width: 300,
    height: 400,
    minWidth: 300,
    minHeight: 400,
    x: 0,
    y: 245,
    initW: 300,
    initH: 400,
    initX: 0,
    initY: 245,
    toolsShow: false
  }
  // {
  //   name: '3d地球',
  //   i: '5',
  //   type: earth3d,
  //   active: false,
  //   isLock: false,
  //   reload: true,
  //   w: 10,
  //   h: 10,
  //   width: 400,
  //   height: 400,
  //   minWidth: 300,
  //   minHeight: 300,
  //   x: 370,
  //   y: 0,
  //   initW: 400,
  //   initH: 400,
  //   initX: 473,
  //   initY: 0,
  //   toolsShow: true
  // },
  // {
  //   name: '音乐播放器',
  //   i: '6',
  //   type: audioPlayer,
  //   active: false,
  //   isLock: false,
  //   w: 12,
  //   h: 16,
  //   width: 300,
  //   height: 50,
  //   // minWidth: 300,
  //   // minHeight: 400,
  //   x: 775,
  //   y: 0,
  //   initW: 300,
  //   initH: 50,
  //   initX: 873,
  //   initY: 0,
  //   toolsShow: false
  // }
]) as any;
const draggableOptions = reactive({
  lineVisible: false
});
// const layout = reactive([
//   {
//     name: '天气预报',
//     i: '1',
//     isLock: false,
//     type: weather,
//     active: false,
//     w: 12,
//     h: 16,
//     width: 300,
//     height: 400,
//     minWidth: 300,
//     minHeight: 400,
//     x: 0,
//     y: 0,
//     initW: 300,
//     initH: 400,
//     initX: 0,
//     initY: 0,
//     toolsShow: false
//   },
//   {
//     name: '标签云',
//     i: '2',
//     active: false,
//     isLock: false,
//     type: BlogTags,
//     w: 12,
//     h: 16,
//     width: 300,
//     height: 400,
//     minWidth: 300,
//     minHeight: 400,
//     x: 12,
//     y: 0,
//     initW: 300,
//     initH: 400,
//     initX: 185,
//     initY: 0,
//     toolsShow: false
//   },
//   {
//     name: '分类',
//     i: '3',
//     active: false,
//     isLock: false,
//     type: BlogType,
//     w: 12,
//     h: 16,
//     width: 300,
//     height: 400,
//     minWidth: 300,
//     minHeight: 400,
//     x: 24,
//     y: 0,
//     initW: 300,
//     initH: 400,
//     initX: 185,
//     initY: 245,
//     toolsShow: false
//   },
//   {
//     name: '用户信息',
//     i: '4',
//     type: BlogUser,
//     active: false,
//     isLock: false,
//     w: 12,
//     h: 16,
//     width: 300,
//     height: 400,
//     minWidth: 300,
//     minHeight: 400,
//     x: 0,
//     y: 16,
//     initW: 300,
//     initH: 400,
//     initX: 0,
//     initY: 245,
//     toolsShow: false
//   },
//   {
//     name: '3d地球',
//     i: '5',
//     type: earth3d,
//     active: false,
//     isLock: false,
//     reload: true,
//     w: 20,
//     h: 20,
//     width: 400,
//     height: 400,
//     minWidth: 300,
//     minHeight: 300,
//     x: 36,
//     y: 0,
//     initW: 400,
//     initH: 400,
//     initX: 473,
//     initY: 0,
//     toolsShow: true
//   },
//   {
//     name: '音乐播放器',
//     i: '6',
//     type: audioPlayer,
//     active: false,
//     isLock: false,
//     w: 12,
//     h: 16,
//     width: 300,
//     height: 400,
//     minWidth: 300,
//     minHeight: 400,
//     x: 0,
//     y: 16,
//     initW: 300,
//     initH: 400,
//     initX: 0,
//     initY: 245,
//     toolsShow: false
//   }
// ]) as any;

function showTools(item: any) {
  item.toolsShow = !item.toolsShow;
  draggableList.forEach((element: any) => {
    if (element.i != item.i) {
      element.toolsShow = false;
    }
  });
}

function lock(item: any) {
  item.isLock = true;
}

function unLock(item: any) {
  item.isLock = false;
}

function move(item: any, direction: String) {
  switch (direction) {
    case 'top':
      item.y = item.y - 1;
      break;
    case 'bottom':
      item.y = item.y + 1;
      break;
    case 'left':
      item.x = item.x - 1;
      break;
    case 'right':
      item.x = item.x + 1;
      break;
    default:
      break;
  }
}

function resizeEndHandle(item: any) {
  if (!item.reload) return;
  item.hidden = true;
  autoClearTimer(() => {
    item.hidden = false;
  }, 500);
}

function refresh(item: any) {
  item.x = item.initX;
  item.y = item.initY;
}

function changeLayout() {
  isGrid.value = !isGrid.value;
  const theme = (document.getElementById('app-main') as any).getAttribute('class');
  if (theme === 'theme-dark')
    (document.getElementById('app-main') as any).setAttribute('class', 'theme-light');
  else {
    (document.getElementById('app-main') as any).setAttribute('class', 'theme-dark');
  }
}
</script>
<style lang="scss" scoped>
@include theme() {
  .drag-main {
    display: flex;
    height: calc(100%);
    width: calc(100%);
    justify-content: space-between;
    flex-direction: column;
    align-content: space-around;
    .vdr-container {
      & > div {
        margin: 0 !important;
      }
      display: flex;
      justify-content: center;
      border: none;
      @include full();
      // box-shadow: get('box-shadow');
      // background: get('back');
      border-radius: 4px;
    }
    .draggableContainer {
      margin: 0 20px;
      width: calc(100% - 40px) !important;
    }

    .vdr-handle-tl {
      background:
    /*上左*/ linear-gradient(to top, #ffd04b, #ffd04b) left top no-repeat,
        /*左上*/ linear-gradient(to right, #ffd04b, #ffd04b) left top no-repeat;
    }

    .vdr-handle-tr {
      background:
    /*右上*/ linear-gradient(to left, #ffd04b, #ffd04b) right top no-repeat,
        /*上右*/ linear-gradient(to bottom, #ffd04b, #ffd04b) right top no-repeat;
    }

    .vdr-handle-bl {
      background:
    /*左下*/ linear-gradient(to left, #ffd04b, #ffd04b) left bottom no-repeat,
        /*下左*/ linear-gradient(to bottom, #ffd04b, #ffd04b) left bottom no-repeat;
    }

    .vdr-handle-br {
      background:
    /*右下*/ linear-gradient(to top, #ffd04b, #ffd04b) right bottom no-repeat,
        /*下右*/ linear-gradient(to left, #ffd04b, #ffd04b) right bottom no-repeat;
    }

    .handle {
      width: 12px;
      height: 12px;
      border: none;
      background-size: 2px 12px, 12px 2px, 2px 12px, 12px 2px;
    }
    .vdr-handle-tm {
      width: 12px;
      height: 5px;
      background: transparent;
      border-top: 2px solid #ffd04b;
    }

    .vdr-handle-bm {
      width: 12px;
      height: 5px;
      background: transparent;
      border-bottom: 2px solid #ffd04b;
    }
    .vdr-handle-ml {
      width: 5px;
      height: 12px;
      background: transparent;
      border-left: 2px solid #ffd04b;
    }
    .vdr-handle-mr {
      width: 5px;
      height: 12px;
      background: transparent;
      border-right: 2px solid #ffd04b;
    }
    .drag-tools {
      color: get('font-color');
      border-radius: 5px;
      // backdrop-filter: blur(20px);
      background: get('back');
      box-shadow: 0 1px 1px 1px #000000;
      box-shadow: get('box-shadow');
      height: 40px;
      margin: 5px 20px 10px 20px;
      display: flex;
      justify-content: start;
      align-items: center;
      .el-icon {
        border: 1px solid white;
        border-radius: 2px;
        margin-left: 10px;
      }
    }
    .vdr-tools {
      z-index: 999;
      height: 25px;
      width: calc(100% - 20px);
      margin-top: 5px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 10px;
      backdrop-filter: blur(20px);
      background: get('back');
      box-shadow: get('box-shadow');
      position: absolute;
      bottom: -31px;
      .el-icon {
        color: get('font-color');
        margin: 0 3px 2px 3px;
      }
      .vdr-tools-left,
      .vdr-tools-right {
        display: flex;
        align-items: center;
        justify-content: flex-start;
      }
    }
    .show-tools {
      position: absolute;
      bottom: 0;
      backdrop-filter: none;
      .el-icon {
        visibility: hidden;
        margin: 0 10px;
        transform: rotate(90deg);
        color: get('font-color');
      }
    }
    .show-tools:hover {
      backdrop-filter: blur(20px);
      .el-icon {
        visibility: visible;
        margin: 5px 10px;
        transform: rotate(90deg);
        color: get('font-color');
      }
    }
    .vue-grid-item {
      box-shadow: get('box-shadow');
    }
    .bt {
      background: get('bk');
    }
  }
}
</style>
