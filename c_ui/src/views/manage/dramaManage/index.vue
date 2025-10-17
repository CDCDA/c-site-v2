<!--
 * @Description: 博客管理（影视管理）
-->
<template>
  <cTable
    :pageApi="pageDramas"
    :deleteApi="deleteDramas"
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
import { pageDramas, deleteDramas } from '@/api/dramaSeries.ts';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: '影视名称',
    prop: 'name',
    placeholder: '请输入影视名称'
  },
  {
    label: '分类',
    prop: 'type',
    width: 150,
    type: 'dict',
    dictType: 'drama_type'
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
    type: 'image',
    label: '封面',
    prop: 'coverUrl',
    width: 150
  },
  {
    label: '名称',
    prop: 'name',
    width: 220,
    showOverflowTooltip: true
  },
  {
    label: '简介',
    prop: 'intro',
    showOverflowTooltip: true
  },
  {
    label: '分类',
    prop: 'type',
    width: 150,
    type: 'dict',
    dictType: 'drama_type'
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
  name: null,
  type: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: '添加影视'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑影视',
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
