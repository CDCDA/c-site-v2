<!--
 * @Description: 更新日志管理
-->
<template>
  <cTable
    :pageApi="pageLogs"
    :deleteApi="deleteLogs"
    :searchColumns="searchColumns"
    :tableColumns="tableColumns"
    :initParams="initParams"
    ref="tableRef"
    @handleAdd="handleAdd"
  />
  <addOrUpdateDialog ref="addOrUpdateDialogRef" @getList="getList" />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageLogs, deleteLogs } from '@/api/system/updateLog';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: '更新内容',
    prop: 'operation',
    labelWidth: 70
  },
  {
    type: 'datepicker',
    label: '更新时间',
    prop: 'dateRange',
    dateType: 'daterange',
    rangeSeparator: '至',
    startPlaceholder: '开始日期',
    endPlaceholder: '结束日期',
    valueFormat: 'YYYY-MM-DD'
  }
]) as any;

const tableRef = ref(null) as any;

const tableColumns = ref([
  {
    type: 'selection',
    width: 55,
    fixed: 'left'
  },
  {
    type: 'index',
    label: '序号',
    width: 55,
    fixed: 'left'
  },
  {
    label: '更新内容',
    prop: 'operation',
    showOverflowTooltip: true
  },
  {
    label: '更新时间',
    prop: 'operateTime',
    width: 220
  },
  {
    type: 'operation',
    label: '操作',
    fixed: 'right',
    width: 220,
    buttons: [
      {
        type: 'primary',
        text: '编辑',
        operate: 'update',
        click: (row: any) => handleEdit(row)
      },
      {
        type: 'danger',
        text: '删除',
        operate: 'delete'
      }
    ]
  }
]) as any;

const initParams = ref({
  operation: null,
  orderBy: 'operate_time'
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: '新增更新日志'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑更新日志',
    row
  });
}

function getList() {
  tableRef.value?.getList();
}

onMounted(() => {
  getList();
});
</script>
