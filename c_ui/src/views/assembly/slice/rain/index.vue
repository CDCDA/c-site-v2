<!--
 * @Description:雨
-->
<template>
  <div class="rain"></div>
</template>
<script>
import { loadScript } from '@/utils/common';
import { autoClearTimer } from '@/utils/timer';

export default {
  name: 'rain',
  mounted() {
    // Please, reload the page if it doesn't works properly <3
    // See the console to check the number of <hr> tags

    let hrElement;
    let counter = 15;
    let rainEl = document.querySelector('.rain');
    for (let i = 0; i < counter; i++) {
      hrElement = document.createElement('HR');
      if (i === counter - 1) {
        hrElement.className = 'thunder';
      } else {
        hrElement.style.left = Math.floor(Math.random() * window.innerWidth) + 'px';
        hrElement.style.animationDuration = 0.2 + Math.random() * 0.3 + 's';
        hrElement.style.animationDelay = Math.random() * 5 + 's';
      }
      rainEl.appendChild(hrElement);
    }
  },
  methods: {}
};
</script>

<style lang="scss">
.rain {
  background-image: linear-gradient(to bottom, #030420, #000000 70%);
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  hr.thunder {
    border: unset;
    position: absolute;
    width: 100vw;
    height: 100vh;
    animation-name: thunder;
    animation-duration: 20s;
    animation-timing-function: linear;
    animation-delay: 10s;
    animation-iteration-count: infinite;
  }

  hr:not(.thunder) {
    width: 50px;
    border-color: transparent;
    border-right-color: rgba(255, 255, 255, 0.7);
    border-right-width: 50px;
    position: absolute;
    bottom: 100%;
    transform-origin: 100% 50%;
    animation-name: rain;
    animation-duration: 60s;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
  }
}

@keyframes rain {
  from {
    transform: rotate(105deg) translateX(0);
  }
  to {
    transform: rotate(105deg) translateX(calc(100vh + 20px));
  }
}

@keyframes thunder {
  0% {
    background-color: transparent;
  }
  1% {
    background-color: white;
  }
  2% {
    background-color: rgba(255, 255, 255, 0.8);
  }
  8% {
    background-color: transparent;
  }
}
</style>
