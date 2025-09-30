<!--
 * @Description: svg列表
-->
<template>
  <div class="page-main svg-main">
    <div class="svg-header">
      <svg-icon
        iconName="commonSvg-物质"
        style="width: 30px; height: 30px; margin-right: 15px"
      />svg列表
      <!-- <div><el-input v-model="searchText" :suffix-icon="Search"></el-input></div> -->
    </div>
    <div class="svg-center">
      <div class="svg-list-wrap" v-for="item in svgData">
        <div class="svg-list-header">
          <div class="title">
            <svg-icon class="icon-windmill" iconName="commonSvg-风车"></svg-icon
            >{{ `${item.label} [${item.list.length}]` }}
          </div>
          <!--          <div class="count">{{ item.list.length }}条</div>-->
        </div>
        <div class="svg-list">
          <div class="svg-item" v-for="svg in item.list" @click="getSvgCode(svg)">
            <svg-icon :iconName="svg.iconName" class="svg-item-icon" />
            <span class="svg-item-name">{{ svg.fileName }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElNotification } from 'element-plus';
import { copyText } from '@/utils/common';
import { Search } from '@element-plus/icons-vue';
const router = useRouter();
const searchText = ref('') as any;
const svgData = ref([
  {
    label: '关联网站svg',
    value: 'linkSvg',
    list: []
  },
  {
    label: '技术栈svg',
    value: 'techStackSvg',
    list: []
  },
  {
    label: '社交svg',
    value: 'socialSvg',
    list: []
  },
  {
    label: '音频svg',
    value: 'audioSvg',
    list: []
  },
  {
    label: '像素svg',
    value: 'pixelSvg',
    list: []
  },
  {
    label: '通用svg',
    value: 'commonSvg',
    list: []
  }
] as any);
function getSvgCode(item: any) {
  copyText(`<svg-icon iconName="commonSvg-${item.fileName}" />`);
  ElNotification.success('svg代码复制成功');
}

function initSvgData() {
  svgData.value.forEach((item: any) => {
    let svgList = [] as any;
    let files = [] as any;
    switch (item.value) {
      case 'audioSvg':
        files = (import.meta as any).glob(`@/assets/svg/audioSvg/*.svg`, { eager: true });
        break;
      case 'socialSvg':
        files = (import.meta as any).glob(`@/assets/svg/socialSvg/*.svg`, { eager: true });
        break;
      case 'linkSvg':
        files = (import.meta as any).glob(`@/assets/svg/linkSvg/*.svg`, { eager: true });
        break;
      case 'techStackSvg':
        files = (import.meta as any).glob(`@/assets/svg/techStackSvg/*.svg`, { eager: true });
        break;
      case 'pixelSvg':
        files = (import.meta as any).glob(`@/assets/svg/pixelSvg/*.svg`, { eager: true });
        break;
      case 'commonSvg':
        files = (import.meta as any).glob(`@/assets/svg/commonSvg/*.svg`, { eager: true });
        break;
      default:
        break;
    }
    Object.keys(files).forEach((name: any) => {
      const fileName = name.split('/').pop().replace('.svg', '');
      svgList.push({
        fileName: fileName,
        src: files[name].default,
        iconName: item.value + '-' + fileName
      });
    });
    item.list = svgList;
  });
  console.log(svgData.value);
}

onMounted(() => {
  initSvgData();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .svg-main {
    @keyframes fade-in {
      0% {
        opacity: 0;
        transform: translateY(50px);
      }
      100% {
        opacity: 1;
        transform: translateY(0px);
      }
    }
    @include flex-column;
    background: get('back');
    border-radius: 8px;
    justify-content: start;
    .svg-header {
      height: 30px;
      width: calc(100% - 80px);
      text-align: left;
      font-size: 1.1rem;
      font-weight: bold;
      padding: 12px 0 5px 0;
      margin: 25px auto 0 auto;
      display: flex;
    }
    .svg-center {
      width: calc(100% - 60px);
      padding: 10px 0 30px 0;
      margin: 0 auto;
      .svg-list-wrap:hover {
        .icon-windmill {
          transform: scale(1.02);
          animation: rotate 1s linear infinite;
        }
      }
      .svg-list-wrap {
        display: flex;
        flex-direction: column;
        .svg-list-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin: 8px 8px;
          .icon-windmill {
            color: #fb7061;
            font-size: 1.3rem;
            margin-right: 5px;
          }
        }
        .svg-list {
          display: flex;
          flex-wrap: wrap;
          justify-content: start;
          align-items: center;
        }
      }
      .svg-item {
        animation: fade-in 0.5s linear forwards;
        margin: 10px 10px;
        cursor: pointer;
        width: calc(10% - 20px);
        min-width: 80px;
        border-radius: 8px;
        overflow: hidden;
        position: relative;
        opacity: 0.9;
        box-shadow: get('box-shadow');
        background: get('back');
        color: get('font-color');
        display: flex;
        justify-content: start;
        align-items: center;
        flex-direction: column;

        .svg-item-name {
          font-size: 0.8rem;
          text-align: center;
          width: calc(100% - 10px);
          height: 30px;
          padding: 0 5px;
          margin-bottom: 10px;
          line-height: 30px;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
          color: get('font-color');
        }
        .svg-item-icon {
          margin: 15px auto 12px auto;
          font-size: 2rem;
        }
      }
      .svg-item:hover {
        transform: scale(1.02);
        @keyframes instoction-in {
          0% {
            transform: translate3d(300%, 0, 0);
          }
          100% {
            transform: translate3d(0%, 0, 0);
          }
        }
        @keyframes divider-in {
          0% {
            width: 0%;
          }
          100% {
            width: calc(100% - 50px);
          }
        }
        .svg-item-cover {
          transform: scale(1.1);
          filter: brightness(60%);
        }
        .svg-item-instoction {
          animation: instoction-in 0.3s forwards linear;
        }
        .svg-item-divider {
          animation: divider-in 0.3s forwards linear;
        }
      }
    }
  }
}
</style>
