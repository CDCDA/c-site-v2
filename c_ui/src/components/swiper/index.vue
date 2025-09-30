<template>
  <swiper
    :effect="effect"
    :grabCursor="true"
    :autoplay="autoplay"
    :modules="modules"
    :loop="loop"
    class="mySwiper"
  >
    <swiper-slide v-for="(item, index) in itemList" :key="index">
      <c-image :src="(item as any).src" @click="emit('itemClick', item)"></c-image>
    </swiper-slide>
  </swiper>
</template>
<script lang="ts" setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/effect-cards';
import { EffectCards, Autoplay } from 'swiper/modules';
import { onMounted } from 'vue';
const props = defineProps({
  itemList: {
    type: Array,
    default: [] as any
  },
  effect: {
    type: String,
    default: 'cards'
  },
  modules: {
    type: Array,
    default: [EffectCards, Autoplay]
  },
  autoplay: {
    type: Object,
    default: { delay: 5000, disableOnInteraction: false }
  },
  loop: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['itemClick']);

onMounted(() => {
  console.log('QQQ', props.itemList);
});
</script>
<style lang="scss" scoped>
.swiper {
  width: 60vh;
  height: 80vh;
}

.swiper-slide {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 18px;
  font-size: 22px;
  font-weight: bold;
  color: #fff;
}

.swiper-slide {
  .c-image {
    height: 100%;
    width: 100%;
  }
}
</style>
