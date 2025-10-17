<!--
 * @Description:  博客分类
-->
<template>
  <div class="blog-type page-main">
    <div class="type-list">
      <div class="type-item-wrap bounceInDown" :class="getAnimateTime()" v-for="item in typeList">
        <HoverTilt class="type-item" @click="toTypePage(item)">
          <template #default>
            <el-image :src="item.coverUrl">
              <template #default="scope">
                <c-image :src="scope.row.coverUrl" />
              </template>
            </el-image>
            <div class="type-count">{{ item.total }}<span>篇</span></div>
            <div class="type-info">
              <h1>{{ item.typeName }}</h1>
              <p>{{ item.intro }}</p>
            </div>
          </template>
        </HoverTilt>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import HoverTilt from '@/components/hoverTilt/index.vue';
import { onMounted, ref } from 'vue';
import { listTypesWithStats } from '@/api/type';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import { getAnimateTime } from '@/utils/animate.ts';
const router = useRouter();
const userStore = useUserStore();
const typeList = ref([]) as any;

async function getTypeList() {
  const { code, data } = (await listTypesWithStats({})) as any;
  if (code === 200) {
    typeList.value = data.list;
  }
}

function toTypePage(item: any) {
  router.push({ name: 'blogTypePage', query: { typeId: item.typeId } });
}

onMounted(() => {
  getTypeList();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .blog-type.page-main {
    display: flex;
    background: get('back');
    box-shadow: get('box-shadow');
    border-radius: 8px;
    transform-style: preserve-3d;
    .type-list {
      width: calc(100% - 40px);
      padding: 20px 20px;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      margin: 55px;
    }
    .type-item {
      width: 320px;
      height: 320px;
      overflow: hidden;
      box-shadow: rgba(0, 0, 0, 0.66) 0 30px 60px 0, inset #333 0 0 0 5px,
        inset rgba(255, 255, 255, 0.4) 0 0 0 6px;
      margin: 10px;
      cursor: pointer;
      border-radius: 12px;
      position: relative;
      .el-image {
        filter: brightness(0.7);
        opacity: 0.9;
        position: absolute;
        top: -30px;
        left: -30px;
        width: 120%;
        height: 120%;
        padding: 20px;
        transition: 0.8s cubic-bezier(0.445, 0.05, 0.55, 0.95),
          opacity 4s 0.8s cubic-bezier(0.445, 0.05, 0.55, 0.95);
        pointer-events: none;
        :deep(.el-image__inner) {
          background-repeat: no-repeat;
          background-position: left 28%;
          object-fit: cover;
        }
      }
      .type-count {
        padding: 15px 18px;
        position: absolute;
        color: #fff;
        top: 0;
        right: -70px;
        font-size: 1.25rem;
        font-weight: 700;
        text-shadow: rgba(0, 0, 0, 0.1) 0 10px 10px;
        transition: 0.6s 0.2s cubic-bezier(0.215, 0.61, 0.355, 1);
        span {
          margin-left: 5px;
          font-size: 0.9rem;
          font-weight: 700;
        }
      }
      .type-info {
        padding: 20px;
        position: absolute;
        bottom: 0;
        color: #fff;
        transform: translateY(42%);
        transition: 0.6s 0.2s cubic-bezier(0.215, 0.61, 0.355, 1);
        h1 {
          font-size: 1.7rem;
          font-weight: 700;
          text-shadow: rgba(0, 0, 0, 0.1) 0 10px 10px;
          padding-left: 0;
          text-align: left;
        }
        p {
          text-align: left;
          text-shadow: black 0 0.2px 0.5px;
          transition: 0.6s 0.6s cubic-bezier(0.215, 0.61, 0.355, 1);
        }
      }
      .type-info:after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        z-index: 0;
        width: 100%;
        height: 100%;
        background-image: linear-gradient(to bottom, transparent 0%, rgba(0, 0, 0, 0.6) 100%);
        background-blend-mode: overlay;
        opacity: 0;
        transform: translateY(100%);
        transition: 5s 1s cubic-bezier(0.445, 0.05, 0.55, 0.95);
      }
    }
    .type-item:hover {
      .el-image {
        opacity: 1;
        filter: brightness(1);
        transition: 0.8s cubic-bezier(0.445, 0.05, 0.55, 0.95);
      }
      .type-info {
        // transition: 1s cubic-bezier(0.23, 1, 0.32, 1);
        opacity: 1;
        transform: translateY(16%);
      }
      .type-count {
        right: 0;
      }
    }
  }
}
</style>
