<!--
 * @Description: 基础表格
-->
<template>
  <div class="manage-main" :class="isSearchShow ? 'is-hidden' : ''">
    <searchForm
      :searchColumns="searchColumns"
      :initParams="props.initParams"
      :searchOptions="searchOptions"
      :pagination="pagination"
      @update:pagination="
        pagi => {
          pagination = pagi;
        }
      "
      @getList="getList"
      ref="searchFormRef"
    />
    <div class="manage-table-wrap">
      <tools
        :selection="selection"
        :tableColumns="tableColumns"
        @handleAdd="emit('handleAdd')"
        @handleDelete="handleDelete"
        @refresh="getList"
        @resizeTable="resizeTable"
      >
        <slot name="leftTools" />
      </tools>
      <el-table
        :data="list"
        border
        v-if="isLoad"
        class="manage-table"
        @row-click="handleRowClick"
        ref="tableRef"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <template v-for="column in tableColumns">
          <el-table-column
            v-if="column.type === 'selection' && !column.hidden"
            type="selection"
            :width="column.width || 'auto'"
            align="center"
          />
          <el-table-column
            v-else-if="column.type === 'index' && !column.hidden"
            type="index"
            align="center"
            :fixed="column.fixed"
            :label="column.label"
            :width="column.width || 'auto'"
          />
          <el-table-column
            v-else-if="column.type === 'operation'"
            :label="column.label"
            :fixed="column.fixed"
            :width="column.width || 'auto'"
            align="center"
          >
            <template #default="scope">
              <template v-for="(button, i) in column.buttons">
                <el-button
                  :type="button.type"
                  :icon="getIcon(button)"
                  size="small"
                  @click="handleOperationClick(button, scope.row)"
                  v-permission="['operate']"
                  >{{ button.text }}
                </el-button>
              </template>
            </template>
          </el-table-column>
          <el-table-column
            v-else-if="!column.hidden"
            :show-overflow-tooltip="column.showOverflowTooltip"
            :label="column.label"
            :prop="column.prop"
            :fixed="column.fixed"
            :width="column.width || 'auto'"
            :sortable="column.sortable"
            align="center"
          >
            <template #default="scope">
              <!-- 默认 -->
              <span v-if="!column.type || column.type === 'default'">{{
                scope.row[column.prop] || scope.row[column.prop] === 0
                  ? scope.row[column.prop]
                  : '--'
              }}</span>
              <!-- 图片 -->
              <c-image
                :preview-src-list="[scope.row[column.prop]]"
                v-else-if="column.type === 'image'"
                :src="scope.row[column.prop] || scope.row[column.replaceProp]"
              />
              <!-- 字典 -->
              <dict-tag
                v-else-if="column.type === 'dict'"
                :dict-value="scope.row[column.prop]"
                :dict-type="column.dictType"
              />
              <!-- 字典 -->
              <el-tag v-else-if="column.type === 'tag'" :type="scope.row[column.tagTypeProp]">{{
                scope.row[column.prop]
              }}</el-tag>
              <!-- 链接 -->
              <el-link
                v-else-if="column.type === 'link'"
                :type="column.linkType"
                @click="column.click(scope.row)"
                >{{ scope.row[column.prop] }}
              </el-link>
            </template>
          </el-table-column>
        </template>
      </el-table>
    </div>
    <Pagination
      v-model:page="pagination.pageNum"
      v-model:page-size="pagination.pageSize"
      :total="totalCount"
      :on-page-change="getList"
      class="manage-pagination"
    />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, toRefs, nextTick, watch } from 'vue';
import Pagination from '@/components/pagination/index.vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import searchForm from './components/searchForm.vue';
import { Edit, Delete, View } from '@element-plus/icons-vue';
import tools from './components/tools.vue';
const isLoad = ref(true);
const props = defineProps({
  searchColumns: [] as any,
  tableColumns: [] as any,
  initParams: {} as any,
  pageApi: {} as any,
  deleteApi: {} as any,
  searchOptions: {} as any,
  idKey: {
    type: String,
    default: 'id'
  }
});

const { searchColumns, tableColumns, initParams, pageApi, deleteApi, searchOptions, idKey } = props;

const emit = defineEmits(['handleAdd']);

const pagination = ref({
  pageNum: 1,
  pageSize: 10
}) as any;

const tableRef = ref(null) as any;

const selection = ref([]) as any;

const loading = ref(false) as any;

const isSearchShow = ref(false) as any;

const selectIds = ref([]) as any;

const list = ref([]) as any;

const totalCount = ref(0);

const searchFormRef = ref(null) as any;

// 获取表格数据
async function getList() {
  loading.value = true;
  const params = {
    ...props.initParams,
    ...pagination.value,
    ...searchFormRef.value?.searchParams
  };
  delete params.dateRange;
  console.log(props.initParams, searchFormRef.value?.searchParams);
  const { code, rows, total } = await pageApi(params);
  if (code === 200) {
    list.value = rows;
    totalCount.value = total;
  }
  loading.value = false;
}

function resizeTable() {
  isLoad.value = false;
  nextTick(() => {
    isLoad.value = true;
  });
}

function getIcon(button: any) {
  if (button.icon) {
    return button.icon;
  }
  switch (button.operate) {
    case 'update':
      return Edit;
    case 'delete':
      return Delete;
    case 'view':
      return View;
    default:
      return null;
  }
}

// 点击即勾选
function handleRowClick(row: any) {
  tableRef.value.toggleRowSelection(row);
}

// 选择项改变
function handleSelectionChange(val: any) {
  selection.value = val;
  selectIds.value = selection.value.map((x: any) => x[idKey]);
}

function handleOperationClick(button: any, row: any) {
  if (button.operate === 'delete') {
    handleDelete(row[props.idKey]);
  } else {
    if (button.click) {
      button.click(row);
    } else {
      ElNotification.error($t('请为操作按钮添加点击事件'));
    }
  }
}

async function handleDelete(id?: String) {
  ElMessageBox.confirm($t('是否确认删除选中数据?'), 'Warning', {
    confirmButtonText: $t('确定'),
    cancelButtonText: $t('取消'),
    type: 'warning'
  }).then(async () => {
    let ids = id ? [id] : selectIds.value;
    const { code } = await deleteApi(ids);
    if (code === 200) {
      ElNotification.success($t('删除成功'));
      getList();
    }
  });
}

onMounted(() => {
  getList();
});

defineExpose({
  getList,
  selection
});
</script>
<style lang="scss" scoped></style>
