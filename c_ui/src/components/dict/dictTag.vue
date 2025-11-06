<template>
  <div class="dict-tag">
    <span v-if="!dict || !dict[props.dictType]">
      {{ props.dictValue }}
    </span>
    <el-tag
      v-else
      :disable-transitions="true"
      :type="tag && tag.listClass ? tag.listClass : 'default'"
    >
      {{ tag ? tag.label : props.dictValue }}
    </el-tag>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t:$t } = useI18n();
import { computed, onMounted, ref } from 'vue';
import { useDict } from '@/utils/dict.ts';
const dict = ref({}) as any;
const props = defineProps({
  dictType: {
    type: String,
    default: ''
  },
  dictValue: {
    default: '0'
  }
});
const tag = computed(() => {
  return dict.value[props.dictType].find((x: any) => String(x?.value) === String(props.dictValue));
}) as any;

onMounted(() => {
  dict.value = useDict([props.dictType]);
});
</script>

<style scoped lang="scss"></style>
