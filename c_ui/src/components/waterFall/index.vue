<template>
  <div class="water-fall-list">
    <div
      class="water-fall-item"
      v-for="(item, index) in waterList"
      :class="`waterFall-img-${index}`"
      :style="{
        width: option.width ? `${option.width}px` : `${100 / option.column}%`
      }"
    >
      <c-image :preview-src-list="[item.url]" :preview-teleported="true" :src="item.url" />
      <!-- <p class="text-box">{{ item.createTime }}</p> -->
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer.ts';
const props = defineProps({
  list: {
    type: Array as any,
    default: [] as any
  }
});

const option = reactive({
  //列数
  column: 4,
  //图片水平间距
  horizontalGap: 20,
  //图片垂直间距
  verticalGap: 20,
  width: null
}) as any;

//列高度数组
const colHeightList = reactive([]) as any;

// 瀑布流数组
const waterList = ref([]) as any;

// 屏幕宽度需要在 mounted 之后拿到
onMounted(() => {
  waterList.value = JSON.parse(JSON.stringify(props.list));
  const { column, horizontalGap, verticalGap } = option;
  let wrap = document.body.querySelector('.water-fall-list') as any;
  // 计算宽度
  option.width = Math.floor((wrap.offsetWidth - horizontalGap * (column - 1)) / column);
  const { width } = option;
  autoClearTimer(() => {
    for (let i = 0; i < waterList.value.length; i++) {
      let localIndex = i % column;
      // 第一行图片
      let el = document.querySelector(`.waterFall-img-${i}`) as any;
      console.log('AAA', el.firstChild.offsetHeight);
      if (i < column) {
        el.style.top = '0px';
        el.style.left = `${(width + horizontalGap) * i}px`;
        el.style.position = 'absolute';
        // 高度数据更新
        if (!colHeightList[localIndex]) colHeightList[localIndex] = 0;
        colHeightList[localIndex] = colHeightList[localIndex] + el.offsetHeight + verticalGap;
      }
      // 后面的就要一张张塞进去，每次找出最低的列往里塞
      else {
        // 最低的高度，先默认为第一列高度
        let current = colHeightList[0];
        // 最低的列，先默认为第一个
        let col = 0;
        // 循环每一列进行比较
        colHeightList.forEach((h: any, i: any) => {
          if (h < current) {
            current = h;
            col = i;
          }
        });
        // 由此计算出该图片的 left、top
        el.style.left = `${(width + horizontalGap) * col}px`;
        el.style.top = `${current}px`;
        el.style.position = 'absolute';
        // 更新列高度数组
        colHeightList[col] = current + verticalGap + el.offsetHeight;
      }
    }
    let current = colHeightList[0];
    let col = 0;
    colHeightList.forEach((h: any, i: any) => {
      if (h > current) {
        current = h;
        col = i;
      }
    });
    console.log(colHeightList);
    wrap.style.height = `${current}px`;
  }, 3000);
});
</script>
<style lang="scss" scoped>
.water-fall-list {
  position: relative;
  display: flex;
  height: 100%;
  flex-wrap: wrap;
  width: 100%;
  .water-fall-item {
    position: absolute;
    left: 0;
    top: 0;
    border-radius: 8px;
    width: 33%;
    overflow: hidden;
    transition: all 0.4s ease;
    .c-image {
      display: block;
    }
    .text-box {
      font-weight: 500px;
      font-size: 0.9rem;
      color: #000000;
    }
  }
  .waterFall-img {
    width: 200px;
  }
}
</style>
