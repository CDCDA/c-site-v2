<template>
  <div>
    <span v-if="!props.options || props.options.length === 0">
      {{ props.value }}
    </span>
    <el-tag
      v-else
      :disable-transitions="true"
      :type="tag && tag.listClass ? tag.listClass : 'default'"
    >
      {{ tag ? tag.label : props.value }}
    </el-tag>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';

const props = defineProps({
  options: {
    type: Array,
    default: []
  },
  value: {
    default: '0'
  }
});
const tag = computed(() => {
  return props.options.find((x: any) => x.value === props.value);
}) as any;

onMounted(() => {});
</script>

<style scoped lang="scss"></style>
