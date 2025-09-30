<!--
 * @Description: 游戏管理
-->
<template>
  <div class="game-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item>
        <el-input
          v-model="queryParams.name"
          placeholder="请输入游戏名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.type" placeholder="请选择分类" clearable>
          <el-option v-for="item in dict.game_type" :value="item.value" :label="item.label" />
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
        <el-table-column type="index" width="55" label="序号" align="center" />
        <el-table-column label="封面" align="center" prop="coverUrl" width="150">
          <template #default="scope">
            <c-image :src="scope.row.coverUrl" />
          </template>
        </el-table-column>
        <el-table-column
          label="游戏名"
          align="center"
          prop="name"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column label="官网" align="center" prop="url" width="160" show-overflow-tooltip>
          <template #default="scope">
            <a :href="scope.row.url" target="_blank" style="text-decoration: none">{{
              scope.row.name
            }}</a>
          </template>
        </el-table-column>
        <el-table-column label="分类" align="center" prop="type" width="100" show-overflow-tooltip>
          <template #default="scope">
            <DictTag :options="dict.game_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column
          label="评分"
          align="center"
          prop="rate"
          width="100"
          show-overflow-tooltip
        />
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
  <c-dialog v-model="open" :title="title" width="700" :modal="true">
    <el-form :model="form" label-width="70" ref="formEl" :rules="rules">
      <el-form-item label="游戏名" prop="name">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="game" />
      </el-form-item>
      <el-form-item label="分类" prop="type">
        <el-radio-group v-model="form.type" class="ml-4">
          <el-radio :label="'0'">单机</el-radio>
          <el-radio :label="'1'">手游</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="官网" prop="url">
        <el-input v-model="form.url" clearable />
      </el-form-item>
      <el-form-item label="评分" prop="rate">
        <el-input type="number" v-model="form.rate" clearable />
      </el-form-item>
      <el-form-item label="简介" prop="intro">
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
import { pageGames, deleteGames, saveGame } from '@/api/game.ts';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import upload from '@/components/upload/upload.vue';
import { useDict } from '@/utils/dict.ts';
const dict = useDict('game_type');
import tools from '../components/tools.vue';
import DictTag from '@/components/dict/dictTag.vue';
const queryParams = ref({
  name: '',
  pageNum: 1,
  pageSize: 10,
  type: ''
} as any);
const dateRange = ref([null, null] as any);
const selection = ref([] as any);
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
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

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'blur' }],
  name: [{ required: true, message: '请输入游戏名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类', trigger: 'blur' }],
  rate: [{ required: true, message: '请输入评分', trigger: 'blur' }]
});
function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getList() {
  const { code, msg, rows, total } = (await pageGames(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const res = (await saveGame(form.value)) as any;
      if (res) {
        console.log(form.value);
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
        await getList();
        open.value = false;
      }
    }
  });
}

function resetQuery() {
  queryParams.value = {
    name: '',
    pageNum: 1,
    pageSize: 10,
    type: ''
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

async function handleDel() {
  ElMessageBox.confirm('是否确认删除选中数据?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    let ids = [];
    ids = selectIds.value;
    const { code } = (await deleteGames(ids)) as any;
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
