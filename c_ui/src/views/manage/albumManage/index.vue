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
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { pageAlbums, deleteAlbums } from '@/api/album.ts';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  { type: 'input', label: $t('相册名称'), prop: 'name' },
  { type: 'input', label: $t('简介'), prop: 'intro' }
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
    label: $t('序号'),
    width: 55,
    fixed: 'left'
  },
  {
    type: 'image',
    label: $t('封面'),
    prop: 'coverUrl',
    width: 150
  },
  {
    label: $t('相册名称'),
    prop: 'name'
  },
  {
    label: $t('简介'),
    prop: 'intro'
  },
  {
    label: $t('创建时间'),
    prop: 'createTime'
  },
  {
    label: $t('修改时间'),
    prop: 'updateTime'
  },
  {
    label: $t('备注'),
    prop: 'remark'
  },
  {
    type: 'operation',
    label: $t('操作'),
    fixed: 'right',
    width: 220,
    buttons: [
      {
        type: 'primary',
        text: $t('编辑'),
        operate: 'update',
        click: (row: any) => handleEdit(row)
      },
      {
        type: 'danger',
        text: $t('删除'),
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
    title: $t('添加相册')
  });
}
function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: $t('编辑相册'),
    row
  });
}

function getList() {
  tableRef.value?.getList();
}
</script>
