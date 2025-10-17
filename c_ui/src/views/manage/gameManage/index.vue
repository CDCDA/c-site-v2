<!--
 * @Description: 游戏管理
-->
<template>
  <cTable
    :pageApi="pageGames"
    :deleteApi="deleteGames"
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
import { pageGames, deleteGames } from '@/api/game.ts';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  { type: 'input', label: '游戏名称', prop: 'name' },
  {
    type: 'dict',
    label: '分类',
    prop: 'type',
    dictType: 'game_type'
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
    label: '游戏名',
    prop: 'name',
    width: 160
  },
  {
    label: '官网',
    type: 'link',
    prop: 'url',
    width: 160,
    showOverflowTooltip: true
  },
  {
    label: '分类',
    type: 'dict',
    dictType: 'game_type',
    prop: 'type',
    width: 100
  },
  {
    label: '评分',
    prop: 'rate',
    width: 100
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
    prop: 'updateTime'
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
    title: '添加游戏'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑游戏',
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
