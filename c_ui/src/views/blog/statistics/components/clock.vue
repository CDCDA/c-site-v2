<template>
  <div id="clock">
    <p class="date">{{ date }}</p>
    <p class="time">{{ time }}</p>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
const time = ref('');
const date = ref('');
const week = ref(['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']);
function updateTime() {
  let cd = new Date();
  time.value =
    zeroPadding(cd.getHours(), 2) +
    ':' +
    zeroPadding(cd.getMinutes(), 2) +
    ':' +
    zeroPadding(cd.getSeconds(), 2);
  date.value =
    zeroPadding(cd.getFullYear(), 4) +
    '-' +
    zeroPadding(cd.getMonth() + 1, 2) +
    '-' +
    zeroPadding(cd.getDate(), 2) +
    ' ' +
    week.value[cd.getDay()];
}

function zeroPadding(num: any, digit: any) {
  var zero = '';
  for (var i = 0; i < digit; i++) {
    zero += '0';
  }
  return (zero + num).slice(-digit);
}
onMounted(() => {
  setInterval(updateTime, 1000);
  updateTime();
});
</script>

<style scoped lang="scss">
#clock {
  font-family: 'Share Tech Mono', monospace;
  text-align: center;
  width: 100%;
  color: #daf6ff;
  text-shadow: 0 0 20px #0aafe6, 0 0 20px rgba(10, 175, 230, 0);
  padding: 15px;
}
#clock .time {
  letter-spacing: 0.05em;
  font-size: 2rem;
  padding: 0;
  margin: 0;
}
#clock .date {
  letter-spacing: 0.1em;
  font-size: 1rem;
  padding: 0;
  margin: 0 0 10px 0;
}
</style>
