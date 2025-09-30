/*
 * @Description:
 */
import { createApp, Directive } from 'vue';
import Loading from '/src/components/loading/loading.vue';

export const isEmptyObj = (obj: any) => {
  return JSON.stringify(obj) === '{}';
};
/** v-eLoading:[loadingConfig]="state.l||state.a */
export const cLoading: Directive = {
  mounted(el, binding) {
    const app = createApp(Loading);
    const instance = app.mount(document.createElement('div')) as any;
    el.instance = instance;
    el.style.position = 'relative';
    const param = binding.value;
    if (!isEmptyObj(param as any)) {
      const options = {
        type: param
      };
      instance.updateInfo(options);
    }

    if (binding.value) {
      appendEl(el);
    } else {
      removeEl(el);
    }
  },
  updated(el, binding) {
    if (binding.value !== binding.oldValue) {
      el.instance.updateInfo({
        type: binding.value
      });
      // binding.value ? appendEl(el) : removeEl(el);
    }
  }
};
const appendEl = (el: { appendChild: (arg0: any) => void; instance: { $el: any } }) => {
  el.appendChild(el.instance.$el);
};
const removeEl = (el: { removeChild: (arg0: any) => void; instance: { $el: any } }) => {
  el.instance.$el.style.display = 'none';
};
