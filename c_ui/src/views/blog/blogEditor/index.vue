<template>
  <div class="mavon-editor-container page-main"><Editor></Editor></div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import Editor from './editor.vue';
</script>
<style lang="scss">
@include theme() {
  .mavon-editor-container.page-main.full-screen {
    width: 100vw !important;
    height: 100vh !important;
    padding: 0 !important;
    margin: 0 !important;
    left: 0;
    border-radius: 0 !important;
    max-width: none !important;
    position: absolute !important;
    z-index: 99999 !important;
    #md-editor-v3 {
      border-radius: 0;
    }
  }
  .mavon-editor-container.page-main {
    background: get('back') !important;
    color: get('font-color');
    display: flex;
    flex-direction: column;
    min-height: calc(100vh - 3.7rem - 20px) !important;
    overflow: auto;
    overflow-x: hidden;
    //padding: 15px;
    transition: all 0.6s ease;
  }
}
</style>
