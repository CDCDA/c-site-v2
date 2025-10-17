<!--
 * @Description: 管理页按钮栏
-->
<template>
  <el-row :gutter="10" class="manage-button-group" style="margin-bottom: 15px">
    <el-col :span="1.5">
      <el-button
        type="primary"
        size="small"
        icon="Plus"
        @click="emit('handleAdd')"
        v-permission="['operate']"
        >新增
      </el-button>
    </el-col>
    <el-col :span="1.5">
      <el-button
        type="danger"
        size="small"
        icon="Delete"
        @click="emit('handleDelete')"
        v-permission="['operate']"
        :disabled="props.selection.length == 0"
        >删除
      </el-button>
    </el-col>
    <div class="manage-tools">
      <el-tooltip content="刷新" placement="top">
        <svg-icon iconName="commonSvg-刷新" @click="emit('refresh')" />
      </el-tooltip>
      <el-tooltip :content="isShowSearch.value ? '隐藏搜索' : '显示搜索'" placement="top">
        <svg-icon iconName="commonSvg-搜索" @click="showSearch" />
      </el-tooltip>
      <el-tooltip content="列控制" placement="top">
        <svg-icon iconName="commonSvg-菜单" @click="showColumnSetting" />
      </el-tooltip>
    </div>
  </el-row>
  <columnSetting
    :tableColumns="props.tableColumns"
    @change="handleColumnChange"
    ref="columnSettingRef"
    @resizeTable="emit('resizeTable')"
  />
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import columnSetting from '../components/columnSetting.vue';
const isShowSearch = ref(true);
const props = defineProps({
  selection: {
    default: []
  },
  tableColumns: {
    default: []
  },
  viewButton: {
    default: false
  }
});
const emit = defineEmits(['handleAdd', 'handleView', 'handleDelete', 'refresh', 'resizeTable']);

function showColumnSetting() {
  columnSettingRef.value.showColumnSetting();
}
function showSearch() {
  let manageMain = document.querySelector('.manage-main');
  if (!manageMain) return;
  if (manageMain.classList.contains('is-hidden')) {
    manageMain.classList.remove('is-hidden');
    isShowSearch.value = true;
  } else {
    manageMain.classList.add('is-hidden');
    isShowSearch.value = false;
  }
}

const columnSettingRef = ref(null) as any;

// 处理列设置改变
function handleColumnChange(val: any) {
  emit('change', val);
}

onMounted(() => {});
</script>
<style>
.circle-menu-dialog {
  .el-dialog__body {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
<style lang="scss" scoped>
.manage-button-group {
  width: 100%;
}
@include theme() {
  .manage-tools {
    position: absolute;
    right: 0px;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    .svg-icon {
      font-size: 1.2rem;
      cursor: pointer;
      margin-left: 8px;
      position: relative;
      // transition: all 0.2s ease;
    }
    .svg-icon:active {
      transform: translateY(2px);
    }
    .svg-icon:focus {
      outline: none;
    }
    // .svg-icon:hover {
    //   &::before {
    //     content: '';
    //     width: 28px;
    //     height: 28px;
    //     background: get('border-color');
    //     color: get('re-font-color');
    //     border-radius: 4px;
    //     position: absolute;
    //     z-index: -1;
    //   }
    // }
  }
}
</style>
