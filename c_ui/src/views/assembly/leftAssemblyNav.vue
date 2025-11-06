<!--
 * @Description:  
-->
<!--
 * @Description: 
-->
<template>
  <el-menu
    default-active="2"
    class="el-menu-vertical-demo"
    @open="handleOpen"
    @close="handleClose"
    background="rgba(29, 32, 33, 0.6)"
    text-color="#fff"
    active-text-color="#ffd04b"
    router
  >
    <el-menu-item v-for="(item, i) in data" :key="i" @click="clickMenu(item)">
      <span class="first-menu-name" slot="title" :index="item.path">{{ item.label }}</span>
    </el-menu-item>
  </el-menu>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const data = ref([
  {
    AssemblyID: '1',
    path: 'antvG6',
    label: $t('antvg6流程图')
  },
  {
    AssemblyID: '2',
    path: 'cascader',
    label: $t('层级选择器')
  },
  {
    AssemblyID: '3',
    path: 'monacoEditor',
    label: $t('monaco编辑器')
  },
  {
    AssemblyID: '4',
    path: 'selectTree',
    label: $t('下拉树选择框')
  },
  {
    AssemblyID: '5',
    path: 'datePicker',
    label: $t('日期选择器')
  },
  {
    AssemblyID: '6',
    path: 'vForm',
    label: $t('表单设计器')
  },
  {
    AssemblyID: '7',
    path: 'rubiks',
    label: $t('魔方')
  },
  {
    AssemblyID: '100',
    path: 'testComponents',
    label: $t('测试')
  }
]) as any;

function handleOpen() {}
function handleClose() {}
function clickMenu(item: any) {
  router.push({
    name: item.path
  });
}
</script>
<style lang="scss" scoped>
.el-menu-vertical-demo {
  border-radius: 10px;
}
</style>
