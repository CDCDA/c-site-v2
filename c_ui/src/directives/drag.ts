import i18n from '@/locales/i18n';
import { Directive } from 'vue';
import { autoClearTimer } from '@/utils/timer';
// 拖拽的指令
export const cDrag: Directive = {
  mounted(el: any, binding: any) {
    autoClearTimer(() => {
      const dialogHeaderEl = el.querySelector('.el-dialog__header');
      const dragDom = binding.value && binding.value.dragSelf ? el : el.querySelector('.el-dialog');
      dialogHeaderEl.style = 'cursor:move;';
      // 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
      const sty = dragDom.currentStyle || window.getComputedStyle(dragDom, null);
      dialogHeaderEl.onmousedown = (e: MouseEvent) => {
        // 鼠标按下，计算当前元素距离可视区的距离
        const disX = e.clientX - dialogHeaderEl.offsetLeft;
        const disY = e.clientY - dialogHeaderEl.offsetTop;
        // 获取到的值带px 正则匹配替换
        let styL = 0 as any;
        let styT = 0 as any;
        if (sty.left.includes('%')) {
          styL = +document.body.clientWidth * (+sty.left.replace(/\%/g, '') / 100);
          styT = +document.body.clientHeight * (+sty.top.replace(/\%/g, '') / 100);
        } else {
          styL = +sty.left.replace(/\px/g, '');
          styT = +sty.top.replace(/\px/g, '');
        }
        document.onmousemove = (e: MouseEvent) => {
          // 通过事件委托，计算移动的距离
          const l = e.clientX - disX;
          const t = e.clientY - disY;
          // 移动当前元素
          dragDom.style.left = `${l + styL}px`;
          dragDom.style.top = `${t + styT}px`;
        };

        document.onmouseup = () => {
          document.onmousemove = null;
          document.onmouseup = null;
        };
      };
    }, 0);
  }
};
