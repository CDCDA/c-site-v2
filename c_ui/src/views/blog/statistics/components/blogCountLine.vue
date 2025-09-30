<!--
 * @Description: 博客统计折线图
-->
<template>
  <baseChart :options="options" class="blog-count-chart"></baseChart>
</template>
<script setup lang="ts">
import { reactive, onMounted, watch, ref } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import { countBlogsByDate } from '@/api/blog';
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
    name: '单位:篇',
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
  list.forEach((blog: any) => {
    options.xAxis.data.push(blog.createTime);
    options.series[0].data.push(blog.total);
  });
}

async function getList() {
  const { code, msg, rows, total } = (await countBlogsByDate({
    userId: userStore.userId
  })) as any;
  if (code === 200 && data) {
    init(data);
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss"></style>
