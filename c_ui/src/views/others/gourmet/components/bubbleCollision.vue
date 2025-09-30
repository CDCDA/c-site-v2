<!--
 * @Description: 气泡碰撞
-->
<template>
  <div id="bubble-wrap" class="bubble-wrap" @click="onClick">
    <i id="bubbleradius"></i>
    <span v-for="(c, index) in circles" :style="style(c)" :key="c.key" :class="{ popped: c.popped }"
      > </span
    >
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { autoClearTimer } from '@/utils/timer';
var globalID = null as any;

const rotate = (x: any, y: any, sin: any, cos: any, reverse: any) => {
  return reverse
    ? {
        x: cos * x + sin * y,
        y: cos * y - sin * x
      }
    : {
        x: cos * x - sin * y,
        y: cos * y + sin * x
      };
};
var circles = deleteDictTypes;
var lastExec = null as any;
var hue = 250 as any;
var lastCollisions = [] as any;
var hiddenProperty = null as any;
var visibilityChangeEvent = null as any;
var moving = true as any;

const flatten = (arr: any) =>
  arr.reduce((a: any, b: any) => a.concat(Array.isArray(b) ? flatten(b) : b), []);

function style(c: any) {
  return `top:${c.y}px;left:${c.x}px;width:150px;height:150px;border-radius:100%;background:url('${c.backUrl}');object-fit:cover;background-position:left 28%`;
}
function update(tm: any) {
  if (!moving) {
    return;
  }
  if (lastExec && circles.value.length) {
    var diff = tm - lastExec;
    // var huediff = (hue + diff/30) % 360
    var box = (document as any).getElementById('bubble-wrap').getBoundingClientRect();
    var radius = (document as any).querySelector('#bubbleradius').getBoundingClientRect().width;

    var couples = [] as any;
    circles.value
      .filter((cc: any) => !cc.popped)
      .forEach((c1: any) => {
        circles.value
          .filter((cc: any) => !cc.popped)
          .forEach((c2: any) => {
            if (c1 !== c2) {
              couples.push([c1, c2]);
            }
          });
      });

    var collisions = couples.filter((couple: any) => {
      var dist = Math.sqrt(
        Math.pow(couple[0].y - couple[1].y, 2) + Math.pow(couple[0].x - couple[1].x, 2)
      );
      return dist < radius * 2;
    });

    var newcollisions = collisions.filter((couple: any) => {
      var key = couple[0].key + couple[1].key;
      return lastCollisions.indexOf(key) < 0;
    });

    newcollisions.forEach((couple: any) => {
      var a = couple[0];
      var b = couple[1];

      if (a.collisionFree && b.collisionFree) {
        if (false) {
          a.new_vx = (a.vx * (a.mass - b.mass) + 2 * b.mass * b.vx) / (a.mass + b.mass);
          a.new_vy = (a.vy * (a.mass - b.mass) + 2 * b.mass * b.vy) / (a.mass + b.mass);
        } else {
          var dx = b.x - a.x;
          var dy = b.y - a.y;
          var collisionAngle = Math.atan2(dy, dx);
          var sin = Math.sin(collisionAngle);
          var cos = Math.cos(collisionAngle);
          var pos_a = { x: 0, y: 0 };
          var pos_b = rotate(dx, dy, sin, cos, true);
          var vel_a = rotate(a.vx, a.vy, sin, cos, true);
          var vel_b = rotate(b.vx, b.vy, sin, cos, true);
          var vxTotal = vel_a.x - vel_b.x;
          vel_a.x = ((a.mass - b.mass) * vel_a.x + 2 * b.mass * vel_b.x) / (a.mass + b.mass);
          vel_b.x = vxTotal + vel_a.x;
          pos_a.x += vel_a.x;
          pos_b.x += vel_b.x;

          var pos_a_final = rotate(pos_a.x, pos_a.y, sin, cos, false);
          var vel_a_final = rotate(vel_a.x, vel_a.y, sin, cos, false);
          a.new_x = a.x + pos_a_final.x;
          a.new_y = a.y + pos_a_final.y;
          a.new_vx = vel_a_final.x;
          a.new_vy = vel_a_final.y;
        }
      }
    });

    newcollisions.forEach((couple: any) => {
      var a = couple[0];
      var b = couple[1];
      if (a.new_vy) {
        a.vx = a.new_vx;
        a.vy = a.new_vy;
        a.x = a.new_x;
        a.y = a.new_y;
        a.hue += 20;
      }
    });

    lastCollisions = collisions.map((couple: any) => {
      return couple[0].key + couple[1].key;
    });

    var collided = [...new Set(flatten(newcollisions))];
    var collidedKeys = collided.map((c: any) => c.key);

    circles.value.forEach((c: any) => {
      c.collisionFree = c.collisionFree || collidedKeys.indexOf(c.key) < 0;
      if (c.y < 0) {
        c.vy = Math.abs(c.vy);
      } else if (c.y > box.height) {
        c.vy = -Math.abs(c.vy);
      }
      if (c.x < 0) {
        c.vx = Math.abs(c.vx);
      } else if (c.x > box.width) {
        c.vx = -Math.abs(c.vx);
      }

      c.y += c.vy * diff;
      c.x += c.vx * diff;
    });
  }
  lastExec = tm;
  globalID = requestAnimationFrame(update);
}
function handleVisibilityChange() {
  if (!(document as any)[hiddenProperty]) {
    globalID = requestAnimationFrame(update);
    moving = true;
  } else {
    cancelAnimationFrame(globalID);
    moving = false;
  }
}
function onClick(evt: any) {
  let m = {
    x: evt.pageX,
    y: evt.pageY
  };
  let nearest = null;
  let nearest_distsq = Infinity;
  let dx, dy, distsq;
  var radius = (document as any).querySelector('#bubbleradius').getBoundingClientRect().width;
  circles.value.forEach((c: any) => {
    dx = m.x - c.x;
    dy = m.y - c.y;
    distsq = dx * dx + dy * dy;
    if (distsq < nearest_distsq && distsq < c.radius * c.radius) {
      nearest = c;
      nearest_distsq = distsq;
    }
  });
  if (nearest) {
    (nearest as any).popped = true;
  }
}

const images = [
  'http://120.48.127.181/file/snowflake.jpg',
  'http://120.48.127.181/file/ultraman.jpg',
  'http://120.48.127.181/file/strange-life.jpg',
  'http://120.48.127.181/file/resting-hexenbiest.jpg',
  'http://120.48.127.181/file/rainy-night-coffee-shop.jpg',
  'http://120.48.127.181/file/rain-cyberpunk-city.jpg',
  'http://120.48.127.181/file/rain-cyberpunk-city.jpg'
];

onMounted(() => {
  autoClearTimer(() => {
    try {
      var box = (document as any).getElementById('bubble-wrap').getBoundingClientRect();
      var radius = (document as any).querySelector('#bubbleradius').getBoundingClientRect().width;
    } catch {}

    var max = (box.width * box.height) / 300 / Math.pow(radius, 1.2);
    for (var i = 0; i < max; i++) {
      circles.value.push({
        key: Math.random(),
        y: Math.random() * box.height,
        x: Math.random() * box.width,
        vx: Math.random() / 5,
        vy: Math.random() / 5,
        hue: Math.random() * 360,
        collisionFree: false,
        mass: 1,
        radius: radius,
        popped: false,
        backUrl: images[i] ? images[i] : null
      });
    }
    globalID = requestAnimationFrame(update);

    var hidden, visibilityChange;
    if (typeof document.hidden !== 'undefined') {
      // Opera 12.10 and Firefox 18 and later support
      hidden = 'hidden';
      visibilityChange = 'visibilitychange';
    } else if (typeof (document as any).msHidden !== 'undefined') {
      hidden = 'msHidden';
      visibilityChange = 'msvisibilitychange';
    } else if (typeof (document as any).webkitHidden !== 'undefined') {
      hidden = 'webkitHidden';
      visibilityChange = 'webkitvisibilitychange';
    }
    hiddenProperty = hidden;
    visibilityChangeEvent = visibilityChange;
    document.addEventListener(visibilityChangeEvent, handleVisibilityChange, false);
  }, 100);
});
// onDestoryed(()=>{
//   document.removeEventListener(visibilityChangeEvent, handleVisibilityChange, false);
// })
</script>

<style lang="scss" scoped>
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
  user-select: none;
}

#bubbleradius {
  width: 4.5rem;
  height: 1px;
  opacity: 0;
  z-index: -1;
  display: block;
}

body {
  overflow: hidden;
}

@include theme() {
  #bubble-wrap {
    position: relative;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: get('back');
    color: get('font-color');
  }
}

span {
  transform: translate(-50%, -50%);
  border-radius: 9rem;
  height: 9rem;
  width: 9rem;
  display: block;
  position: absolute;
  opacity: 1;
  transition: box-shadow 0.5s ease-in-out, transform 0.07s ease-out, opacity 0.04s ease-in;
  /*&:before,
  &:after{
  	content: '';
  	position: absolute;
  	top: 20%;
  	left: 20%;
  	background-color: rgba(cyan, 0.3);
  	width: 55%;
  	height: 55%;
  	border-radius: 50%;
  }
  
  &:after {
  	top: 23%;
  	left: 23%;
  	background-color: $black;
  }*/
}
span.popped {
  transform: translate(-50%, -50%) scale(2);
  opacity: 0;
}
span:after {
  content: '';
  position: absolute;
  top: 18%;
  left: 18%;
  // background-color: rgba(191, 255, 255, 0.6);
  width: 0.6428571429rem;
  height: 1.5rem;
  border-radius: 50%;
  transform: rotate(45deg) scale(0.8);
}
</style>
