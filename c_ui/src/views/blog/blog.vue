<!--
 * @Description: 
 * @Author: cyd 1205489124@qq.com
 * @Date: 2023-06-26 16:56:13
 * @LastEditTime: 2023-10-20 14:10:51
-->
<template>
  <div class="blog-main-container page-main">
    <div class="blog-left bounceInRight animated">
      <blog-user class="blog-user"></blog-user>
      <blog-type class="blog-type"></blog-type>
    </div>
    <div class="blog-main bounceInLeft animated">
      <BlogNews class="blog-news"></BlogNews>
    </div>
    <div class="blog-right bounceInLeft animated">
      <blogArticle></blogArticle>
      <BlogTags></BlogTags>
    </div>
    <!-- <blog-float-item></blog-float-item> -->
  </div>
</template>

<script setup lang="ts">
import BlogEditor from './blogEditor.vue';
import { ref } from 'vue';
import BlogUser from './components/blogUser.vue';
import BlogType from './components/blogType.vue';
import BlogNews from './components/blogNews.vue';
import blogArticle from './components/blogArticle.vue';
import BlogTags from './components/blogTags.vue';
// import BlogFloatItem from './components/BlogFloatItem.vue';
// import { getBlogByUserId } from "@/api/blog";
const blogList = ref([]) as any;

function getBlogData() {
  // getBlogByUserId({ userId: this.$store.state.userId }).then(res => {
  //   res.data ? (this.blogList = res.data) : '';
  // });
}
function toBlogEdit() {
  // this.$router.push({
  //   path: '/BlogEditor',
  //   query: { blogData: this.blogData }
  // });
}
</script>
<style lang="scss">
@keyframes rotate {
  100% {
    transform: rotate(1turn);
  }
}
@include theme() {
  .blog-main-container {
    justify-content: center;
    overflow: auto;
    display: flex;
    height: 100%;
    .blog-latest-articles,
    .blog-user,
    .blog-type,
    .blog-tags,
    .blog-news-down {
      // background: rgba(29, 32, 33, 0.6);
      background: get('back');
      border-radius: 10px;
      box-shadow: get('box-shadow');
      color: rgba(255, 255, 255, 0.6);
    }
    .blog-main,
    .blog-right {
      border-radius: 10px;
    }
    .blog-left {
      margin: 10px 0 30px 12%;
      width: 200px;
    }
    .blog-main {
      margin: 10px 30px 30px 30px;
      width: 500px;
    }
    .blog-right {
      margin: 10px 12% 30px 0;
      width: 225px;
      // min-width: 250px;
      // max-width: 350px;
    }
    .blog-type,
    .blog-tags {
      margin-top: 20px;
      height: auto;
    }
    .blog-news-down {
      margin-top: 25px;
    }
  }
}
</style>
