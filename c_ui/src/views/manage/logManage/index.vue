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
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { pageLogs, deleteLogs } from '@/api/system/updateLog';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: $t('更新内容'),
    prop: 'operation',
    labelWidth: 70
  },
  {
    type: 'dateRange',
    label: $t('创建时间'),
    prop: 'dateRange'
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
    label: $t('序号'),
    width: 55,
    fixed: 'left'
  },
  {
    label: $t('更新内容'),
    prop: 'operation',
    showOverflowTooltip: true
  },
  {
    label: $t('更新时间'),
    prop: 'operateTime',
    width: 220
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
  operation: null,
  orderBy: 'operate_time'
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: $t('新增更新日志')
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: $t('编辑更新日志'),
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
