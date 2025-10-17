<!--
 * @Description: 博客前后一个博客跳转组件
-->
<template>
  <div class="blog-pre-next" ref="blogPreNext">
    <div class="blog-pre" @click="toDetail(preBlogData)" v-if="preBlogData">
      <c-image class="image" :src="preBlogData.coverUrl" />
      <span class="date">
        <el-icon><Calendar /></el-icon>
        <span class="date-time">{{
          formatDate(new Date(preBlogData.createTime), 'YY-MM-dd')
        }}</span>
      </span>
      <span class="title">{{ preBlogData.blogTitle }}</span>
    </div>
    <div class="blog-pre" v-else>
      <c-image class="image" :src="startImg" />
      <span class="title">已经到第一篇了</span>
    </div>
    <div class="blog-next" @click="toDetail(nextBlogData)" v-if="nextBlogData">
      <c-image class="image" :src="nextBlogData.coverUrl" />
      <span class="date">
        <el-icon><Calendar /></el-icon>
        <span class="date-time">{{
          formatDate(new Date(nextBlogData.createTime), 'YY-MM-dd')
        }}</span>
      </span>
      <span class="title">{{ nextBlogData.blogTitle }}</span>
    </div>
    <div class="blog-next" v-else>
      <c-image class="image" :src="endImg" />
      <span class="title">已经到最后一篇了</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { getNeighborBlogs } from '@/api/blog';
import { formatDate } from '@/utils/date.ts';
import { autoClearTimer } from '@/utils/timer';
const props = defineProps(['blogId']);
const router = useRouter();
const blogPreNext = ref(null) as any;
const startImg = new URL('@/assets/images/snow-rail.jpg', import.meta.url).href;
const endImg = new URL('@/assets/images/feitu-bridge.jpg', import.meta.url).href;
const preBlogData = ref({}) as any;
const nextBlogData = ref({}) as any;
async function getPreNextBlog() {
  const { code, data } = (await getNeighborBlogs(props.blogId)) as any;
  if (code === 200) {
    nextBlogData.value = data.nextBlog;
    preBlogData.value = data.preBlog;
  }
}

function toDetail(data: any) {
  router.push({
    name: 'refresh'
  });

  autoClearTimer(() => {
    router.push({
      name: 'blogDisplay',
      query: { blogId: data.blogId }
    });
  }, 500);
}

watch(
  () => props.blogId,
  (val: any) => {
    if (!val) return;
    getPreNextBlog();
  },
  {
    deep: true
  }
);

onMounted(() => {
  // useLazyAppear(blogPreNext.value);
});
</script>
<style lang="scss" scoped>
@include theme() {
  .blog-pre-next {
    box-shadow: get('box-shadow');
    width: calc(100% - 40px);
    border-radius: 8px;
    height: 130px;
    margin: 10px 20px 20px 20px;
    display: flex;
    overflow: hidden;
    .blog-pre,
    .blog-next {
      cursor: pointer;
      width: 50%;
      height: 100%;
      background-repeat: no-repeat;
      object-fit: cover;
      background-size: 22% auto;
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      position: relative;
      color: white;
      span {
        color: white;
      }
      .date {
        display: flex;
        align-items: center;
        z-index: 10;
        .date-time {
          margin-left: 5px;
          font-size: 0.8rem;
        }
        margin-bottom: 10px;
      }
      .title {
        z-index: 10;
      }
      .image {
        width: 100%;
        height: 100%;
        position: absolute;
        filter: brightness(60%);
        transition: all 0.5s ease;
      }
    }
    .blog-pre:hover,
    .blog-next:hover {
      .image {
        transform: scale(1.1);
        filter: brightness(100%);
      }
    }
  }
}
</style>
