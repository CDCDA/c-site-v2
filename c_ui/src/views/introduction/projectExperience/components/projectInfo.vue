<!--
 * @Description: 项目经历
-->
<template>
  <el-dialog
    class="project-info-dialog filter"
    v-model="visible"
    width="100%"
    :modal="true"
    destroy-on-close
    :show-close="false"
    append-to=".dialog-base"
    @close="close"
  >
    <projectSwiper :item-list="projectList" :thumbs-swiper="thumbsSwiper"></projectSwiper>
    <topSwiper
      :item-list="projectList"
      @getThumbsSwiper="getThumbsSwiper"
      @itemClick="itemClick"
    ></topSwiper>
  </el-dialog>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import projectList from '../projectData';
import { useRouter } from 'vue-router';

import topSwiper from './insideSwiper.vue';
import projectSwiper from './projectSwiper.vue';
const thumbsSwiper = ref(null) as any;
const visible = ref(false);
function open() {
  visible.value = true;
}
function close() {
  visible.value = false;
}
function getThumbsSwiper(swiper: any) {
  thumbsSwiper.value = swiper;
}
function itemClick(swiper: any) {
  thumbsSwiper.value = swiper;
}
onMounted(() => {});
defineExpose({
  open,
  close
});
</script>
<style lang="scss">
.project-info-dialog {
  margin-top: 1%;
  border-radius: 0 !important;
  .el-dialog__header {
    display: none !important;
  }
  .el-dialog__body {
    padding: 13px 0 !important;
  }
}
</style>
