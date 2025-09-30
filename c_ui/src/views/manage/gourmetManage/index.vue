<!--
 * @Description: 美食管理
-->
<template>
  <div class="cate-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form
      class="manage-query-form"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      label-width="40"
      size="mini"
    >
      <el-form-item label="名称">
        <el-input
          v-model="queryParams.cateName"
          placeholder="请输入美食名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <!-- <el-form-item label="分类">
        <el-select v-model="queryParams.type">
          <el-option value="0" label="单机"></el-option>
          <el-option value="1" label="手游"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getList">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="c-divider"></div>
    <tools
      @handleAdd="handleAdd"
      @handleEdit="handleEdit"
      @handleDel="handleDel"
      :selection="selection"
      @refresh="getList"
    />
    <el-table :data="tableList" class="manage-table" style="" @selection-change="selectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="封面" align="center" prop="coverUrl" width="150">
        <template #default="scope">
          <c-image :src="scope.row.coverUrl" />
        </template>
      </el-table-column>
      <el-table-column label="美食名" align="center" prop="name" show-overflow-tooltip />
      <!-- <el-table-column label="官网" align="center" prop="url" show-overflow-tooltip>
        <template #default="scope">
          <a :href="scope.row.url" target="_blank" style="text-decoration: none">{{
            scope.row.name
          }}</a>
        </template>
      </el-table-column> -->
      <el-table-column
        label="简介"
        width="auto"
        align="center"
        prop="intro"
        show-overflow-tooltip
      />
      <el-table-column label="创建时间" width="auto" align="center" prop="createTime" />
      <el-table-column label="修改时间" width="auto" align="center" prop="updateTime" />
      <el-table-column label="备注" width="auto" align="center" prop="remark" />
    </el-table>
    <Pagination
      v-model:page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :on-page-change="getList"
      :showSizes="true"
      :pageSizeList="[10, 20, 30]"
      :on-page-size-change="getList"
      class="pagi page-content"
    />
  </div>
  <!-- 新增或编辑 -->
  <c-dialog v-model="open" :title="title" width="500" :modal="true">
    <el-form :model="form" label-width="60">
      <el-form-item label="美食名">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item label="封面">
        <upload v-if="!fromNet" v-model="form.coverUrl" path="gourmet" />
        <el-input v-else v-model="form.coverUrl" clearable />
      </el-form-item>

      <!-- <el-form-item label="分类">
        <el-radio-group v-model="form.type" class="ml-4">
          <el-radio :label="'0'" size="mini">单机</el-radio>
          <el-radio :label="'1'" size="mini">手游</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="做法">
        <el-input v-model="form.url" clearable />
      </el-form-item>
      <el-form-item label="简介">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.intro"
          placeholder="写点什么吧。。。"
          clearable
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submit"> 确定 </el-button>
      </span>
    </template>
  </c-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageCates, deleteCates, saveCate } from '@/api/cate.ts';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import upload from '@/components/upload/upload.vue';

import tools from '../components/tools.vue';
const router = useRouter();
const queryParams = ref({
  name: null,
  pageNum: 1,
  pageSize: 10
} as any);
const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const fromNet = ref(true as any);
const form = ref({
  coverUrl: null,
  url: null,
  intro: '',
  type: '0',
  name: ''
} as any);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);

async function getList() {
  const { code, msg, rows, total } = (await pageCates(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  const res = (await saveCate(form.value)) as any;
  if (res) {
    if (!form.value.id)
      ElNotification({
        title: 'Success',
        message: '新增成功',
        type: 'success'
      });
    else
      ElNotification({
        title: 'Success',
        message: '修改成功',
        type: 'success'
      });
    getList();
    open.value = false;
  }
}

function resetQuery() {
  queryParams.value = {
    name: null,
    pageNum: 1,
    pageSize: 10
  };
  dateRange.value = [null, null];
}

function selectionChange(val: any) {
  selection.value = val;
  selectIds.value = [];
  val.forEach((e: any) => {
    selectIds.value.push(e.id);
  });
}

function resetForm() {
  form.value = {
    coverUrl: null,
    url: null,
    intro: '',
    type: '0',
    name: ''
  };
}

function handleAdd() {
  open.value = true;
  title.value = '新增';
  resetForm();
}

function handleEdit() {
  open.value = true;
  title.value = '编辑';
  form.value = JSON.parse(JSON.stringify(selection.value[0]));
}

// function handleView(item: any) {
//   router.push({ name: 'blogDisplay', query: { blogId: item.blogId } });
// }
async function handleDel() {
  ElMessageBox.confirm('是否确认删除选中数据?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    let ids = [];
    ids = selectIds.value;
    const { code, msg, rows, total } = (await deleteCates(ids)) as any;
    if (code == 200) {
      ElNotification({
        title: 'Success',
        message: '删除成功',
        type: 'success'
      });
      getList();
    }
  });
}

function hideSearch() {
  isSearchShow.value = !isSearchShow.value;
}
onMounted(() => {
  getList();
});
</script>
<style lang="scss" scoped>
@keyframes hidden {
  0% {
    height: 50px;
  }
  100% {
    height: 0;
  }
}
@keyframes show {
  0% {
    height: 0;
  }
  100% {
    height: 50px;
  }
}
@keyframes table-show {
  0% {
    height: calc(100% - 140px);
  }
  100% {
    height: calc(100% - 210px);
  }
}
@keyframes table-hidden {
  0% {
    height: calc(100% - 210px);
  }
  100% {
    height: calc(100% - 140px);
  }
}
</style>
