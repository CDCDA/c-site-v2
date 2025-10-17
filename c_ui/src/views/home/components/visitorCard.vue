<!--
 * @Description: 访问人卡
-->
<template>
  <div class="visitor-card c-card" id="card">
    <div class="card-header">
      <svg-icon
        iconName="commonSvg-礼花"
        style="width: 20px; height: 20px; margin-right: 8px"
      /><span class="tag-name">welcome</span>
    </div>
    <div class="visitor-location">
      欢迎来自
      <span>{{ ` ${visitorInfo.location?.regionName} ${visitorInfo.location?.city} ` }}</span
      >的小伙伴
    </div>
    <div class="visitor-distance">
      当前位置距离博主 <span>{{ ` ${visitorInfo.location.distance} ` }}</span
      >公里
    </div>
    <div class="visitor-ip">
      您的ip地址为<span>{{ ` ${visitorInfo.ip} ` }}</span>
    </div>
    <div class="visitor-greeting">{{ getGreeting() }}</div>
    <div class="visitor-salutation">{{ getSalutation() }}</div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue';
import { useLazyAppear } from '@/utils/lazy';
import axios from 'axios';
const visitorInfo = reactive({
  location: {
    distance: ''
  },
  ip: ''
}) as any;

// 获取ip
async function getIp() {
  // axios
  //   .get('https://api.ipify.org?format=json')
  //   .then(response => {
  //     visitorInfo.ip = response.data.ip; // 输出客户端IP地址
  //     getLocationByIp();
  //   })
  //   .catch(error => {
  //     console.log(error);
  //   });
}

// 获取地理位置
async function getLocationByIp() {
  const ip = visitorInfo.ip; // 替换为你要查询的IP地址

  axios
    .get(`http://ip-api.com/json/${ip}?lang=zh-CN`)
    .then(response => {
      visitorInfo.location = response.data;
      visitorInfo.location.distance = getDistance(
        39.9042,
        116.4074,
        visitorInfo.location.lat,
        visitorInfo.location.lon
      );
      // 在这里处理地理位置信息
    })
    .catch(error => {
      console.error(error);
      // 处理错误
    });
}

// 计算城市距离
function getDistance(lat1: any, lon1: any, lat2: any, lon2: any) {
  const R = 6371; // 地球半径，单位为千米

  const dLat = degreeToRadians(lat2 - lat1);
  const dLon = degreeToRadians(lon2 - lon1);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(degreeToRadians(lat1)) *
      Math.cos(degreeToRadians(lat2)) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  const distance = R * c; // 距离，单位为千米
  return Math.floor(distance);
}

function degreeToRadians(deg: any) {
  return deg * (Math.PI / 180);
}

//获取问候语
function getSalutation() {
  const now = new Date();
  const currentHour = now.getHours();
  if (currentHour <= 6) {
    return '现在是~夜猫子时间！';
  } else if (currentHour > 6 && currentHour <= 7) {
    return '早起的虫子被鸟吃';
  } else if (currentHour > 7 && currentHour <= 9) {
    return '睡醒的第一件事就是重睡';
  } else if (currentHour > 9 && currentHour <= 12) {
    return '现在是超棒的晨间时间，如果你不在工作的话';
  } else if (currentHour > 12 && currentHour <= 15) {
    return '我先睡了';
  } else if (currentHour > 15 && currentHour <= 16) {
    return '三点几了 饮茶先啦';
  } else if (currentHour > 15 && currentHour <= 18) {
    return '上班的等下班，上课等的下课';
  } else if (currentHour > 18 && currentHour <= 19) {
    return '该吃饭了伙计';
  } else if (currentHour > 19 && currentHour <= 23) {
    return '希望别熬夜，如果熬，建议通宵';
  }
}

function getGreeting() {
  const now = new Date();
  const currentHour = now.getHours();
  if (currentHour <= 6) {
    return '晚上好';
  } else if (currentHour > 6 && currentHour <= 12) {
    return '早上好';
  } else if (currentHour > 12 && currentHour <= 18) {
    return '下午好';
  } else if (currentHour > 18 && currentHour <= 23) {
    return '晚上好';
  }
}

function range(value: any, percent: any) {
  if (percent >= 0.5) {
    const p = (percent - 0.5) / 0.5;

    return -1 * value * p;
  }

  const p = (0.5 - percent) / 0.5;

  return value * p;
}

onMounted(() => {
  getIp();
  const element = document.querySelector('.visitor-card') as any;
  if (element) {
    useLazyAppear(element) as any;
  }
});
</script>
<style lang="scss" scoped>
@include theme() {
  #card {
    text-align: left;
    font-size: 1rem;
    padding: 15px !important;
    width: calc(100% - 30px) !important;
    .card-header {
      @include flex;
      width: 100%;
      justify-content: start;
      font-size: 1rem;
      font-weight: bold;
      .el-icon {
        margin: 8px;
        font-size: 22px;
      }
    }

    .visitor-location,
    .visitor-distance,
    .visitor-ip,
    .visitor-salutation {
      margin: 10px 0;
    }
    .visitor-location span {
      color: #49b1f5;
    }
    .visitor-distance span {
      color: #e38100;
    }
    .visitor-ip span {
      color: #49b1f5;
    }
    .visitor-greeting {
      color: #e38100;
    }
  }
}
</style>
