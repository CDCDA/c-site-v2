<!--
 * @Description: 相册管理
-->
<template>
  <cTable
    :pageApi="pageAlbums"
    :deleteApi="deleteAlbums"
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
import { pageAlbums, deleteAlbums } from '@/api/album.ts';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  { type: 'input', label: '相册名称', prop: 'name' },
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
    width: 180
  },
  {
    label: '相册名称',
    prop: 'name'
  },
  {
    label: '简介',
    prop: 'intro'
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
  name: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: '添加相册'
  });
}
function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: '编辑相册',
    row
  });
}

function getList() {
  tableRef.value?.getList();
}
</script>
