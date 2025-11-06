import i18n from '@/locales/i18n';
//主题风格
export const themes = [
  {
    key: 'theme-dark',
    label: i18n.global.t('深色'),
    background: 'linear-gradient(to right, rgba(55, 57, 58, 0.95), rgb(35 37 38 / 95%))',
    active: true
  },
  // {
  //   key: 'theme-light',
  //   label: i18n.global.t('棕黄'),
  //   background: 'linear-gradient(to right, rgb(236 233 230 / 95%), rgb(255 255 255 / 95%))',
  //   active: false
  // },
  {
    key: 'theme-white',
    label: i18n.global.t('白色'),
    background: 'rgb(255 255 255 / 95%)',
    active: false
  }
  // {
  //   key: 'theme-green',
  //   label: i18n.global.t('青色'),
  //   background: 'linear-gradient(to right, rgb(61 159 169/90%), rgb(12 51 76/90%))',
  //   active: false
  // }
];

//字体
export const fontFamilys = [
  {
    value: 'DaoLiTi',
    name: i18n.global.t('刀隶体')
  },
  {
    value: 'FangDaKai',
    label: i18n.global.t('东方大楷')
  },
  { value: 'DingDing', label: i18n.global.t('钉钉进步体') },
  { value: 'Uranus', label: i18n.global.t('天王星像素') },
  { value: 'Shark', label: i18n.global.t('优设鲨鱼菲特健康体') },
  { value: "'Press Start 2P', cursive", label: 'Press Start 2P' },
  {
    value: 'SimSun',
    label: i18n.global.t('宋体')
  },
  {
    value: 'SimHei',
    label: i18n.global.t('黑体')
  },
  {
    value: 'Microsoft YaHei',
    label: i18n.global.t('微软雅黑')
  },
  {
    value: 'LiSu',
    label: i18n.global.t('隶书')
  },
  {
    value: 'FangSong',
    label: i18n.global.t('仿宋')
  },
  {
    value: 'KaiTi',
    label: i18n.global.t('楷体')
  },
  {
    value: 'YaHei',
    label: i18n.global.t('雅黑')
  }
];
