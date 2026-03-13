<!--
 * @Description: 个人信息
-->
<template>
  <contact-dialog v-model="showContact" />
  <div class="person-fixed-button" @click="router.push({ name: 'projectExperience' })">
    <el-icon>
      <Right />
    </el-icon>{{ $t('查看项目') }}
  </div>
  <div class="person-fixed-button contact-button" @click="showContact = true">
    <el-icon>
      <Message />
    </el-icon>
    <span>{{ $t('联系我') }}</span>
  </div>
  <a href="https://github.com/CDCDA" target="_blank" class="person-fixed-button github-button">
    <svg-icon class="github-svg" iconName="techStackSvg-git-white" />
    <span>{{ $t('git') }}</span>
  </a>
  <div class="page-main personal-profile-main">
    <PersonalInfo class="bounceInDown animated"></PersonalInfo>
    <div class="ps-welcome c-left animated">
      <video class="welcome-video" :src="'/video/video_1800042575.mp4'" autoplay loop muted />
      <span class="welcome-title">{{ $t('CCCC的记录站点') }}</span>
      <!-- <svg-icon iconName="commonSvg-礼花" /> -->
      <span class="welcome-name">{{ $t('陈毅东(1999年生)') }}</span>
      <span class="welcome-profession">{{ $t('全栈开发工程师') }}</span>
    </div>
    <!-- <div class="ps-timeline c-right animated">
      <GoalTimeLine></GoalTimeLine>
    </div> -->
    <div class="ps-info">
      <div class="ps-info-left c-left animated">
        <div class="title">{{ $t('技术栈') }}</div>
        <div class="text">{{ $t('感觉脑子不是自己的') }}o_O</div>
        <technology-stack-card></technology-stack-card>
      </div>
      <div class="ps-info-right">
        <div class="ps-info-map c-right animated">
          <c-image class="map-img" :src="'http://120.48.127.181/file/other/工作地址.png'" />
          <div class="map-tip">{{ $t('现居 福州') }}</div>
        </div>
        <div class="ps-info-life c-right animated">
          <div class="life-college">{{ $t('毕业于') }}<span>福州大学(本科)</span></div>
          <div class="life-profession">
            {{ $t('专业为') }}<span>{{ $t('计算机科学与技术') }}</span>
          </div>
          <div class="life-education">
            {{ $t('目前') }}<span>{{ $t('在职') }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="ps-info" style="height: 250px">
      <div class="ps-character">
        <div class="character-title">{{ $t('性格') }}</div>
        <p class="character-name">{{ $t('执政官') }}</p>
        <p class="character-abbreviation">ESFJ-A</p>
        <p class="character-comment">{{ $t('感觉不太准，但又有很多人喜欢玩这个') }}</p>
        <p class="character-associate">
          {{ $t('感兴趣的可以去玩玩')
          }}<a target="_blank" href="https://www.16personalities.com/ch/%E4%BA%BA%E6%A0%BC%E6%B5%8B%E8%AF%95">{{
            $t('性格测试') }}</a>
        </p>
      </div>
      <div class="ps-strong-point">
        <div class="point-title">{{ $t('爱好') }}</div>
        <p class="point-name">{{ $t('游戏') }}</p>
        <p class="point-name">{{ $t('健身') }}</p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { useRouter } from 'vue-router';
const router = useRouter();
import { ref } from 'vue';
import { Message } from '@element-plus/icons-vue';
import PersonalInfo from './components/personalInfo.vue';
import TechnologyStackCard from './components/technologyStackCard.vue';
import GoalTimeLine from './components/goalTimeLine.vue';
import ContactDialog from '@/components/contactDialog/index.vue';

const showContact = ref(false);
</script>
<style lang="scss" scoped>
@include theme() {
  .person-fixed-button {
    position: fixed;
    top: 65px;
    right: 6%;
    z-index: 99;
    width: 6rem;
    height: 2rem;
    background: #404040;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 25px;
    backdrop-filter: blur(10px);
    color: white;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;

    .el-icon {
      font-size: 18px;
      margin-right: 5px;
    }
  }

  .person-fixed-button:hover {
    transform: translateX(5px);
  }

  .github-button {
    top: 165px;

    .github-svg {
      width: 18px;
      height: 18px;
      margin-right: 8px;
      margin-bottom: -3px;
    }
  }

  .contact-button {
    top: 115px;
  }

  .personal-profile-main {
    @include flex-column;
    justify-content: start;
    background: transparent !important;
    backdrop-filter: none !important;

    .ps-welcome {
      width: 100%;
      height: 300px;
      @include flex-column;
      justify-content: space-evenly;
      background: get('back');
      box-shadow: get('box-shadow');
      margin: 20px;
      position: relative;
      overflow: hidden;
      border-radius: 12px;
      font-weight: bold;

      .welcome-video {
        height: 100%;
        width: 100%;
        object-fit: cover;
        position: absolute;
        top: 0;
        left: 0;
      }

      .welcome-title {
        @include flex;
        font-size: 30px;
        // background-image: linear-gradient(to right, #f0c27b, #4b1248);
        -webkit-background-clip: text;

        // -webkit-text-fill-color: transparent;
        .svg-icon {
          width: 40px;
          height: 40px;
          margin-left: 10px;
        }
      }

      .welcome-name {
        font-size: 30px;
        // color: get('font-color');
      }

      .welcome-profession {
        font-size: 35px;
      }
    }

    span {
      z-index: 10;
      color: white;
    }
  }

  .ps-timeline {
    width: 100%;
    height: 450px;
    box-shadow: get('box-shadow');
    border-radius: 12px;
    overflow: hidden;
    margin-bottom: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .ps-info {
    width: 100%;
    height: 450px;
    margin-bottom: 20px;
    @include flex;

    .ps-info-left {
      width: 50%;
      height: calc(100% - 40px);
      background: get('back');
      box-shadow: get('box-shadow');
      border-radius: 12px;
      margin-right: 20px;
      overflow: hidden;
      padding: 20px 0;
      position: relative;

      .title {
        text-align: left;
        margin-left: 33px;
        margin-bottom: 15px;
      }

      .text {
        text-align: left;
        margin-left: 26px;
        font-size: 37px;
        margin-top: 2px;
        font-weight: bold;
        margin-bottom: 18px;
      }
    }

    .ps-info-right {
      width: calc(50% - 20px);
      height: 100%;
      @include flex-column;
      justify-content: start;

      .ps-info-map {
        height: calc(100% - 170px);
        width: 100%;
        background: get('back');
        box-shadow: get('box-shadow');
        border-radius: 12px;
        margin-bottom: 20px;
        position: relative;
        overflow: hidden;

        .map-img {
          height: 100%;
          width: 100%;
        }

        .map-tip {
          height: 50px;
          width: 100%;
          color: get('re-font-color');
          position: absolute;
          bottom: 0;
          backdrop-filter: blur(20px);
          @include flex;
          justify-content: start;
          background: rgba(0, 0, 0, 0.2);
          padding-left: 20px;
          font-size: 22px;
        }
      }

      @keyframes tip-up {
        0% {
          bottom: -70px;
        }

        100% {
          bottom: 0;
        }
      }

      @keyframes tip-down {
        0% {
          bottom: 0;
        }

        100% {
          bottom: -70px;
        }
      }

      .ps-info-map:hover {
        .map-tip {
          animation: tip-down 0.4s forwards linear;
        }

        .map-img {
          transform: scale(1.2);
          transition: 1s ease-in-out;
        }
      }

      .ps-info-map:not(:hover) {
        .map-tip {
          animation: tip-up 0.4s forwards linear;
        }

        .map-img {
          transform: scale(1);
          transition: 1s ease-in-out;
        }
      }

      .ps-info-life {
        height: 150px;
        width: 100%;
        @include flex;
        justify-content: space-evenly;
        background: get('back');
        box-shadow: get('box-shadow');
        border-radius: 12px;

        .life-college,
        .life-profession,
        .life-education {
          height: 100%;
          @include flex-column;

          span {
            font-size: 30px;
            font-weight: bold;
            margin-top: 10px;
            color: get('bk');
          }
        }
      }
    }
  }

  .ps-character {
    width: calc(60% - 40px);
    height: calc(100% - 40px);
    background: get('back');
    box-shadow: get('box-shadow');
    border-radius: 12px;
    margin-right: 20px;
    overflow: hidden;
    padding: 20px 20px;
    position: relative;
    background-image: url('@/assets/png/fire.png');
    background-repeat: no-repeat;
    object-fit: cover;
    background-size: 22% auto;
    background-position: 96% 46%;

    p,
    div {
      text-align: left;
    }

    p {
      margin: 10px 0;
    }

    // <p class="character-name">{{ $t('执政官') }}</p>
    //   <p class="character-abbreviation">ESFJ-A</p>
    //   <p class="character-comment">{{ $t('感觉不太准，但又有很多人喜欢玩这个') }}</p>
    //   <p class="character-associate">
    .character-name,
    .character-abbreviation {
      font-size: 35px;
      font-weight: bold;
    }

    .character-comment {
      position: absolute;
      bottom: 40px;
    }

    .character-associate {
      position: absolute;
      bottom: 10px;
    }
  }

  .ps-strong-point {
    width: calc(40% - 60px);
    height: calc(100% - 40px);
    background: url('@/assets/images/star.webp') no-repeat;
    background-size: 100% 100%;
    box-shadow: get('box-shadow');
    border-radius: 12px;
    overflow: hidden;
    padding: 20px 20px;

    p,
    div {
      text-align: left;
      z-index: 10;
      color: white;
    }

    p {
      font-size: 1.2rem;
    }
  }
}
</style>
