<!--
 * @Description:  侧边设置
-->
<template>
  <div class="side-setting" :class="props.isHideen ? 'is-hidden' : ''">
    <div
      class="setting-item"
      :class="menu.isShow ? 'is-show' : ''"
      @click="clickMenu(menu)"
      v-for="menu in menuData"
    >
      <el-tooltip
        class="box-item"
        effect="dark"
        :content="menu.tip"
        placement="top-start"
        show-after="1000"
      >
        <div class="setting-item-img" :class="menu.value">
          <svg-icon :iconName="menu.iconName" />
        </div>
      </el-tooltip>
    </div>
  </div>
  <theme-dialog @closeThemeDialog="closeThemeDialog" v-if="isThemeDialogShow" ref="themeDialog" />
  <AddLog v-if="isAddLogShow" @close="closeLog" :form="{}" />
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref } from 'vue';
import ThemeDialog from '@/views/layout/theme/themeDialog.vue';
import AddLog from '@/views/layout/sideSetting/components/addLog.vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const props = defineProps({
  isHideen: true as any
});
const isThemeDialogShow = ref(false) as any;
const isAddLogShow = ref(false) as any;
const isFull = ref(false) as any;
const menuData = ref([
  {
    iconName: 'commonSvg-博客',
    tip: $t('发布博客'),
    value: 'blog',
    isShow: false
  },
  {
    iconName: 'commonSvg-编辑',
    tip: $t('发布随笔'),
    value: 'edit',
    isShow: false
  },
  {
    iconName: 'commonSvg-皮肤',
    tip: $t('皮肤选择'),
    value: 'skin',
    isShow: true
  },
  {
    iconName: 'commonSvg-设置',
    tip: $t('更多设置'),
    value: 'setting',
    isShow: true
  },
  {
    iconName: 'commonSvg-上-实心三角',
    tip: $t('回到顶部'),
    value: 'up',
    isShow: true
  },
  {
    iconName: 'commonSvg-下-实心三角',
    tip: $t('直达底部'),
    value: 'down',
    isShow: true
  },
  {
    iconName: 'commonSvg-日志',
    tip: $t('新增日志'),
    value: 'log',
    isShow: false
  }
]) as any;

const pageMainStyle = ref({
  width: '',
  height: '',
  padding: '',
  margin: '',
  borderRadius: '',
  position: '',
  maxWidth: ''
}) as any;

const showAll = ref(false) as any;

function closeLog() {
  isAddLogShow.value = false;
}

function clickMenu(item: any) {
  switch (item.value) {
    case 'up': {
      scrollToView(item);
      break;
    }
    case 'down': {
      scrollToView(item);
      break;
    }
    case 'setting': {
      showMoreSetting();
      break;
    }
    case 'skin': {
      isThemeDialogShow.value = true;
      break;
    }
    case 'blog': {
      router.push({ name: 'blogEditor' });
      break;
    }
    case 'edit': {
      router.push({ name: 'essayEditor' });
      break;
    }
    case 'log': {
      isAddLogShow.value = true;
      break;
    }
  }
}

/**
 * @description: 控制设置显示
 * @return {*}
 */
function showMoreSetting() {
  // const allowSettings = ['up', 'down', 'setting', 'skin'];
  if (!showAll.value) {
    menuData.value.forEach((e: any) => {
      e.isShow = true;
    });
  } else {
    menuData.value.forEach((e: any) => {
      if (e.value == 'up' || e.value == 'down' || e.value == 'setting' || e.value == 'skin')
        e.isShow = true;
      else e.isShow = false;
    });
  }
  showAll.value = !showAll.value;
}

/**
 * @description: 滚动到指定的位置
 * @param {*} scrollTop
 * @return {*}
 */
function scrollToView(item: any) {
  let page = document.querySelector('.el-main') as any;
  if (item.value == 'down') page.scrollTo({ top: page.scrollHeight, behavior: 'smooth' });
  else page.scrollTo({ top: 0, behavior: 'smooth' });
}

function closeThemeDialog() {
  isThemeDialogShow.value = false;
}
</script>

<style lang="scss" scoped>
@keyframes right-in {
  0% {
    transform: translateX(100px);
  }
  100% {
    transform: translateX(0);
  }
}
@keyframes right-out {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(100px);
  }
}
@keyframes rotate {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
.is-hidden.side-setting {
  .setting-item {
    animation: right-out 0.5s linear forwards !important;
  }
}
.side-setting {
  .setting-item.is-show {
    animation: right-in 0.5s linear forwards;
  }
  .setting-item {
    animation: right-out 0.5s linear forwards;
  }
  .setting-item {
    .svg-icon:active {
      transform: translateY(1px) !important;
    }
  }
}

.side-setting {
  @include flex-column;
  width: 50px;
  position: absolute;
  right: 1.4%;
  bottom: 32%;
  z-index: 1;
  .setting-item {
    height: 2rem;
    width: 2rem;
    margin-bottom: 10px;
    cursor: pointer;
    transition: all 0.5s linear;
    box-shadow: 0 1px 1px 0 #000000;
    border-radius: 4px;
  }
  .setting-item-img {
    width: 100%;
    height: 100%;
    position: relative;
    border-radius: 4px;
    position: relative;
    @include flex;
    .svg-icon {
      z-index: 1;
      position: absolute;
      width: 70%;
      height: 70%;
      @include flex;
    }
  }
  .setting {
    .svg-icon {
      animation: rotate 2s linear infinite;
    }
  }
  //.blog,
  //.log {
  //  .svg-icon {
  //    margin-left: 4px;
  //  }
  //}
}
@include theme() {
  .setting-item-img {
    background: get('back');
  }
  .side-setting {
    .setting-item {
      color: get('re-back');
    }
  }
}
</style>
