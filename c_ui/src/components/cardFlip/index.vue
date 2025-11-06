<!--
 * @Description: 翻转卡片
-->
<template>
  <div class="content">
    <a class="card">
      <div class="front">
        <slot name="front">front</slot>
      </div>
      <div class="back">
        <slot name="back">back</slot>
      </div>
    </a>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
</script>

<style lang="scss" scoped>
.content {
  display: flex;
  margin: 0 auto;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  max-width: 1000px;
  width: 100%;
  height: 100%;
}

.card {
  color: inherit;
  cursor: pointer;
  width: 100%;
  height: 100%;
  position: relative;
}
.front,
.back {
  display: flex;
  border-radius: 6px;
  background-position: center;
  background-size: cover;
  text-align: center;
  justify-content: center;
  align-items: center;
  position: absolute;
  height: 100%;
  width: 100%;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
  transform-style: preserve-3d;
  transition: ease-in-out 600ms;
}

.front {
  background-size: cover;
  font-size: 1.618rem;
  font-weight: 600;
  color: #fff;
  overflow: hidden;
}
.front:before {
  position: absolute;
  display: block;
  content: '';
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0.25;
  z-index: -1;
}
.card:hover .front {
  transform: rotateY(180deg);
}
.card:nth-child(even):hover .front {
  transform: rotateY(-180deg);
}

.back {
  transform: rotateY(-180deg);
}
.card:hover .back {
  transform: rotateY(0deg);
}
.card:nth-child(even) .back {
  transform: rotateY(180deg);
}
.card:nth-child(even):hover .back {
  transform: rotateY(0deg);
}
</style>
