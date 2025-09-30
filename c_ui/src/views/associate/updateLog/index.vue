<!--
 * @Description:  更新日志
-->
<template>
  <div class="update-log page-main">
    <div class="update-log-top fade-in">
      <c-image
        :src="'http://120.48.127.181/file/quiet-forest.jpg'"
        style="width: 100%; height: 100%"
      />
    </div>
    <div class="update-log-preface">
      <h2>前言</h2>
      <p>许多样式来源于网络，只不过是用我自己的方法区实现类似效果</p>
      <p>随缘更新，随缘记录</p>
      <h2>更新日志</h2>
    </div>
    <section class="timeline">
      <ul>
        <li class="" v-for="date in Object.keys(updateLogs)">
          <div>
            <time>{{ date }}</time>
            <div class="discovery">
              <h1 v-for="(e, i) in updateLogs[date]">
                <span>{{ i + 1 }}</span
                >{{ e }}
              </h1>
            </div>
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { listUpdateLog } from '@/api/system/updateLog';
import { autoClearTimer } from '@/utils/timer';
const updateLogs = ref({} as any);

async function getList() {
  const { code, data } = (await listUpdateLog({ operation: '', pageNum: 1, pageSize: 999 })) as any;
  if (code == 200) {
    updateLogs.value = [];
    data.list.forEach((e: any) => {
      if (!updateLogs.value[e.operateTime]) {
        updateLogs.value[e.operateTime] = [];
      }
      updateLogs.value[e.operateTime].push(e.operation);
    });
    autoClearTimer(() => {
      var items = document.querySelectorAll('.timeline li');

      function isElementInViewport(el: any) {
        var rect = el.getBoundingClientRect();
        return (
          rect.top >= 0 &&
          rect.left >= 0 &&
          rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
          rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
      }

      function callbackFunc() {
        for (var i = 0; i < items.length; i++) {
          if (isElementInViewport(items[i])) {
            if (!items[i].classList.contains('in-view')) {
              items[i].classList.add('in-view');
            }
          } else if (items[i].classList.contains('in-view')) {
            items[i].classList.remove('in-view');
          }
        }
      }
      window.addEventListener('scroll', callbackFunc, true);
    }, 0);
  }
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss" scoped>
.update-log-top {
  height: 40vh;
  width: calc(100%);
  border-radius: 8px;
  overflow: hidden;
  object-fit: cover;
  margin: 40px 0 30px 0;
  background-position: center;
}
.update-log-preface {
  width: 100%;
  // text-align: left;
  h2 {
    margin-bottom: 20px;
  }
  p {
    margin: 10px 0;
  }
}

@include theme() {
  .update-log-top {
    box-shadow: get('box-shadow');
  }
  .page-main.update-log {
    @include flex-column;
    font: normal 16px/1.5 'Titillium Web', sans-serif;
    background: get('back');
    color: #3c3f64;
    overflow-x: hidden;
    border-radius: 8px;
    padding-bottom: 50px;
    padding: 0 80px 50px 80px;
    box-shadow: get('box-shadow');
    color: get('font-color');
  }
}
*,
*::before,
*::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.timeline ul li {
  list-style-type: none;
  position: relative;
  width: 6px;
  margin: 0 auto;
  padding-top: 50px;
  background: #fff;
}

.timeline ul li::after {
  content: '';
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%) rotate(45deg);
  width: 20px;
  height: 20px;
  z-index: 2;
  background: #eee;
}
.timeline ul li div {
  position: relative;
  bottom: 0;
  width: 25rem;
  padding: 20px;
  background: #fff;
  box-shadow: 4px 13px 30px 1px rgba(252, 56, 56, 0.2);
  border-radius: 5px;
  display: flex;
  align-items: center;
}
.timeline ul li div time {
  position: absolute;
  background: #f5af19;
  color: white;
  width: fit-content;
  height: 30px;
  top: -15px;
  border-radius: 5px;
  padding: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  letter-spacing: 2px;
}
.timeline ul li div div {
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.timeline ul li div div p {
  text-align: center;
}
.timeline ul li .discovery {
  text-align: left;
  margin-right: 10px;
  h1 {
    width: 90%;
  }
  span {
    margin-top: 0.65em;
    width: 1.45em;
    height: 1.45em;
    border-radius: 0.725em;
    text-align: center;
    font-size: 0.85em;
    line-height: 1.45em;
    display: inline-block;
    margin-right: 5px;
    background: #f5af19;
    color: white;
  }
}
.timeline ul li:nth-of-type(odd) > div {
  left: 3rem;
}
.timeline ul li:nth-of-type(even) > div {
  left: -28rem;
}

.timeline ul li div {
  visibility: hidden;
  opacity: 0;
  transition: all 0.5s ease-in-out;
}
.timeline ul li:nth-of-type(odd) div {
  transform: translate3d(100px, -10px, 0) rotate(10deg);
}
.timeline ul li:nth-of-type(even) div {
  transform: translate3d(-100px, -10px, 0) rotate(10deg);
}
.timeline ul li.in-view div {
  transform: none;
  visibility: visible;
  opacity: 1;
}

@media screen and (max-width: 900px) {
  .timeline ul li div {
    width: 250px;
    flex-direction: column;
  }
  .timeline ul li div div {
    width: 80%;
    margin: 10px;
  }
  .timeline ul li:nth-of-type(even) > div {
    left: -289px;
  }
}
@media screen and (max-width: 600px) {
  body {
    background: #8bfff4;
  }

  .timeline ul li {
    margin-left: 20px;
  }
  .timeline ul li div {
    width: calc(100vw - 91px);
  }
  .timeline ul li:nth-of-type(even) > div {
    left: 4rem;
  }
}
</style>
