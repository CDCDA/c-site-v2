<!--
 * @Description: 随笔管理
-->
<template>
  <div class="essay-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item>
        <el-input
          v-model="queryParams.content"
          placeholder="请输入随笔内容"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        >
        </el-date-picker>
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
        <el-table-column label="图片(仅展示第一张)" align="center" prop="isOriginal" width="150">
          <template #default="scope">
            <c-image :src="scope.row.images[0]" />
          </template>
        </el-table-column>
        <el-table-column label="内容" align="center" prop="content" show-overflow-tooltip />
        <el-table-column label="创建时间" width="250" align="center" prop="createTime" />
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
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageEssays, saveEssay, deleteEssays } from '@/api/essay.ts';
import { useRouter } from 'vue-router';
import { autoClearTimer } from '@/utils/timer';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
const dateRange = ref([null, null] as any);
import tools from '../components/tools.vue';
const router = useRouter();
const queryParams = ref({
  content: '',
  pageNum: 1,
  pageSize: 10
} as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const form = ref({
  id: null,
  content: '',
  imageRelations: []
} as any);
const tableList = ref([] as any);
const total = ref(0);

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'blur' }],
  name: [{ required: true, message: '请输入影视名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类', trigger: 'blur' }]
});

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getList() {
  if (dateRange.value) {
    queryParams.value.startTime = dateRange.value[0];
    queryParams.value.endTime = dateRange.value[1];
  } else {
    queryParams.value.startTime = '';
    queryParams.value.endTime = '';
  }
  const { code, data } = (await pageEssays(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

function resetQuery() {
  queryParams.value = {
    content: '',
    pageNum: 1,
    pageSize: 10
  };
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
    id: null,
    content: '',
    imageRelations: []
  };
}

function handleAdd() {
  resetForm();
  router.push({ name: 'essayManageEditor' });
}

function handleEdit() {
  router.push({ name: 'essayManageEditor', query: { id: selection.value[0].id } });
}

async function handleDel() {
  ElMessageBox.confirm('是否确认删除选中数据?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    let ids = [];
    ids = selectIds.value;
    const { code, msg, rows, total } = (await deleteEssays(ids)) as any;
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
