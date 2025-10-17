<!--
 * @Description: 分类管理
-->
<template>
  <cTable
    :pageApi="pageTypes"
    :deleteApi="deleteTypes"
    :searchColumns="searchColumns"
    :tableColumns="tableColumns"
    :initParams="initParams"
    ref="tableRef"
    idKey="typeId"
    @handleAdd="handleAdd"
  />
  <addOrUpdateDialog ref="addOrUpdateDialogRef" @getList="getList" />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageTypes, deleteTypes } from '@/api/type.ts';
import { ElNotification } from 'element-plus';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  { type: 'input', label: '分类名称', prop: 'typeName' },
  { type: 'input', label: '简介', prop: 'intro' }
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
    type: 'image',
    label: '封面',
    prop: 'coverUrl',
    width: 150
  },
  {
    label: '分类名称',
    prop: 'typeName'
  },
  {
    label: '简介',
    prop: 'intro',
    showOverflowTooltip: true
  },
  {
    label: '创建时间',
    prop: 'createTime',
    width: 220
  },
  {
    label: '修改时间',
    prop: 'updateTime',
    width: 220
  },
  {
    label: '备注',
    prop: 'remark'
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
  typeName: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: '添加分类'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑分类',
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
