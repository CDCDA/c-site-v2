<template>
  <el-drawer title="列控制" v-model="visible" @close="handleClose" size="33%" :show-close="false">
    <el-alert
      title="隐藏某列后剩余列没有对齐是因为没有设置auto宽度"
      type="warning"
      style="margin-bottom: 10px"
    />
    <el-table :data="props.tableColumns" border style="border-radius: 4px; overflow: hidden">
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column prop="label" label="列名" align="center">
        <template #default="scope">
          {{ scope.row.label || '--' }}
        </template>
      </el-table-column>
      <el-table-column prop="prop" label="宽度" align="center" width="90">
        <template #default="scope">
          <el-input v-model="scope.row.width" />
        </template>
      </el-table-column>
      <el-table-column prop="prop" label="固定" align="center" width="180">
        <template #default="scope">
          <el-radio-group v-model="scope.row.fixed" @change="handleChange">
            <el-radio-button label="left"> 左 </el-radio-button>
            <el-radio-button :label="undefined"> 无 </el-radio-button>
            <el-radio-button label="right"> 右 </el-radio-button>
          </el-radio-group>
        </template>
      </el-table-column>
      <el-table-column prop="hidden" label="隐藏" width="80" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.hidden"
            @change="handleChange"
            :active-value="true"
            :nactive-value="false"
          />
        </template>
      </el-table-column>
      <el-table-column prop="width" label="排序" width="80" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.sortable"
            @change="handleChange"
            :active-value="true"
            :nactive-value="false"
          />
        </template>
      </el-table-column>
    </el-table>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
const props = defineProps({
  tableColumns: {
    default: []
  }
});

const visible = ref(false);
const emit = defineEmits(['change']);
function handleChange(val: any) {
  console.log('val', val);
  // emit('change', val);
}

function resizeTable() {
  emit('resizeTable');
}

function showColumnSetting() {
  visible.value = true;
}

function handleClose() {
  visible.value = false;
}
defineExpose({
  showColumnSetting
});
</script>
