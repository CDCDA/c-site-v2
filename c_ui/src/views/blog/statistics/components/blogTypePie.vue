<!--
 * @Description: 博客分类环图
-->
<template>
  <baseChart :options="options" class="blog-type-chart"></baseChart>
</template>
<script setup lang="ts">
import { reactive, onMounted, watch, ref } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import { countBlogsByType } from '@/api/blog';
import useUserStore from '@/store/modules/user';

const userStore = useUserStore();
const options = reactive({
  tooltip: {
    trigger: 'item',
    textStyle: {
      color: 'black'
    }
  },
  legend: {
    type: 'scroll',
    orient: 'vertical',
    right: '20%',
    top: '20%',
    bottom: 20,
    textStyle: {
      color: '#ffffff'
    }
  },
  series: [
    {
      type: 'pie',
      radius: ['40%', '85%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      center: ['25%', '50%'],
      label: {
        show: true,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1048, name: 'Search Engine' },
        { value: 735, name: 'Direct' },
        { value: 580, name: 'Email' },
        { value: 484, name: 'Union Ads' },
        { value: 300, name: 'Video Ads' }
      ]
    }
  ]
} as any);

function init(list: Array<Object>) {
  options.series[0].data = [];
  list.forEach((blog: any) => {
    options.series[0].data.push({
      value: blog.total,
      name: blog.typeName
    });
  });
}

async function getList() {
  const { code, msg, rows, total } = (await countBlogsByType({ userId: userStore.userId })) as any;
  if (code === 200 && data) {
    init(data);
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss"></style>
