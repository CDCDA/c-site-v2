<!--
 * @Description: 连线绘图
 * @Author: cyd 1205489124@qq.com
 * @Date: 2023-06-26 16:56:13
 * @LastEditTime: 2023-07-24 09:25:59
-->
<template>
  <div id="wrapper" ref="wrapper">
    <div class="top-line"></div>
    <div class="bottom-line"></div>
    <div
      v-for="(item, i) in list"
      :class="['state-item', item.value < 10 ? 'item-low' : '', item.value > 12 ? 'item-high' : '']"
      :id="`item-${i}`"
      :style="{ left: item.x || 0, top: item.y || 0 }"
    >
      <div class="value">{{ item.value }}</div>
      <div class="label">{{ item.name }}</div>
    </div>
    <!--    <div class="line-wrap" style="margin-left: 70px">-->
    <!--      <div id="item-1" class="state-item"></div>-->
    <!--      <div id="item-2" class="state-item"></div>-->
    <!--      <div id="item-3" class="state-item"></div>-->
    <!--    </div>-->
    <!--    <div class="line-wrap">-->
    <!--      <div id="item-4" class="state-item"></div>-->
    <!--      <div id="item-5" class="state-item"></div>-->
    <!--      <div id="item-6" class="state-item"></div>-->
    <!--      <div id="item-7" class="state-item"></div>-->
    <!--    </div>-->
    <!--    <div class="line-wrap" style="margin-left: 215px">-->
    <!--      <div id="item-8" class="state-item"></div>-->
    <!--      <div id="item-9" class="state-item"></div>-->
    <!--    </div>-->
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { jsPlumb } from 'jsplumb';
const wrapper = ref(null) as any;
const list = ref([
  {
    name: 'A',
    value: 10.1
  },
  {
    name: 'A',
    value: 10.2
  },
  {
    name: 'A',
    value: 9.9
  },
  {
    name: 'A',
    value: 12.1
  },
  {
    name: 'A',
    value: 12.2
  },
  {
    name: 'A',
    value: 11.1
  }
]) as any;

function initList() {
  //单位宽度

  const unitX = wrapper.value.offsetWidth / list.value.length;
  const topLine = wrapper.value.querySelector('.top-line');
  const bottomLine = wrapper.value.querySelector('.bottom-line');
  wrapper.value.style.setProperty('--before-width', `${unitX}px`);
  wrapper.value.style.setProperty('--before-width', `${wrapper.value.offsetHeight - 20}px`);
  const top = topLine.offsetTop;
  const bottom = bottomLine.offsetTop;
  const diffHeight = top - bottom;
  list.value.forEach((item: any, i: any) => {
    item.x = `${i * unitX + unitX / 2}px`;
    let diff = parseInt(String(item.value - 10));
    if (diff > 0) {
      item.y = `${bottom - 10 + (Math.abs(diff) / 2) * diffHeight}px`;
    } else {
      item.y = `${bottom - 10 - (Math.abs(diff) / 2) * diffHeight}px`;
    }
  });
  setTimeout(() => {
    init();
  }, 1000);
}

function init() {
  let plumbIns = jsPlumb.getInstance();
  let defaultConfig = {
    anchor: [0.5, 0.5, 0, 0],
    connector: ['Straight'],
    endpoint: 'Blank',
    overlays: [['Arrow', { width: 1, length: 1, location: 1 }]], // overlay
    // 添加样式
    paintStyle: { stroke: '#10de85', strokeWidth: 3 } // connector
  };
  plumbIns.ready(function () {
    list.value.forEach((item: any, i: any) => {
      item;
      console.log('FFF', `item-${i}`, `item-${i + 1}`);
      plumbIns.connect(
        {
          // 对应上述基本概念
          source: `item-${i}`,
          target: `item-${i + 1}`
        },
        defaultConfig
      );
    });
  });
}

onMounted(() => {
  console.log('AAA');
  initList();
});
</script>
<style lang="scss">
#wrapper {
  --before-width: 100px;
  --before-height: 100px;
  background: #042762;
  height: 120px !important;
  width: 700px;
  position: relative;
  .top-line {
    width: 100%;
    height: 2px;
    background: #14376f;
    position: absolute;
    top: 30%;
  }
  .bottom-line {
    width: 100%;
    height: 2px;
    background: #14376f;
    bottom: 30%;
    position: absolute;
  }
}
.state-item {
  width: 10px;
  height: 10px;
  border-radius: 20px;
  background: #042762;
  border: 4px solid #10de85;
  z-index: 1;
  position: absolute;
  color: white;
  .label {
    width: 50px;
    position: absolute;
    bottom: -25px;
    left: -20px;
    text-align: center;
  }
  .value {
    width: 50px;
    position: absolute;
    top: -25px;
    left: -20px;
    text-align: center;
  }

  &::before {
    content: '';
    border-radius: 5px;
    position: absolute;
    width: var(--before-width);
    height: var(--before-height);
    left: calc(var(--before-width) / 2 * -1 + 5px);
    top: calc(var(--before-height) / 2 * -1 + 30px);
    z-index: -1;
  }
}
.item-low {
  background: red;
  border-color: white;
  &::before {
    background: linear-gradient(to bottom, transparent, #c34e5d);
    left: calc(var(--before-width) / 2 * -1 + 5px);
    top: calc(var(--before-height) / 2 * -1 - 15px);
  }
}
.item-high {
  background: red;
  border-color: white;
  &::before {
    background: linear-gradient(to bottom, #c34e5d, transparent);
  }
}
.line-wrap {
  display: flex;
  margin-bottom: 40px;
}
</style>
