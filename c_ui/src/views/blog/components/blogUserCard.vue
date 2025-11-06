<!--
 * @Description: 作者信息卡
-->
<template>
  <div class="blog-user-card c-card" ref="blogUserCard" id="blog-user-card">
    <div class="filter-bg">
      <div class="user-header">{{ $t('熬夜成仙吧') }}</div>
      <div class="user-avatar">
        <el-avatar :src="userInfo.avatar"></el-avatar>
        <div class="intro">{{ $t('我是一名全栈开发工程师') }}</div>
      </div>
      <div class="user-footer">
        <div class="footer-leftSide">
          <div class="user-name">{{ userInfo.nickName }}</div>
          <el-tooltip :content="userInfo.intro">
            <div class="user-motto no-wrap">{{ userInfo.intro }}</div>
          </el-tooltip>
        </div>
        <div class="footer-rightSide">
          <svg-icon
            iconName="commonSvg-微信"
            class="footer-icon weixin"
            @click="toQrCode"
          ></svg-icon>
          <svg-icon iconName="commonSvg-github" class="footer-icon github"></svg-icon>
        </div>
      </div>
    </div>
  </div>

  <c-dialog class="weixin-qrcode" v-model="dialogVisible" width="300" :modal="true">
    <c-image :src="weixinQrcodeUrl"></c-image>
  </c-dialog>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import useUserStore from '@/store/modules/user';
import { getUserById } from '@/api/system/user';
import { useLazyAppear } from '@/utils/lazy';
const userStore = useUserStore();
const userInfo = ref({}) as any;
const blogUserCard = ref(null) as any;
const weixinQrcodeUrl = new URL(
  '@/assets/images/3ca475b86ed6c381f709391a8edf54af.jpg',
  import.meta.url
).href;
const props = defineProps({
  visibleLazy: {
    type: Boolean,
    default: true
  }
});
const dialogVisible = ref(false);

// 获取用户信息
async function getUserInfo(userId: any) {
  const { code, data } = (await getUserById(userId)) as any;
  if (code === 200) {
    Object.assign(userInfo.value, data);
  }
}

function toQrCode() {
  dialogVisible.value = true;
}

onMounted(() => {
  getUserInfo(userStore.userId);
  props.visibleLazy ? useLazyAppear(blogUserCard.value) : '';
});
</script>
<style lang="scss" scoped>
@include theme() {
  .filter-bg {
    background: transparent;
    backdrop-filter: blur(5px);
    padding: 20px;
    @include flex-column;
  }
  .blog-user-card {
    transition: cubic-bezier(0.075, 0.82, 0.165, 1) 0.4s;
    overflow: hidden;
    padding: 0 !important;
    width: calc(100%) !important;
    background: url('http://120.48.127.181/file/other/Ruins.jpg') center 28% / cover no-repeat !important;
    color: get('re-font-color') !important;
    .user-header {
      height: 24px;
      width: fit-content;
      padding: 2px 20px;
      border-radius: 8px;
      font-size: 0.8rem;
      margin: 10px 0 25px 0;
      color: get('re-font-color');
      cursor: pointer;
      @include flex;
      background: rgba(255, 255, 255, 0.2);
    }
    .user-header:hover {
      transform: scale(1.05);
      color: get('font-color');
      background: white;
      transition: ease-in-out 0.3s;
    }
    .intro {
      position: absolute;
      opacity: 0;
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;
      @include flex;
    }
    .user-avatar {
      height: 10rem;
      margin: 10px 0 0 0;
      width: 10rem;
      position: relative;
      @include flex;
      .el-avatar {
        width: 7rem;
        height: 7rem;
        position: absolute;
        @include flex;
        animation: huxi_light 4s infinite;
        border: solid 5px white;
        transition: cubic-bezier(0.075, 0.82, 0.165, 1) 0.4s;
      }
    }
    .user-footer {
      height: 60px;
      width: 100%;
      display: flex;
      justify-content: space-around;
      align-items: center;

      margin: 20px 0 0 0;
      .footer-leftSide {
        @include flex-column;
        align-items: start;
        .user-name {
          font-weight: bold;
          font-size: 22px;
          margin: 2px 0;
        }
        .user-motto {
          font-size: 16px;
          float: left;
          margin: 0;
          margin: 2px 0;
        }
      }
      .footer-rightSide {
        @include flex;
        margin-right: -10px;
        .footer-icon {
          width: 40px;
          height: 40px;
          margin: 10px;
          cursor: pointer;
        }
      }
    }
  }
  .blog-user-card:hover {
    .el-avatar {
      height: 0;
      width: 0;
      animation: none;
      border: none;
    }
    .intro {
      opacity: 1;
      transition: cubic-bezier(0.075, 0.82, 0.165, 1) 0.4s;
    }
  }
}
</style>
