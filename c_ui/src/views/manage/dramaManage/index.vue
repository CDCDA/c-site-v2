<!--
 * @Description: 博客管理
-->
<template>
  <div class="blog-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item prop="blogTitle">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入影视名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item prop="typeId">
        <el-select
          v-model="queryParams.type"
          clearable
          placeholder="请选择分类"
          @keyup.enter="getList"
        >
          <el-option v-for="item in dict.drama_type" :value="item.value" :label="item.label" />
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
        :data="blogList"
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
        <el-table-column
          label="名称"
          align="center"
          prop="name"
          width="220"
          show-overflow-tooltip
        />
        <el-table-column label="简介" align="center" prop="intro" show-overflow-tooltip />
        <el-table-column label="分类" align="center" prop="type" width="150">
          <template #default="scope">
            <DictTag :options="dict.drama_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="220" prop="createTime" />
        <el-table-column label="修改时间" align="center" width="220" prop="updateTime" />
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
  <c-dialog v-model="open" :title="title" width="600" :modal="true">
    <el-form :model="form" label-width="85" ref="formEl" :rules="rules">
      <el-form-item label="影视名称" prop="name">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="drama" />
      </el-form-item>
      <el-form-item label="分类" prop="type">
        <el-radio-group v-model="form.type" class="ml-4">
          <el-radio :value="item.value" v-for="item in dict.drama_type">
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="播放地址" prop="url">
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
import { pageDramas, deleteDramas, saveDrama, getDramaById } from '@/api/dramaSeries.ts';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import Upload from '@/components/upload/upload.vue';
import tools from '../components/tools.vue';
import { useDict } from '@/utils/dict.ts';
import DictTag from '@/components/dict/dictTag.vue';
const dict = useDict('drama_type');
const title = ref('');

const form = ref({
  coverUrl: null,
  url: null,
  intro: '',
  type: '0',
  name: ''
} as any);
const open = ref(false as any);
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
const isSearchShow = ref(false as any);
const selectIds = ref([] as any);
const blogList = ref([] as any);
const total = ref(0);

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'blur' }],
  name: [{ required: true, message: '请输入影视名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类', trigger: 'blur' }]
});

async function getList() {
  queryParams.value.startTime = dateRange.value[0];
  queryParams.value.endTime = dateRange.value[1];
  const { code, data } = (await pageDramas(queryParams.value)) as any;
  if (code == 200) {
    blogList.value = rows;
    total.value = data.total;
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

function resetForm() {
  form.value = {
    coverUrl: null,
    url: null,
    intro: '',
    type: '0',
    name: ''
  };
}

function selectionChange(val: any) {
  selection.value = val;
  selectIds.value = [];
  val.forEach((e: any) => {
    selectIds.value.push(e.id);
  });
}

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
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
    const { code } = (await deleteDramas(ids)) as any;
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

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const { code } = (await saveDrama(form.value)) as any;
      if (code == 200) {
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

onMounted(() => {
  getList();
});
</script>
<style lang="scss" scoped></style>
