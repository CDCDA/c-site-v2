<template>
  <div class="await-to-be-done">
    <div class="title">{{ $t('待处理项') }}</div>
    <div class="content">
      <div class="item">
        <div class="label">{{ $t('待处理项1') }}</div>
        <div class="value">{{ $t('待处理项1的值') }}</div>
      </div>
      <div class="item">
        <div class="label">{{ $t('待处理项2') }}</div>
        <div class="value">{{ $t('待处理项2的值') }}</div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'AwaitToBeDone'
});
</script>
<style scoped>
.await-to-be-done {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}
.content {
  display: flex;
  flex-direction: column;
}
.item {
  display: flex;
  margin-bottom: 5px;
}
.label {
  font-weight: bold;
  margin-right: 5px;
}
.value {
  flex: 1;
}
</style>
