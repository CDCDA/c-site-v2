<template>
  <div class="app-container">
    <GridMerge
      ref="gridRef"
      :gridData="gridData"
      :headers="headers"
      :cellWidth="120"
      :cellHeight="40"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import GridMerge from './GridMerge.vue';

const headers = ['列1', '列2', '列3', '列4', '列5'];
const rowLength = ref(5);
const colLength = ref(5);
// 初始化网格数据
const gridData = ref([]);
const gridRef = ref(null);
const reset = () => {
  gridData.value = [];
  for (let i = 1; i <= rowLength.value; i++) {
    for (let j = 1; j <= colLength.value; j++) {
      gridData.value.push({
        rowStart: i,
        rowEnd: i + 1,
        colStart: j,
        colEnd: j + 1,
        hidden: false,
        selected: false,
        merge: false
      });
    }
  }
};
reset();
const merge = () => {
  gridRef.value?.handleMerge();
};
const clear = () => {
  gridRef.value?.clearSelection();
};
</script>

<style>
.app-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.title {
  color: #333;
  margin-bottom: 10px;
}

.instructions {
  color: #666;
  margin-bottom: 20px;
  font-size: 14px;
}
</style>
