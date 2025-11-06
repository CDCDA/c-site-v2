<!--
 * @Description: 博客统计折线图
-->
<template>
  <baseChart :options="options" class="updateLog-count-line-bar"></baseChart>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
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
    splitLine: {
      show: false // 隐藏背景横线
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
      color: '#EE6666',
      lineStyle: {
        color: '#EE6666'
      }
    },
    {
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'bar',
      barWidth: '15',
      color: '#73C0DE',
      itemStyle: {
        barBorderRadius: [3, 3, 0, 0] // 设置圆角，[topLeft, topRight, bottomRight, bottomLeft]
      },
      barStyle: {
        color: '#5470C6'
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
}) as any;

function init(list: any) {
  list = list.sort((a: any, b: any) => {
    return new Date(a.operateTime).getTime() - new Date(b.operateTime).getTime();
  });
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
  if (code === 200) {
    init(data);
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss"></style>
