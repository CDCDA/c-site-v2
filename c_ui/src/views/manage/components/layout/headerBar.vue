<!--
 * @Description: 管理页顶栏
-->
<template>
  <div class="headerBar flex-between">
    <div class="left-toolbar">
      <el-icon @click="changeCollapse">
        <Fold v-if="!themeStore.isCollapse" />
        <Expand v-else />
      </el-icon>
      <TagsView></TagsView>
    </div>
    <div class="right-toolbar">
      <el-tooltip :content="$t('首页')" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon
            iconName="commonSvg-主页1"
            class="header-icon console"
            v-permission="'show'"
            @click="toMain"
          />
        </i>
      </el-tooltip>
      <el-tooltip :content="$t('皮肤')" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon
            iconName="commonSvg-皮肤1"
            class="header-icon logout"
            @click="openThemeDialog"
          />
        </i>
      </el-tooltip>
      <el-tooltip :content="$t('语言切换')" placement="top">
        <language class="header-icon language"></language>
      </el-tooltip>
      <el-tooltip :content="$t('消息')" placement="top">
        <message class="header-icon bell"></message>
      </el-tooltip>
      <avatar class="avatar"></avatar>
    </div>
    <theme-dialog @closeThemeDialog="closeThemeDialog" v-if="isThemeDialogShow" ref="themeDialog" />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import useUserStore from '@/store/modules/user';
import useThemeStore from '@/store/modules/theme.ts';
import { ElMessageBox } from 'element-plus';
import ThemeDialog from '@/views/layout/theme/themeDialog.vue';
import TagsView from '@/views/manage/components/layout/tabsView.vue';
import Language from '@/views/layout/commonHeader/components/language.vue';
import Message from '@/views/layout/commonHeader/components/message.vue';
import Avatar from '@/views/layout/commonHeader/components/avatar.vue';
const router = useRouter();
const userStore = useUserStore() as any;
const themeStore = useThemeStore() as any;
const isThemeDialogShow = ref(false) as any;

function closeThemeDialog() {
  isThemeDialogShow.value = false;
}

function changeCollapse() {
  themeStore.isCollapse = !themeStore.isCollapse;
}

function openThemeDialog() {
  isThemeDialogShow.value = true;
}

function toMain() {
  const routeUrl = router.resolve({
    name: 'home'
  });
  window.open(routeUrl.href, '_blank');
}

function logout() {
  ElMessageBox.confirm($t('确定注销并退出系统吗？'), $t('提示'), {
    confirmButtonText: $t('确定'),
    cancelButtonText: $t('取消'),
    type: 'warning'
  })
    .then(() => {
      userStore.userId = '';
      userStore.userName = '';
      userStore.token = '';
      userStore.permission = [];
      window.localStorage.setItem('userData', '');
      router.push({ name: 'login' });
    })
    .catch(() => {});
}

onMounted(() => {});
</script>
<style lang="scss" scoped>
.headerBar {
  height: 2.8rem;
  background: white;
  padding: 0 15px;
  z-index: 10;
  position: relative;

  .icon {
    font-size: 1.2rem;
    cursor: pointer;
  }
  .left-toolbar {
    display: flex;
    align-items: center;
    justify-content: start;
    width: calc(100% - 200px);
    .el-icon {
      margin-right: 10px;
    }
  }
  .right-toolbar {
    display: flex;
    align-items: center;
    .svg-icon-wrap {
      cursor: pointer;
      font-size: 1.2rem;
      margin-right: 10px;
    }
  }
}
</style>
<style lang="scss" scoped>
@include theme() {
  .headerBar {
    box-shadow: get('box-shadow');
    :deep(.svg-icon-wrap:hover) {
      color: get('re-font-color') !important;
    }
  }
}
</style>
