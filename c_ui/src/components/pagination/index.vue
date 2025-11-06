<template>
  <div class="c-pagination">
    <ul class="pager clear" @click="onPage">
      <li
        :class="{ disabled: page <= 1 }"
        :aria-disabled="page <= 1"
        tabindex="0"
        class="pn prev"
        data-page="-1"
        role="button"
      >
        <el-icon><ArrowLeftBold /></el-icon>
      </li>

      <div class="pagi-number" style="display: flex; align-items: center; margin-right: 30px">
        <div style="display: flex; align-items: center">
          <svg-icon iconName="commonSvg-总共" style="height: 25px; width: 25px" /><span>{{
            `${total}${$t('条')}`
          }}</span>
        </div>
        <template v-for="(group, index) in slices" :key="'g' + index">
          <li
            v-if="group[0] === -1"
            :data-page="group[1]"
            :data-jumper="index"
            :aria-label="arialLabel(group[1])"
            class="pn jumper"
          >
            <span class="dots">...</span>
          </li>
          <li
            v-else
            v-for="i in group"
            :key="'l' + i"
            :class="{ active: page === i }"
            :data-page="i"
            :aria-label="arialLabel(i)"
            class="pn"
            role="button"
          >
            <span>{{ i }}</span>
          </li>
        </template>
        <div v-if="showSizes" class="page-size">
          <div class="page-select" @click="showPageList = !showPageList">
            {{ `${pageSize}${$t('条/页')}` }}
            <div v-if="showPageList" class="select-box">
              <div
                v-for="(item, index) in pageSizeList"
                :key="index"
                class="seleclt-opotion"
                @click.stop="onSize(item)"
              >
                {{ item }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <li
        :class="{ disabled: page >= pages }"
        :aria-disabled="page >= pages"
        :aria-label="arialLabel(0)"
        tabindex="0"
        class="pn next"
        data-page="0"
        role="button"
      >
        <el-icon><ArrowRightBold /></el-icon>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, computed, watch, onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer';

const props = defineProps({
  page: {
    type: Number,
    default: 1
  },
  total: {
    type: Number,
    default: 0
  },
  pageSize: {
    type: Number,
    default: 10
  },
  onPageChange: {
    type: Function,
    default: () => {}
  },
  onPageSizeChange: {
    type: Function,
    default: () => {}
  },
  showSizes: {
    type: Boolean,
    default: true
  },
  pageSizeList: {
    type: Array,
    default: () => [10, 20, 50]
  }
});

const emit = defineEmits(['update:page', 'update:pageSize']);

const pages = ref(0);
const slices = ref([[1]]);
const showPageList = ref(false);

const updateSlices = () => {
  pages.value = Math.ceil(props.total / props.pageSize);
  if (pages.value < 5) {
    slices.value = [
      Array(pages.value)
        .fill(1)
        .map((o: any, i: number) => {
          o;
          return i + 1;
        })
    ];
  } else if (props.page < 4) {
    slices.value = [[1, 2, 3], [-1, 4], [pages.value]];
  } else if (pages.value - props.page < 3) {
    slices.value = [[1], [-1, 2], [pages.value - 2, pages.value - 1, pages.value]];
  } else {
    slices.value = [
      [1],
      [-1, 2],
      [props.page - 1, props.page, props.page + 1],
      [-1, props.page + 2],
      [pages.value]
    ];
  }
};

const arialLabel = (i: any) => {
  if (i === -1) {
    return '上一页';
  }
  if (i === 0) {
    return `${$t('下一页')}`;
  }
  return `${$t('第')}${i}${$t('页')}`;
};

const onSize = (e: any) => {
  emit('update:pageSize', e);
  emit('update:page', 1);
  props.onPageChange();
  showPageList.value = false;
};

const onPage = (e: any) => {
  let target = e.target;
  if (target.tagName === 'SPAN') {
    target = target.parentElement;
  }
  if (target.className.indexOf('disabled') !== -1 || target.tagName !== 'LI') {
    return;
  }

  const page = +target.getAttribute('data-page');
  const jumper = target.getAttribute('data-jumper');
  if (jumper) {
    emit('update:page', page);
    props.onPageChange(page);
  } else {
    emit('update:page', calcNextPage(page));
    props.onPageChange(calcNextPage(page));
  }
};

const calcNextPage = (page: any) => {
  return !page ? props.page + 1 : page < 0 ? props.page - 1 : page;
};

const showJumper = (num: any, target: any) => {
  if (num && num > 0 && num <= pages.value) {
    const newSlices = [...slices.value];
    newSlices[num][2] = 1;
    slices.value = newSlices;
    autoClearTimer(() => {
      target.children[1].focus();
    }, 100);
  }
};

const onJump = (e: any) => {
  const val = +e.target.value;
  if (val && val > 0 && val <= pages.value) {
    props.onPageChange(val);
  }
};

const onBlur = (e: any) => {
  const num = +e.target.parentNode.getAttribute('data-jumper');
  const newSlices = [...slices.value];
  newSlices[num][2] = 0;
  slices.value = newSlices;
};

// 监听器
watch(() => props.page, updateSlices);
watch(() => props.total, updateSlices);
watch(() => props.pageSize, updateSlices);

onMounted(() => {
  updateSlices();
});
</script>

<style lang="scss" scoped>
@include theme() {
  .c-pagination {
    color: get('font-color');
    width: 100%;
    font-weight: 400;
    line-height: 30px;
    display: flex;
    text-align: center;

    .pn:active {
      transform: translateY(1px);
    }

    & > .pager {
      display: flex;
      width: 100%;
      justify-content: space-between;
      white-space: nowrap;
      align-items: center;
      padding: 0;
      margin: 0;
    }

    .pn {
      float: left;
      list-style: none;
      cursor: pointer;
      line-height: 35px;
      margin-left: 15px;
      text-align: center;
      background: get('re-font-color');
      border: 1px solid get('border-color');
      border-radius: 2px;
      font-size: 0.9rem;
      color: get('font-color');
      font-weight: 400;
      outline: none;
      height: 35px;
      width: 35px;
      border-radius: 4px;

      &:first-child {
        margin-left: 0;
      }

      &:hover:not(.disabled) {
        background: get('bk');
        color: #fff;
      }

      &.active.active {
        background: get('bk');
        color: #fff;
      }

      & > .dots {
        display: block;
        font-weight: bold;
        line-height: 30px;
        padding-bottom: 6px;
      }

      & > input {
        color: #666;
        border: 0;
        font-size: 0.8rem;
        max-width: 40px;
        padding: 2px 1px;
      }

      &.prev,
      &.next {
        color: get('font-color');
        display: flex;
        align-items: center;
        justify-content: center;
      }

      &.next,
      &.prev {
        color: #fff !important;
        background: get('bk');
      }

      &.disabled {
        cursor: not-allowed;
        background: get('re-font-color');
        color: get('font-color') !important;
      }
    }

    & > .elevator {
      display: inline-block;
      color: #888f9c;
      font-size: 14px;
      height: 40px;
      line-height: 40px;
      margin-left: 20px;
      position: relative;
      vertical-align: top;

      & > .pagenum {
        appearance: none;
        background: transparent;
        color: #666;
        border: 1px solid #e6e7eb;
        border-radius: 0;
        font-size: 0.9rem;
        margin: 0 10px 0 2px;
        padding-left: 10px;
        width: 60px;
        height: 40px;
      }

      & > .icn {
        display: block;
        border-top: 6px solid #5c5c5c;
        border-left: 5px solid transparent;
        border-right: 5px solid transparent;
        border-bottom: none;
        pointer-events: none;
        position: absolute;
        top: 17px;
        left: 142px;
      }
    }

    .page-size {
      display: flex;
      align-items: center;
      width: auto;
      height: 35px;
      padding: 10px 15px;
      margin: 0;

      .page-select {
        white-space: nowrap;
        height: 100%;
        border: 1px solid get('border-color');
        background: get('back');
        min-width: 55px;
        padding: 0 8px 0 12px;
        margin: 0;
        outline: none;
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        cursor: pointer;
        width: auto;
        border-radius: 4px;

        .select-box {
          position: absolute;
          left: -2px;
          width: 100%;
          bottom: 40px;
          background: get('back');
          border: 1px solid get('border-color');
          border-radius: 4px;
          z-index: 11;
          overflow: hidden;

          .seleclt-opotion {
            padding: 0 10px;
            transition: 0.3s cubic-bezier(0.215, 0.61, 0.355, 1);

            &:hover {
              background: get('border-color');
              color: white;
            }
          }
        }
      }
    }
  }
}
</style>
