<!--
 * @Description: 游戏影视分类环图
-->
<template>
  <baseChart :options="options" class="game-drama-pie"></baseChart>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { reactive, onMounted, watch, ref } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';
import { countGamesByType } from '@/api/game.ts';
import { countDramasByType } from '@/api/dramaSeries.ts';
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
    right: '40%',
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
      center: ['20%', '50%'],
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
    },
    {
      type: 'pie',
      radius: ['40%', '85%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      center: ['80%', '50%'],
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
}) as any;

function init(list: Array<Object>) {
  options.series[0].data = [];
  list.forEach((blog: any) => {
    options.series[0].data.push({
      value: blog.total,
      name: blog.typeName
    });
  });
}

async function getGameList() {
  const { code, data } = (await countGamesByType({})) as any;
  if (code === 200) {
    options.series[0].data = [];
    data.forEach((item: any) => {
      let typeName = '';
      if (item.type == '0') {
        typeName = $t('单机');
      } else if (item.type == '1') {
        typeName = $t('手游');
      }
      options.series[0].data.push({
        value: item.total,
        name: typeName
      });
    });
  }
}

async function getDramaList() {
  const { code, data } = (await countDramasByType({})) as any;
  if (code === 200) {
    options.series[1].data = [];
    data.forEach((item: any) => {
      let typeName = '';
      if (item.type == '0') {
        typeName = $t('电影');
      } else if (item.type == '1') {
        typeName = $t('电视剧');
      } else if (item.type == '2') {
        typeName = $t('动漫');
      }
      options.series[1].data.push({
        value: item.total,
        name: typeName
      });
    });
  }
}

onMounted(() => {
  getGameList();
  getDramaList();
});
</script>
<style lang="scss"></style>
