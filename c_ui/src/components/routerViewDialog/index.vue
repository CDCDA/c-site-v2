<!--
 * @Description: 路由跳转弹窗
-->
<template>
  <c-dialog
    class="router-view-dialog bounceInLeft animated"
    v-model="dialogVisible"
    :fullscreen="isFull"
    width="88%"
    top="80px"
    :title="props.title"
    :modal="props.modal"
    v-if="dialogVisible"
    @close="emit('close')"
  >
    <slot></slot>
    <router-view />
  </c-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
const dialogVisible = ref(false);
const props = defineProps({
  title: {
    type: String,
    default: null
  },
  modal: {
    type: Boolean,
    default: true
  }
});
const emit = defineEmits(['close']);
const isFull = ref(false);
function close() {
  dialogVisible.value = false;
}
function open() {
  dialogVisible.value = true;
}

function handleFull() {
  isFull.value = !isFull.value;
}

onMounted(() => {
  // getTypeTree();
});
defineExpose({
  close,
  open
});
</script>
<style lang="scss">
@include theme() {
  .router-view-dialog {
    height: 88%;

    .el-dialog__body {
      height: calc(100% - 2.5rem - 13px) !important;
      padding-top: 0 !important;
      & > :first-child {
        border-radius: 8px !important;
        height: 100% !important;
      }
    }
  }
}
</style>
