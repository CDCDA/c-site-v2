<!--
 * @Description: 更新日志管理
-->
<template>
  <div class="log-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label-width="70">
        <el-input
          v-model="queryParams.operation"
          placeholder="请输入更新内容"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item prop="dateRange">
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
        <el-button log="primary" icon="Search" @click="getList">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
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
        <el-table-column
          label="更新内容"
          align="center"
          prop="operation"
          width="auto"
          show-overflow-tooltip
        />
        <el-table-column label="更新时间" width="auto" align="center" prop="operateTime" />
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
  <addLog
    v-if="addLogOpen"
    :title="title"
    :form="form"
    @close="addLogOpen = false"
    @getList="getList"
  ></addLog>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { listUpdateLog, deleteLogs } from '@/api/system/updateLog';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import addLog from '@/views/layout/sideSetting/components/addLog.vue';

import tools from '../components/tools.vue';
const addLogOpen = ref(false);
const queryParams = ref({
  operation: '',
  pageNum: 1,
  pageSize: 10,
  orderBy: 'operate_time'
} as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const form = ref({
  id: null,
  operation: '',
  operateTime: new Date()
} as any);
const tableList = ref([] as any);
const total = ref(0);
const dateRange = ref([] as any);
const manageTable = ref(null) as any;

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
  const { code, data } = (await listUpdateLog(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

function resetQuery() {
  queryParams.value = {
    operation: '',
    pageNum: 1,
    pageSize: 10,
    orderBy: 'operate_time'
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
    operation: '',
    operateTime: new Date()
  };
}

function handleAdd() {
  addLogOpen.value = true;
  title.value = '新增更新日志';
  resetForm();
}

function handleEdit() {
  addLogOpen.value = true;
  title.value = '编辑更新日志';
  form.value = JSON.parse(JSON.stringify(selection.value[0]));
}

async function handleDel() {
  (ElMessageBox as any)
    .confirm('是否确认删除选中数据?', 'Warning', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    .then(async () => {
      let ids = [];
      ids = selectIds.value;
      const { code, msg, rows, total } = (await deleteLogs(ids)) as any;
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
