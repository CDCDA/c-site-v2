<!--
 * @Description: 文字滚动
-->
<template>
  <div class="tricker">
    <p v-for="text in props.list">{{ text }}</p>
  </div>
</template>
<script setup lang="ts">
import { onMounted } from 'vue';
const props = defineProps({
  list: {
    default: ["Hey I'm first Here", 'Me, the second', 'And I am the Third!']
  }
});
function scroll() {
  var duration = 2500; // change this to change rotation time in milliseconds
  var current = 1;
  var tricker = document.querySelector('.tricker') as any;
  var height = tricker.clientHeight;
  let children = tricker.getElementsByTagName('p');
  var number = children.length;
  var first = children[0];
  setInterval(function () {
    var interv = current * -1 * height;
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
  scroll();
});
</script>

<style lang="scss" scoped>
.tricker {
  height: 40px;
  overflow: hidden;
  margin: 100px auto 20px;
}
.tricker p {
  color: #333;
  margin: 0;
  height: 40px;
  line-height: 40px;
  font-size: 1rem;
  text-align: center;
  text-transform: uppercase;
  transition: margin-top 0.3s ease;
}
</style>
