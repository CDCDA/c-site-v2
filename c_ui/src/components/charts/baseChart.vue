<!--
 * @Description:基础图
-->
<template>
  <div class="base-chart" :id="id"></div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import * as echarts from 'echarts';
import { onMounted, watch, nextTick } from 'vue';

const id = Math.random().toString(36).slice(-8);
const props = defineProps({
  options: null
});

watch(
  () => props.options,
  val => {
    nextTick(() => {
      val ? init() : '';
    });
  },
  {
    deep: true
  }
);
function init(options?: any) {
  let myChart = echarts.init(document.getElementById(id) as any);
  myChart.setOption(options || props.options);
}

defineExpose({ init });
</script>
<style lang="scss">
.base-chart {
  width: 100%;
  height: 100%;
}
</style>
