<!--
 * @Description: 表单设计器
 * @Author: cyd 1205489124@qq.com
 * @Date: 2023-06-26 16:56:13
 * @LastEditTime: 2023-07-24 09:25:59
-->
<template>
  <div class="v-form">
    <v-form-designer
      ref="vfDesigner"
      :designerConfig="config"
      :tableFeildData="tableFeildData"
    ></v-form-designer>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref } from 'vue';
const tableFeildData = ref([]) as any;
const config = ref([]) as any;
</script>
<style lang="scss">
.v-form {
  div,
  span,
  .el-form-item__label,
  i {
    color: black;
    background: transparent;
  }
  .el-container.main-container.full-height {
    // background: transparent;
    border-radius: 5px;
  }
  .el-form--label-left .el-form-item__label {
    justify-content: flex-start;
  }
  .el-container.main-container .el-container.main-content {
    overflow: auto;
  }
}
</style>
