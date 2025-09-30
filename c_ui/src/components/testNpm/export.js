/*
 * @Description: 单独导出 tesetNpm 组件
 */

// 导入 Pagination 组件
import TestNpmComponent from './index.vue';

// 定义插件安装方法
export const install = (app) => {
  app.component('TestNpm', TestNpmComponent);
};

// 导出组件和插件
const TestNpmPlugin = {
  install,
  TestNpmComponent
};

// 默认导出
export default TestNpmPlugin;

// 同时支持按需导入
export const TestNpm = TestNpmComponent;