<!--
 * @Description: 壁纸管理
-->
<template>
  <cTable
    :pageApi="pageWallpapers"
    :deleteApi="deleteWallpapers"
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
import { pageWallpapers, deleteWallpapers } from '@/api/system/wallpaper';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  { type: 'input', label: '壁纸名称', prop: 'name' },
  {
    type: 'dict',
    label: '分类',
    prop: 'type',
    dictType: 'wallpaper_type'
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
    label: '封面',
    prop: 'coverUrl',
    type: 'image',
    replaceProp: 'url',
    width: 150
  },
  {
    label: '壁纸名称',
    prop: 'name',
    showOverflowTooltip: true
  },
  {
    label: '类型',
    prop: 'type',
    dictType: 'wallpaper_type',
    type: 'dict'
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
    title: '添加壁纸'
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑壁纸',
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
