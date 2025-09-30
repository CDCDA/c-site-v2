/*
 * @Description:菜单数据
 */
export const menuData = [
  {
    label: '博客',
    icon: '',
    name: '',
    children: [
      { label: '分类', icon: 'FolderOpened', name: 'blogType' },
      { label: '标签', icon: 'PriceTag', name: 'blogTag' },
      { label: '统计', icon: 'Histogram', name: 'statistics' }
    ]
  },
  {
    label: '简介',
    icon: '',
    name: '',
    children: [
      { label: '个人信息', icon: 'User', name: 'personalProfile' }
      // { label: '技术栈', icon: 'Promotion', name: 'technologyStack' },
      // { label: '项目经历', icon: 'Postcard', name: 'projectExperience' }
    ]
  },
  {
    label: '我的',
    icon: '',
    name: '',
    children: [
      { label: '随笔', icon: 'Notebook', name: 'essay' },
      { label: '相册', icon: 'Camera', name: 'album' },
      { label: '装备', icon: 'Suitcase', name: 'equipment' }
      // { label: '音乐', icon: 'Headset', name: 'music' }
    ]
  },
  {
    label: '组件',
    icon: '',
    name: '',
    children: [
      { label: '切片', icon: 'Orange', name: 'slice' },
      { label: '试验田', icon: 'OfficeBuilding', name: 'testField' }
    ]
  },
  {
    label: '其他',
    icon: '',
    name: '',
    children: [
      // { label: '健身', icon: '', svgIcon: '@/assets/svg/fitness.svg', name: 'fitness' },
      { label: '游戏', icon: '', svgIcon: '@/assets/svg/game.svg', name: 'game' },
      { label: '影视', icon: 'VideoCamera', name: 'drama' }
      // { label: '美食', icon: 'KnifeFork', name: 'gourmet' }
    ]
  }
];
