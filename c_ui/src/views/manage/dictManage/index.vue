<!--
 * @Description: 字典管理
-->
<template>
  <cTable
    :pageApi="pageDictTypes"
    :deleteApi="deleteDictTypes"
    :searchColumns="searchColumns"
    :tableColumns="tableColumns"
    :initParams="initParams"
    ref="tableRef"
    @handleAdd="handleAdd"
  />
  <addOrUpdateDialog ref="addOrUpdateDialogRef" @getList="getList" />
  <DictDataDrawer ref="dictDataDrawer" />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageDictTypes, deleteDictTypes } from '@/api/system/dict/dictType.ts';
import DictDataDrawer from '@/views/manage/dictManage/components/dictDataDrawer.vue';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: '字典名称',
    prop: 'dictName',
    placeholder: '请输入字典名称',
    labelWidth: 40
  },
  {
    type: 'dict',
    label: '状态',
    prop: 'status',
    dictType: 'status'
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
    label: '字典名称',
    prop: 'dictName'
  },
  {
    label: '状态',
    prop: 'status',
    type: 'dict',
    dictType: 'status'
  },
  {
    label: '字典类型',
    prop: 'dictType',
    type: 'link',
    linkType: 'primary',
    click: (row: any) => manageDictData(row)
  },
  {
    label: '创建时间',
    prop: 'createTime'
  },
  {
    label: '修改时间',
    prop: 'updateTime'
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
  dictName: null,
  status: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;
const dictDataDrawer = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: '新增字典'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑字典',
    row
  });
}

function manageDictData(row: any) {
  dictDataDrawer.value?.init({
    dictType: row.dictType,
    title: row.dictName
  });
}

function getList() {
  tableRef.value?.getList();
}

onMounted(() => {
  getList();
});
</script>

<style lang="scss" scoped>
.dictType-column {
  color: #5a8cf8;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}
</style>
