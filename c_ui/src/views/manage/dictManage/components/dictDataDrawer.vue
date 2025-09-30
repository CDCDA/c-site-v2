<template>
  <el-drawer
    v-model="drawer"
    size="60%"
    @close="close"
    :title="props.title"
    direction="rtl"
    :show-close="false"
  >
    <div class="manage-main">
      <el-form class="manage-query-form" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item>
          <el-input
            v-model="queryParams.dictLabel"
            placeholder="请输入字典标签"
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
          <el-table-column label="字典标签" align="center" prop="dictLabel" show-overflow-tooltip>
            <template #default="scope">
              <el-tag :type="scope.row.listClass">{{ scope.row.dictLabel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="字典键值" align="center" prop="dictValue" show-overflow-tooltip />
          <el-table-column label="状态" align="center" prop="status" show-overflow-tooltip>
            <template #default="scope">
              <DictTag :options="dict.status" :value="scope.row.status" />
            </template>
          </el-table-column>

          <!--          <el-table-column label="创建时间" width="auto" align="center" prop="createTime" />-->
          <el-table-column label="修改时间" align="center" prop="updateTime" width="220" />
          <el-table-column label="备注" width="auto" align="center" prop="remark" />
          <el-table-column label="排序" align="center" prop="dictSort" show-overflow-tooltip />
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
      <el-form :model="form" label-width="85" ref="formEl" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="字典类型" prop="dictType">
              <el-input v-model="form.dictType" clearable disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回显样式" prop="listClass">
              <el-select v-model="form.listClass">
                <el-option
                  v-for="item in dict.tag_css_type"
                  :value="item.value"
                  :label="item.label"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="字典键值" prop="dictValue">
              <el-input v-model="form.dictValue" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字典标签" prop="dictLabel">
              <el-input v-model="form.dictLabel" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-switch
                v-model="form.status"
                active-text="正常"
                inactive-text="停用"
                active-value="1"
                inactive-value="0"
              >
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="dictSort">
              <el-input-number v-model="form.dictSort"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="备注" prop="remark" style="width: 100%">
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
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="open = false">取消</el-button>
          <el-button type="primary" @click="submit"> 确定 </el-button>
        </span>
      </template>
    </c-dialog>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { pageDicts, deleteDictData, saveDictData } from '@/api/system/dict/dictData.ts';
import Pagination from '@/components/pagination/index.vue';
import { ElNotification, ElMessageBox } from 'element-plus';
import tools from '@/views/manage/components/tools.vue';
import { useDict } from '@/utils/dict.ts';
import DictTag from '@/components/dict/dictTag.vue';
const dict = useDict('tag_css_type', 'status');
const props = defineProps({
  title: {
    type: String,
    default: null
  },
  dictType: {
    type: String,
    default: null
  }
});

const queryParams = ref({
  dictLabel: '',
  status: null,
  pageNum: 1,
  pageSize: 10,
  dictType: props.dictType
} as any);
const selection = ref([] as any);
const selectIds = ref([] as any);
const title = ref('新增' as String);
const form = ref({
  dictLabel: null,
  dictType: null,
  dictSort: 0,
  status: '1',
  listClass: 'default'
} as any);
const drawer = ref(false);
const tableList = ref([] as any);
const open = ref(false as any);
const total = ref(0);
const formEl = ref(null) as any;
const manageTable = ref(null) as any;
const rules = ref({
  listClass: [{ required: true, message: '请选择回显样式', trigger: 'blur' }],
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典键值', trigger: 'blur' }],
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
  dictSort: [{ required: true, message: '请输入字典排序', trigger: 'blur' }]
});

async function getList() {
  queryParams.value.dictType = props.dictType;
  const { code, data } = (await pageDicts(queryParams.value)) as any;
  if (code == 200) {
    tableList.value = rows;
    total.value = data.total;
  }
}

watch(
  () => props.dictType,
  () => {
    getList();
  },
  {
    deep: true
  }
);

function handleRowClick(row: any) {
  manageTable.value.toggleRowSelection(row);
}

function openDrawer() {
  drawer.value = true;
}
function close() {
  drawer.value = false;
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const res = (await saveDictData(form.value)) as any;
      if (res.code === 200) {
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
    dictLabel: '',
    status: null,
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
    dictLabel: null,
    dictSort: 0,
    status: '1',
    listClass: 'default',
    dictType: props.dictType
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
    const { code } = (await deleteDictData(ids)) as any;
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

onMounted(() => {});

defineExpose({
  openDrawer,
  close
});
</script>
<style lang="scss" scoped></style>
