<template>
  <div class="test-npm-container">
    {{ $t('这是一个测试组件') }}
    <el-input v-model="message" :placeholder="$t('请输入')" />
    <div class="test-npm">{{ $t('测试传入变量') }}</div>
    <div class="test-npm">{{ message }}</div>
    <div class="test-npm">{{ props.message1 }}</div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, watch } from 'vue';
import { defineProps } from 'vue';
const message = ref('');
const props = defineProps({
  message1: {
    type: String,
    default: $t('默认值')
  }
});
</script>
<style scoped lang="scss">
.test-npm-container {
  background: #fff;
}
.test-npm {
  color: red;
}
</style>
