import i18n from '@/locales/i18n';
import { cDrag } from './drag';
import { permission } from './permission';
import { cLoading } from './cLoading';
import { lazyLoad } from './lazyLoad';

// 导出的对象 key 保持驼峰命名
const directivesList = {
  permission,
  cLoading,
  lazyLoad
} as any;

export default {
  install(app: any) {
    Object.keys(directivesList).forEach(key => {
      // 在这里注册指令
      app.directive(key, directivesList[key]);
    });
  }
};
