import { resolveComponent, createElementBlock, openBlock, createElementVNode, normalizeClass, createVNode, withCtx, createCommentVNode, toDisplayString, Fragment, renderList, createTextVNode, withModifiers } from "vue";
const autoClearTimer = (func, delay = 0) => {
  let timer = setTimeout(() => {
    func();
    clearInterval(timer);
  }, delay);
};
const _export_sfc = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main = {
  name: "Pagination",
  props: {
    page: {
      type: Number,
      default: 1
    },
    total: {
      type: Number,
      default: 0
    },
    pageSize: {
      type: Number,
      default: 10
    },
    onPageChange: {
      type: Function,
      default: () => {
      }
    },
    onPageSizeChange: {
      type: Function,
      default: () => {
      }
    },
    // 选择分页size
    showSizes: {
      type: Boolean,
      default: false
    },
    // 页码list
    pageSizeList: {
      type: Array,
      default: [10, 20]
    }
  },
  data() {
    return {
      pages: 0,
      slices: [[1]],
      showPageList: false
    };
  },
  watch: {
    page() {
      this.updateSlices();
    },
    total() {
      this.updateSlices();
    },
    pageSize() {
      this.updateSlices();
    }
  },
  mounted() {
    this.updateSlices();
  },
  methods: {
    arialLabel(i) {
      if (i === -1) {
        return "上一页";
      }
      if (i === 0) {
        return "下一页";
      }
      return `第${i}页`;
    },
    updateSlices() {
      const pages = this.pages = Math.ceil(this.total / this.pageSize);
      if (pages < 5) {
        this.slices = [
          Array(pages).fill(1).map((o, i) => i + 1)
        ];
      } else if (this.page < 4) {
        this.slices = [[1, 2, 3], [-1, 4], [pages]];
      } else if (pages - this.page < 3) {
        this.slices = [[1], [-1, 2], [pages - 2, pages - 1, pages]];
      } else {
        this.slices = [
          [1],
          [-1, 2],
          [this.page - 1, this.page, this.page + 1],
          [-1, this.page + 2],
          [pages]
        ];
      }
    },
    // 选择size
    onSize(e) {
      this.$emit("update:pageSize", e);
      this.$emit("update:page", 1);
      this.onPageChange();
      this.showPageList = false;
    },
    onPage(e) {
      let target = e.target;
      if (target.tagName === "SPAN") {
        target = target.parentElement;
      }
      if (target.className.indexOf("disabled") !== -1 || target.tagName !== "LI") {
        return;
      }
      const page = +target.getAttribute("data-page");
      const jumper = target.getAttribute("data-jumper");
      if (jumper) {
        this.$emit("update:page", page);
        this.onPageChange(page);
      } else {
        this.$emit("update:page", this.calcNextPage(page));
        this.onPageChange(this.calcNextPage(page));
      }
    },
    calcNextPage(page) {
      return !page ? this.page + 1 : page < 0 ? this.page - 1 : page;
    },
    showJumper(num, target) {
      if (num && num > 0 && num <= this.pages) {
        const slices = [...this.slices];
        slices[num][2] = 1;
        this.slices = slices;
        autoClearTimer(() => {
          target.children[1].focus();
        }, 100);
      }
    },
    onJump(e) {
      const val = +e.target.value;
      if (val && val > 0 && val <= this.pages) {
        this.onPageChange(val);
      }
    },
    onBlur(e) {
      const num = +e.target.parentNode.getAttribute("data-jumper");
      const slices = [...this.slices];
      slices[num][2] = 0;
      this.slices = slices;
    }
  }
};
const _hoisted_1 = { class: "c-pagination" };
const _hoisted_2 = ["aria-disabled", "arial-label"];
const _hoisted_3 = {
  class: "pagi-number",
  style: { "display": "flex", "align-items": "center", "margin-right": "30px" }
};
const _hoisted_4 = { style: { "display": "flex", "align-items": "center" } };
const _hoisted_5 = ["data-page", "data-jumper", "arial-label"];
const _hoisted_6 = ["data-page", "arial-label"];
const _hoisted_7 = {
  key: 0,
  class: "page-size"
};
const _hoisted_8 = {
  key: 0,
  class: "select-box"
};
const _hoisted_9 = ["onClick"];
const _hoisted_10 = ["aria-disabled", "arial-label"];
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_ArrowLeftBold = resolveComponent("ArrowLeftBold");
  const _component_el_icon = resolveComponent("el-icon");
  const _component_svg_icon = resolveComponent("svg-icon");
  const _component_ArrowRightBold = resolveComponent("ArrowRightBold");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createElementVNode("ul", {
      class: "pager clear",
      onClick: _cache[1] || (_cache[1] = (...args) => $options.onPage && $options.onPage(...args))
    }, [
      createElementVNode("li", {
        class: normalizeClass([{ disabled: $props.page <= 1 }, "pn prev"]),
        "aria-disabled": $props.page <= 1,
        "arial-label": $options.arialLabel(-1),
        tabindex: "0",
        "data-page": "-1",
        role: "button"
      }, [
        createVNode(_component_el_icon, null, {
          default: withCtx(() => [
            createVNode(_component_ArrowLeftBold)
          ]),
          _: 1
        })
      ], 10, _hoisted_2),
      createElementVNode("div", _hoisted_3, [
        createElementVNode("div", _hoisted_4, [
          createVNode(_component_svg_icon, {
            iconName: "commonSvg-总共",
            style: { "height": "25px", "width": "25px" }
          }),
          createElementVNode("span", null, toDisplayString($props.total) + "条", 1)
        ]),
        (openBlock(true), createElementBlock(Fragment, null, renderList($data.slices, (group, index) => {
          return openBlock(), createElementBlock(Fragment, null, [
            group[0] === -1 ? (openBlock(), createElementBlock("li", {
              key: "g" + index,
              "data-page": group[1],
              "data-jumper": index,
              "arial-label": $options.arialLabel(group[1]),
              class: "pn jumper"
            }, _cache[2] || (_cache[2] = [
              createElementVNode("span", { class: "dots" }, "...", -1)
            ]), 8, _hoisted_5)) : (openBlock(true), createElementBlock(Fragment, { key: 1 }, renderList(group, (i) => {
              return openBlock(), createElementBlock("li", {
                key: "l" + i,
                class: normalizeClass([{ active: $props.page === i }, "pn"]),
                "data-page": i,
                "arial-label": $options.arialLabel(i),
                role: "button"
              }, [
                createElementVNode("span", null, toDisplayString(i), 1)
              ], 10, _hoisted_6);
            }), 128))
          ], 64);
        }), 256)),
        $props.showSizes ? (openBlock(), createElementBlock("div", _hoisted_7, [
          createElementVNode("div", {
            class: "page-select",
            onClick: _cache[0] || (_cache[0] = ($event) => $data.showPageList = !$data.showPageList)
          }, [
            createTextVNode(toDisplayString($props.pageSize) + "条/页 ", 1),
            $data.showPageList ? (openBlock(), createElementBlock("div", _hoisted_8, [
              (openBlock(true), createElementBlock(Fragment, null, renderList($props.pageSizeList, (item, index) => {
                return openBlock(), createElementBlock("div", {
                  key: index,
                  class: "seleclt-opotion",
                  onClick: withModifiers(($event) => $options.onSize(item), ["stop"])
                }, toDisplayString(item), 9, _hoisted_9);
              }), 128))
            ])) : createCommentVNode("", true)
          ])
        ])) : createCommentVNode("", true)
      ]),
      createElementVNode("li", {
        class: normalizeClass([{ disabled: $props.page >= $data.pages }, "pn next"]),
        "aria-disabled": $props.page >= $data.pages,
        "arial-label": $options.arialLabel(0),
        tabindex: "0",
        "data-page": "0",
        role: "button"
      }, [
        createVNode(_component_el_icon, null, {
          default: withCtx(() => [
            createVNode(_component_ArrowRightBold)
          ]),
          _: 1
        })
      ], 10, _hoisted_10)
    ])
  ]);
}
const PaginationComponent = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-f40d8cb7"]]);
const install = (app) => {
  app.component("Pagination", PaginationComponent);
};
const PaginationPlugin = {
  install,
  Pagination: PaginationComponent
};
const Pagination = PaginationComponent;
export {
  Pagination,
  PaginationPlugin as default,
  install
};
