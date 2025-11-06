import i18n from '@/locales/i18n';
/*
 * @Description:菜单数据
 */
export const menuData = [
  {
    label: i18n.global.t('博客'),
    icon: '',
    name: '',
    children: [
      { label: i18n.global.t('分类'), icon: 'FolderOpened', name: 'blogType' },
      { label: i18n.global.t('标签'), icon: 'PriceTag', name: 'blogTag' },
      { label: i18n.global.t('统计'), icon: 'Histogram', name: 'statistics' }
    ]
  },
  {
    label: i18n.global.t('简介'),
    icon: '',
    name: '',
    children: [
      { label: i18n.global.t('个人信息'), icon: 'User', name: 'personalProfile' }
      // { label: i18n.global.t('技术栈'), icon: 'Promotion', name: 'technologyStack' },
      // { label: i18n.global.t('项目经历'), icon: 'Postcard', name: 'projectExperience' }
    ]
  },
  {
    label: i18n.global.t('我的'),
    icon: '',
    name: '',
    children: [
      { label: i18n.global.t('随笔'), icon: 'Notebook', name: 'essay' },
      { label: i18n.global.t('相册'), icon: 'Camera', name: 'album' },
      { label: i18n.global.t('装备'), icon: 'Suitcase', name: 'equipment' }
      // { label: i18n.global.t('音乐'), icon: 'Headset', name: 'music' }
    ]
  },
  {
    label: i18n.global.t('组件'),
    icon: '',
    name: '',
    children: [
      { label: i18n.global.t('切片'), icon: 'Orange', name: 'slice' },
      { label: i18n.global.t('试验田'), icon: 'OfficeBuilding', name: 'testField' }
    ]
  },
  {
    label: i18n.global.t('其他'),
    icon: '',
    name: '',
    children: [
      // { label: i18n.global.t('健身'), icon: '', svgIcon: '@/assets/svg/fitness.svg', name: 'fitness' },
      { label: i18n.global.t('游戏'), icon: '', svgIcon: '@/assets/svg/game.svg', name: 'game' },
      { label: i18n.global.t('影视'), icon: 'VideoCamera', name: 'drama' }
      // { label: i18n.global.t('美食'), icon: 'KnifeFork', name: 'gourmet' }
    ]
  }
];
