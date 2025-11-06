<!--
 * @Description: 天气插件
-->
<template>
  <div
    id="he-plugin-standard"
    class="weather"
    style="background: transparent; box-shadow: 0 2px 12px 0 #000000"
  ></div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted } from 'vue';
onMounted(() => {
  const windowData = window as any;
  windowData.WIDGET = {
    CONFIG: {
      layout: '2',
      width: 230,
      height: 270,
      background: '1',
      dataColor: 'FFFFFF',
      key: '923e3789e7c74e07bef0edd9481149e2'
    }
  };
  var script = document.createElement('script');
  script.type = 'text/javascript';
  script.src = 'https://widget.qweather.net/standard/static/js/he-standard-common.js?v=2.0';
  document.getElementsByTagName('head')[0].appendChild(script);
});
</script>
<style lang="scss">
@include theme() {
  #he-plugin-standard {
    box-shadow: get('box-shadow');
    background: get('back');
    background: transparent !important;
    width: 100% !important;
    height: 100% !important;
    border-radius: 15px !important;
    span,
    a {
      color: get('font-color') !important;
    }
    .wv-lt-col-5 {
      display: none;
    }
  }
}
</style>
