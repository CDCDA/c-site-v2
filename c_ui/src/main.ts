/*
 * @Description:入口文件
 */
import { createApp } from 'vue';
import App from './App.vue';
//粒子特效
import Particles from 'particles.vue3';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import image from './components/image/image.vue';
import dialog from './components/dialog/index.vue';
import '@/assets/styles/animate.scss';
import '@/assets/styles/element-ui-out-app.scss';
import '@/assets/styles/mobile.scss';
import 'md-editor-v3/lib/preview.css';
import VueGridLayout from 'vue-grid-layout';
import router from './router';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import directives from '@/directives/index';
import pinia from './store';
import VueLazyComponent from '@xunlei/vue-lazy-component';
import SvgIcon from './components/icon-component.vue';
import 'virtual:svg-icons-register';
import visibleLazy from '@/components/visibleLazy/visibleLazy.vue';
/* vForm 表单构建器 */
// import VForm3 from '@/../lib/vform/designer.umd.js';
// import '@/../lib/vform/designer.style.css';
// import { useRouter } from 'vue-router';

// const router = useRouter();

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.component('svg-icon', SvgIcon);
app.component('visible-lazy', visibleLazy);
app.component('c-dialog', dialog);
app.component('c-image', image);
app.provide('$theme', 'theme-light');
app.use(directives);
// app.use(VForm3);
app.use(pinia);
app.use(ElementPlus, { zIndex: 3000 });
app.use(Particles);
app.use(router);
app.use(VueGridLayout);
app.use(VueLazyComponent);

// app.use(MdEditor);
app.mount('#app');
