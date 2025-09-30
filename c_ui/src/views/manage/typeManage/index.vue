<!--
 * @Description: 分类管理
-->
<template>
  <div class="type-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item>
        <el-input
          v-model="queryParams.typeName"
          placeholder="请输入分类名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getList">搜索</el-button>
        <el-button type="danger" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="manage-table-wrap">
      <tools
        @handleAdd="handleAdd"
        @handleEdit="handleEdit"
        @handleDel="handleDel"
        :selection="selection"
        @refresh="getList"
      />
      <el-table
        :data="tableList"
        border
        class="manage-table"
        @row-click="handleRowClick"
        ref="manageTable"
        @selection-change="selectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="60" label="序号" align="center" />
        <el-table-column label="封面" align="center" prop="isOriginal" width="150">
          <template #default="scope">
            <c-image :src="scope.row.coverUrl" />
          </template>
        </el-table-column>
        <el-table-column label="分类名称" align="center" prop="typeName" show-overflow-tooltip />
        <el-table-column label="简介" align="center" prop="intro" show-overflow-tooltip />
        <el-table-column label="创建时间" width="auto" align="center" prop="createTime" />
        <el-table-column label="修改时间" width="auto" align="center" prop="updateTime" />
        <el-table-column label="备注" width="auto" align="center" prop="remark" />
      </el-table>
    </div>
    <Pagination
      v-model:page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :on-page-change="getList"
      :showSizes="true"
      :pageSizeList="[10, 20, 30]"
      :on-page-size-change="getList"
      class="manage-pagination"
    />
  </div>
  <!-- 新增或编辑 -->
  <c-dialog :title="title" v-model="open" width="500" :modal="true">
    <el-form ref="submitForm" :model="form" label-width="55" :rules="rules">
      <el-form-item label="名称" prop="typeName">
        <el-input v-model="form.typeName" clearable></el-input>
      </el-form-item>
      <el-form-item label="简介" prop="intro">
        <el-input v-model="form.intro" clearable></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="blogType"></upload>
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
import { pageTypes, deleteTypes, saveType } from '@/api/type.ts';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import upload from '@/components/upload/upload.vue';

import tools from '../components/tools.vue';
const submitForm = ref(null as any);
const router = useRouter();
const queryParams = ref({
  typeName: null,
  pageNum: 1,
  pageSize: 10
} as any);

const manageTable = ref(null) as any;

const rules = ref({
  typeName: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  intro: [{ required: true, message: '简介不能为空', trigger: 'blur' }]
});
const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const form = ref({
  coverUrl: '',
  intro: '',
  typeName: ''
} as any);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getList() {
  const { code, msg, rows, total } = (await pageTypes(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  submitForm.value.validate(async (valid: any) => {
    if (valid) {
      const { code } = (await saveType(form.value)) as any;
      if (code == 200) {
        if (!form.value.typeId) {
          ElNotification.success({
            title: 'Success',
            message: '新增成功',
            offset: 100
          });
        } else {
          ElNotification.success({
            title: 'Success',
            message: '修改成功',
            offset: 100
          });
        }
        getList();
        open.value = false;
      }
    }
  });
}

function resetQuery() {
  queryParams.value = {
    typeName: null,
    pageNum: 1,
    pageSize: 10
  };
  dateRange.value = [null, null];
}

function selectionChange(val: any) {
  selection.value = val;
  selectIds.value = [];
  val.forEach((e: any) => {
    selectIds.value.push(e.typeId);
  });
}

function resetForm() {
  form.value = {
    coverUrl: '',
    intro: '',
    typeName: ''
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

async function handleDel() {
  ElMessageBox.confirm('是否确认删除选中数据?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    let ids = [];
    ids = selectIds.value;
    const { code } = (await deleteTypes(ids)) as any;
    if (code == 200) {
      ElNotification.success({
        title: 'Success',
        message: '删除成功',
        offset: 100
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
