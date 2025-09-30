<template>
  <div class="page-main essay-editor">
    <Editor />
  </div>
</template>
<script setup lang="ts">
import Editor from './components/editor.vue';
</script>
<style lang="scss" scoped>
@include theme() {
  .page-main {
    background: get('back') !important;
    box-shadow: get('box-shadow');
    padding: 0 !important;
    width: calc(80% - 60px) !important;
    min-height: calc(100vh - 3.7rem) !important;
    position: relative;
    display: flex;
    justify-content: center;
  }
}
</style>
