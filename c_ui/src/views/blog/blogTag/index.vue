<!--
 * @Description:  博客标签
-->
<template>
  <div class="blog-tag page-main">
    <div class="cards re-slide-in" v-for="cards in tagCardList">
      <div class="card" v-for="(item, i) in cards" @click="toTagPage(item)">
        <div class="card-face">
          <div class="card-label">
            <div
              style="
                width: 100%;
                height: 100%;
                border-radius: 12px;
                background: linear-gradient(to right, rgb(65 67 69 / 100%), rgb(35 37 38 / 100%));
                backdrop-filter: blur(10px);
              "
            ></div>
            <el-image class="card-bg" :src="item.coverUrl" fit="cover">
              <template #placeholder>
                <div
                  class="image-slot"
                  v-cLoading="'rotate'"
                  style="width: 100%; height: 100%"
                ></div>
              </template>
            </el-image>
            <div class="card-bg-cover"></div>
            <span class="card-label-count"
              >{{ item.total }}<span>{{ $t('篇') }}</span></span
            >
            <span class="card-label-text">{{ item.tagName }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref } from 'vue';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import { pageTags } from '@/api/tag.ts';
const router = useRouter();
const tagCardList = ref([]) as any;
const tagList = ref([]) as any;

async function getTagList() {
  const { code, rows, total } = (await pageTags({ pageSize: 20 })) as any;
  if (code === 200) {
    tagList.value = rows;
    const tempList = [];
    for (let i = 0; i < tagList.value.length; i = i + 10) {
      tempList.push(tagList.value.slice(i, i + 10));
    }
    tagCardList.value = tempList;
  }
}

function toTagPage(item: any) {
  router.push({ name: 'blogTagPage', query: { tagId: item.tagId } });
}

const animateTimeList = ['0s5', '0s7', '1s', '1s2', '1s5', '1s7', '2s', '2s2', '2s5', '2s7', '3s'];

function getAnimateTime() {
  return `animated-${animateTimeList[Math.floor(Math.random() * 9) + 1]}`;
}

onMounted(() => {
  getTagList();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .blog-tag {
    @include flex;
    flex-direction: column;
    background: get('back');
    box-shadow: get('box-shadow');
    border-radius: 8px;
    min-height: calc(100vh - 75px) !important;
  }
  .cards {
    bottom: 0;
    display: flex;
    height: 250px;
    padding: 0 50px;
    justify-content: center;
    // position: fixed;
    left: 0;
    right: 0;
  }

  .card {
    height: 200px;
    margin: 0 -25px;
    position: relative;
    width: 150px;
    cursor: pointer;
  }
  // .card:after {
  //   bottom: 0;
  //   content: '';
  //   left: -60px;
  //   position: absolute;
  //   right: -60px;
  //   top:0;
  //   z-index: 10;
  // }

  .card-face {
    bottom: 0;
    content: '';
    left: 0;
    pointer-events: none;
    position: absolute;
    right: 0;
    top: 0;
    transition: all 1000ms cubic-bezier(0.19, 1, 0.22, 1);
    box-shadow: get('box-shadow');
  }

  .card-bg {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 1;
    border-radius: 10px;
    object-fit: cover;
    box-shadow: get('box-shadow');
    opacity: 0.9;
    background: get('font-color');
    filter: brightness(0.6);
    transition: all 1000ms cubic-bezier(0.19, 1, 0.22, 1);
  }
  .card-label-text {
    color: white;
    font-weight: 700;
    font-size: 21px;
    z-index: 10;
    position: absolute;
    margin-left: 15px;
    top: 10px;
    left: 10px;
  }
  .card-label-count {
    position: absolute;
    left: 25px;
    bottom: 15px;
    color: white;
    font-weight: 700;
    font-size: 0.9rem;
    z-index: 10;
  }

  .card-label {
    font-size: 24px;
    font-weight: bold;
    letter-spacing: -0.025em;
    text-align: left;
    position: relative;
    width: 100%;
    height: 100%;
    @include flex;
  }

  .card-bg-cover {
    position: absolute;
    width: calc(100% - 10px);
    height: calc(100% - 10px);
    margin: 5px;
    border: 1px solid #a9a9a9;
    z-index: 1;
    border-radius: 8px;
  }

  .card .card-face {
    background: get('back');
    // box-shadow: get('box-shadow');
    border-radius: 10px;
    transform: translateY(15px) rotate(5deg);
  }
  // .card:hover:after {
  //   top: -175px;
  // }
  // .card:hover .card-face:after {
  //   animation: fade 1s ease-out forwards linear;
  //   transition: all 1s;
  // }
  .card:hover .card-face {
    // box-shadow: 0 10px 20px rgba(0, 0, 0, 0.4), inset 0 0 0 2px rgba(255, 214, 153, 0.75);
    transform: translateY(-50px) rotate(0deg) scale(1.3);
    z-index: 999;
    .card-bg {
      filter: brightness(1);
    }
  }

  @keyframes fade {
    0% {
      opacity: 0.9;
      transform: scale(1);
    }
    100% {
      opacity: 0;
      transform: scale(1.15);
    }
  }
}
.loader {
  height: 50px !important;
}
</style>
