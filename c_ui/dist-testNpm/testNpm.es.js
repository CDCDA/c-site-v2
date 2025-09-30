import { defineComponent, ref, resolveComponent, createElementBlock, openBlock, createTextVNode, createVNode, createElementVNode, toDisplayString } from "vue";
const _hoisted_1 = { class: "test-npm-container" };
const _hoisted_2 = { class: "test-npm" };
const _hoisted_3 = { class: "test-npm" };
const _sfc_main = /* @__PURE__ */ defineComponent({
  __name: "index",
  props: {
    message1: {
      type: String,
      default: "默认值"
    }
  },
  setup(__props) {
    const message = ref("");
    const props = __props;
    return (_ctx, _cache) => {
      const _component_el_input = resolveComponent("el-input");
      return openBlock(), createElementBlock("div", _hoisted_1, [
        _cache[1] || (_cache[1] = createTextVNode(toDisplayString("这是一个测试组件") + " ")),
        createVNode(_component_el_input, {
          modelValue: message.value,
          "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => message.value = $event),
          placeholder: "请输入"
        }, null, 8, ["modelValue"]),
        _cache[2] || (_cache[2] = createElementVNode("div", { class: "test-npm" }, toDisplayString("测试传入变量"), -1)),
        createElementVNode("div", _hoisted_2, toDisplayString(message.value), 1),
        createElementVNode("div", _hoisted_3, toDisplayString(props.message1), 1)
      ]);
    };
  }
});
const _export_sfc = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const TestNpmComponent = /* @__PURE__ */ _export_sfc(_sfc_main, [["__scopeId", "data-v-2f6489b5"]]);
const install = (app) => {
  app.component("TestNpm", TestNpmComponent);
};
const TestNpmPlugin = {
  install,
  TestNpmComponent
};
const TestNpm = TestNpmComponent;
export {
  TestNpm,
  TestNpmPlugin as default,
  install
};
