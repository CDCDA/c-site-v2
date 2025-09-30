<!--
 * @Description: 博客统计折线图
-->
<template>
  <baseChart
    ref="assemblyCountRadarRef"
    :options="options"
    class="assembly-count-radar"
  ></baseChart>
</template>
<script setup lang="ts">
import { reactive, onMounted, watch, ref } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import { autoClearTimer } from '@/utils/timer.ts';
const userStore = useUserStore();
const router = useRouter();
const assemblyCountRadarRef = ref(null) as any;
const options = reactive({
  color: ['#912BD5', '#FFE434', '#56A3F1', '#FF917C'],
  legend: {
    textStyle: {
      color: '#ffffff'
    }
  },
  radar: [
    {
      indicator: [
        { text: 'svg', max: 150 },
        { text: '切片', max: 150 },
        { text: '大模块', max: 150 }
      ],
      center: ['25%', '50%'],
      radius: 60,
      startAngle: 90,
      splitNumber: 5,
      shape: 'circle',
      axisName: {
        formatter: '【{value}】',
        color: '#428BD4',
        fontSize: 15
      },
      splitArea: {
        areaStyle: {
          color: ['#77EADF', '#26C3BE', '#64AFE9', '#428BD4'],
          shadowColor: 'rgba(0, 0, 0, 0.2)',
          shadowBlur: 10
        }
      },
      axisLine: {
        lineStyle: {
          color: 'rgba(211, 253, 250, 0.8)'
        }
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(211, 253, 250, 0.8)'
        }
      }
    },
    {
      indicator: [
        { text: 'svg', max: 150 },
        { text: '切片', max: 150 },
        { text: '大模块', max: 150 }
      ],
      legend: {
        textStyle: {
          color: '#ffffff'
        }
      },
      center: ['75%', '60%'],
      radius: 75,
      axisName: {
        color: '#fff',
        backgroundColor: '#666',
        borderRadius: 3,
        fontSize: 15,
        padding: [3, 5]
      }
    }
  ],
  series: [
    {
      type: 'radar',
      emphasis: {
        lineStyle: {
          width: 4
        }
      },
      data: [
        {
          value: []
        }
      ]
    },
    {
      type: 'radar',
      radarIndex: 1,
      data: [
        {
          value: [],
          symbol: 'rect',
          symbolSize: 12,
          lineStyle: {
            type: 'dashed'
          },
          label: {
            show: true,
            formatter: function (params: any) {
              return params.value;
            }
          }
        }
      ]
    }
  ]
} as any);

const svgData = ref([
  {
    label: '关联网站svg',
    value: 'linkSvg',
    list: []
  },
  {
    label: '技术栈svg',
    value: 'techStackSvg',
    list: []
  },
  {
    label: '社交svg',
    value: 'socialSvg',
    list: []
  },
  {
    label: '音频svg',
    value: 'audioSvg',
    list: []
  },
  {
    label: '像素svg',
    value: 'pixelSvg',
    list: []
  },
  {
    label: '通用svg',
    value: 'commonSvg',
    list: []
  }
] as any);

async function init() {
  options.series[0].data[0].value = [];
  options.series[0].data[0].value.push(getSvgCount());
  options.series[0].data[0].value.push(getSliceCount());
  options.series[0].data[0].value.push(getTestFieldCount());
  options.series[1].data[0].value = options.series[0].data[0].value;
}

function getSvgCount() {
  let svgCount = 0;
  svgData.value.forEach((item: any) => {
    let files = [] as any;
    switch (item.value) {
      case 'audioSvg':
        files = (import.meta as any).glob(`@/assets/svg/audioSvg/*.svg`, { eager: true });
        break;
      case 'socialSvg':
        files = (import.meta as any).glob(`@/assets/svg/socialSvg/*.svg`, { eager: true });
        break;
      case 'linkSvg':
        files = (import.meta as any).glob(`@/assets/svg/linkSvg/*.svg`, { eager: true });
        break;
      case 'techStackSvg':
        files = (import.meta as any).glob(`@/assets/svg/techStackSvg/*.svg`, { eager: true });
        break;
      case 'pixelSvg':
        files = (import.meta as any).glob(`@/assets/svg/pixelSvg/*.svg`, { eager: true });
        break;
      case 'commonSvg':
        files = (import.meta as any).glob(`@/assets/svg/commonSvg/*.svg`, { eager: true });
        break;
      default:
        break;
    }
    svgCount += Object.keys(files).length;
  });
  return svgCount;
}

function getSliceCount() {
  let sliceCount = 0;
  router.getRoutes().forEach((x: any) => {
    if (x.components.default.toString().includes('slice')) {
      sliceCount++;
    }
  });
  return sliceCount;
}

function getTestFieldCount() {
  let testFieldCount = 0;
  router.getRoutes().forEach((x: any) => {
    if (x.components.default.toString().includes('testField')) {
      testFieldCount++;
    }
  });
  return testFieldCount;
}

onMounted(() => {
  init();
});
</script>
<style lang="scss"></style>
