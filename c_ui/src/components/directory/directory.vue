<template>
  <div class="directory" v-if="titles && Object.keys(titles).length > 0">
    <div class="directory-header">
      <div>
        <span></span>
        <span>{{ $t('目录') }}</span>
      </div>
      <span class="progress">{{ progress }}</span>
    </div>
    <div class="directory-content">
      <div
        v-for="title in titles"
        :key="title.id"
        @click="scrollToView(title.scrollTop)"
        :class="['directory-item', currentTitle.value.id == title.id ? 'active' : 'not-active']"
        :style="{ marginLeft: title.level * 20 + 'px' }"
        :title="title.rawName"
      >
        {{ title.name }}
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue';
import { autoClearTimer } from '@/utils/timer';
const titles = ref(null) as any;
const currentTitle = ref({}) as any;
const progress = ref(null) as any;
const articleElement = ref(null) as any;
const props = defineProps({
  html: {
    type: String,
    default: '.post-body .article-content'
  }
});

function scrollEvent() {
  if (articleElement.value)
    progress.value =
      (
        (articleElement.value.scrollTop /
          (articleElement.value.scrollHeight - articleElement.value.clientHeight)) as any
      ).toFixed(2) *
        100 +
      '%';
  let visibleTitles = [];
  if (titles.value && titles.value.length) {
    let tempTitles = titles.value;
    for (let i = tempTitles.length - 1; i >= 0; i--) {
      const title = tempTitles[i];
      if (title.scrollTop <= window.scrollY) {
        if (currentTitle.value.id === title.id) return;

        Object.assign(currentTitle.value, title);

        // 展开节点
        setChildrenVisible(title, true);
        visibleTitles.push(title);

        // 展开父节点
        let parent = title.parent;
        while (parent) {
          setChildrenVisible(parent, true);
          visibleTitles.push(parent);
          parent = parent.parent;
        }

        // 折叠其余节点
        for (const t of tempTitles) {
          if (!visibleTitles.includes(t)) {
            setChildrenVisible(t, false);
          }
        }
        return;
      }
    }
  }
}
// 获取目录的标题
function getTitles() {
  let titles = [] as any;
  let levels = ['h1', 'h2', 'h3', 'h4'];
  let articleElement = document.querySelector('.' + props.html);
  articleElement = articleElement;
  if (!articleElement) {
    return titles;
  }

  let elements = Array.from(articleElement.querySelectorAll('*')) as any;
  // 调整标签等级
  let tagNames = new Set(elements.map((el: any) => el.tagName.toLowerCase()));
  for (let i = levels.length - 1; i >= 0; i--) {
    if (!tagNames.has(levels[i])) {
      levels.splice(i, 1);
    }
  }

  let serialNumbers = levels.map(() => 0);
  for (let i = 0; i < elements.length; i++) {
    const element = elements[i];
    let tagName = element.tagName.toLowerCase();
    let level = levels.indexOf(tagName);
    if (level == -1) continue;

    let id = tagName + '-' + element.innerText + '-' + i;
    let node = {
      id,
      level,
      parent: null,
      children: [],
      rawName: element.innerText,
      scrollTop: element.offsetTop
    } as any;

    if (titles.length > 0) {
      let lastNode = titles.at(-1);

      // 遇到子标题
      if (lastNode.level < node.level) {
        node.parent = lastNode;
        lastNode.children.push(node);
      }
      // 遇到上一级标题
      else if (lastNode.level > node.level) {
        serialNumbers.fill(0, level + 1);
        let parent = lastNode.parent;
        while (parent) {
          if (parent.level < node.level) {
            parent.children.push(node);
            node.parent = parent;
            break;
          }
          parent = parent.parent;
        }
      }
      // 遇到平级
      else if (lastNode.parent) {
        node.parent = lastNode.parent;
        lastNode.parent.children.push(node);
      }
    }

    serialNumbers[level] += 1;
    let serialNumber = serialNumbers.slice(0, level + 1).join('.');

    node.isVisible = node.parent == null;
    node.name = serialNumber + '. ' + element.innerText;
    titles.push(node);
  }
  return titles;
}
// 滚动到指定的位置
function scrollToView(scrollTop: any) {
  articleElement.value = document.querySelector('.el-main');
  articleElement.value.scrollTo({ top: scrollTop, behavior: 'smooth' });
}
// 设置子节点的可见性
function setChildrenVisible(title: any, isVisible: any) {
  for (const child of title.children) {
    child.isVisible = isVisible;
  }
}

onMounted(() => {
  autoClearTimer(() => {
    titles.value = getTitles();
    // 监听滚动事件并更新样式
    window.addEventListener('scroll', scrollEvent, true);
  }, 1000);
});
</script>

<style lang="scss">
@include theme() {
  .directory {
    border-radius: 8px;
    background: get('back');
    box-shadow: get('box-shadow');
    padding: 20px 24px;
    width: 100%;
    margin-top: 15px;
    box-sizing: border-box;
    .directory-header {
      text-align: left !important;
      margin-bottom: 15px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .directory-icon {
      font-size: 0.9rem;
      margin-right: 10px;
      color: #363c41;
    }

    .directory-header div > span {
      font-size: 0.9rem;
      color: get('font-color');
    }

    .progress {
      color: get('font-color');
      font-style: italic;
      font-size: 140%;
    }

    .directory-content {
      max-height: calc(100vh - 120px);
      overflow: auto;
      margin-right: -24px;
      padding-right: 20px;
      text-align: left;
    }

    .directory-item {
      color: get('font-color');
      margin: 5px 0;
      line-height: 28px;
      cursor: pointer;
      transition: all 0.2s ease-in-out;
      font-size: 14px;
      padding: 2px 6px;
      display: -webkit-box;
      overflow: hidden;
      text-overflow: ellipsis;
      -webkit-line-clamp: 1;
      -webkit-box-orient: vertical;

      &:hover {
        color: #8d9daa;
      }
    }

    .active {
      //   background: #;
      color: get('font-color');

      &:hover {
        background: #828e97;
        color: get('font-color');
      }
    }
  }
}
</style>
