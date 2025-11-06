<template>
  <el-drawer v-model="drawer" size="60%" :title="title" direction="rtl" :show-close="false">
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
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, watch } from 'vue';
import { pageDicts, deleteDictDatas } from '@/api/system/dict/dictData.ts';
import cTable from '@/components/cTable/index.vue';
import addOrUpdateDialog from './addOrUpdateDialog.vue';
const searchColumns = ref([
  {
    type: 'input',
    label: $t('字典标签'),
    prop: 'dictLabel',
    placeholder: $t('请输入字典标签')
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
    type: 'tag',
    label: $t('字典标签'),
    prop: 'dictLabel',
    tagTypeProp: 'listClass'
  },
  {
    label: $t('字典键值'),
    prop: 'dictValue'
  },
  {
    label: $t('状态'),
    prop: 'status',
    type: 'dict',
    dictType: 'status'
  },
  {
    label: $t('修改时间'),
    prop: 'updateTime',
    width: 220
  },
  {
    label: $t('备注'),
    prop: 'remark'
  },
  {
    label: $t('排序'),
    prop: 'dictSort',
    showOverflowTooltip: true
  },
  {
    type: 'operation',
    label: $t('操作'),
    width: 220,
    fixed: 'right',
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
    title: $t('新增字典数据')
  });
}

function handleEdit(row: any) {
  addOrUpdateDialogRef.value.init({
    operation: 'update',

    title: $t('编辑字典数据'),
    row
  });
}

function getList() {
  tableRef.value?.getList();
}

const title = ref('');

function init(params: any) {
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
