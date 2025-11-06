<!--
 * @Description: 文字滚动
-->
<template>
  <div class="tricker">
    <p v-for="item in props.list">{{ item.text }}</p>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t:$t } = useI18n();
import { onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer';
const props = defineProps({
  list: {
    default: [
      { text: "Hey I'm first Here" },
      { text: 'Me, the second' },
      { text: 'And I am the Third!' }
    ]
  },
  duration: {
    default: 5000
  }
});
function scroll() {
  var duration = props.duration; // change this to change rotation time in milliseconds
  var current = 1;
  var tricker = document.querySelector('.tricker') as any;
  var height = tricker.clientHeight;
  let children = tricker.getElementsByTagName('p');
  var number = children.length;
  var first = children[0];
  setInterval(function () {
    var interv = current * -1 * height;
    if (!first) {
      return;
    }
    first.style.marginTop = interv + 'px';
    if (current == number) {
      first.style.marginTop = '0px';
      current = 1;
    } else {
      current++;
    }
  }, duration);
}
onMounted(() => {
  autoClearTimer(() => {
    scroll();
  }, 1000);
});
</script>

<style lang="scss" scoped>
.tricker {
  height: 2rem;
  overflow: hidden;
  margin: auto;
}
@include theme() {
  .tricker p {
    color: get('font-color');
    margin: 0;
    height: 2rem;
    line-height: 2rem;
    font-size: 1rem;
    text-align: center;
    text-transform: uppercase;
    transition: margin-top 0.3s ease;
  }
}
</style>
