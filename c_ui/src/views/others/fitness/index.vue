<!--
 * @Description: 健身
-->
<template>
  <div class="page-main fitness-main">
    <div class="fitness-left">
      <div class="fitness-card-wrap animated-0s5 bounceInLeft">
        <WeekNavication @get-index="getIndex" class="fitness-nav" />
        <div class="fitness-card">
          <div class="fitness-card-main" v-for="plan in trainingPlan">
            <h1>{{ plan.title }}</h1>
            <div class="plan">
              <div class="plan-item" v-for="item in plan.plan">
                <span class="movement">{{ item.movement }}</span>
                <span class="quantity">{{ item.quantity }}</span>
                <span class="turn">{{ item.turn }}</span>
                <span class="time">{{ item.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="fitness-stage-goal animated bounceInUp">
        <div class="goal-header">
          <el-icon><HelpFilled /><span class="tag-name">目标</span></el-icon>
        </div>
        <div class="goal-content">
          <div class="goal-text">
            <span>体重</span><span>59kg</span><el-icon><Right /></el-icon><span>57.5kg</span>
          </div>
          <div class="goal-text">
            <span>体脂</span><span>15%-16%</span><el-icon><Right /></el-icon><span>13%-14%</span>
          </div>
          <div class="goal-text">
            <span>单次不间断跑步距离</span><span>4km</span><el-icon><Right /></el-icon
            ><span>5km</span>
          </div>
        </div>
      </div>
    </div>
    <div class="fitness-right animated-1s5 bounceInDown">
      <img class="fitness-right-img" :src="'http://120.48.127.181/file/fitness.jpg'" />
      <div class="fitness-title">健身</div>
      <div class="fitness-motto">还是先锻炼起来再说吧 <br />不管怎么说，再小的力量也是力量</div>
      <div class="fitness-grievance">好难减啊<img :src="'@/assets/svg/emote/cry.svg'" /></div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import WeekNavication from './components/weekNavigation.vue';
import bubbles from '@/components/bubbles.vue';
const trainingPlan = ref([
  {
    title: '胸+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '哑铃卧推(4个动作)',
        quantity: '12个',
        time: '25min',
        turn: '3轮'
      },
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '3min',
        turn: '3轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '腹+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '腹肌撕裂者',
        quantity: '',
        time: '15min',
        turn: '1轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '胸+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '哑铃卧推(4个动作)',
        quantity: '12个',
        time: '25min',
        turn: '3轮'
      },
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '3min',
        turn: '3轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '腹+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '腹肌撕裂者',
        quantity: '',
        time: '15min',
        turn: '1轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '胸+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '哑铃卧推(4个动作)',
        quantity: '12个',
        time: '25min',
        turn: '3轮'
      },
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '3min',
        turn: '3轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '腹+燃脂',
    plan: [
      {
        movement: '跪姿俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '标准俯卧撑',
        quantity: '12个',
        time: '1min',
        turn: '1轮'
      },
      {
        movement: '腹肌撕裂者',
        quantity: '',
        time: '15min',
        turn: '1轮'
      },
      {
        movement: 'hit+搏击操/跑步/爬楼',
        quantity: '',
        time: '40min+',
        turn: '1轮'
      }
    ]
  },
  {
    title: '躺平',
    plan: [
      {
        movement: '放纵餐',
        quantity: '1顿',
        time: '',
        turn: ''
      }
    ]
  }
]) as any;

const index = ref(0) as any;

function getIndex(i: any) {
  headerRoll(i);
}

function headerRoll(i: any) {
  let el = document.querySelector('.fitness-card') as any;
  let offset = 0;
  offset = -396.8 * i;
  el.style.transform = `translate3d(0px, ${offset}px,0)`;
}

onMounted(() => {});
</script>

<style lang="scss" scoped>
@include theme() {
  .fitness-card {
    transition-duration: 300ms;
    transform: translate3d(0px, 0, 0);
  }
  .fitness-card-main {
    overflow: hidden;
    height: 377px;
    @include flex-column;
    position: relative;
    border: 3px dashed #7d7d7d;
    padding: 0 20px;
    border-radius: 8px;
    margin: 15px;
    color: get('font-color');
  }
  .fitness-main {
    @include flex;
    align-items: start;
    overflow: hidden;
    background: transparent !important;
    backdrop-filter: none !important;
  }
  .fitness-right {
    background: get('back');
    box-shadow: get('box-shadow');
    border-radius: 8px;
    height: 95vh;
  }
  .fitness-left {
    width: calc(100% - 370px);
    @include flex-column;
    margin-right: 20px;
    border-radius: 8px;
    height: 95vh;
    justify-content: start;
    .fitness-card-wrap {
      height: 420px;
      width: 100%;
      @include flex;
      background: get('back');
      box-shadow: get('box-shadow');
      border-radius: 8px;
      overflow: hidden;
      .fitness-nav {
        @include flex;
        width: 90px;
        background: transparent;
        border-right: 2px solid #d1d1d1;
        // box-shadow: get('box-shadow');
        border-radius: 15px 0 0 15px;
      }
      .fitness-card {
        height: 100%;
        // padding:0 200px;
        width: calc(100% - 90px);
        background: transparent;
        border-radius: 0 15px 15px 0;
      }
    }
    .fitness-stage-goal {
      height: calc(87vh - 400px);
      width: calc(100% - 20px);
      color: get('font-color');
      margin-top: 20px;
      padding: 10px 10px 20px 10px;
      background: get('back');
      box-shadow: get('box-shadow');
      border-radius: 8px;
      @include flex-column;
      justify-content: space-between;
      .goal-header {
        @include flex;
        width: 100%;
        justify-content: start;
        font-size: 1rem;
        font-weight: bold;
        .el-icon {
          margin: 8px;
          font-size: 22px;
          width: auto;
        }
      }
      .goal-content {
        width: calc(100% - 60px);
        height: 100%;
        .goal-text {
          @include flex;
          justify-content: space-evenly;
          padding: 10px;
          border-bottom: 3px dashed #7d7d7d;
          span,
          .el-icon {
            width: 25%;
          }
          & > span {
            padding: 5px 15px;
          }
          & > span:last-child {
            color: green;
          }
          & > span:hover {
            background: get('bk');

            color: white;
            border-radius: 5px;
          }
        }
      }
      .goal-grievance {
        background: get('bk');
        color: white;
        width: 90%;
        padding: 10px 30px;
        border-radius: 10px;
      }
      .goal-grievance:hover {
        transform: scale(1.03);
      }
    }
  }
  .fitness-right {
    width: 350px;
    position: relative;
    color: white;
    .fitness-right-img {
      object-fit: cover;
      background-position: center;
      height: 100%;
      width: 100%;
      border-radius: 8px;
    }
    .fitness-title {
      position: absolute;
      top: 10px;
      left: 10px;
      writing-mode: vertical-rl; /* 设置文字竖排 */
      text-orientation: upright; /* 设置文字正立显示 */
    }
    .fitness-motto {
      font-weight: bold;
      top: 10px;
      left: 40px;
      writing-mode: vertical-rl; /* 设置文字竖排 */
      text-orientation: upright; /* 设置文字正立显示 */
      font-size: 1.2rem;
      position: absolute;
    }
    .fitness-grievance {
      position: absolute;
      writing-mode: vertical-rl; /* 设置文字竖排 */
      text-orientation: upright; /* 设置文字正立显示 */
      bottom: 10px;
      right: 10px;
      img {
        width: 20px;
        margin: 3px;
      }
    }
  }
  .plan {
    @include flex-column;
    justify-content: start;
    height: calc(100% - 100px);
    width: 100%;
  }
  .plan-item {
    display: flex;
    width: calc(100% - 20px);
    justify-content: space-between;
    border-bottom: 3px dashed #7d7d7d;
    padding: 10px 0;
    span {
      width: 20%;
      text-align: left;
    }
    .movement {
      width: 40%;
    }
    .time {
      width: 20%;
      text-align: right;
    }
  }
}
</style>
