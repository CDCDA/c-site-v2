<!--
 * @Description: 博客统计折线图
-->
<template>
  <baseChart :options="options" class="updateLog-count-line-bar"></baseChart>
</template>
<script setup lang="ts">
import { reactive, onMounted } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import { countLogsByDate } from '@/api/system/updateLog.ts';
import useUserStore from '@/store/modules/user';

const userStore = useUserStore();
const options = reactive({
  tooltip: {},
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      textStyle: {
        // fontSize: '0.7rem',
        color: 'white' // 设置X轴标签文字颜色为蓝色
      }
    }
  },
  yAxis: {
    type: 'value',
    name: '单位:次',
    nameTextStyle: {
      //y轴上方单位的颜色
      color: '#fff'
    },
    axisLabel: {
      textStyle: {
        // fontSize: '0.7rem',
        color: 'white' // 设置X轴标签文字颜色为蓝色
      }
    }
  },
  series: [
    {
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'line',
      lineStyle: {
        color: '#61D3FB'
      }
    },
    {
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'bar',
      barWidth: '20px',
      barStyle: {
        color: '#61D3FB'
      }
    }
  ],
  grid: {
    show: false,
    top: '15%', // 一下数值可为百分比也可为具体像素值
    right: '3%',
    bottom: '15%',
    left: '5%'
  }
} as any);

function init(list: any) {
  options.xAxis.data = [];
  options.series[0].data = [];
  options.series[1].data = [];
  list.forEach((blog: any) => {
    options.xAxis.data.push(blog.operateTime);
    options.series[0].data.push(blog.total);
    options.series[1].data.push(blog.total);
  });
}

async function getList() {
  const { code, data } = (await countLogsByDate({})) as any;
  if (code === 200 && data) {
    init(data);
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss"></style>
