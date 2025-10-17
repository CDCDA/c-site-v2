<template>
  <el-drawer
    v-model="drawer"
    size="60%"
    @close="close"
    :title="title"
    direction="rtl"
    :show-close="false"
  >
    <cTable
      :pageApi="pageDicts"
      :deleteApi="deleteDictDatas"
      :searchColumns="searchColumns"
      :tableColumns="tableColumns"
      :initParams="initParams"
      ref="tableRef"
      @handleAdd="handleAdd"
    />
    <addOrUpdateDialog ref="addOrUpdateDialogRef" @getList="getList" />
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { pageDicts, deleteDictDatas } from '@/api/system/dict/dictData.ts';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';
const searchColumns = ref([
  {
    type: 'input',
    label: '字典标签',
    prop: 'dictLabel',
    placeholder: '请输入字典标签'
  },
  {
    type: 'dict',
    label: '状态',
    prop: 'status',
    dictType: 'status',
    type: 'dict'
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
    type: 'tag',
    label: '字典标签',
    prop: 'dictLabel',
    tagTypeProp: 'listClass'
  },
  {
    label: '字典键值',
    prop: 'dictValue'
  },
  {
    label: '状态',
    prop: 'status',
    type: 'dict',
    dictType: 'status'
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
    label: '排序',
    prop: 'dictSort',
    showOverflowTooltip: true
  },
  {
    type: 'operation',
    label: '操作',
    width: 220,
    fixed: 'right',
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
  dictLabel: null,
  status: null,
  dictType: null
}) as any;

const drawer = ref(false);
const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    dictType: initParams.value.dictType,
    title: '新增字典数据'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    dictType: initParams.value.dictType,
    title: '编辑字典数据',
    row
  });
}

function getList() {
  tableRef.value?.getList();
}

const title = ref('');

function init(params: any) {
  console.log(params);
  initParams.value.dictType = params.dictType;
  title.value = params.title;
  drawer.value = true;
  getList();
}

defineExpose({
  init,
  close
});
</script>
