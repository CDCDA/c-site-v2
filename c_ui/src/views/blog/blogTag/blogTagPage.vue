<!--
 * @Description:  博客分类
-->
<template>
  <div class="blog-tag-page page-main c-left">
    <div class="header-tag-list">
      <div
        class="header-tag-item"
        :class="item.isActive ? 'is-active' : ''"
        v-for="item in tagList"
        @click="getList(item)"
      >
        <span class="tag-item-prefix" v-if="item.tagId">#</span>
        <span class="tag-item-content">{{ item.tagName }}</span>
        <span class="tag-item-count">{{ item.total }}</span>
      </div>
    </div>
    <div class="c-divider" style="margin: 15px 32px 6px 32px; width: calc(100% - 64px)"></div>
    <div class="blog-list-wrap">
      <div
        v-if="loading"
        class="blog-list-loading"
        v-cLoading="loading"
        :class="loading ? 'blog-list-loading-active' : ''"
      />
      <div class="blog-list" :class="loading ? '' : ' slide-in'">
        <div class="blog-item" v-for="(item, i) in blogList" @click="toDetail(item)">
          <c-image class="blog-item-left-img" :src="item.coverUrl" />
          <div class="blog-item-right">
            <div class="blog-title no-wrap">{{ item.blogTitle }}</div>
            <div class="tag-list">
              <div class="tag-item" v-for="tag in item.tags">
                <span class="tag-item-pretend">#</span>
                <span class="tag-item-text">{{ tag.tagName }}</span>
              </div>
            </div>
            <div class="blog-time">
              <el-icon><Clock /></el-icon>{{ item.createTime }}
            </div>
          </div>
          <span class="blog-sort-item-index">{{ i }}</span>
        </div>
      </div>
    </div>
    <Pagination
      v-model:page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :on-page-change="getBlogList"
      :showSizes="true"
      :pageSizeList="[10, 20, 30]"
      :on-page-size-change="getBlogList"
      class="pagi page-content"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { pageTags } from '@/api/tag';
import { pageBlogs } from '@/api/blog';
import { ElMessage } from 'element-plus';
import useUserStore from '@/store/modules/user';
import Pagination from '@/components/pagination/index.vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const userStore = useUserStore();
const loading = ref(false as any);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  tagId: null
} as any);
const total = ref(100 as any);
const tagList = ref([] as any);
const blogList = deleteDictTypes;
const isShowView = ref([] as any);

async function getTagList() {
  const { code, msg, rows, total } = (await pageTags({})) as any;
  if (code === 200 && data) {
    tagList.value = rows;

    tagList.value.forEach((e: any) => {
      if (e.tagId === queryParams.value.tagId) e.isActive = true;
    });
    getBlogList();
  }
}

function getList(item: any) {
  if (item) {
    tagList.value.forEach((tag: any) => {
      tag.isActive = false;
    });
    item.isActive = true;
  }
  queryParams.value.pageNum = 1;
  queryParams.value.tagId = item.tagId;
  getBlogList();
}

async function getBlogList() {
  loading.value = 'rotate';
  const { code, msg, rows, total } = await pageBlogs(queryParams.value);
  if (code === 200) {
    blogList.value = rows;
    blogList.value.forEach((e: any) => {
      e.tags.length > 5 ? (e.tags.length = 5) : '';
    });
    total.value = data.total;
    if (tagList.value[0].tagName !== '全部') {
      tagList.value.unshift({
        tagName: '全部',
        isActive: false,
        total: total.value
      });
    }
    tagList.value[0].total = total;
    loading.value = false;
  } else {
    ElMessage.error('博客数据获取失败', msg);
  }
}

// 博客详情
function toDetail(item: any) {
  router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
}

onMounted(() => {
  queryParams.value.tagId = (router as any).currentRoute._value.query.tagId;
  console.log('AAA', (router as any).currentRoute);
  getTagList();
});
</script>
<style lang="scss" scoped>
@include theme() {
  @keyframes fade-out {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }
  .blog-tag-page.page-main {
    justify-content: start;
    display: flex;
    flex-direction: column;
    position: relative;
    align-items: center;
    animation: fade-out 0.5s forwards linear;
    transition: all 0.5s ease-in-out;
    .pagi {
      margin: 6px 7px 15px 7px;
      width: calc(100% - 66px) !important;
    }
    .page-right {
      width: 300px;
      margin-left: 20px;
    }
    .header {
      font-weight: bold;
    }
    .header-tag-list {
      @include flex;
      justify-content: start;
      flex-wrap: wrap;
      width: calc(100% - 50px);
      margin: 25px 25px 0 25px;
      .header-tag-item {
        @include flex;
        padding: 4px 5px;
        margin: 5px 5px;
        border-radius: 6px;
        color: #363636;
        font-size: 0.9rem;
        background: white;
        cursor: pointer;
        border: 3px solid get('border-color');
        .tag-item-prefix {
          opacity: 0.6;
          font-weight: bold;
        }
        .tag-item-content {
          margin-left: 4px;
          font-weight: bold;
        }
        .tag-item-count {
          background: #f7f7f9;
          padding: 2px 3px;
          border-radius: 4px;
          text-align: center;
          min-width: 21px;
          display: inline-block;
          margin-left: 6px;
        }
      }
      .header-tag-item:hover,
      .header-tag-item.is-active {
        background: get('border-color');
        color: white;
        .tag-item-count {
          color: get('font-color');
        }
      }
    }
    .blog-list-wrap {
      margin: 5px 25px 0 25px;
      min-height: calc(100vh - 296px);
      width: calc(100% - 50px);
      position: relative;
      .blog-list-loading {
        position: absolute !important;
        margin-top: 7px;
        width: calc(100% - 12px);
        height: calc(100% - 22px);
        top: 0;
        left: 6px;
        border-radius: 10px;
        overflow: hidden;
        min-height: calc(64vh - 10px);
        .loading-container {
          background: transparent !important;
        }
      }
      .blog-list-loading-active {
        z-index: 1;
      }
    }
    .blog-list {
      @include flex;
      flex-wrap: wrap;
      justify-content: start;
      position: relative;
      .blog-item {
        background: get('back');
        cursor: pointer;
        width: calc(50% - 1rem);
        position: relative;
        height: 6rem;
        margin: 0.5rem;
        border-radius: 10px;
        transition: all 0.3s linear;
        display: flex;
        align-items: center;
        justify-content: space-between;
        box-shadow: get('box-shadow');
        .blog-item-left-img {
          object-fit: cover;
          background-position: left 28%;
          transition: all 0.4s ease-in-out;
          width: 7rem;
          height: 5rem;
          border-radius: 8px;
          margin: 0.5rem;
        }
        .blog-item-right {
          @include flex-column;
          width: calc(100% - 13.5rem);
          padding: 0 4.5rem 0 1rem;
          align-items: start;
          text-align: left;
          .blog-title {
            font-size: 1rem;
            font-weight: bold;
            width: 100%;
            margin: 5px 0;
            height: 1.2rem;
          }
          .tag-list {
            width: 100%;
            height: 25px;
            @include flex;
            margin: 5px 0 10px 0;
            flex-wrap: wrap;
            justify-content: start;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            .tag-item {
              margin: 5px 10px 5px 0;
              font-size: 1rem;
              @include flex;
            }
            .tag-item-pretend {
              opacity: 0.5;
              font-weight: bold;
              margin-right: 3px;
            }
            .tag-item-text {
              @include flex;
            }
          }
          .tag-list:hover {
            flex-wrap: wrap;
          }
          .blog-time {
            height: 1.5rem;
            display: block;
            line-height: 1.5rem;
            border-radius: 1.5rem;
            opacity: 0.9;
            width: fit-content;
            text-align: center;
            font-size: 0.9rem;
            @include flex;
            .el-icon {
              margin-right: 5px;
            }
          }
        }
      }
      .blog-item:hover {
        transform: scale(1.01);
      }
    }
  }
  .blog-sort-item-index {
    opacity: 0.5;
    position: absolute;
    top: 16%;
    right: 31px;
    font-style: italic;
    font-size: 68px;
  }
}
</style>
