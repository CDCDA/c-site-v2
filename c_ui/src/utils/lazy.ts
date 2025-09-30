/*
 * @Description: 优化后的懒加载动画工具函数
 */

import { autoClearTimer } from './timer';

// 全局观察器实例
const observerMap = new WeakMap<Element, IntersectionObserver>();

export const useLazyAppear = (el: Element, delay: number = 0, animationName: string = 'appear') => {
  if (!el) return;

  // 复用已有观察器
  if (observerMap.has(el)) return;

  const io = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const target = entry.target;
        observer.unobserve(target);
        observerMap.delete(target);

        autoClearTimer(() => {
          target.classList.add(animationName);
          autoClearTimer(() => {
            target.classList.remove(animationName);
          }, 1000);
        }, delay);
      }
    });
  }, {
    threshold: 0.01,  // 降低触发阈值
    rootMargin: '20px' // 预加载区域
  });

  io.observe(el);
  observerMap.set(el, io);
};
