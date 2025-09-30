<template>
  <div class="tilt-carousel">
    <div class="carousel-container">
      <!-- 轮播项目 -->
      <transition-group
        name="carousel"
        tag="div"
        :style="{
          width: '100%',
          height: '100%',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center'
        }"
      >
        <div
          v-for="(item, index) in items"
          :key="index"
          class="carousel-item"
          @click="goToIndex(index)"
          :class="{ active: currentIndex === index }"
          :style="{
            transform: `translateX(${translateX(index)}) rotate(${tiltAngle(
              index
            )}deg) scale(${scale(index)})`,
            zIndex: getZIndex(index)
          }"
        >
          <div class="card" :style="{ background: item.back }">
            <div class="back">
              <c-image :src="item.back" :alt="item.name" />
            </div>
          </div>
        </div>
      </transition-group>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

// 轮播数据
const items = ref([
  {
    back: new URL('@/assets/images/feitu-bridge.jpg', import.meta.url).href
  },
  {
    back: new URL('@/assets/images/snow-rail.jpg', import.meta.url).href
  },
  {
    back: new URL('@/assets/images/img_2001186811.jpg', import.meta.url).href
  },
  {
    back: new URL('@/assets/images/123123123.jpg', import.meta.url).href
  },
  {
    back: new URL('@/assets/images/天使.png', import.meta.url).href
  }
]);

const middleIndex = Math.floor(items.value.length / 2);
// 当前索引
const currentIndex = ref(4);

// 自动播放定时器
let autoPlayTimer = null;

// 生命周期钩子
onMounted(() => {
  startAutoPlay();
});

onUnmounted(() => {
  stopAutoPlay();
});

// 开始自动播放
const startAutoPlay = () => {
  autoPlayTimer = setInterval(() => {
    nextItem();
  }, 5000);
};

// 停止自动播放
const stopAutoPlay = () => {
  if (autoPlayTimer) {
    clearInterval(autoPlayTimer);
  }
};

const tiltAngle = index => {
  return getOffset(index) * 3;
};

const translateX = index => {
  return `${getOffset(index) * 6}%`;
};

const scale = index => {
  return (1 - Math.abs(getOffset(index)) * 0.04).toFixed(2);
};

const getOffset = index => {
  let offset = index - currentIndex.value;
  if (Math.abs(offset) > middleIndex) {
    offset = -(offset - middleIndex + currentIndex.value);
  }
  return offset;
};

const getZIndex = index => {
  let offset = index - currentIndex.value;
  return items.value.length - Math.abs(offset);
};

// 上一个项目
const prevItem = () => {
  currentIndex.value = (currentIndex.value - 1 + items.value.length) % items.value.length;
};

// 下一个项目
const nextItem = () => {
  currentIndex.value = (currentIndex.value + 1) % items.value.length;
};

// 跳转到指定索引
const goToIndex = index => {
  console.log('AAi', index);
  currentIndex.value = index;
};
</script>

<style scoped>
.tilt-carousel {
  width: 100%;
  height: 100%;
  overflow: hidden;
  perspective: 1200px;
  background: transparent;
}

.carousel-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}

.carousel-item {
  position: absolute;
  width: fit-content;
  height: fit-content;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.8s ease-in-out;
  transform-style: preserve-3d;
}

.carousel-item.active {
  z-index: 1;
}

.card {
  cursor: pointer;
  aspect-ratio: 4/5;
  height: 80vh;
  background-color: white;
  border-radius: 20px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
}

.back {
  position: relative;
  width: 100%;
  height: 100%;
}

.back .c-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.back span {
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 5px 10px;
  border-radius: 10px;
  font-size: 14px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.content {
  text-align: center;
}

.time-counter {
  font-size: 1.3rem;
  font-weight: bold;
  color: #333;
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(255, 255, 255, 0.8);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  z-index: 2;
}

.nav-btn:hover {
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.prev {
  left: 20px;
}

.next {
  right: 20px;
}

.carousel-enter-active,
.carousel-leave-active {
  transition: all 0.8s ease;
}

.carousel-enter-from {
  transform: translateX(100%) rotateY(-15deg) scale(0.9);
  opacity: 0;
}

.carousel-leave-to {
  transform: translateX(-100%) rotateY(15deg) scale(0.9);
  opacity: 0;
}
</style>
