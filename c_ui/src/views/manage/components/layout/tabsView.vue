<template>
  <div class="tabs-box">
    <div class="tabs-menu">
      <el-tabs v-model="tabsMenuValue" type="card" @tab-click="tabClick" @tab-remove="tabRemove">
        <el-tab-pane
          v-for="item in menuList"
          :key="item.name"
          :label="item.meta.title"
          :name="item.name"
          :closable="!item.meta.affix"
        >
          <template #label>
            <svg-icon class="tab-icon" :iconName="item.meta.svgIcon" />
            <span @contextmenu.prevent.native="openMenu(item, $event)">{{ item.meta.title }}</span>
          </template>
        </el-tab-pane>
      </el-tabs>
      <MoreButton />
    </div>
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
import Sortable from 'sortablejs';
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import SvgIcon from '@/components/SvgIcon/index.vue';
import { useTabsViewStore } from '@/store/modules/tabsView.ts';

const tabsViewStore = useTabsViewStore();
const visible = ref(false);
defineOptions({
  name: 'TabsView'
});
const selectedTag = ref({}) as any;

const route = useRoute();
const router = useRouter();

const tabsMenuValue = ref(route.fullPath);
const menuList = computed(() => tabsViewStore.visitedViews);

onMounted(() => {
  tabsDrop();
  initTabs();
});

const addTab = (tab: any) => {
  const existTabs = tabsViewStore.visitedViews.filter((x: any) => x.name === tab.name);
  console.log('val', tab, existTabs);
  if (existTabs.length === 0) {
    tabsViewStore.visitedViews.push(tab);
  }
};

watch(
  () => menuList.value,
  (val: any) => {
    console.log('menuList', val);
  },
  {
    deep: true
  }
);

// 监听路由的变化（防止浏览器后退/前进不变化 tabsMenuValue）
watch(
  () => router,
  (val: any) => {
    addTab(val.currentRoute.value);
    tabsMenuValue.value = val.currentRoute.value.name;
  },
  {
    deep: true
  }
);
// tabs 拖拽排序
const tabsDrop = () => {
  Sortable.create(document.querySelector('.el-tabs__nav') as any, {
    draggable: '.el-tabs__item',
    animation: 300,
    onEnd({ newIndex, oldIndex }) {
      const tabsList = [...tabsViewStore.visitedViews];
      const currRow = tabsList.splice(oldIndex as number, 1)[0];
      tabsList.splice(newIndex as number, 0, currRow);
      tabStore.setTabs(tabsList);
    }
  });
};

const filterAffixTabs = (routers: any) => {
  let tabs = [] as any;
  console.log('router', router);
  routers.forEach((route: any) => {
    if (route.meta && route.meta.affix) {
      tabs.push(route);
    }
    // if (route.children && route.children.length != 0) {
    //   const tempTabs = filterAffixTabs(route.children);
    //   if (tempTabs.length >= 1) {
    //     tabs = [...tabs, ...tempTabs];
    //   }
    // }
  });
  return tabs;
};

// 初始化需要固定的 tabs
const initTabs = () => {
  const affixTabs = filterAffixTabs(router.getRoutes());
  console.log('affixTabs', affixTabs);

  affixTabs.forEach((tab: any) => {
    addTab(tab);
  });
  addTab(route);
  tabsMenuValue.value = route.name;
};

// Tab Click
const tabClick = (tab: any) => {
  console.log('tabClick', tab);
  const view = menuList.value.find((x: any) => x.name === tab.props.name);
  router.push({ path: view.path });
};

// // Last View
// const toLastView = (visitedViews: any, view: any) => {
//   const latestView = visitedViews.slice(-1)[0];
//   if (latestView) {
//     router.push({ path: latestView.path });
//   }
// };

// Remove Tab
const tabRemove = (tabName: any) => {
  console.log('tabRemove', tabName);
  const view = menuList.value.find((x: any) => x.name === tabName);
  tabsViewStore.delView(view).then(({ visitedViews }: any) => {
    toLastView(visitedViews, view);
  });
};

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
  console.log('aa', view);
  tabsViewStore.delView(view).then(({ visitedViews }: any) => {
    toLastView(visitedViews, view);
  });
}

function closeRightTags() {
  console.log(selectedTag.value);
  tabsViewStore.delRightViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeLeftTags() {
  tabsViewStore.delLeftViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeOthersTags() {
  tabsViewStore.delOthersViews(selectedTag.value).then(({ visitedViews }: any) => {
    toLastView(visitedViews, selectedTag.value);
  });
}
function closeAllTags() {
  tabsViewStore.delVisitedView().then(({ visitedViews }: any) => {
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

const left = ref(0);
const top = ref(0);

function openMenu(tag: any, e: any) {
  console.log('qq', tag);
  left.value = e.clientX - 150;
  top.value = e.clientY;
  visible.value = true;
  selectedTag.value = tag;
}

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
function isAffix(tag: any) {
  console.log(tag);
  return tag && tag.meta && tag.meta.affix;
}
</script>

<style scoped lang="scss">
.tabs-box {
  width: 100%;
  background-color: var(--el-bg-color);

  :deep(.el-tabs__nav-scroll) {
    justify-content: flex-start !important;
  }

  .tabs-menu {
    position: relative;
    width: 100%;

    .el-dropdown {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;

      .more-button {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 43px;
        cursor: pointer;
        border-left: 1px solid var(--el-border-color-light);
        transition: all 0.3s;

        &:hover {
          background-color: var(--el-color-info-light-9);
        }

        .iconfont {
          font-size: 12.5px;
        }
      }
    }

    :deep(.el-tabs) {
      .el-tabs__header {
        box-sizing: border-box;
        height: 40px;
        padding: 0 10px;
        margin: 0;
        border-bottom: none;

        .el-tabs__nav-wrap {
          position: absolute;
          width: calc(100% - 70px);

          .el-tabs__nav {
            display: flex;
            border: none;

            .el-tabs__item {
              display: flex;
              align-items: center;
              justify-content: center;
              color: #afafaf;
              border: none;
              font-size: 0.8rem !important;

              .tab-icon {
                margin: 1.5px 4px 0 0;
                font-size: 15px !important;
              }

              .is-icon-close {
                margin-top: 1px;
              }

              &.is-active {
                color: var(--el-color-primary);

                &::before {
                  position: absolute;
                  bottom: 0;
                  width: 100%;
                  height: 0;
                  content: '';
                  border-bottom: 2px solid var(--el-color-primary) !important;
                }
              }
            }
          }
        }
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
  font-size: 0.75rem;
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
</style>
