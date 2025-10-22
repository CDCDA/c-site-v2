import { createI18n } from "vue-i18n";
import zhCn from "./zh_cn.json";
import zhHant from "./zh_hant.json";
import enUs from "./en_us.json";
import vi from "./vi.json";
import ElementPlus from "element-plus";

console.log("QQ", enUs);
// 创建 i18n 实例
const i18n = createI18n({
  legacy: false, // 关闭 Vue 2 兼容模式，Vue 3 推荐使用
  locale: window.localStorage.getItem("language") || "zh_cn", // 从 localStorage 中获取，默认中文
  messages: {
    en_us: enUs,
    zh_cn: zhCn,
    zh_hant: zhHant,
    vi: vi,
  },
});

// 配置 ElementPlus 的 i18n
const elementPlusConfig = {
  locale: i18n.global.t,
};

export { i18n, elementPlusConfig };
export default i18n;
