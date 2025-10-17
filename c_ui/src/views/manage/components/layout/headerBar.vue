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
      <el-tooltip content="首页" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon
            iconName="commonSvg-主页1"
            class="header-icon console"
            v-permission="'show'"
            @click="toMain"
          />
        </i>
      </el-tooltip>
      <el-tooltip content="皮肤" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon
            iconName="commonSvg-皮肤1"
            class="header-icon logout"
            @click="openThemeDialog"
          />
        </i>
      </el-tooltip>
      <el-tooltip content="登出" placement="top">
        <i class="svg-icon-wrap">
          <svg-icon iconName="commonSvg-登出" class="header-icon logout" @click="logout" />
        </i>
      </el-tooltip>
      <c-image class="avatar" :src="userStore.avatar"></c-image>
    </div>
    <theme-dialog @closeThemeDialog="closeThemeDialog" v-if="isThemeDialogShow" ref="themeDialog" />
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import useUserStore from '@/store/modules/user';
import useThemeStore from '@/store/modules/theme.ts';
import { ElMessageBox } from 'element-plus';
import ThemeDialog from '@/views/layout/theme/themeDialog.vue';
import TagsView from '@/views/manage/components/layout/tabsView.vue';
const router = useRouter();
const userStore = useUserStore() as any;
const themeStore = useThemeStore() as any;
const isThemeDialogShow = ref(false) as any;

function closeThemeDialog() {
  isThemeDialogShow.value = false;
}
function toPersonal() {
  router.push({ path: '/personalInfo' });
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
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
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
@include theme() {
  .headerBar {
    box-shadow: get('box-shadow');
  }
}
.headerBar {
  height: 2.8rem;
  background: white;
  padding: 0 15px;
  z-index: 10;
  position: relative;
  .avatar {
    height: 2rem;
    width: 2rem;
    border-radius: 2rem;
    margin-left: 0.5rem;
    border: 3px solid white;
    cursor: pointer;
  }
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
