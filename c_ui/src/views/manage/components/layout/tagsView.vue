<template>
  <div id="tags-view-container" class="tags-view-container">
    <scroll-pane ref="scrollPane" class="tags-view-wrapper" @scroll="handleScroll">
      <router-link
        v-for="tag in visitedViews"
        :key="tag.path"
        ref="tag"
        :class="isActive(tag) ? 'active' : ''"
        :to="{ path: tag.path, query: tag.query }"
        class="tags-view-item"
        tag="span"
        @click.middle.native="!isAffix(tag) ? closeSelectedTag(tag) : ''"
        @contextmenu.prevent.native="openMenu(tag, $event)"
      >
        {{ tag.meta.remark }}
        <el-icon
          v-if="!isAffix(tag)"
          class="el-icon-close"
          @click.prevent.stop="closeSelectedTag(tag)"
          ><Close
        /></el-icon>
      </router-link>
    </scroll-pane>
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <!--      <li @click="refreshSelectedTag(selectedTag)">-->
      <!--        <i class="el-icon-refresh-right"></i> {{ '刷新页面' }}-->
      <!--      </li>-->
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <i class="el-icon-close"></i> {{ '关闭当前' }}
      </li>
      <li @click="closeOthersTags"><i class="el-icon-circle-close"></i> {{ '关闭其他' }}</li>
      <li v-if="!isFirstView()" @click="closeLeftTags">
        <i class="el-icon-back"></i> {{ '关闭左侧' }}
      </li>
      <li v-if="!isLastView()" @click="closeRightTags">
        <i class="el-icon-right"></i> {{ '关闭右侧' }}
      </li>
      <li @click="closeAllTags"><i class="el-icon-circle-close"></i> {{ '全部关闭' }}</li>
    </ul>
  </div>
</template>
<script setup lang="ts">
import ScrollPane from './ScrollPane.vue';
import { computed, onMounted, watch, ref } from 'vue';
import { useTagsViewStore } from '@/store/modules/tagsView.ts';
import { useRouter } from 'vue-router';
const router = useRouter() as any;
const tagsViewStore = useTagsViewStore();

const visitedViews = computed(() => {
  return tagsViewStore.visitedViews;
}) as any;
const visible = ref(false) as any;
const top = ref(0) as any;
const left = ref(0) as any;
const selectedTag = ref({}) as any;
watch(
  () => router,
  (val: any) => {
    const existTag = tagsViewStore.visitedViews.filter(
      (x: any) => x.name === val.currentRoute.value.name
    );
    if (existTag.length === 0) {
      tagsViewStore.visitedViews.push(val.currentRoute.value);
    }
  },
  {
    deep: true
  }
);
watch(
  () => visible,
  (val: any) => {
    if (val) {
      document.body.addEventListener('click', closeMenu);
    } else {
      document.body.removeEventListener('click', closeMenu);
    }
  },
  {
    deep: true
  }
);

function refreshSelectedTag(view: any) {
  console.log(view);
  const { fullPath } = view;
  router.replace({
    path: view.path
  });
}

function closeMenu() {
  visible.value = false;
}
function handleScroll() {
  closeMenu();
}

function isActive(route: any) {
  return route.path === router.currentRoute.value.path;
}

function isAffix(tag: any) {
  return tag.meta && tag.meta.affix;
}

function initTags() {
  const affixTags = filterAffixTags(router.getRoutes());
  affixTags.forEach((tag: any) => {
    if (tag.name) {
      const existTag = tagsViewStore.visitedViews.filter((x: any) => x.name === tag.name);
      if (existTag.length === 0) {
        tagsViewStore.visitedViews.push(tag);
      }
    }
  });
  const existTag = tagsViewStore.visitedViews.filter(
    (x: any) => x.name === router.currentRoute.value.name
  );
  if (existTag.length === 0) {
    tagsViewStore.visitedViews.push(router.currentRoute.value);
  }
}

function filterAffixTags(router: any) {
  let tags = [] as any;
  router.forEach((route: any) => {
    if (route.meta && route.meta.affix) {
      tags.push(route);
    }
    if (route.children && route.children.length != 0) {
      const tempTags = filterAffixTags(route.children);
      if (tempTags.length >= 1) {
        tags = [...tags, ...tempTags];
      }
    }
  });
  return tags;
}

function isFirstView() {
  try {
    return (
      selectedTag.value.fullPath === visitedViews[1].fullPath ||
      selectedTag.value.fullPath === '/index'
    );
  } catch (err) {
    return false;
  }
}
function isLastView() {
  try {
    return selectedTag.value.fullPath === visitedViews[visitedViews.length - 1].fullPath;
  } catch (err) {
    return false;
  }
}

function closeSelectedTag(view: any) {
  tagsViewStore.delView(view).then(({ visitedViews }: any) => {
    if (isActive(view)) {
      toLastView(visitedViews, view);
    }
  });
}

function closeRightTags() {
  console.log(selectedTag.value);
  tagsViewStore.delRightViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeLeftTags() {
  tagsViewStore.delLeftViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeOthersTags() {
  tagsViewStore.delOthersViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeAllTags() {
  tagsViewStore.delVisitedView().then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}

function toLastView(visitedViews: any, view: any) {
  const latestView = visitedViews.slice(-1)[0];
  if (latestView) {
    router.push({ path: latestView.path });
  } else {
    if (view.name === 'Dashboard') {
      // to reload home page
      router.replace({ path: '/redirect' + view.fullPath });
    } else {
      router.push('/');
    }
  }
}

function openMenu(tag: any, e: any) {
  left.value = e.clientX - 150;
  top.value = e.clientY;
  visible.value = true;
  selectedTag.value = tag;
}

onMounted(() => {
  initTags();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .tags-view-item {
    font-size: 0.75rem !important;
    font-family: 'Shark';
    text-decoration: none;
    border-radius: 4px;
    display: flex !important;
    align-items: center;
    justify-content: center;
  }
  .router-link-exact-active {
    background: #409eff !important;
    color: white !important;
  }
}
.tags-view-container.headerTagsView {
  position: absolute;
  left: 45px;
  top: 7px;
  border-bottom: none;
  box-shadow: none;
  width: calc(100% - 230px);
}
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  //border-bottom: 1px solid #d8dce5;
  //box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);

  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;

      &:first-of-type {
        margin-left: 15px;
      }

      &:last-of-type {
        margin-right: 15px;
      }

      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;

        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;

      &:hover {
        background: #eee;
      }
    }
  }
}
</style>

<style lang="scss">
//reset element css of el-icon-close
.tags-view-wrapper {
  .tags-view-item {
    .el-icon-close {
      width: 0.8rem;
      height: 0.8rem;
      margin-left: 5px;
      margin-bottom: 2px;
      vertical-align: 2px;
      border-radius: 50%;
      text-align: center;
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      transform-origin: 100% 50%;

      &:before {
        transform: scale(0.6);
        display: inline-block;
        vertical-align: -3px;
      }

      &:hover {
        background-color: #b4bccc;
        color: #fff;
      }
    }
  }
}
</style>
