<template>
  <div class="grid-container">
    <div class="grid-tools">
      <el-input-number v-model="rowLength" placeholder="请输入行数" class="tool-item" />
      <el-input-number v-model="colLength" placeholder="请输入列数" class="tool-item" />
      <el-button @click="reset" class="tool-item">重置</el-button>
      <el-button @click="handleMerge" class="tool-item">合并</el-button>
      <el-button @click="handleClear" class="tool-item">清空选择</el-button>
      <el-button @click="mode = 'designComponent'" class="tool-item">设计组件</el-button>
    </div>
    <!-- 网格内容 -->
    <div
      class="grid-content"
      :style="{
        'grid-template-columns': `repeat(${5}, calc(${20}% - 5px))`,
        'grid-template-rows': `repeat(${5}, calc(${20}% - 5px))`
      }"
    >
      <div
        v-for="(item, index) in gridData"
        class="grid-cell"
        :style="{
          'grid-row': `${item.rowStart} / ${item.rowEnd}`,
          'grid-column': `${item.colStart} / ${item.colEnd}`,
          'background-color': item.selected ? '#6dc542' : 'transparent',
          display: item.hidden ? 'none' : 'block'
        }"
        @click="handleSelect(item)"
        :key="`{${item.rowStart}-${item.colStart}`"
      >
        <el-dropdown
          placement="bottom"
          v-if="mode == 'designComponent'"
          @command="handleCommand($event, item)"
        >
          <el-icon class="setting-icon"><Setting /></el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="component in componentData" :command="component">{{
                component.name
              }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <component :is="item.component" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Setting } from '@element-plus/icons-vue';
import { componentData } from './componentData';

const mode = ref('designGrid');
const props = defineProps({
  gridData: {
    type: Array,
    required: true
  }
});

const rowLength = ref(5);
const colLength = ref(5);

const handleSelect = item => {
  item.selected = !item.selected;
};
const reload = ref(true);
const handleMerge = () => {
  const selectedCells = props.gridData.filter(item => item.selected);
  console.log(selectedCells);

  if (selectedCells.length < 2) {
    ElMessage.error('请至少选择2个单元格');
    return;
  }

  // 1. 检查是否包含已合并单元格
  const hasMergedCell = selectedCells.some(cell => cell.merge);
  if (hasMergedCell) {
    ElMessage.error('选中区域包含已合并单元格，请先取消合并');
    return;
  }

  // 2. 检查是否形成矩形区域
  const rows = [...new Set(selectedCells.map(cell => cell.rowStart))].sort((a, b) => a - b);
  const cols = [...new Set(selectedCells.map(cell => cell.colStart))].sort((a, b) => a - b);
  console.log(rows, cols);

  // 计算理论上应该选中的单元格数量
  const expectedCount =
    (rows[rows.length - 1] - rows[0] + 1) * (cols[cols.length - 1] - cols[0] + 1);

  if (selectedCells.length !== expectedCount) {
    ElMessage.error('选中的单元格必须形成一个矩形区域');
    return;
  }

  // 合并逻辑实现
  console.log('执行合并操作', selectedCells);
  selectedCells.forEach((item, index) => {
    if (index !== 0) {
      item.hidden = true;
      item.selected = false;
    }
  });
  selectedCells[0].merge = true;
  selectedCells[0].rowEnd = rows[rows.length - 1] + 1;
  selectedCells[0].colEnd = cols[cols.length - 1] + 1;
  handleClear();
};

const handleClear = () => {
  props.gridData.forEach(item => {
    item.selected = false;
  });
};

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
        merge: false,
        component: null
      });
    }
  }
};

const handleCommand = (originalCommand, item) => {
  console.log(originalCommand, item);
  item.component = originalCommand.component;
};

onMounted(() => {
  if (!props.gridData || props.gridData.length === 0) {
    reset();
  }
});

defineExpose({
  handleMerge,
  handleClear
});
</script>

<style>
.merge-error {
  color: #ff4d4f;
  margin: 10px 0;
  padding: 8px;
  background-color: #fff1f0;
  border: 1px solid #ffa39e;
  border-radius: 4px;
}
.grid-tools {
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 10px 0px;
  .tool-item {
    margin-right: 10px;
  }
}

.grid-content {
  display: grid;
  position: relative;
  gap: 10px;
  height: 600px;
  width: 800px;
}

.grid-cell:hover {
  background-color: #6dc542;
}

.grid-cell {
  border: 1px solid #ccc;
  height: 100%;
  width: 100%;
  border-radius: 4px;
  cursor: pointer;
  position: relative;
  .el-dropdown {
    right: 0;
    top: 0;
    position: absolute;
    z-index: 100;
  }
}
.setting-icon {
  position: absolute;
  font-size: 20px;
  right: 3px;
  top: 3px;
  cursor: pointer;
}
</style>
