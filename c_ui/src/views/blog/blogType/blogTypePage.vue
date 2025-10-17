<!--
 * @Description:  博客分类具体列表
-->
<template>
  <div class="blog-type-page page-main c-left">
    <div class="type-list">
      <div
        class="type-item"
        :class="item.isActive ? 'is-active' : ''"
        v-for="item in typeList"
        @click="getList(item)"
      >
        <span class="type-item-content">{{ item.typeName }}</span>
        <span class="type-item-count">{{ item.total }}</span>
      </div>
    </div>
    <div class="c-divider"></div>
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
              <el-icon><Calendar /></el-icon>{{ formatDate(new Date(item.createTime), 'YY-MM-dd') }}
            </div>
          </div>
          <span class="blog-sort-item-index">{{ i + 1 }}</span>
          <div class="blog-cursor-cover">
            <span class="no-wrap" style="-webkit-line-clamp: 3">{{
              item.blogAbstract || '暂时没有摘要 ╮(￣▽￣)╭'
            }}</span>
          </div>
        </div>
      </div>
    </div>

    <Pagination
      v-model:page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :on-page-change="getBlogList"
      :showSizes="true"
      :on-page-size-change="getBlogList"
      class="pagi page-content"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { listTypesWithStats } from '@/api/type';
import { pageBlogs } from '@/api/blog';
import { ElNotification } from 'element-plus';
import useUserStore from '@/store/modules/user';
import Pagination from '@/components/pagination/index.vue';
import { formatDate } from '@/utils/date.ts';
import { useRouter } from 'vue-router';
import { Calendar } from '@element-plus/icons-vue';
const router = useRouter();
const userStore = useUserStore();
const loading = ref('rotate') as any;
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  typeId: null
}) as any;
const total = ref(100) as any;
const typeList = ref([]) as any;
const blogList = ref([]) as any;

async function getTypeList() {
  const { code, data } = (await listTypesWithStats({})) as any;
  if (code === 200) {
    typeList.value = data.list;
    let total = 0;
    typeList.value.forEach((e: any) => {
      total += e.total;
    });
    typeList.value.unshift({
      typeName: '全部',
      isActive: false,
      total
    });
    typeList.value.forEach((e: any) => {
      if (e.typeId === queryParams.value.typeId) e.isActive = true;
    });
  }
}

function getList(item: any) {
  if (item) {
    typeList.value.forEach((type: any) => {
      type.isActive = false;
    });
    item.isActive = true;
  }
  queryParams.value.pageNum = 1;
  queryParams.value.typeId = item.typeId;
  getBlogList();
}

async function getBlogList() {
  loading.value = 'rotate';
  try {
    const { code, rows, total: totalCount } = await pageBlogs(queryParams.value);
    if (code === 200) {
      blogList.value = rows.map((item: any) => ({
        ...item,
        tags: item.tags.slice(0, 5) // 直接截取前5个标签
      }));
      total.value = totalCount;
    }
  } finally {
    loading.value = false;
  }
}

// 博客详情
function toDetail(item: any) {
  router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
}

onMounted(() => {
  queryParams.value.typeId = (router as any).currentRoute._value.query.typeId;
  getTypeList();
  getBlogList();
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
  .blog-type-page.page-main {
    position: relative;
    transition: all 0.5s ease-in-out;
    justify-content: start;
    align-items: center;
    display: flex;
    flex-direction: column;
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
    .type-list {
      @include flex;
      justify-content: start;
      flex-wrap: wrap;
      width: calc(100% - 50px);
      margin: 25px 25px 0 25px;
      .type-item {
        @include flex;
        cursor: pointer;
        padding: 3px 8px;
        margin: 5px 8px;
        //border-radius: 12px 0 12px 0;
        border-radius: 6px;
        color: get('font-color');
        font-size: 0.9rem;
        transition: all 0.3s ease;
        border: 3px solid get('page-border-color');
        .type-item-prefix {
          opacity: 0.8;
          font-weight: bold;
        }
        .type-item-content {
          margin-left: 2px;
          font-weight: bold;
        }
        .type-item-count {
          // background: get('placeholder');
          color: get('font-color');
          padding: 3px 1px;
          border-radius: 8px;
          text-align: center;
          min-width: 21px;
          display: flex;
          margin-left: 6px;
          justify-content: center;
          align-items: center;
          transition: all 0.3s ease-in-out;
        }
      }
      .type-item:active {
        transform: translateY(2px);
      }
      .type-item:hover,
      .type-item.is-active {
        background: get('page-border-color');
        color: get('re-font-color');

        // box-shadow: get('box-shadow');

        .type-item-count {
          color: get('re-font-color');
        }
      }
    }
    .blog-list-wrap {
      margin: 4px 25px 0 25px;
      min-height: calc(100vh - 260px);
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
      width: 100%;

      .blog-item {
        background: get('back');
        cursor: pointer;
        color: get('font-color');
        width: calc(50% - 1rem);
        position: relative;
        height: 6rem;
        margin: 7px;
        border-radius: 10px;
        transition: all 0.15s linear;
        display: flex;
        align-items: center;
        overflow: hidden;
        justify-content: space-between;
        // box-shadow: get('box-shadow');
        border: 3px solid get('page-border-color');
        .blog-cursor-cover {
          position: absolute;
          //background: get('border-color');
          background: get('page-border-color');
          width: calc(85% - 20px);
          height: calc(100% - 20px);
          padding: 10px 20px 10px 15%;
          left: 100%;
          top: 0;
          color: get('font-color');
          display: flex !important;
          align-items: center;
          justify-content: center;
          transition: all 0.4s ease-in-out;
        }
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
        .blog-item-left-img {
          width: 0;
        }
        .blog-sort-item-index {
          right: calc(100% - 125px);
          z-index: 199;
          opacity: 1;
          // color: get('font-color');
          justify-content: start;
          color: white;
        }
        // transform: scale(1.02);
        // box-shadow:00 7px 0 get('border-color');
        .blog-cursor-cover {
          left: 0;
          color: white;
        }
      }
    }
  }
  .blog-sort-item-index {
    opacity: 0.5;
    position: absolute;
    top: 13%;
    right: 30px;
    font-style: italic;
    font-size: 68px;
    transition: all 0.4s ease-in-out;
    display: flex;
    justify-content: end;
    align-items: center;
    width: 100px;
  }
  .c-divider {
    margin: 15px 32px 6px 32px;
    border-color: get('page-border-color') !important;
    width: calc(100% - 64px);
  }
}
</style>
