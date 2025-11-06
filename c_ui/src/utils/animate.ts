import i18n from '@/locales/i18n';
import { fr } from 'element-plus/es/locale';

const animateTimeList = ['0s5', '0s7', '1s', '1s2', '1s5', '1s7', '2s', '2s2', '2s5', '2s7', '3s'];

export function getAnimateTime(frequency?: any) {
  if (!frequency) frequency = 9;
  return `animated-${animateTimeList[Math.floor(Math.random() * 9) + 1]}`;
}
