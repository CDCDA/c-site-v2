<template>
  <baseChart
    ref="assemblyCountRadarRef"
    :options="options"
    class="assembly-count-radar"
  ></baseChart>
</template>

<script setup lang="ts">
import { reactive, onMounted, ref } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const assemblyCountRadarRef = ref(null) as any;

// 预计算的SVG数量
const SVG_COUNTS = {
  audioSvg: 10,
  socialSvg: 20,
  linkSvg: 8,
  techStackSvg: 12,
  pixelSvg: 25,
  commonSvg: 30
};

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
}) as any;

function getSvgCount(): number {
  return 152;
}

function getSliceCount(): number {
  return router.getRoutes().filter((x: any) => x.components?.default?.toString().includes('slice'))
    .length;
}

function getTestFieldCount(): number {
  return router
    .getRoutes()
    .filter((x: any) => x.components?.default?.toString().includes('testField')).length;
}

function init() {
  const svgCount = getSvgCount();
  const sliceCount = getSliceCount();
  const testFieldCount = getTestFieldCount();

  options.series[0].data[0].value = [svgCount, sliceCount, testFieldCount];
  options.series[1].data[0].value = [svgCount, sliceCount, testFieldCount];
}

onMounted(() => {
  init();
});
</script>
