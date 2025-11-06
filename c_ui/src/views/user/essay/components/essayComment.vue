<!--
 * @Description: 随笔评论
-->
<template>
  <c-dialog
    v-model="props.visible"
    :title="title"
    width="60%"
    dialogClass="dialog-normal"
    class="essay-comment-dialog"
    :modal="true"
    @close="close"
    style="height: 80%; margin-top: 5%"
  >
    <div class="essay-item">
      <div class="essay-item-user">
        <c-image class="essay-item-user-avatar" :src="props.userData.avatar"></c-image>
        <div class="essay-item-info">
          <span class="essay-item-user-nickName">{{ props.userData.nickName }}</span>
          <span class="essay-item-date">
            {{ sformatDate(new Date(props.essayData.createTime)) }}
          </span>
        </div>
      </div>
      <div class="c-divider" style="margin: 20px 1px"></div>
      <div class="essay-item-content">{{ props.essayData.content }}</div>
      <div class="img-list" v-if="props.essayData.images && props.essayData.images.length > 0">
        <c-image class="img-list-item" v-for="img in props.essayData.images" :src="img" />
      </div>
      <div class="essay-item-tag-list">
        <span class="essay-item-tag" v-for="tag in props.essayData.tags">{{
          `# ${tag.tagName}`
        }}</span>
      </div>
      <div class="c-divider" style="margin: 20px 1px"></div>
    </div>
    <comment :type="1" :relevanceId="props.essayData.i" :data="props.essayData" />
  </c-dialog>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import useUserStore from '@/store/modules/user';
import { sformatDate } from '@/utils/date.ts';
import comment from '@/components/comment/index.vue';
const router = useRouter();
const userStore = useUserStore();
const open = ref(false);
const title = ref($t('评论'));
const props = defineProps(['essayData', 'userData', 'visible']);
const emit = defineEmits(['close']);
function close() {
  emit('close');
}

onMounted(() => {});
</script>
<style lang="scss">
.essay-comment-dialog {
  .el-dialog__body {
    padding-top: 0;
    overflow-y: auto !important;
  }
}
@include theme() {
  .essay-item {
    transition: all 0.2s ease-in-out;
    border-radius: 6px;
    position: relative;
    padding: 8px 20px 10px 20px;
    // padding-top: 11px;
    text-align: left;
    // box-shadow: get('box-shadow');
    background: get('back');
    color: get('font-color');
    .essay-item-user {
      display: flex;
      margin-bottom: 12px;
      .essay-item-user-avatar {
        width: 47px;
        height: 45px;
        border-radius: 6px;
      }
      .essay-item-info {
        width: calc(100% - 50px);
        padding: 3px 10px;
        @include flex-column;
        justify-content: space-between;
        .essay-item-user-nickName,
        .essay-item-date {
          text-align: left;
          width: 100%;
        }
        .essay-item-user-nickName {
          font-size: 16px;
          font-weight: 400;
          color: #6dbdc3;
        }
        .essay-item-date {
          font-size: 12px;
          color: #969394;
        }
      }
    }
    .essay-item-content {
      white-space: pre-wrap;
      margin-bottom: 12px;
    }
    .img-list {
      overflow-x: auto;
      width: 100%;
      @include flex;
      min-height: 40px;
      flex-wrap: wrap;
      justify-content: start;
      margin-bottom: 12px;
      .img-list-item {
        width: calc(25% - 10px);
        aspect-ratio: 3 / 2;
        margin: 5px 10px 5px 0;
        border-radius: 5px;
      }
    }
    .essay-item-tag-list {
      width: calc(100% - 40px);
      display: flex;
      flex-wrap: nowrap;
      .essay-item-tag {
        margin-right: 10px;
        font-size: 14px;
        padding: 2px 5px;
        color: #999;
        background: #f2f2f2;
        border-radius: 4px;
      }
    }
    .svg-icon {
      width: 20px;
      height: 20px;
      cursor: pointer;
    }
  }
}
</style>
