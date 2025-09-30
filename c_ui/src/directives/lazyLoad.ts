// 进入可视页面再加载组件的指令
import { DirectiveBinding } from 'vue';

export const lazyLoad = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          // 加载组件
          const component = binding.value;
          el.outerHTML = component;
          observer.unobserve(el);
        }
      });
    });

    observer.observe(el);
  }
};
