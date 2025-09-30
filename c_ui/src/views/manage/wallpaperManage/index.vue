<!--
 * @Description: 壁纸管理
-->
<template>
  <div class="wallpaper-manage manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item>
        <el-input
          v-model="queryParams.name"
          placeholder="请输入壁纸名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.type" placeholder="请选择分类" clearable>
          <el-option v-for="item in dict.wallpaper_type" :value="item.value" :label="item.label" />
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
        <el-table-column label="封面" align="center" prop="url" width="150">
          <template #default="scope">
            <c-image v-if="scope.row.type == 'img'" :src="scope.row.url" />
            <c-image v-if="scope.row.type == 'video'" :src="scope.row.coverUrl" />
            <div
              class="el-image c-image"
              v-if="scope.row.type == 'color'"
              :style="{ background: scope.row.url }"
            />
          </template>
        </el-table-column>
        <el-table-column label="壁纸名称" align="center" prop="name" show-overflow-tooltip />
        <el-table-column label="类型" align="center" prop="type">
          <template #default="scope">
            <DictTag :options="dict.wallpaper_type" :value="scope.row.type" />
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
  <c-dialog v-model="open" width="500" :title="title" :modal="true">
    <el-form :model="form" label-width="85" ref="formEl" :rules="rules">
      <el-form-item label="壁纸名称" prop="name">
        <el-input v-model="form.name" clearable></el-input>
      </el-form-item>
      <el-form-item label="壁纸类型" prop="type">
        <el-select v-model="form.type">
          <el-option label="静态壁纸" value="img" />
          <el-option label="动态壁纸" value="video" />
          <el-option label="纯色壁纸" value="color" />
        </el-select>
      </el-form-item>
      <el-form-item label="壁纸路径" prop="url">
        <el-input v-model="form.url" clearable></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl" v-if="form.type == 'video'">
        <upload v-model="form.coverUrl" path="wallpaper"></upload>
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
import { pageWallpapers, deleteWallpapers, saveWallpaper } from '@/api/system/wallpaper';
import { useRouter } from 'vue-router';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import upload from '@/components/upload/upload.vue';
import { useDict } from '@/utils/dict.ts';
const dict = useDict('wallpaper_type');
import tools from '../components/tools.vue';
import DictTag from '@/components/dict/dictTag.vue';
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
const form = ref({
  coverUrl: '',
  url: '',
  name: '',
  type: 'img',
  id: null
} as any);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);

const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  name: [{ required: true, message: '请输入壁纸名称', trigger: 'blur' }],
  url: [{ required: true, message: '请输入壁纸路径', trigger: 'blur' }],
  type: [{ required: true, message: '请选择壁纸类型', trigger: 'blur' }]
});

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

async function getList() {
  const { code, msg, rows, total } = (await pageWallpapers(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const { code } = (await saveWallpaper(form.value)) as any;
      if (code == 200) {
        if (!form.value.id)
          ElNotification.success({
            title: 'Success',
            message: '新增成功',
            offset: 100
          });
        else
          ElNotification.success({
            title: 'Success',
            message: '修改成功',
            offset: 100
          });

        getList();
        open.value = false;
      }
    }
  });
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
    coverUrl: '',
    url: '',
    name: '',
    type: 'img',
    id: null
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

function getType(row: any) {
  let typeName = null;
  switch (row.type) {
    case 'img':
      typeName = '静态壁纸';
      break;
    case 'video':
      typeName = '动态壁纸';
      break;
    case 'color':
      typeName = '纯色壁纸';
      break;
    default:
      break;
  }
  return typeName;
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
    const { code, msg, rows, total } = (await deleteWallpapers(ids)) as any;
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
