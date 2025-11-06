<!--
 * @Description:  分类卡
-->
<template>
  <div class="blog-type-card c-card">
    <div class="card-header">
      <el-icon><FolderOpened /></el-icon><span class="type-nmae">{{ $t('分类') }}</span>
    </div>
    <div class="type-list">
      <div class="type-item" v-for="item in typeList" @click="toBlogType(item)">
        <span class="type-item-prepend">#</span>{{ item.typeName
        }}<span class="type-item-suffix">{{ item.total }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref } from 'vue';
import { listTypesWithStats } from '@/api/type';
import useUserStore from '@/store/modules/user';
import { useLazyAppear } from '@/utils/lazy';
import { useRouter } from 'vue-router';
const typeList = ref([]) as any;
const router = useRouter();
function toBlogType(item: any) {
  router.push({ name: 'blogTypePage', query: { typeId: item.typeId } });
}

async function getTypeTree() {
  const { code, data } = (await listTypesWithStats({})) as any;
  if (code === 200) {
    typeList.value = data.list;
  }
}

onMounted(() => {
  getTypeTree();
  const element = document.querySelector('.blog-type-card') as any;
  if (element) {
    useLazyAppear(element) as any;
  }
});
</script>
<style lang="scss" scoped>
@include theme() {
  .blog-type-card {
    text-align: left;
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
    .type-list {
      @include flex;
      justify-content: start;
      flex-wrap: wrap;
      .type-item {
        padding: 7px 12px;
        border-radius: 8px;
        color: get('font-color');
        border: 1px solid #b9b8b8;
        margin: 8px;
        font-size: 0.9rem;
        cursor: pointer;
      }
      .type-item-prepend {
        margin-right: 6px;
        opacity: 0.8;
      }
      .type-item-suffix {
        margin-left: 6px;
        opacity: 0.8;
        background: #d1d1d1;
        border-radius: 5px;
        padding: 4px 5px;
      }
      .type-item:hover {
        background: get('bk');
        color: get('re-font-color');
      }
    }
  }
}
</style>
