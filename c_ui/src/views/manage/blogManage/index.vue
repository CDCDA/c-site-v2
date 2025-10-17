<!--
 * @Description: 博客管理
-->
<template>
  <cTable
    :pageApi="pageBlogs"
    :deleteApi="deleteBlogs"
    :searchColumns="searchColumns"
    :tableColumns="tableColumns"
    :initParams="initParams"
    ref="tableRef"
    @handleAdd="handleAdd"
  />
</template>

<script setup lang="ts" name="blogManage">
import { ref, onMounted } from 'vue';
import { pageBlogs, deleteBlogs } from '@/api/blog.ts';
import { pageTypes } from '@/api/type.ts';
import { pageTags } from '@/api/tag.ts';
import { useRouter } from 'vue-router';
import cTable from '@/components/cTable/index.vue';

const router = useRouter();

const searchColumns = ref([
  {
    type: 'input',
    label: '博客标题',
    prop: 'blogTitle'
  },
  {
    type: 'select',
    label: '分类',
    prop: 'typeId',
    options: [] // 动态加载
  },
  {
    type: 'select',
    label: '标签',
    prop: 'tagId',
    options: [] // 动态加载
  },
  {
    type: 'datepicker',
    label: '创建时间',
    prop: 'dateRange',
    dateType: 'daterange',
    rangeSeparator: '至',
    startPlaceholder: '开始日期',
    endPlaceholder: '结束日期',
    valueFormat: 'YYYY-MM-DD'
  }
]) as any;

const tableRef = ref(null) as any;

const tableColumns = ref([
  {
    type: 'selection',
    width: 55,
    fixed: 'left',
    fixed: 'left'
  },
  {
    type: 'index',
    label: '序号',
    width: 60,
    fixed: 'left'
  },
  {
    type: 'image',
    label: '封面',
    prop: 'coverUrl',
    width: 150
  },
  {
    label: '博客标题',
    prop: 'blogTitle',
    width: 280,
    showOverflowTooltip: true
  },
  {
    label: '博客简介',
    prop: 'blogAbstract',
    width: 300,
    showOverflowTooltip: true
  },
  {
    label: '博客分类',
    prop: 'typeName',
    width: 120
  },
  {
    label: '是否原创',
    prop: 'isOriginal',
    width: 120,
    type: 'dict',
    dictType: 'blog_original'
  },
  {
    label: '是否推荐',
    prop: 'isRecommend',
    width: 120,
    type: 'dict',
    dictType: 'blog_recommend'
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
    label: '作者',
    prop: 'author',
    width: 150
  },
  {
    type: 'operation',
    label: '操作',
    fixed: 'right',
    width: 320,
    fixed: 'right',
    buttons: [
      {
        type: 'primary',
        text: '编辑',
        operate: 'update',
        click: (row: any) => handleEdit(row)
      },
      {
        type: 'info',
        text: '查看',
        operate: 'view',
        click: (row: any) => handleView(row)
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
  blogTitle: null,
  typeId: null,
  tagId: null
}) as any;

const typeList = ref([]) as any;
const tagList = ref([]) as any;

function handleAdd() {
  router.push({ name: 'blogManageEditor' });
}

function handleEdit(row: any) {
  router.push({ name: 'blogManageEditor', query: { blogId: row.blogId } });
}

function handleView(row: any) {
  router.push({ name: 'blogDisplay', query: { blogId: row.blogId } });
}

async function getTypeList() {
  const { code, rows } = await pageTypes({});
  if (code === 200) {
    typeList.value = rows;
    // 更新搜索选项
    const typeSelect = searchColumns.value.find(item => item.prop === 'typeId');
    if (typeSelect) {
      typeSelect.options = rows.map((item: any) => ({
        value: item.typeId,
        label: item.typeName
      }));
    }
  }
}

async function getTagList() {
  const { code, rows } = await pageTags({});
  if (code === 200) {
    tagList.value = rows;
    // 更新搜索选项
    const tagSelect = searchColumns.value.find(item => item.prop === 'tagId');
    if (tagSelect) {
      tagSelect.options = rows.map((item: any) => ({
        value: item.tagId,
        label: item.tagName
      }));
    }
  }
}

function getList() {
  tableRef.value?.getList();
}

onMounted(() => {
  getTypeList();
  getTagList();
});
</script>
