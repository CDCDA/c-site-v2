<!--
 * @Description: 美食管理
-->
<template>
  <cTable
    :pageApi="pageCates"
    :deleteApi="deleteCates"
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
import { pageCates, deleteCates } from '@/api/cate.ts';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: $t('名称'),
    prop: 'cateName',
    labelWidth: 40
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
    type: 'image',
    label: $t('封面'),
    prop: 'coverUrl',
    width: 150
  },
  {
    label: $t('美食名'),
    prop: 'name',
    showOverflowTooltip: true
  },
  {
    label: $t('简介'),
    prop: 'intro',
    showOverflowTooltip: true
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
  cateName: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: $t('添加美食')
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: $t('编辑美食'),
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
