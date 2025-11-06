<!--
 * @Description:  上升气泡
-->
<template>
  <canvas id="bubbles"></canvas>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer';
// Canvas Init
var c = null as any,
  ctx = null as any,
  width = null as any,
  height = null as any,
  particles = null as any,
  minRadius = null as any,
  maxRadius = null as any,
  speed = null as any,
  x = null as any;

// Bubbles.value
var Bubbles = ref([]) as any;

for (var i = 0; i < particles; i++) {
  Bubbles.value.push({
    x: i * x,
    y: height * Math.random(),
    r: minRadius + Math.random() * (maxRadius - minRadius),
    speed: 10 * Math.random()
  });
}

function bubble() {
  c.width = width;
  c.height = height;
  for (i = 0; i < Bubbles.value.length; i++) {
    var b = Bubbles.value[i];
    console.log(i, b);
    ctx.beginPath();
    ctx.arc(b.x, b.y, b.r, 0, 2 * Math.PI);

    b.alpha = 0.5 * (b.y / height);
    b.speed += speed;

    ctx.strokeStyle = 'rgba(255, 255, 255, .5)';
    ctx.stroke();
    ctx.fillStyle = 'hsla(203, 75%, 69%,' + b.alpha + ')';
    ctx.fill();
    b.y -= b.speed;
    if (b.y < 0) {
      b.y = height;
      b.speed = Math.random() * 5;
    }
  }
}

// Draw
function draw() {
  bubble();

  window.requestAnimationFrame(draw);
}

// Resize Canvas
function resizeCanvas() {
  (width = window.innerWidth), (height = window.innerHeight);
  c.width = width;
  c.height = height;

  draw();
}

onMounted(() => {
  autoClearTimer(() => {
    c = document.getElementById('bubbles') as any;
    ctx = c.getContext('2d');
    console.log('CCC', ctx);
    width = window.innerWidth;
    height = window.innerHeight;
    particles = 60;
    minRadius = 5;
    maxRadius = 20;
    speed = 0.01;
    x = width / particles;
    resizeCanvas();
    window.addEventListener('resize', resizeCanvas, false);
  }, 2000);
});
</script>
<style lang="scss" scoped>
#bubbles {
  width: 100%;
  height: 100%;
}
</style>
