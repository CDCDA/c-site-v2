/*
 * @Description:
 */
/*
 * @Description:声明文件
 */
declare module 'particles.vue3';
declare module '@/../lib/vform/designer.umd.js';
declare module 'vue-draggable-resizable';
declare module 'vue-grid-layout';
declare module 'js-cookie';
declare module '@/utils/techStackData.js';
declare module 'vue-mini-weather';
declare module 'vuepress-plugin-sakura';
declare module '@xunlei/vue-lazy-component';
declare module '/weatherCard/script.js';
declare module '/expression/data.js';
declare module 'vue-virtual-scroller-classic';
declare module 'vue-waterfall-easy';
declare module '@wangeditor/editor-for-vue';
declare module '@/locales/i18n';
declare module 'element-plus/dist/locale/zh-cn.mjs';
declare module 'element-plus/dist/locale/zh-tw.mjs';
declare module 'element-plus/dist/locale/en.mjs';
// declare module 'vite-plugin-require-transform';
declare module 'md-editor-v3';

declare module '*.vue' {
  import { ComponentOptions } from 'vue';
  const componentOptions: ComponentOptions;
  export default componentOptions;
}
