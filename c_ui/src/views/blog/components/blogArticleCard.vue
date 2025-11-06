<!--
 * @Description: 博客右侧文章列表
-->
<template>
  <div class="blog-article">
    <template v-for="(blogData, index) in blogList" :key="index">
      <div class="blog-article-item" @click="viewBlog(blogData)">
        <!-- <el-image :src="url" class="article-image"></el-image> -->
        <div class="article-item-right">
          <span class="article-title">{{ blogData.blogTitle }}</span>
          <span class="article-type">{{ blogData.blogType }}</span>
          <span class="article-date">{{ blogData.createTime }}</span>
        </div>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref } from 'vue';
import { pageBlogs } from '@/api/blog';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
const router = useRouter();
const blogList = ref([
  {
    blogTitle: 'AAAD',
    blogType: 'as',
    updateTime: '2023-6-14'
  },
  {
    blogTitle: 'AAAD',
    blogType: 'as',
    updateTime: '2023-6-14'
  },
  {
    blogTitle: 'AAAD',
    blogType: 'as',
    updateTime: '2023-6-14'
  }
]) as any;

function viewBlog(blogData: any) {
  router.push({ name: 'BlogDisplay', query: { blogId: blogData.blogId } });
}

/**
 * @description:获取推荐
 * @return {*}
 */
async function getBlogList() {
  const params = {
    pageNum: 1,
    pageSize: 5
  };
  const { code, rows, total } = (await pageBlogs(params)) as any;
  if (code === 200) {
    blogList.value = rows;
  }
}

onMounted(() => {
  getBlogList();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .blog-article {
    border-radius: 5px;
    // box-shadow: get('box-shadow');
    color: get('font-color');
  }

  .blog-article-item {
    text-align: left;
    padding: 10px;
    border-radius: 5px;
    background: get('back');
    color: get('font-color');
    display: flex;
    box-shadow: get('box-shadow');
    justify-content: space-between;
    /* border: 1px solid; */
    margin: 5px 0;
    height: 60px;
    background: get('back');
  }
  .blog-article-item:hover {
    background: get('back');
    width: calc(100% - 16px);
    height: 60px;
    margin-left: -2px;
  }
}

.article-image {
  width: 40%;
  height: auto;
  margin-right: 10px;
}
.article-date {
  font-size: 11px;
  display: block;
}
.article-title {
  font-size: 12px;
  display: block;
}
.article-type {
  font-size: 12px;
  display: block;
}
.article-item-right {
  margin-left: 5px;
  width: calc(100% - 10px);
  span {
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
    overflow: hidden;
    text-align: left;
    height: 33%;
  }
  .article-date {
    color: #a29b9b;
    font-size: 12px;
  }
  .article-title {
    font-size: 0.8rem;
    margin-bottom: 5px;
  }
  .article-type {
    color: #a29b9b;
    font-size: 12px;
  }
}
</style>
