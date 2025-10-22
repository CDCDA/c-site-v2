<!--
 * @Description: 随笔管理
-->
<template>
  <cTable
    :pageApi="pageEssays"
    :deleteApi="deleteEssays"
    :searchColumns="searchColumns"
    :tableColumns="tableColumns"
    :initParams="initParams"
    ref="tableRef"
    @handleAdd="handleAdd"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageEssays, deleteEssays } from '@/api/essay.ts';
import { useRouter } from 'vue-router';
import cTable from '@/components/cTable/index.vue';

const router = useRouter();

const searchColumns = ref([
  {
    type: 'input',
    label: '内容',
    prop: 'content',
    placeholder: '请输入随笔内容'
  },
  {
    type: 'dateRange',
    label: '创建时间',
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
    label: '序号',
    width: 55,
    fixed: 'left'
  },
  {
    label: '内容',
    prop: 'content',
    showOverflowTooltip: true
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
  content: null
}) as any;

function handleAdd() {
  router.push({ name: 'essayManageEditor' });
}

function handleEdit(row: any) {
  router.push({ name: 'essayManageEditor', query: { id: row.id } });
}

function getList() {
  tableRef.value?.getList();
}

onMounted(() => {
  getList();
});
</script>
