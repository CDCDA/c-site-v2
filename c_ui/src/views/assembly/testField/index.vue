<!--
 * @Description: 切片列表
-->
<template>
  <div class="page-main testField-main">
    <div class="testField-header">
      <svg-icon
        iconName="commonSvg-实验数据"
        style="width: 30px; height: 30px; margin-right: 15px"
      />大模块测试
    </div>
    <div class="testField-center">
      <div class="testField-item" v-for="(item, i) in testFieldList" @click="totestField(item)">
        <c-image class="testField-item-cover" :src="item.meta.url" />
        <span class="testField-item-name">{{ item.meta.title }}</span>
        <span class="testField-item-divider"></span>
        <span class="testField-item-instoction no-wrap">{{ item.meta.introduction }}</span>
        <!-- <div class="instoction-cover">
          {{ item.introduction }}
        </div> -->
      </div>
    </div>
  </div>
  <router-view-dialog ref="routerDialog" :title="title"></router-view-dialog>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import routerViewDialog from '@/components/routerViewDialog/index.vue';
const router = useRouter();
const routerDialog = ref(null) as any;
const title = ref(null) as any;
const testFieldList = ref([]) as any;

function totestField(item: any) {
  title.value = item.meta.title;
  routerDialog.value.open();
  router.push({ name: item.name });
}

function close() {
  routerDialog.value.close();
}

onMounted(() => {
  testFieldList.value = (router.options.routes as any).find(
    (x: any) => x.name == 'testField'
  ).children;
});
</script>
<style lang="scss" scoped>
@include theme() {
  .testField-main {
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
    justify-content: start;
    .testField-header {
      height: 30px;
      width: calc(100% - 80px);
      text-align: left;
      font-size: 22px;
      font-weight: bold;
      padding: 12px 0 5px 0;
      margin: 25px auto 0 auto;
      display: flex;
    }
    .testField-center {
      display: flex;
      flex-wrap: wrap;
      justify-content: start;
      align-items: center;
      width: calc(100% - 60px);
      padding: 10px 0 30px 0;
      margin: 0 auto;
      .testField-item {
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
        color: get('font-color');
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-direction: column;
        .testField-item-cover {
          width: 100%;
          height: calc(100%);
          transition: transform 0.3s ease;
        }
        .testField-item-name {
          font-weight: bold;
          position: absolute;
          color: white;
          top: 15%;
          left: 25px;
          font-size: 1rem;
        }
        .testField-item-divider {
          width: 0%;
          left: 25px;
          position: absolute;
          top: 30%;
          height: 2px;
          background: white;
        }
        .testField-item-instoction {
          color: white;
          position: absolute;
          top: 35%;
          left: 25px;
          transform: translate3d(300%, 0, 0);
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
      .testField-item:hover {
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
        .testField-item-cover {
          transform: scale(1.1);
          filter: brightness(60%);
        }
        .testField-item-instoction {
          animation: instoction-in 0.3s forwards linear;
        }
        .testField-item-divider {
          animation: divider-in 0.3s forwards linear;
        }
      }
    }
  }
}
</style>
