// i18n.js - 简化版本
import { createI18n } from 'vue-i18n';
import zhCn from './zh_cn.json';
import zhHant from './zh_hant.json';
import enUs from './en_us.json';

// ElementPlus 语言包
import zhCnElement from 'element-plus/dist/locale/zh-cn.mjs';
import zhTwElement from 'element-plus/dist/locale/zh-tw.mjs';
import enElement from 'element-plus/dist/locale/en.mjs';

const i18n = createI18n({
  legacy: false,
  locale: window.localStorage.getItem('language') || 'zh_cn',
  messages: {
    en_us: { ...enUs, el: enElement.el },
    zh_cn: { ...zhCn, el: zhCnElement.el },
    zh_hant: { ...zhHant, el: zhTwElement.el }
  }
});

// 动态获取 ElementPlus 语言配置
export const getElementPlusLocale = () => {
  const currentLocale = i18n.global.locale.value;
  const messages = i18n.global.getLocaleMessage(currentLocale);
  return messages.el || zhCnElement;
};

export { i18n };
export default i18n;
