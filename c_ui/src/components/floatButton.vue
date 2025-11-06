<!--
 * @Description:  
-->
<template>
  <div class="float-item-container" v-drag>
    <el-dropdown trigger="click" ref="dropDown" class="float-item" placement="top">
      <el-button icon="Menu" circle class="float-button"></el-button>
      <template #dropdown>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="routerTo('blogEditor')">
            <span>{{ $t('发布博客') }}</span>
          </el-dropdown-item>
          <el-dropdown-item
            @click.native="routerTo('blogEditor')"
            :disabled="disabled ? true : false"
          >
            <span>{{ $t('编辑博客') }}</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="routerTo('blogEditor')">
            <span>{{ $t('我的博客') }}</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="routerTo('blogEditor')">
            <span>{{ $t('修改记录') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
// import { createWebRecord } from '@/api/web';
const router = useRouter();
const webRecord = ref({
  time: '',
  operation: '',
  type: ''
});
const blogData = ref();
const disabled = ref(true);

function routerTo(path: string) {
  router.push({ name: path });
}
function toMyBlogs() {
  //
}
function addWebRecord() {
  //
}

onMounted(() => {
  let dropDown = document.querySelector('.float-item') as any;
  dropDown.style.right = '50px';
  dropDown.style.bottom = '50px';
});
</script>
<style lang="scss">
@include theme() {
  .float-item {
    position: fixed;
    cursor: pointer;
    .el-button.is-circle {
      padding: 6px;
      background: rgba(17, 17, 17, 0.8);
      color: get('font-color');
      i {
        font-size: 1rem;
      }
    }
    .el-button.is-circle:hover {
      // background: get('font-color');
      border: 1px solid #dcdfe6;
      background: rgba(17, 17, 17, 0.8);
      color: get('font-color');
      // color: #606266;
    }
  }
  .el-dropdown-menu {
    background: rgba(29, 32, 33, 0.6);
    box-shadow: get('box-shadow');
    border: none;
    padding: 0;
    .el-dropdown-menu__item {
      background: transparent;
      span {
        color: get('font-color');
      }
      padding: 0 12px;
    }
    .el-dropdown-menu__item:hover {
      background: rgba(54, 55, 56, 0.9);
      span {
        color: get('font-color');
      }
    }
  }
}
</style>
