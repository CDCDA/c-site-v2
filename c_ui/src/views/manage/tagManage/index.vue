<!--
 * @Description: 标签管理
-->
<template>
  <div class="tag-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item>
        <el-input
          v-model="queryParams.tagName"
          placeholder="请输入标签名称"
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
        <el-table-column label="标签名称" align="center" prop="tagName" show-overflow-tooltip />
        <el-table-column label="关联文章数" align="center" prop="total" show-overflow-tooltip />
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
  <c-dialog v-model="open" :title="title" width="500" :modal="true">
    <el-form :model="form" label-width="55" ref="formEl" :rules="rules">
      <el-form-item label="名称" prop="tagName">
        <el-input v-model="form.tagName" clearable></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="blogTag"></upload>
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
import { pageTags, deleteTags, saveTag } from '@/api/tag.ts';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import upload from '@/components/upload/upload.vue';
import tools from '../components/tools.vue';
const queryParams = ref({
  tagName: null,
  pageNum: 1,
  pageSize: 10
} as any);
const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const form = ref({
  coverUrl: null,
  intro: '',
  tagName: ''
} as any);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
});

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getList() {
  const { code, msg, rows, total } = (await pageTags(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const res = (await saveTag(form.value)) as any;
      if (res) {
        if (!form.value.tagId)
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
  });
}

function resetQuery() {
  queryParams.value = {
    tagName: null,
    pageNum: 1,
    pageSize: 10
  };
  dateRange.value = [null, null];
}

function selectionChange(val: any) {
  selection.value = val;
  selectIds.value = [];
  val.forEach((e: any) => {
    selectIds.value.push(e.tagId);
  });
}

function resetForm() {
  form.value = {
    coverUrl: null,
    intro: '',
    tagName: ''
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
    const { code, msg, rows, total } = (await deleteTags(ids)) as any;
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
