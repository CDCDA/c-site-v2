<template>
  <div class="container">
    <gantt class="left-container" :tasks="tasks"></gantt>
  </div>
</template>

<script>
import Gantt from './components/ganttChart.vue';
// 中文引入
// import "dhtmlx-gantt/codebase/locale/locale_cn.js";
export default {
  name: 'app',
  components: { Gantt },
  data() {
    return {
      tasks: {
        data: [
          {
            id: 1,
            text: $t('合同签订'),
            start_date: '2021-01-01',
            duration: 45,
            order: 10,
            progress: 0.4,
            open: true
          },
          {
            id: 11,
            text: $t('第一次付款'),
            start_date: '2021-03-01',
            duration: 45,
            order: 10,
            progress: 0.6,
            parent: 1
          },
          {
            id: 12,
            text: $t('第二次付款'),
            start_date: '2021-12-01',
            duration: 45,
            order: 20,
            progress: 0.6,
            parent: 1
          },
          {
            id: 13,
            text: $t('第三次付款'),
            start_date: '2022-01-01',
            duration: 45,
            order: 20,
            progress: 0.6,
            parent: 1
          },
          {
            id: 2,
            text: $t('项目实施'),
            start_date: '2021-01-01',
            duration: 45,
            order: 20,
            progress: 0.4,
            open: true
          },
          {
            id: 21,
            text: $t('准备阶段'),
            start_date: '2021-03-01',
            duration: 78,
            order: 10,
            progress: 0.6,
            parent: 2
          },
          {
            id: 22,
            text: $t('设计阶段'),
            start_date: '2021-04-01',
            duration: 100,
            order: 20,
            progress: 0.6,
            parent: 2
          },
          {
            id: 24,
            text: $t('系统开发'),
            start_date: '2021-06-01',
            duration: 80,
            order: 40,
            progress: 1,
            parent: 2
          },
          {
            id: 25,
            text: $t('系统测试'),
            start_date: '2021-10-01',
            duration: 40,
            order: 50,
            progress: 0.4,
            parent: 2
          },
          {
            id: 26,
            text: $t('上线准备'),
            start_date: '2021-12-01',
            duration: 45,
            order: 60,
            progress: 0.4,
            parent: 2
          }
        ],
        // 脱线
        links: [
          // { id: 1, source: 1, target: 2, type: '0' }
        ]
      }
    };
  }
};
</script>

<style>
.container {
  height: 450px;
  width: 100%;
}
.left-container {
  overflow: hidden;
  position: relative;
  height: 450px;
}
</style>
