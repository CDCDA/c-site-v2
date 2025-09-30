<!--
 * @Description: 切片列表
-->
<template>
  <div class="page-main slice-main">
    <div class="slice-header">
      <svg-icon
        iconName="commonSvg-研磨"
        style="width: 30px; height: 30px; margin-right: 15px"
      />一些小组件
    </div>
    <div class="slice-center">
      <div class="slice-item" v-for="(item, i) in sliceList" @click="toSlice(item)">
        <c-image class="slice-item-cover" :src="item.meta.url" />
        <span class="slice-item-name">{{ item.meta.remark }}</span>
        <span class="slice-item-divider"></span>
        <span class="slice-item-instoction no-wrap">{{ item.meta.introduction }}</span>
        <!-- <div class="instoction-cover">
          {{ item.introduction }}
        </div> -->
      </div>
    </div>
  </div>
  <router-view-dialog ref="routerDialog" :title="title"></router-view-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import routerViewDialog from '@/components/routerViewDialog/index.vue';
const router = useRouter();
const routerDialog = ref(null as any);
const title = ref(null as any);
const sliceList = ref([] as any);

function toSlice(item: any) {
  title.value = item.meta.remark;
  routerDialog.value.open();
  router.push({ name: item.name });
}

function close() {
  routerDialog.value.close();
}

onMounted(() => {
  sliceList.value = (router.options.routes as any).find((x: any) => x.name == 'slice').children;
});
</script>
<style lang="scss" scoped>
@include theme() {
  .slice-main {
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
    .slice-header {
      height: 30px;
      width: calc(100% - 80px);
      text-align: left;
      font-size: 22px;
      font-weight: bold;
      padding: 12px 0 5px 0;
      margin: 25px auto 0 auto;
      display: flex;
    }
    .slice-center {
      display: flex;
      flex-wrap: wrap;
      justify-content: start;
      align-items: center;
      width: calc(100% - 60px);
      padding: 10px 0 30px 0;
      margin: 0 auto;
      .slice-item {
        animation: fade-in 0.5s linear forwards;
        margin: 10px 10px;
        cursor: pointer;
        width: calc(33.33% - 20px);
        aspect-ratio: 5/3.2;
        border-radius: 8px;
        overflow: hidden;
        position: relative;
        opacity: 0.9;
        box-shadow: get('box-shadow');
        background: get('back');
        position: relative;
        color: get('font-color');
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-direction: column;
        overflow: hidden;
        .slice-item-cover {
          width: 100%;
          height: calc(100%);
          object-fit: cover;
          object-position: 70%;
          background-repeat: no-repeat;
          transition: transform 0.3s ease;
        }
        .slice-item-name {
          font-weight: bold;
          position: absolute;
          color: white;
          top: 15%;
          left: 25px;
          font-size: 1rem;
        }
        .slice-item-divider {
          width: 0%;
          left: 25px;
          position: absolute;
          top: 30%;
          height: 2px;
          background: white;
        }
        .slice-item-instoction {
          color: white;
          position: absolute;
          top: 35%;
          left: 25px;
          transform: translate3d(500px, 0, 0);
        }

        // .instoction-cover {
        //   position: absolute;
        //   right: -150px;
        //   width: 100%;
        //   display: flex;
        //   align-items: center;
        //   justify-content: center;
        // }
      }
      .slice-item:hover {
        transform: scale(1.02);
        @keyframes instoction-in {
          0% {
            transform: translate3d(500px, 0, 0);
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
        .slice-item-cover {
          transform: scale(1.1);
          filter: brightness(60%);
        }
        .slice-item-instoction {
          animation: instoction-in 0.3s forwards linear;
        }
        .slice-item-divider {
          animation: divider-in 0.3s forwards linear;
        }
      }
    }
  }
}
</style>
