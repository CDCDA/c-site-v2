<!--
 * @Description: 博客搜索弹窗
-->
<template>
  <c-dialog
    class="blog-search-dialog"
    v-model="dialogVisible"
    title="搜索"
    width="30rem"
    @close="emit('close')"
    style="height: 460px"
    :modal="true"
  >
    <div class="finder">
      <div class="finder__outer">
        <div class="finder__inner">
          <div class="finder__icon" ref="icon"></div>
          <input
            class="finder__input"
            type="text"
            name="q"
            v-model="queryParams.blogTitle"
            @keyup.enter="getBlogList"
          />
        </div>
      </div>
      <div class="c-divider"></div>
      <div class="finder__outer finder-list">
        <div class="finder-list-item" v-for="item in blogList" @click="routeTo(item)">
          <span>{{ item.blogTitle }}</span>
          <svg-icon iconName="commonSvg-右" />
        </div>
      </div>
      <div class="c-divider"></div>
      <div class="footer">
        {{ `找到${blogList.length}条结果` }}
      </div>
    </div>
  </c-dialog>
</template>
<script setup lang="ts">
import { ref, nextTick, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { autoClearTimer } from '@/utils/timer';
import { pageBlogs } from '@/api/blog.ts';
const dialogVisible = ref(true) as any;
const router = useRouter();
const total = ref(0);
const searchText = ref('' as String);
const emit = defineEmits(['close']);
const queryParams = ref({
  blogTitle: null
}) as any;
const blogList = ref([]) as any;
async function getBlogList() {
  const { code, rows, total } = (await pageBlogs(queryParams.value)) as any;
  if (code === 200) {
    blogList.value = rows;
    total.value = total;
  }
}
function routeTo(item: any) {
  router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
  emit('close');
}

onMounted(() => {
  getBlogList();
  var input = document.querySelector('.finder__input') as any;
  var finder = document.querySelector('.finder') as any;
  var form = document.querySelector('form') as any;
  autoClearTimer(() => {
    input = document.querySelector('.finder__input') as any;
    finder = document.querySelector('.finder') as any;
    form = document.querySelector('form') as any;

    input.addEventListener('focus', () => {
      finder.classList.add('active');
    });

    input.addEventListener('blur', () => {
      if (input.value.length === 0) {
        finder.classList.remove('active');
      }
    });
    if (form)
      form.addEventListener('submit', (ev: any) => {
        ev.preventDefault();
        finder.classList.add('processing');
        finder.classList.remove('active');
        input.disabled = true;
        autoClearTimer(() => {
          finder.classList.remove('processing');
          input.disabled = false;
          if (input.value.length > 0) {
            finder.classList.add('active');
          }
        }, 1000);
      });
  }, 1000);
});
</script>
<style lang="scss" scoped>
@include theme() {
  .finder__inner {
    border: 2px solid get('border-color');
  }
  .finder {
    border-radius: 8px;
    width: 100%;
    height: calc(100% - 60px);
  }

  .finder__outer {
    display: flex;
    width: 100%;
    border-radius: 10px;
  }
  .finder-list {
    width: 100%;
    height: calc(100% - 75px);
    margin-top: 10px;
    overflow: auto;
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    .finder-list-item {
      width: calc(100% - 2.6rem);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.5rem 1rem;
      margin: 0.25rem 0.25rem;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 0.9rem;
      .svg-icon {
        font-size: 1rem;
      }
    }
  }

  .finder__inner {
    display: flex;
    border-radius: 8px;
    padding: 5px 20px;
    align-items: center;
    position: relative;
    flex: 1;
  }

  .finder__form {
    flex: 1;
    height: calc(100% + 3rem);
  }

  .finder__input {
    height: calc(100% + 3rem);
    border: none;
    background-color: transparent;
    outline: none;
    font-size: 1.2rem;
    letter-spacing: 0.75px;
    width: calc(100% - 34px);
  }
  .finder__icon {
    width: 14px;
    height: 14px;
    margin-right: 10px;
    transition: all 0.2s;
    border-radius: 50%;
    position: relative;
    margin-top: 3px;
    border: 3px solid get('font-color');
  }
  .finder__icon:before {
    display: block;
    content: '';
    position: absolute;
    transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  }
  .finder__icon:before {
    width: 3px;
    height: 15px;
    background-color: get('font-color');
    top: 18%;
    left: 12px;
    transform: rotateZ(45deg) translate(-50%, 0);
    transform-origin: 0 0;
    border-radius: 4px;
  }
  .active .finder__icon:before {
    width: 4px;
    top: -25%;
    left: 20px;
    transform: rotateZ(45deg) translate(-50%, 25px);
  }
  .processing .finder__icon {
    transform-origin: 50%;
    animation: spinner 0.3s linear infinite;
    animation-delay: 0.5s;
  }
  .active .finder__icon {
    transform: translateY(-5px);
  }

  @keyframes spinner {
    0% {
      transform: rotateZ(45deg);
    }
    100% {
      transform: rotateZ(405deg);
    }
  }
  .footer {
    display: flex;
    align-items: center;
    justify-content: end;
    color: get('font-color');
    font-size: 0.8rem;
  }
}
</style>
<style lang="scss">
//.blog-search-dialog {
//  .el-dialog__body {
//    height: calc(100% - 78px) !important;
//    padding-top: 10px !important;
//  }
//}
</style>
