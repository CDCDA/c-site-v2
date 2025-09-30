<!--
 * @Description:  主页
-->
<template>
  <div class="home-main">
    <div class="main-header">
      <el-icon><ChromeFilled /></el-icon>
      <div class="main-header-text-list">
        <div
          class="main-header-text"
          @click="router.push({ name: item.routerName })"
          v-for="item in headerList"
        >
          {{ item.text }}
        </div>
      </div>
      <el-icon><Right /></el-icon>
    </div>
    <div class="main-container">
      <div class="recommend">
        <div class="recommend-left">
          <div class="recommend-left-top">
            <div class="recommd-left-title">
              <span>我的技术栈</span>
            </div>
            <TechnologyStackCard
              class="recommd-left-stack"
              :isHoverShow="false"
            ></TechnologyStackCard>
            <div class="recommend-left-top-cover" @click="toRange()">
              <div>随便逛逛</div>
              <svg-icon iconName="commonSvg-right" style="color: white"></svg-icon>
            </div>
          </div>
          <div class="recommend-left-bottom">
            <div
              class="bottom-item"
              @click="router.push({ name: 'blogTypePage', query: { typeId: '1' } })"
            >
              <span>前端小记</span><svg-icon iconName="commonSvg-book" />
            </div>
            <div
              class="bottom-item"
              @click="router.push({ name: 'blogTypePage', query: { typeId: '2' } })"
            >
              <span>后端总结</span><svg-icon iconName="commonSvg-hot" />
            </div>
            <div class="bottom-item" @click="router.push({ name: 'essay' })">
              <span>生活随笔</span><svg-icon iconName="commonSvg-edit" />
            </div>
          </div>
        </div>
        <div class="recommend-right">
          <div class="right-item" v-for="item in tRecommends" @click="toDetail(item)">
            <div class="icon-rec">荐</div>
            <c-image class="item-image" :src="item.coverUrl"></c-image>
            <div class="item-title">
              <el-text truncated>
                {{ item.blogTitle }}
              </el-text>
            </div>
          </div>
        </div>
      </div>
      <div class="display-page">
        <div class="display-left">
          <div class="display-header">
            <div class="type-list">
              <div
                class="type-item"
                :class="item.isActive ? 'is-active' : ''"
                v-for="item in typeList"
                @click="setQuery(item)"
              >
                <!-- <span class="type-item-prefix" v-if="item.typeId">#</span> -->
                <span class="type-item-content">{{ item.typeName }}</span>
                <!-- <span class="type-item-count">{{ item.total }}</span> -->
              </div>
            </div>
          </div>
          <div class="display-list">
            <div class="list-item" v-for="item in recommends" @click="toDetail(item)">
              <div class="list-item-img">
                <el-image :src="item.coverUrl" lazy>
                  <template #placeholder>
                    <div
                      class="image-slot"
                      v-cLoading="'rotate'"
                      style="width: 100%; height: 100%"
                    ></div> </template
                  ><template #error>
                    <div class="image-error-slot">
                      <svg-icon iconName="commonSvg-图片加载失败"></svg-icon>
                    </div> </template
                ></el-image>
              </div>

              <div class="list-item-footer">
                <span class="list-item-title">{{ item.blogTitle }}</span>
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
          <Pagination
            v-model:page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :total="total"
            :on-page-change="getBlogList"
            :show-sizes="true"
            :page-size-list="[10, 20, 30]"
            :on-page-size-change="getBlogList"
            class="pagi page-content"
          />
        </div>
        <div class="display-right">
          <BlogUserCard />
          <!-- <WeatherCard /> -->
          <VisitorCard />
          <BlogTypeCard />
          <BlogTagCard />
          <BlogCountCard />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { pageBlogs } from '@/api/blog';
import { ElMessage } from 'element-plus';
import { listTypesWithStats } from '@/api/type';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import BlogUserCard from '@/views/blog/components/blogUserCard.vue';
import BlogTypeCard from '@/views/blog/components/blogTypeCard.vue';
import BlogTagCard from '@/views/blog/components/blogTagCard.vue';
import BlogCountCard from '@/views/blog/components/blogCountCard.vue';
import TechnologyStackCard from '../../introduction/personalProfile/components/technologyStackCard.vue';
import useThemeStore from '@/store/modules/theme.ts';
import VisitorCard from '../components/visitorCard.vue';
const themeStore = useThemeStore();
const recommends = deleteDictTypes;
const tRecommends = deleteDictTypes;
const userStore = useUserStore();
const theme = ref('' as any);
const loading = ref('rotate' as any);

const headerList = ref([
  {
    text: '加了个滚动技术栈',
    routerName: 'main'
  },
  {
    text: '相册已完成',
    routerName: 'album'
  },
  {
    text: '修整了一下首页',
    routerName: 'main'
  },
  {
    text: '放假放假放假',
    routerName: 'essay'
  }
]) as any;
const router = useRouter() as any;
const typeList = ref([] as any);

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userId: null
} as any);

const total = ref(100 as any);
async function getBlogList() {
  const { code, msg, rows, total } = (await pageBlogs(queryParams.value)) as any;
  if (code === 200) {
    recommends.value = rows;
    tRecommends.value = JSON.parse(JSON.stringify(rows));
    tRecommends.value.length > 6 ? (tRecommends.value.length = 6) : '';
    total.value = total;
  } else {
    ElMessage.error('博客数据获取失败', msg);
  }
}

/**
 * @description: 滚动一个页面长度
 * @return {*}
 */
function toMainPage() {
  let el = document.querySelector('.el-main') as any;
  el?.scrollTo({ top: window.innerHeight - 80, behavior: 'smooth' });
}

/**
 * @description: 获取分类树
 * @return {*}
 */

async function getTypeTree() {
  const { code, msg, data } = (await listTypesWithStats(queryParams.value)) as any;
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
    queryParams.value.typeId = router.currentRoute.value.query.typeId;
    typeList.value.forEach((e: any) => {
      if (e.typeId === queryParams.value.typeId) e.isActive = true;
    });
  } else {
    ElMessage.error('分类数据获取失败', msg);
  }
}
let displacement = ref(72 as any);

function headerRoll(header: any) {
  displacement.value -= 49;
  if (displacement.value == -75) displacement.value = 72;
  header.style.transform = `translate3d(0px, ${displacement.value}px,0)`;
}

function formateToDay(date: any) {
  return date?.substring(0, 10);
}

function isActive(item: any) {
  typeList.value.forEach((e: any) => {
    e.isActive = false;
  });
  item.isActive = true;
  queryParams.value.userId = item.typeId;
  getBlogList();
}

// 博客详情
function toDetail(item: any) {
  router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
}

const routes = router.getRoutes().filter((e: any) => {
  return e.meta.isHidden !== true && e.meta.remark;
}) as any;

function toRange() {
  router.push({
    name: 'blogDisplay',
    query: { blogId: recommends.value[Math.ceil(Math.random() * recommends.value.length)].blogId }
  });
}

function setQuery(item: any) {
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

onMounted(() => {
  // 渐出

  // autoClearTimer(() => {
  //   (document.getElementById('app-theme') as any).style.animation =
  //     'blur-to-clear 2.5s cubic-bezier(0.6, 0.25, 0.25, 1) 0s 1 backwards,scale 2.2s cubic-bezier(0.6, 0.1, 0.25, 1) 0.5s 1 backwards';
  // }, 500);
  getBlogList();
  getTypeTree();
  let header = document.querySelector('.main-header-text-list');
  if (header) {
    setInterval(() => {
      headerRoll(header);
    }, 3000);
  }
});
</script>
<style lang="scss" scoped>
@keyframes slide-in {
  0% {
    width: 33%;
  }
  100% {
    width: 50%;
  }
}
@keyframes rotate-img {
  0% {
    opacity: 0.2;
    transform: rotate(15deg);
  }
  100% {
    opacity: 1;
    right: 5%;
    top: -5%;
    filter: blur(0px);
    transform: rotate(0deg);
  }
}
@keyframes re-slide-in {
  0% {
    width: 50%;
  }
  100% {
    width: 33%;
  }
}
@keyframes re-rotate-img {
  0% {
    opacity: 1;
    right: 5%;
    top: -5%;
    filter: blur(0px);
    transform: rotate(0deg);
  }
  100% {
    opacity: 0.2;
    transform: rotate(15deg);
  }
}
@include theme() {
  .home-container {
    @include flex-column;
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
      font-size: 40px;
      height: 60px;
      color: get('font-color');
      bottom: 40px;
      cursor: pointer;
      position: absolute;
    }
    .home-main {
      width: calc(88vw);
      max-width: 1500px;
      @include flex-column;

      .main-header {
        height: 7vh;
        width: calc(100% - 60px);
        color: get('font-color');
        border-radius: 8px;
        background: get('back');
        box-shadow: get('box-shadow');
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 30px;
        font-size: 22px;
        font-weight: bold;
        overflow: hidden;
        .main-header-text-list {
          @include flex-column;
          height: 100%;
          transition-duration: 300ms;
          transform: translate3d(0px, 72px, 0);
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
        .display-page {
          width: 100%;
          display: flex;
          margin-top: 20px;
          .display-right {
            width: calc(100% - 300px);
            margin-right: 20px;
            .display-header {
              @include flex;
              justify-content: start;
              height: 8vh;
              background: get('back');
              box-shadow: get('box-shadow');
              border-radius: 8px;
              padding: 0 20px;
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
                border-radius: 8px;
              }
            }
            .display-list {
              margin-top: 20px;
              border-radius: 8px;
              display: flex;
              justify-content: space-between;
              flex-wrap: wrap;
              // .list-item:hover {
              // --i: -1;
              // mask-position: 0 0;
              // transform: perspective(1800px) rotate3d(1, -1, 0, calc(var(--i, 1) * 8deg));
              // mask: linear-gradient(135deg, #000c 40%, #000, #000c 60%) 100% 100%/240% 240%;
              // transition: 0.4s;
              // }
              .list-item {
                cursor: pointer;
                width: calc(50% - 10px);
                aspect-ratio: 3/2.5;
                margin-bottom: 20px;
                background: get('back');
                box-shadow: get('box-shadow');
                border-radius: 8px;
                @include flex-column;
                justify-content: start;
                .list-item-img {
                  aspect-ratio: 7/4;
                  margin: 5px;
                  width: calc(100% - 10px);
                  border-radius: 10px;
                  .el-image {
                    width: 100%;
                    height: 100%;
                  }
                }
                .list-item-img {
                  position: relative;
                  /* 盒子阴影 */
                  box-shadow: 0 5px 45px rgba(0, 0, 0, 0.1);
                  /* 背景模糊 */
                  backdrop-filter: blur(2px);
                  /* 加个动画过渡，动画才不会太过生硬 */
                  transition: all 0.3s;
                  overflow: hidden;
                }
                .list-item-img::before {
                  content: '';
                  position: absolute;
                  top: 0;
                  left: 0;
                  width: 500px;
                  height: 100%;
                  background-color: #fff;
                  opacity: 0.5;
                  /* 元素沿X轴45横切，沿X轴右移150px */
                  transform: skewX(45deg) translateX(850px);
                  /* 动画过渡 */
                  transition: all 0.3s;
                }
                .list-item-img:hover {
                  &::before {
                    z-index: 1;
                    /* 元素沿X轴45横切，沿X轴左移150px */
                    transform: skewX(45deg) translateX(-650px);
                  }
                  .el-image {
                    aspect-ratio: 5/3;
                    transform: scale(1.1);
                    transition: all 0.2s;
                  }
                }
                .list-item-img:not(:hover) {
                  &::before {
                    /* 元素沿X轴45横切，沿X轴左移150px */
                    transform: skewX(0deg) translateX(0px);
                  }
                  .el-image {
                    transform: scale(1);
                    transition: all 0.2s;
                  }
                }
                .list-item-footer {
                  width: calc(100% - 40px);
                  height: 100px;
                  display: flex;
                  justify-content: space-evenly;
                  flex-direction: column;
                  padding: 0 20px;
                  align-items: start;
                  .list-item-title {
                    font-size: 1rem;
                    font-weight: bold;
                  }
                  .list-item-tag {
                    width: 100%;
                    @include flex;
                    justify-content: space-between;
                    .item-tag-list {
                      @include flex;
                      justify-content: start;
                      overflow: hidden;
                      height: 30px;
                      width: 80%;
                      text-align: left;
                      display: -webkit-box;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      -webkit-line-clamp: 1;
                      -webkit-box-orient: vertical;
                      .item-tag {
                        margin-right: 15px;
                        font-size: 1rem;
                        display: inline-block;
                      }
                      .item-tag-pretend {
                        opacity: 0.5;
                        font-weight: bold;
                      }
                    }
                    .item-time {
                    }
                  }
                }
              }
            }
          }
          .display-left {
            width: 300px;
            // background: get('back');
            // box-shadow: get('box-shadow');
            // border-radius: 8px;
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
      transition: all 0.3s ease-in-out;
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
