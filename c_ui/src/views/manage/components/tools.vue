<!--
 * @Description: 管理页按钮栏
-->
<template>
  <el-row :gutter="10" class="manage-button-group" style="margin-bottom: 15px">
    <el-col :span="1.5">
      <el-button
        type="primary"
        plain
        size="small"
        icon="Plus"
        @click="emit('handleAdd')"
        v-permission="['operate']"
        >{{ $t('新增') }}</el-button
      >
    </el-col>
    <el-col :span="1.5">
      <el-button
        type="success"
        plain
        size="small"
        icon="Edit"
        @click="emit('handleEdit')"
        v-permission="['operate']"
        :disabled="props.selection.length == 0"
        >{{ $t('修改') }}</el-button
      >
    </el-col>
    <el-col :span="1.5" v-if="props.viewButton === true">
      <el-button
        type="warning"
        plain
        size="small"
        icon="View"
        @click="emit('handleView')"
        v-permission="['operate']"
        :disabled="props.selection.length == 0"
        >{{ $t('查看') }}</el-button
      >
    </el-col>
    <el-col :span="1.5">
      <el-button
        type="danger"
        plain
        size="small"
        icon="Delete"
        @click="emit('handleDel')"
        v-permission="['operate']"
        :disabled="props.selection.length == 0"
        >{{ $t('删除') }}</el-button
      >
    </el-col>
    <div class="manage-tools">
      <!--      <el-tooltip :content="$t('菜单')" placement="top">-->
      <!--        <svg-icon iconName="commonSvg-菜单" @click="openMenu" />-->
      <!--      </el-tooltip>-->
      <el-tooltip :content="$t('刷新')" placement="top">
        <svg-icon iconName="commonSvg-刷新" @click="emit('refresh')" />
      </el-tooltip>
      <!--      <el-tooltip :content="$t('全屏')" placement="top">-->
      <!--        <svg-icon iconName="commonSvg-全屏" @click="fullScreen" />-->
      <!--      </el-tooltip>-->
      <el-tooltip :content="$t('搜索栏')" placement="top">
        <svg-icon iconName="commonSvg-搜索" @click="showSearch" />
      </el-tooltip>
    </div>
  </el-row>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
const props = defineProps({
  selection: {
    default: []
  },
  viewButton: {
    default: false
  }
});
const emit = defineEmits(['handleAdd', 'handleEdit', 'handleView', 'handleDel', 'refresh']);

function fullScreen() {
  let pageMain = document.querySelector('.page-main') as any;
  if (!pageMain) return;
  if (pageMain.classList.contains('is-full')) {
    pageMain.classList.remove('is-full');
  } else {
    pageMain.classList.add('is-full');
  }
}
function showSearch() {
  let manageMain = document.querySelector('.manage-main');
  if (!manageMain) return;
  if (manageMain.classList.contains('is-hidden')) {
    manageMain.classList.remove('is-hidden');
  } else {
    manageMain.classList.add('is-hidden');
  }
  //
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
@include theme() {
  .manage-tools {
    position: absolute;
    right: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    .svg-icon {
      font-size: 1.2rem;
      cursor: pointer;
      margin-left: 8px;
      position: relative;
      transition: all 0.2s ease;
    }
    .svg-icon:active {
      transform: translateY(2px);
    }
    .svg-icon:hover {
      &::before {
        content: '';
        width: 28px;
        height: 28px;
        background: get('border-color');
        color: get('re-font-color');
        border-radius: 4px;
        position: absolute;
        z-index: -1;
      }
    }
  }
}
</style>
