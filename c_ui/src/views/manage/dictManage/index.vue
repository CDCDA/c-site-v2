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
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { pageDictTypes, deleteDictTypes } from '@/api/system/dict/dictType.ts';
import DictDataDrawer from '@/views/manage/dictManage/components/dictDataDrawer.vue';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';

const searchColumns = ref([
  {
    type: 'input',
    label: $t('字典名称'),
    prop: 'dictName',
    placeholder: $t('请输入字典名称'),
    labelWidth: 40
  },
  {
    type: 'dict',
    label: $t('状态'),
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
    label: $t('序号'),
    width: 55,
    fixed: 'left'
  },
  {
    label: $t('字典名称'),
    prop: 'dictName'
  },
  {
    label: $t('状态'),
    prop: 'status',
    type: 'dict',
    dictType: 'status'
  },
  {
    label: $t('字典类型'),
    prop: 'dictType',
    type: 'link',
    linkType: 'primary',
    click: (row: any) => manageDictData(row)
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
  dictName: null,
  status: null
}) as any;

const addOrUpdateDialogRef = ref(null) as any;
const dictDataDrawer = ref(null) as any;

function handleAdd() {
  addOrUpdateDialogRef.value.init({
    operation: 'add',
    title: $t('新增字典')
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',
    title: $t('编辑字典'),
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
