<!--
 * @Description: 博客管理
-->
<template>
  <div class="blog-manage manage-main">
    <el-form
      class="manage-query-form"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      label-width="40"
    >
      <el-form-item prop="blogTitle">
        <el-input
          v-model="queryParams.blogTitle"
          placeholder="请输入博客标题"
          clearable
          @keyup.enter="getBlogList"
        />
      </el-form-item>
      <el-form-item prop="typeId">
        <el-select v-model="queryParams.typeId" clearable placeholder="请选择分类">
          <el-option v-for="item in typeList" :value="item.typeId" :label="item.typeName" />
        </el-select>
      </el-form-item>
      <el-form-item prop="tagId">
        <el-select v-model="queryParams.tagId" clearable placeholder="请选择标签">
          <el-option v-for="item in tagList" :value="item.tagId" :label="item.tagName" />
        </el-select>
      </el-form-item>
      <el-form-item prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          clearable
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getBlogList">搜索</el-button>
        <el-button type="danger" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="manage-table-wrap">
      <tools
        @handleAdd="handleAdd"
        @handleEdit="handleEdit"
        @handleView="handleView"
        @handleDel="handleDel"
        :selection="selection"
        :viewButton="true"
        @refresh="getBlogList"
      />
      <el-table
        :data="blogList"
        border
        class="manage-table"
        @row-click="handleRowClick"
        ref="manageTable"
        @selection-change="selectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="60" label="序号" align="center" />
        <el-table-column label="封面" align="center" prop="coverUrl" width="150">
          <template #default="scope">
            <c-image :src="scope.row.coverUrl" />
          </template>
        </el-table-column>
        <el-table-column label="博客标题" align="center" prop="blogTitle" show-overflow-tooltip />
        <el-table-column label="博客分类" width="120" align="center" prop="typeName" />
        <el-table-column label="是否原创" width="120" align="center" prop="isOriginal">
          <template #default="scope">
            <DictTag :options="dict.blog_original" :value="scope.row.isOriginal" />
          </template>
        </el-table-column>
        <el-table-column label="是否推荐" width="120" align="center" prop="isRecommend">
          <template #default="scope">
            <DictTag :options="dict.blog_recommend" :value="scope.row.isRecommend" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="250" align="center" prop="createTime" />
        <el-table-column label="修改时间" width="250" align="center" prop="updateTime" />
        <el-table-column label="作者" width="150" align="center" prop="author" />
      </el-table>
    </div>
    <Pagination
      v-model:page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :on-page-change="getBlogList"
      :showSizes="true"
      :pageSizeList="[10, 20, 30]"
      :on-page-size-change="getBlogList"
      class="manage-pagination"
    />
  </div>
</template>
<script setup lang="ts" name="blogManage">
import { ref, onMounted } from 'vue';
import { pageBlogs, deleteBlogs } from '@/api/blog.ts';
import { pageTypes } from '@/api/type.ts';
import { pageTags } from '@/api/tag.ts';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import { useDict } from '@/utils/dict.ts';
const dict = useDict('blog_original', 'blog_recommend');
const manageTable = ref(null) as any;
import tools from '../components/tools.vue';
import DictTag from '@/components/dict/dictTag.vue';
const router = useRouter();
const queryParams = ref({
  blogTitle: null,
  typeId: null,
  tagId: null,
  startTime: null,
  endTime: null,
  pageNum: 1,
  pageSize: 10
} as any);
const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const selectIds = ref([] as any);
const blogList = ref([] as any);
const typeList = ref([] as any);
const tagList = ref([] as any);
const total = ref(0);

async function getBlogList() {
  if (dateRange.value) {
    queryParams.value.startTime = dateRange.value[0];
    queryParams.value.endTime = dateRange.value[1];
  } else {
    queryParams.value.startTime = '';
    queryParams.value.endTime = '';
  }
  // delete queryParams.value.dateRange;
  const { code, msg, rows, total } = (await pageBlogs(queryParams.value)) as any;
  if (code == 200) {
    blogList.value = rows;
    total.value = data.total;
  }
}

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getTypeList() {
  const { code, msg, rows, total } = (await pageTypes({})) as any;
  if (code == 200) {
    typeList.value = rows;
  }
}

async function getTagList() {
  const { code, msg, rows, total } = (await pageTags({})) as any;
  if (code == 200) {
    tagList.value = rows;
  }
}

function resetQuery() {
  queryParams.value = {
    blogTitle: '',
    typeId: '',
    tagId: '',
    pageNum: 0,
    pageSize: 10
  };
  dateRange.value = [null, null];
}

function selectionChange(val: any) {
  selection.value = val;
  selectIds.value = [];
  val.forEach((e: any) => {
    selectIds.value.push(e.blogId);
  });
}

function handleAdd() {
  router.push({ name: 'blogManageEditor' });
}
function handleEdit() {
  router.push({ name: 'blogManageEditor', query: { blogId: selection.value[0].blogId } });
}
function handleView() {
  router.push({ name: 'blogDisplay', query: { blogId: selection.value[0].blogId } });
}
async function handleDel() {
  ElMessageBox.confirm('是否确认删除选中数据?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    appendTo: '.messageBox-base'
  }).then(async () => {
    let ids = [];
    ids = selectIds.value;
    const { code } = (await deleteBlogs(ids)) as any;
    if (code == 200) {
      ElNotification.success('删除成功');
      getBlogList();
    }
  });
}

onMounted(() => {
  //
  getBlogList();
  getTypeList();
  getTagList();
});
</script>
<style lang="scss" scoped></style>
