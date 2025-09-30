<!--
 * @Description: 旋转边框
-->
<template>
  <div class="rotating-border c-subscribe-box u-align-center">
    <div class="rainbow"><span></span></div>
    <div class="c-subscribe-box__wrapper">
      <slot name="default"></slot>
    </div>
  </div>
</template>

<script setup lang="ts"></script>
<style lang="scss">
:root {
  --color-first: #65587f;
  --color-second: #f18867;
  --color-third: #e85f99;
  --color-forth: #50bda1;
  --block-width: 300px;
  --block-height: 270px;
  --border-width: 0.625rem;
  --border-radius-outer: 8px;
  --border-radius-inner: calc(var(--border-radius-outer) / 2);
  --font-plain: 'IBM Plex Sans', sans-serif;
  --font-special: 'Fredoka One', sans-serif;
  box-sizing: border-box;
  line-height: 1.4;
}

.rainbow span {
  display: block;
  width: 100%;
  height: 100%;
  position: relative;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
.rainbow span:after {
  display: block;
  content: '';
  width: 100%;
  height: 100%;
  position: absolute;
  left: 100%;
}
.rainbow span:first-child {
  background: var(--color-first);
}
.c-subscribe-box {
  width: 100%;
  height: 400px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 10px 40px -10px rgba(0, 64, 128, 0.2);
  border-radius: 8px;
  background: white;
}
.c-subscribe-box:hover {
  width: calc(100% + 6px);
  .rainbow {
    width: 100%;
    height: 100%;
    -webkit-animation: o-rotate-360 linear 4s infinite;
    animation: o-rotate-360 linear 4s infinite;
  }
}
.c-subscribe-box__wrapper {
  width: calc(100% - 10px);
  height: calc(100% - 10px);
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  background: #fff;
  display: -webkit-box;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  border-radius: 12px;
}
.c-subscribe-box__title {
  font-size: 1.6rem;
}
.c-subscribe-box__desc {
  font-size: 0.935rem;
  margin: 0.7rem auto 1.8rem;
  max-width: 240px;
}
.c-subscribe-box__form {
  margin-top: auto;
}

.c-form--accent input:hover,
.c-form--accent input:active,
.c-form--accent input:focus {
  border-color: var(--color-third);
  box-shadow: 0 0 0 3px rgba(232, 94, 152, 0.25);
}
.c-form--accent [type='submit'] {
  background: var(--color-third);
  border-color: var(--color-third);
  color: #fff;
}

@-webkit-keyframes o-rotate-360 {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}

@keyframes o-rotate-360 {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
[type='submit'] {
  margin-bottom: 0;
  font-weight: normal;
  letter-spacing: 0.015em;
  font-size: 1.1rem;
}
[type='submit']:active {
  -webkit-transform: scale(0.97);
  transform: scale(0.97);
}

input {
  color: inherit;
  outline: none;
  font-size: 93%;
  -webkit-transition: all 300ms ease;
  transition: all 300ms ease;
}

h3 {
  margin: 0;
  letter-spacing: -0.015em;
  font-weight: normal;
  line-height: 1.4;
}

.u-align-center {
  text-align: center;
}
</style>
