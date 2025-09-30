import { autoClearTimer } from './timer';
export const useTableResize = () => {
  autoClearTimer(() => {
    let queryForm = document.querySelector('.manage-query-form') as any;
    let table = document.querySelector('.manage-table') as any;
    let divider = document.querySelector('.c-divider') as any;
    let buttonGroup = document.querySelector('.manage-button-group') as any;
    let pagination = document.querySelector('.c-pagination') as any;
    table.style.height = `calc(100% - ${
      getTotalHeight(queryForm) +
      getTotalHeight(divider) +
      getTotalHeight(buttonGroup) +
      getTotalHeight(pagination) +
      (divider.offsetHeight == 0 ? 0 : 10)
    }px)`;
  }, 200);
};

function getTotalHeight(element: any) {
  if (!element) {
    return 0;
  }
  var style = window.getComputedStyle(element);
  var height = element.offsetHeight; // 获取元素的高度
  var marginTop = parseFloat(style.marginTop); // 解析上边距
  var marginBottom = parseFloat(style.marginBottom); // 解析下边距
  return height + marginTop + marginBottom; // 返回总高度
}
