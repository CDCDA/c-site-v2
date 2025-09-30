<!--
 * @Description: 博客展示页海浪header
-->
<template>
  <div class="wave-header" v-cLoading="loading">
    <div :class="loading ? '' : 'wave-active'" ref="waveHeader">
      <div class="inner-header flex"></div>
      <div style="opacity: 0.5; height: 15vh">
        <svg
          class="waves"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          viewBox="0 24 150 28"
          preserveAspectRatio="none"
          shape-rendering="auto"
        >
          <defs>
            <path
              id="gentle-wave"
              d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"
            />
          </defs>
          <g class="parallax">
            <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7" />
            <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)" />
            <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)" />
            <use xlink:href="#gentle-wave" x="48" y="7" fill="#f7f9fe" fill-opacity="100" />
          </g>
        </svg>
      </div>
      <div class="blog-info">
        <div class="blog-info-header">
          <span class="post-meta-original">{{
            props.blogData.isOriginal == 0 ? '转载' : '原创'
          }}</span>
          <span class="post-meta-categories">博客</span>
          <div class="tag-item" v-for="tag in props.blogData.tags">
            <span class="tag-item-pretend">#</span>
            <span class="tag-item-text">{{ tag.tagName }}</span>
          </div>
        </div>
        <div class="blog-info-title">{{ props.blogData.blogTitle }}</div>
        <div class="blog-info-footer">
          <span class="footer-item">
            <svg-icon iconName="commonSvg-羽毛笔" />{{ props.blogData?.author }}
          </span>
          <span class="footer-item">
            <svg-icon iconName="commonSvg-日期" />{{ props.blogData.createTime || '2023-10-1' }}
          </span>
          <span class="footer-item">
            <svg-icon iconName="commonSvg-字数" />{{ props.blogData.content?.length }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
const props = defineProps({
  blogData: {
    type: Object,
    default: { coverUrl: '', tags: [] }
  }
});
const loading = ref('rotate' as any);
const background = ref('' as any);
const waveHeader = ref(null as any);
watch(
  () => props.blogData,
  val => {
    loading.value = 'rotate';
    if (val && val.coverUrl) {
      (document.querySelector('.blog-display-main') as any).style.animation = 'none';
      background.value = `url('${val.coverUrl}') left/cover  no-repeat`;
      var img = new Image();
      img.src = new URL(val.coverUrl, import.meta.url).href; //需要判断的图片地址
      img.onload = function () {
        (waveHeader as any)._value.style.background = background.value;
        // (document.querySelector('.blog-display-main') as any).style.animation =
        //   'display-in 1.5s backwards';
        loading.value = false;
      };
    }
    // else background.value = `url('http://120.48.127.181/file/dragon-maiden.jpg') left/cover  no-repeat`;
    else {
      background.value = '';
    }
    if (val.tags.length > 5) {
      val.tags.length = 5;
    }
  },
  {
    deep: true
  }
);

onMounted(() => {
  loading.value = 'rotate';
  if (props.blogData && props.blogData.coverUrl) {
    background.value = `url('${props.blogData.coverUrl}') left/cover  no-repeat`;
    var img = new Image();
    img.src = new URL(props.blogData.coverUrl, import.meta.url).href; //需要判断的图片地址
    img.onload = function () {
      (waveHeader as any)._value.style.background = background.value;
      // (document.querySelector('.blog-display-main') as any).style.animation =
      //   'display-in 1.5s backwards';
      loading.value = false;
    };
  } else {
    background.value = '';
  }
  if (props.blogData.tags.length > 5) {
    props.blogData.tags.length = 5;
  }
});
</script>
<style scoped lang="scss">
@include theme() {
  .blog-info {
    font-size: 1rem;
    @include flex-column;
    align-items: start;
    position: absolute;
    top: 25%;
    left: 100px;
    .post-meta-original,
    .post-meta-categories {
      background: rgba(255, 255, 255, 0.2);
      color: get('re-font-color') !important;
      padding: 0 0.5rem;
      font-size: 1rem;
      border-radius: 8px;
      font-weight: bold;
      height: 32px;
      line-height: 32px;
      width: 40px;
      // height: 25px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 10px;
      cursor: pointer;
    }
    .blog-info-title {
      font-size: 45px;
      font-weight: bold;
      margin: 40px 0 20px 0;
      text-align: left;
    }
  }
  .blog-info-footer {
    opacity: 0.8;
    @include flex;
    margin-top: 15px;
    justify-content: start;
    .svg-icon {
      height: 25px;
      width: 25px;
      margin-right: 5px;
    }
    .footer-item {
      @include flex;
      margin-right: 15px;
    }
  }
}
.blog-info-header {
  width: 100%;
  @include flex;
  margin: 5px 0;
  flex-wrap: nowrap;
  justify-content: start;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;

  .tag-item {
    margin: 5px;
    flex: 0 0 auto;
  }
  .tag-item {
    margin-right: 15px;
    font-size: 1rem;
  }
  .tag-item-pretend {
    opacity: 0.5;
    font-weight: bold;
  }
}

@keyframes wave-in {
  0% {
    filter: brightness(50%);
    transform: scale(1.1);
  }
  100% {
    filter: brightness(100%);
    transform: scale(1);
  }
}

.wave-header {
  position: relative;
  text-align: center;
  overflow: hidden;
  // background: linear-gradient(60deg, rgba(84, 58, 183, 1) 0%, rgba(0, 172, 193, 1) 100%);
  color: white;
  background-position: center;
  opacity: 1;
}

.wave-active {
  animation: wave-in 1.5s 0.3s backwards;
  // transition: cubic-bezier(0.71, 0.15, 0.16, 1.15) 2s;
}

.logo {
  width: 50px;
  fill: white;
  padding-right: 15px;
  display: inline-block;
  vertical-align: middle;
}

.inner-header {
  height: 45vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

.flex {
  /*Flexbox for containers*/
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.waves {
  position: relative;
  width: 100%;
  height: 15vh;
  margin-bottom: -7px; /*Fix for safari gap*/
  min-height: 100px;
  max-height: 150px;
}

.content {
  position: relative;
  height: 20vh;
  text-align: center;
  background-color: white;
}

.content a {
  margin: 0 5px;
  font-size: 12px;
  color: #333;
}

.content a:hover {
  color: #000;
}

/* Animation */

.parallax > use {
  animation: move-forever 25s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
}
.parallax > use:nth-child(1) {
  animation-delay: -2s;
  animation-duration: 7s;
}
.parallax > use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 10s;
}
.parallax > use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 13s;
}
.parallax > use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 20s;
}
@keyframes move-forever {
  0% {
    transform: translate3d(-90px, 0, 0);
  }
  100% {
    transform: translate3d(85px, 0, 0);
  }
}
/*Shrinking for mobile*/
@media (max-width: 768px) {
  .waves {
    height: 40px;
    min-height: 40px;
  }
  .content {
    height: 30vh;
  }
  h1 {
    font-size: 24px;
  }
}
</style>
