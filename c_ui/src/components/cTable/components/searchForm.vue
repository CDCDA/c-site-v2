<template>
  <el-form :model="searchParams" :inline="true" class="manage-search-form" :label-width="'auto'">
    <div class="search-form-left">
      <el-form-item v-for="item in searchColumns" :key="item.prop" :label="item.label">
        <el-input
          v-if="item.type === 'input' || !item.type"
          v-model="searchParams[item.prop]"
          :placeholder="`请输入${item.label}`"
          clearable
          @keyup.enter="handleSearch"
        />
        <el-select
          v-else-if="item.type === 'select'"
          v-model="searchParams[item.prop]"
          :placeholder="`请选择${item.label}`"
          clearable
          filterable
          @keyup.enter="handleSearch"
        >
          <el-option
            v-for="opt in item.options"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>
        <dict-select
          v-else-if="item.type === 'dict'"
          v-model="searchParams[item.prop]"
          :dict-type="item.dictType"
          :placeholder="`请选择${item.label}`"
          @keyup.enter="handleSearch"
        />
        <el-date-picker
          v-else-if="item.type === 'dateRange'"
          v-model="searchParams[item.prop]"
          type="daterange"
          value-format="YYYY-MM-DD"
          :placeholder="`请选择${item.label}`"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
    </div>
    <div class="search-form-right">
      <el-form-item style="margin-right: 0">
        <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
        <el-button type="info" icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </div>
  </el-form>
</template>
<script setup lang="ts">
import { ref, toRefs } from 'vue';

const props = defineProps({
  searchColumns: [] as any,
  initParams: {} as any,
  pagination: {
    pageNum: 1,
    pageSize: 10
  } as any,
  searchOptions: {
    default: () => ({
      formLabelWidth: 'auto',
      formItemWidth: 'auto'
    })
  } as any
});

const emit = defineEmits(['getList', 'update:pagination']);
const { searchColumns, searchOptions } = toRefs(props);

const searchParams = ref({}) as any;

// 重置查询参数
function handleReset() {
  emit('update:pagination', {
    pageNum: 1,
    pageSize: 10
  });
  searchParams.value = {};
}

// 处理查询
function handleSearch() {
  if (searchParams.value.dateRange) {
    searchParams.value.startTime = searchParams.value.dateRange[0];
    searchParams.value.endTime = searchParams.value.dateRange[1];
  } else {
    delete searchParams.value.startTime;
    delete searchParams.value.endTime;
  }
  emit('getList', searchParams.value);
}

defineExpose({
  searchParams
});
</script>
