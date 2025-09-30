<!--
 * @Description: 标签管理
-->
<template>
  <div class="dict-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form
      class="manage-query-form"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      label-width="40"
    >
      <el-form-item>
        <el-input
          v-model="queryParams.tagName"
          placeholder="请输入字典名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.status" clearable>
          <el-option
            v-for="item in dict.status"
            :value="item.value"
            :label="item.label"
          ></el-option>
        </el-select>
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
        <el-table-column label="字典名称" align="center" prop="dictName" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status" show-overflow-tooltip>
          <template #default="scope">
            <DictTag :options="dict.status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="字典类型" align="center" prop="dictType" width="auto">
          <template #default="scope">
            <span class="dictType-column" @click="manageDictData(scope.row)">{{
              scope.row.dictType
            }}</span>
          </template>
        </el-table-column>
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
  <c-dialog v-model="open" :title="title" width="450" :modal="true">
    <el-form :model="form" label-width="55" ref="formEl" :rules="rules">
      <el-form-item label="名称" prop="dictName">
        <el-input v-model="form.dictName" clearable></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="dictType">
        <el-input v-model="form.dictType" clearable></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-switch
          v-model="form.status"
          active-text="正常"
          inactive-text="停用"
          active-value="1"
          inactive-value="0"
        ></el-switch>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.remark"
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
  <DictDataDrawer ref="dictDataDrawer" :dictType="dictType" :title="title"></DictDataDrawer>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { pageDictTypes, deleteDictTypes, saveDictType } from '@/api/system/dict/dictType.ts';
import DictDataDrawer from '@/views/manage/dictManage/components/dictDataDrawer.vue';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import { useDict } from '@/utils/dict.ts';
const dict = useDict('status');
import DictTag from '@/components/dict/dictTag.vue';
import tools from '../components/tools.vue';
const queryParams = ref({
  dictName: null,
  pageNum: 1,
  pageSize: 10
} as any);

const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增') as any;
const dictDataDrawer = ref(null) as any;
const form = ref({
  dictName: null,
  dictType: null,
  status: '1'
} as any);
const drawer = ref(true);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);
const dictType = ref(null) as any;

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }]
});

async function getList() {
  const { code, data } = (await pageDictTypes(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const res = (await saveDictType(form.value)) as any;
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
  });
}

function resetQuery() {
  queryParams.value = {
    dictName: null,
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
    dictName: null,
    dictType: null,
    status: '1'
  };
}

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

function handleAdd() {
  open.value = true;
  title.value = '新增';
  resetForm();
}

function manageDictData(row: any) {
  dictType.value = row.dictType;
  title.value = row.dictName;
  dictDataDrawer.value.openDrawer();
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
    const { code, msg, rows, total } = (await deleteDictTypes(ids)) as any;
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
.dictType-column {
  color: #1b62ff;
  cursor: pointer;
}
</style>
