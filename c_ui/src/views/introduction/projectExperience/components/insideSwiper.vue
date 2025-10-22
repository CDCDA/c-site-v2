<template>
  <!--  @autoplayTimeLeft="onAutoplayTimeLeft"-->
  <swiper
    :direction="'vertical'"
    :slidesPerView="1"
    :spaceBetween="30"
    :mousewheel="true"
    :autoplay="{
      delay: 5000,
      disableOnInteraction: false
    }"
    :loop="true"
    :pagination="{
      clickable: true
    }"
    :modules="modules"
    class="inside-swiper"
    style="height: 100%"
    :centeredSlides="true"
  >
    <swiper-slide v-for="(image, index) in item.images" class="flex-center" :key="item.id">
      <c-image
        :src="image"
        :preview="true"
        :lazy="false"
        :initial-index="index"
        :previewSrcList="item.images"
      />
    </swiper-slide>
    <template #container-end>
      <div class="autoplay-progress">
        <svg viewBox="0 0 48 48" ref="progressCircle">
          <circle cx="24" cy="24" r="20"></circle>
        </svg>
        <span ref="progressContent"></span>
      </div>
    </template>
  </swiper>
</template>
<script lang="ts" setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css/thumbs';
import { Pagination, Navigation, Thumbs, Mousewheel, Autoplay } from 'swiper/modules';
import { onMounted, ref, watch, computed } from 'vue';
const modules = [Navigation, Thumbs, Pagination, Mousewheel, Autoplay];
const props = defineProps({
  item: {
    default: {} as any
  }
});

// const progressCircle = ref(null) as any;
// const progressContent = ref(null) as any;
// const onAutoplayTimeLeft = (s: any, time: any, progress: any) => {
//   progressCircle.value.style.setProperty('--progress', 1 - progress);
//   progressContent.value.textContent = `${Math.ceil(time / 1000)}s`;
// };
</script>
<style lang="scss" scoped>
//.autoplay-progress {
//  position: absolute;
//  right: 16px;
//  bottom: 16px;
//  z-index: 10;
//  width: 48px;
//  height: 48px;
//  display: flex;
//  align-items: center;
//  justify-content: center;
//  font-weight: bold;
//  color: var(--swiper-theme-color);
//}
//
//.autoplay-progress svg {
//  --progress: 0;
//  position: absolute;
//  left: 0;
//  top: 0px;
//  z-index: 10;
//  width: 100%;
//  height: 100%;
//  stroke-width: 4px;
//  stroke: var(--swiper-theme-color);
//  fill: none;
//  stroke-dashoffset: calc(125.6px * (1 - var(--progress)));
//  stroke-dasharray: 125.6;
//  transform: rotate(-90deg);
//}
.c-image {
  height: 100%;
}
</style>
