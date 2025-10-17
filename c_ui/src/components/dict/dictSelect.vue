<template>
  <el-select
    class="dict-select"
    v-model="props.value"
    :placeholder="props.placeholder"
    filterable
    clearable
  >
    <el-option
      v-for="item in dict[props.dictType]"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useDict } from '@/utils/dict.ts';
const dict = ref({}) as any;

const props = defineProps({
  dictType: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  value: {
    default: null
  }
});

onMounted(() => {
  dict.value = useDict([props.dictType]);
});
</script>
