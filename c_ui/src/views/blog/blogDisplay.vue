<!--
 * @Description: 博客展示页
-->
<template>
  <wave :blogData="blogData"></wave>
  <div class="blog-display-main page-main">
    <div class="blog-diaplay">
      <div class="blog-display-left">
        <blogAi :blog-content="blogData.content"></blogAi>
        <md-preview :editorId="'preview-only'" :modelValue="blogData.content" />
        <div></div>
        <blogPreAndNext :blog-id="blogData.blogId" v-show="preNextShow" />
        <div class="c-divider" style="margin: 40px 21px 30px 21px"></div>
        <comment :type="0" :relevanceId="blogData.blogId" :data="blogData" />
        <!-- <el-divider></el-divider> -->
        <div class="blog-display-footer">
          <div v-for="(item, index) in footerData" :key="index">
            <el-tooltip class="item" effect="dark" :content="item.content" placement="top">
              <img :src="item.imageUrl" :class="item.className" @click="footerClick()" />
            </el-tooltip>
            <span>{{ item.count }}</span>
          </div>
        </div>
      </div>

      <div class="blog-display-right">
        <BlogUserCard :visibleLazy="false" />

        <VisitorCard />
        <div class="affix c-card">
          <div class="card-header">
            <el-icon><Memo /></el-icon><span class="tag-name">文章目录</span>
          </div>
          <MdCatalog :editorId="'preview-only'" :scrollElement="scrollElement" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import wave from './components/wave/index.vue';
import { getBlogById } from '@/api/blog';
import { MdPreview, MdCatalog } from 'md-editor-v3';
import comment from '@/components/comment/index.vue';
import 'md-editor-v3/lib/preview.css';
import blogAi from './components/blogAi.vue';
import BlogUserCard from '@/views/blog/components/blogUserCard.vue';
import VisitorCard from '@/views/home/components/visitorCard.vue';
import blogPreAndNext from './components/blogPreAndNext.vue';
import { autoClearTimer } from '@/utils/timer';
import { useLazyAppear } from '@/utils/lazy';
const preNextShow = ref(false);
const router = useRouter();
const footerData = ref({}) as any;
const blogData = ref({ coverUrl: '', tags: [], content: '' }) as any;
const loading = ref('gear') as any;
const scrollElement = document.querySelector('.el-main');
function footerClick() {}

async function getBlog(id: any) {
  loading.value = true;
  const { code, data } = (await getBlogById(id)) as any;
  if (code === 200) {
    blogData.value = data;
  }
  loading.value = false;
}

onMounted(() => {
  const { blogId } = router.currentRoute.value.query;
  getBlog(blogId);
  const affixElement = document.querySelector('.affix') as any;
  if (affixElement) {
    useLazyAppear(affixElement);
  }
  autoClearTimer(() => {
    preNextShow.value = true;
  }, 1000);
});
</script>
<style lang="scss">
@keyframes blur-to-clear {
  0% {
    -webkit-filter: blur(50px);
    filter: blur(50px);
  }
  100% {
    -webkit-filter: blur(0);
    filter: blur(0);
  }
}

// @keyframes display-in {
//   0% {
//     transform: translateY(-30px);
//   }
//   100% {
//     transform: translateY(0px);
//   }
// }

@keyframes enLarge {
  0% {
    font-size: 16px;
  }
  100% {
    font-weight: bold;
    font-size: 1rem;
  }
}

@keyframes shrink {
  0% {
    font-weight: bold;
    font-size: 1rem;
  }
  100% {
    transform: scale(1) translateX(0px);
  }
}
@include theme() {
  .md-editor-catalog {
    background: transparent;
    // box-shadow: get('box-shadow');
    border-radius: 8px;
    min-height: 300px;
    // padding: 10px;
    text-align: left;
  }
  .md-editor-catalog > .md-editor-catalog-link {
    padding: 10px 8px;
    font-size: 16px;
  }
  .md-editor-catalog {
    span {
      transition: ease-in-out 0.3s;
      font-size: 16px;
      font-weight: normal;
    }
  }
  .md-editor-catalog-active {
    span {
      font-size: 0.9rem;
      font-weight: bold;
      transition: ease-in-out 0.3s;
    }
  }
  .blog-diaplay {
    width: 100%;
    display: flex;
    overflow: visible;
  }
  .md-editor-previewOnly {
    color: get('font-color');
    margin: 20px;
    width: calc(100% - 40px);
    // min-height: 100%;
  }
  .blog-display-left:hover {
    box-shadow: 0 1px 15px 0 rgb(231, 231, 184) !important;
  }
  .md-editor-preview {
    text-align: left;
  }
  #main {
    .blog-display-main.page-main {
      animation: none;
      @include flex;
      background: transparent !important;
      backdrop-filter: none !important;
      .card-header {
        @include flex;
        width: 100%;
        justify-content: start;
        font-size: 1rem;
        font-weight: bold;
        .el-icon {
          margin: 8px;
          font-size: 22px;
        }
        span {
          margin-bottom: 2px;
        }
      }
      overflow: visible;
      align-items: start;
      .blog-display-left {
        overflow: hidden;
        width: calc(100% - 16rem);
        border-radius: 12px;
        background: get('back');
        box-shadow: get('box-shadow');
        min-height: 300px;
        height: fit-content;
      }
      .blog-display-right {
        width: 16rem;
        margin-left: 20px;
        min-height: 310px;
        position: sticky;
        top: 80px;
        border-radius: 12px;
        .affix {
          background: get('back');
          position: sticky;
          max-height: 87vh;
          min-height: 300px;
          overflow: auto;
          top: 60px;
          border-radius: 8px;
          & ::-webkit-scrollbar {
            visibility: hidden !important;
          }
        }
        // .directory {
        //   border-radius: 12px;
        //   background: get('back');
        //   box-shadow: get('box-shadow');
        //   margin:0;
        // }
      }
      .blog-user-component,
      .directory {
        background: get('back');
        // box-shadow: get('box-shadow');
        border-radius: 5px;
        margin-top: 5px;
        margin-bottom: 15px;
      }
      .el-backtop {
        right: 19% !important;
        bottom: 80px !important;
        border: 1px solid #dcdfe6;
        background: transparent;
        i {
          color: #dcdfe6;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.page-main {
  // overflow: auto;
}
</style>
