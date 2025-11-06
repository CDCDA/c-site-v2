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
    idKey="blogId"
    @handleAdd="handleAdd"
  />
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();

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
    label: $t('博客标题'),
    prop: 'blogTitle'
  },
  {
    type: 'select',
    label: $t('分类'),
    prop: 'typeId',
    options: [] // 动态加载
  },
  {
    type: 'select',
    label: $t('标签'),
    prop: 'tagId',
    options: [] // 动态加载
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
    width: 60,
    fixed: 'left',
    hidden: true
  },
  {
    type: 'image',
    label: $t('封面'),
    prop: 'coverUrl',
    width: 150
  },
  {
    label: $t('博客标题'),
    prop: 'blogTitle',
    width: 280,
    showOverflowTooltip: true
  },
  {
    label: $t('博客简介'),
    prop: 'blogAbstract',
    width: 300,
    showOverflowTooltip: true
  },
  {
    label: $t('博客分类'),
    prop: 'typeName',
    width: 120
  },
  {
    label: $t('是否原创'),
    prop: 'isOriginal',
    width: 120,
    type: 'dict',
    dictType: 'blog_original'
  },
  {
    label: $t('是否推荐'),
    prop: 'isRecommend',
    width: 120,
    type: 'dict',
    dictType: 'blog_recommend'
  },
  {
    label: $t('创建时间'),
    prop: 'createTime',
    width: 220
  },
  {
    label: $t('修改时间'),
    prop: 'updateTime',
    width: 220
  },
  {
    label: $t('作者'),
    prop: 'author',
    width: 150
  },
  {
    type: 'operation',
    label: $t('操作'),
    fixed: 'right',
    width: 340,
    buttons: [
      {
        type: 'primary',
        text: $t('编辑'),
        operate: 'update',
        click: (row: any) => handleEdit(row)
      },
      {
        type: 'info',
        text: $t('查看'),
        operate: 'view',
        click: (row: any) => handleView(row)
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
    const typeSelect = searchColumns.value.find((item: any) => item.prop === 'typeId');
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
    const tagSelect = searchColumns.value.find((item: any) => item.prop === 'tagId');
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
