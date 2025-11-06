import i18n from '@/locales/i18n';
import CardFlip from '@/views/assembly/slice/cardFlip/index.vue';
import HeatLoading from '@/views/assembly/slice/heartLoading/index.vue';
import NeonRain from '@/views/assembly/slice/neonRain/index.vue';

export const componentData = [
  {
    name: i18n.global.t('卡片'),
    component: CardFlip
  },
  {
    name: i18n.global.t('心跳加载'),
    component: HeatLoading
  },
  {
    name: i18n.global.t('霓虹雨'),
    component: NeonRain
  }
];
