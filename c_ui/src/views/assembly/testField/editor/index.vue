<!--
 * @Description: 富文本编辑器模块
-->
<template>
  <editor class="editor" />
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import editor from '@/components/editor/index.vue';

onMounted(() => {});
</script>
<style lang="scss">
.editor {
  width: 100%;
}
</style>
