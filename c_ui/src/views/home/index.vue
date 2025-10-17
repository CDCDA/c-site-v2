<!--
 * @Description:  主页
-->
<template>
  <div class="home-container">
    <div class="home-top">
      <div class="main-top-title">
        <div data-text></div>
      </div>
      <svg-icon iconName="commonSvg-下" class="CycleUpDown" @click="toMainPage" />
    </div>
    <div class="home-main page-main">
      <div class="main-header">
        <el-icon><ChromeFilled /></el-icon>
        <div class="main-header-text-list">
          <rollText :list="headerList" />
        </div>
        <el-icon><Right /></el-icon>
      </div>
      <div class="main-container">
        <div class="recommend">
          <RecommendLeft />
          <RecommendRight />
        </div>
        <div class="display-page">
          <div class="display-left">
            <BlogUserCard />
            <WeatherCard />
            <VisitorCard />
            <BlogTypeCard />
            <BlogTagCard />
            <BlogCountCard />
          </div>
          <div class="display-right">
            <div v-for="typeBlog in typeBlogList">
              <div class="display-header">
                <div class="display-header-left">
                  <svg-icon iconName="commonSvg-分类"></svg-icon>{{ typeBlog.typeName }}
                </div>
                <div class="display-header-right" @click="toBlogType(typeBlog.typeName)">
                  <svg-icon iconName="commonSvg-更多"></svg-icon>MORE
                </div>
              </div>
              <div class="display-list">
                <div
                  @click="toDetail(item)"
                  class="list-item"
                  :class="`list-item-${item.blogId}`"
                  v-for="(item, i) in typeBlog.blogList"
                >
                  <div class="list-item-img">
                    <c-image :src="item.coverUrl" />
                  </div>

                  <div class="list-item-footer">
                    <el-tooltip effect="dark" :content="item.blogTitle" placement="top">
                      <span class="list-item-title no-wrap">{{ item.blogTitle }}</span>
                    </el-tooltip>

                    <div class="list-item-tag">
                      <div class="item-tag-list">
                        <template v-for="(tag, i) in item.tags">
                          <span class="item-tag" v-if="i <= 4">
                            <span class="item-tag-pretend">#</span>
                            <span class="item-tag-text">{{ tag.tagName }}</span>
                          </span>
                        </template>
                      </div>
                      <span class="item-time">{{ formateToDay(item.createTime) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, defineAsyncComponent, ref, watch } from 'vue';
import { pageBlogs, listBlogsByType } from '@/api/blog';
import { ElMessage } from 'element-plus';
import { listTypesWithStats } from '@/api/type';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import { pageLogs } from '@/api/system/updateLog';
import { autoClearTimer } from '@/utils/timer';
import Pagination from '@/components/pagination/index.vue';
import { verifyToken } from '@/api/system/auth';
import { useLazyAppear } from '@/utils/lazy';
const BlogUserCard = defineAsyncComponent(() => import('@/views/blog/components/blogUserCard.vue'));
const WeatherCard = defineAsyncComponent(() => import('./components/weatherCard.vue'));
const BlogTypeCard = defineAsyncComponent(() => import('@/views/blog/components/blogTypeCard.vue'));
const BlogTagCard = defineAsyncComponent(() => import('@/views/blog/components/blogTagCard.vue'));
const BlogCountCard = defineAsyncComponent(
  () => import('@/views/blog/components/blogCountCard.vue')
);
const VisitorCard = defineAsyncComponent(() => import('./components/visitorCard.vue'));
import RecommendRight from './components/recommendRight.vue';
import RecommendLeft from './components/recommendLeft.vue';
import useThemeStore from '@/store/modules/theme.ts';

import rollText from '@/components/rollText/index.vue';
const themeStore = useThemeStore();
const theme = ref('') as any;
const loading = ref('rotate') as any;

const slogans = ref([
  '记录',
  '来逛逛吧',
  '生活不只有眼前的苟且',
  '还有远方的苟且',
  '几年很快的',
  '风一吹就没了'
]) as any;

const headerList = ref([]) as any;
const router = useRouter() as any;
const typeList = ref([]) as any;
const typeBlogList = ref({}) as any;
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userId: null
}) as any;

async function getUpdateLogList() {
  const { code, rows } = await pageLogs({ operation: '', pageNum: 1, pageSize: 999 });
  if (code === 200) {
    headerList.value = rows;
    headerList.value.forEach((x: any) => {
      x.text = x.operation;
    });
  }
}

/**
 * @description: 滚动一个页面长度
 * @return {*}
 */
function toMainPage() {
  let el = document.querySelector('.el-main') as any;
  el?.scrollTo({ top: window.innerHeight - 15, behavior: 'smooth' });
}

/**
 * @description: 获取分类树
 * @return {*}
 */

async function getTypeBlogList() {
  loading.value = true;
  const { code, data } = (await listBlogsByType({ number: 3 })) as any;
  if (code === 200) {
    typeBlogList.value = data;
    autoClearTimer(() => {
      for (const typeBlog of typeBlogList.value) {
        typeBlog.blogList.forEach((item: any) => {
          const element = document.querySelector(`.list-item-${item.blogId}`) as HTMLElement;
          if (element) {
            useLazyAppear(element);
          }
        });
      }
    }, 500);
  }
}

function formateToDay(date: any) {
  return date?.substring(0, 10);
}

// 博客详情
function toDetail(item: any) {
  router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
}

// 打字效果
function setTyping() {
  let wrapper = null as any;
  const sleep = (ms: any) => new Promise(resolve => autoClearTimer(resolve, ms));

  // 使用requestAnimationFrame优化动画性能
  function writingAll(container: any) {
    requestAnimationFrame(async () => {
      wrapper = document.querySelector('[' + container + ']');
      // const stringsContainer = document.getElementsByClassName(stringTarget);

      while (wrapper) {
        for (let i = 0; i < slogans.value.length; i++) {
          const string = slogans.value[i];
          await write(string);
          await sleep(2000);
          await erase();
          await sleep(1000);
        }
      }
    });
  }

  async function write(text: any) {
    let index = 0;
    while (index < text.length) {
      const timeout = 100;
      await sleep(timeout);
      index++;
      wrapper.innerHTML = text.substring(0, index);
    }
  }

  async function erase() {
    while (wrapper.textContent.length) {
      const timeout = 100;
      await sleep(timeout);
      wrapper.textContent = wrapper.textContent.substring(0, wrapper.textContent.length - 2);
    }
  }
  writingAll('data-text');
}

function toBlogType(name: String) {
  let type = null as any;
  typeList.value.forEach((x: any) => {
    x.typeName == name ? (type = x) : '';
  });
  router.push({ name: 'blogTypePage', query: { typeId: type.typeId } });
}

// 设置首页和顶栏颜色
function setHomeColor() {
  if (themeStore.options) {
    const { mhFontColor } = themeStore.options;
    let homeTop = document.querySelector('.home-top') as any;
    let CycleUpDown = document.querySelector('.CycleUpDown') as any;
    if (homeTop) homeTop.style.color = mhFontColor;
    if (CycleUpDown) {
      let themeIcon = CycleUpDown.querySelector('.theme-icon') as any;
      if (themeIcon) {
        themeIcon.style.fill = mhFontColor;
      }
    }
  }
}

onMounted(() => {
  verifyToken();
  getUpdateLogList();
  getTypeBlogList();
  setTyping();
  autoClearTimer(() => {
    setHomeColor();
  }, 500);
  themeStore.isFooterShow = true;
});
</script>
<style lang="scss" scoped>
@include theme() {
  .home-container {
    @include flex-column;
    min-height: 100vh;
    color: get('font-color');
    .home-top {
      width: 100%;
      min-height: 100vh;
      @include flex-column;
      position: relative;
    }
    .main-top-title {
      font-weight: bold;
      font-size: 40px;
    }
    .main-top-vice-title {
      margin-top: 10px;
      margin-bottom: 100px;
      font-size: 1.2rem;
    }
    .main-top-back {
      height: 100vh;
      width: 100%;
      @include flex-column;
    }
    .down-icon {
      transform: rotate(90deg);
    }
    .CycleUpDown {
      font-size: 70px;
      color: get('font-color');
      bottom: 40px;
      cursor: pointer;
      position: absolute;
    }
    .home-main {
      width: calc(90vw);
      max-width: 1200px;
      background: transparent !important;
      backdrop-filter: none !important;
      @include flex-column;
      .main-header {
        width: calc(100% - 60px);
        color: get('font-color');
        border-radius: 12px;
        background: get('back');
        box-shadow: get('box-shadow');
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px 30px;
        font-size: 22px;
        font-weight: bold;
        overflow: hidden;
        .main-header-text-list {
          @include flex-column;
          height: 100%;
          transition-duration: 300ms;
          transform: translate3d(0px, 0, 0);
          .main-header-text {
            height: 30px;
            margin: 10px;
            cursor: pointer;
          }
        }
        .el-icon {
          font-size: 1.2rem;
          color: get('foont-color');
          cursor: pointer;
        }
      }
      .main-container {
        width: 100%;
        margin-top: 20px;
        color: get('font-color');
        .recommend {
          @include flex;
          width: 100%;
          height: 18rem;
        }
        .display-page {
          width: 100%;
          display: flex;
          margin-top: 20px;
          .display-right {
            width: calc(100% - 16rem - 20px);
            margin-left: 20px;
            .display-header {
              .display-header-left,
              .display-header-right {
                @include flex;
              }
              .display-header-right {
                cursor: pointer;
              }
              .svg-icon {
                width: 30px;
                height: 30px;
                margin-right: 10px;
              }
              @include flex;
              justify-content: space-between;
              background: get('back');
              box-shadow: get('box-shadow');
              border-radius: 8px;
              padding: 8px 20px;
              position: relative;
              .header-item {
                font-size: 0.9rem;
                font-weight: bold;
                padding: 5px 15px 7px 15px;
                cursor: pointer;
                margin-right: 10px;
              }
              .header-more {
                position: absolute;
                right: 10px;
                cursor: pointer;
                font-size: 0.9rem;
                font-weight: bold;
                padding: 5px 15px;
              }
              .header-item:hover,
              .header-item.is-active {
                background: linear-gradient(to right, #56ccf2, #2f80ed);
                // box-shadow: get('box-shadow');
                color: white;
                border-radius: 12px;
              }
            }
            .display-list {
              margin-top: 20px;
              // height: 400px;
              border-radius: 12px;
              display: flex;
              justify-content: space-between;
              flex-wrap: wrap;
              .list-item {
                cursor: pointer;
              }
              .list-item:hover {
                transition: transform 0.4s ease;
              }
              .list-item {
                width: calc(33% - 10px);
                will-change: transform;
                aspect-ratio: 3/2;
                margin-bottom: 20px;
                background: get('back');
                box-shadow: get('box-shadow');
                border-radius: 10px;
                @include flex-column;
                justify-content: start;
                .list-item-img {
                  overflow: hidden;
                  border-radius: 8px;
                  // height: calc(100% - 100px);
                  aspect-ratio: 5/3;
                  margin: 8px;
                  width: calc(100% - 16px);
                  .c-image {
                    height: 100%;
                    width: 100%;
                    transition: transform 0.6s;
                  }
                }
                .list-item-img:hover {
                  .c-image {
                    transform: scale(1.2);
                    transition: transform 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
                  }
                }
                // .list-item-img::before {
                //   content: '';
                //   position: absolute;
                //   top:0;
                //   left:0;
                //   width: 500px;
                //   height: 100%;
                //   background-color: #fff;
                //   opacity: 0.5;
                //   /* 元素沿X轴45横切，沿X轴右移150px */
                //   transform: skewX(45deg) translateX(850px);
                //   /* 动画过渡 */
                //   transition: all 0.3s;
                // }
                // .list-item-img:hover {
                //   &::before {
                //     z-index: 1;
                //     /* 元素沿X轴45横切，沿X轴左移150px */
                //     transform: skewX(45deg) translateX(-650px);
                //   }
                //   .el-image {
                //     aspect-ratio: 5/3;
                //     transform: scale(1.1);
                //     transition: all 0.2s;
                //   }
                // }
                // .list-item-img:not(:hover) {
                //   &::before {
                //     /* 元素沿X轴45横切，沿X轴左移150px */
                //     transform: skewX(0deg) translateX(0px);
                //   }
                //   .el-image {
                //     transform: scale(1);
                //     transition: all 0.2s;
                //   }
                // }
                .list-item-footer {
                  width: calc(100% - 20px);
                  padding: 0.4rem 0 0.6rem 0;
                  height: 2.7rem;
                  display: flex;
                  justify-content: space-between;
                  flex-direction: column;
                  align-items: start;
                  .list-item-title {
                    font-size: 1rem;
                    font-weight: bold;
                    width: 100%;
                  }
                  .list-item-tag {
                    width: 100%;
                    @include flex;
                    justify-content: space-between;
                    .item-tag-list {
                      @include flex;
                      justify-content: start;
                      overflow: hidden;
                      height: 1rem;
                      width: calc(100% - 105px);
                      text-align: left;
                      display: -webkit-box;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      -webkit-line-clamp: 1;
                      -webkit-box-orient: vertical;
                      .item-tag {
                        margin-right: 0.75rem;
                        font-size: 0.8rem;
                        height: 1rem;
                        display: inline-block;
                        span {
                          height: 100%;
                          display: inline-block;
                        }
                      }
                      .item-tag-pretend {
                        opacity: 0.8;
                        font-size: 0.8rem;
                        font-weight: bold;
                        margin-right: 2px;
                      }
                    }
                    .item-time {
                      font-size: 0.8rem;
                      width: 105px;
                      height: 1rem;
                      display: flex;
                      align-items: center;
                      justify-content: end;
                    }
                  }
                }
              }
            }
          }
          .display-left {
            width: 16rem;
          }
        }
      }
    }
  }
  .type-list {
    @include flex;
    justify-content: start;
    flex-wrap: wrap;
    width: 100%;
    .type-item {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      padding: 6px 12px;
      margin: 3px;
      border-radius: 10px;
      color: get('font-color');
      font-size: 0.9rem;
      transition: transform 0.3s ease-in-out;
      // border: 2px solid get('border-color');
      .type-item-prefix {
        opacity: 0.8;
        font-weight: bold;
      }
      .type-item-content {
        margin-left: 4px;
        font-weight: bold;
      }
      .type-item-count {
        background: #f7f7f9;
        padding: 0 5px;
        border-radius: 8px;
        text-align: center;
        min-width: 21px;
        display: inline-block;
        margin-left: 6px;
      }
    }
    .type-item:active {
      transform: translateY(5px);
    }
    .type-item:hover,
    .type-item.is-active {
      background: get('border-color');
      color: white;

      // box-shadow: get('box-shadow');

      .type-item-count {
        color: get('font-color');
      }
    }
  }
}
</style>
<style>
.main-top-title .item {
  visibility: hidden;
  display: none;
}
.main-top-title h2 {
  font-size: 50px;
  margin: 0;
  height: 50px;
  color: white;
  text-align: left;
}
.main-top-title > div {
  min-height: 20px;
  height: 50px;
  text-transform: uppercase;
  letter-spacing: 1px;
  display: flex;
  align-items: flex-end;
  font-weight: bold;
}
.main-top-title > div:after {
  content: '';
  width: 2.5rem;
  height: 4px;
  display: block;
  background: white;
  color: orange;
  margin-bottom: 4px;
  margin-left: 2px;
  animation-duration: 350ms;
  animation-name: fade;
  animation-direction: alternate;
  animation-iteration-count: infinite;
}

@keyframes fade {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
</style>
