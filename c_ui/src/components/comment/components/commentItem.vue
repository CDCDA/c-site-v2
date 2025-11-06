<!--
 * @Description: 单块评论
-->
<template>
  <div class="comment-item">
    <div class="ct-left">
      <c-image class="ct-avatar" :src="commentData.avatar"></c-image>
    </div>
    <div class="ct-right">
      <div class="ct-header">
        <div class="ct-base-info">
          <span class="ct-commentator">{{ props.commentData.nickName }}</span>
          <span class="ct-isBlogger" v-if="props.data?.userId == props.commentData.userId">{{
            $t('博主')
          }}</span>
          <span class="ct-time">{{ props.commentData.createTime }}</span>
        </div>
        <div class="ct-count-info">
          <!-- <svg-icon iconName="commonSvg-点赞"></svg-icon> -->
          <!-- <svg-icon iconName="commonSvg-评论" @click="showEditor"></svg-icon> -->
          <el-tag class="reply" @click="showEditor"
            >{{
              `${
                props.commentData.children
                  ? props.commentData.children.length != 0
                    ? props.commentData.children.length
                    : ''
                  : ''
              } `
            }}回复</el-tag
          >
        </div>
      </div>
      <div class="ct-main" v-html="props.commentData.comment"></div>
      <div class="ct-footer">
        <el-tag>{{ props.commentData.os }}</el-tag>
        <el-tag>{{ props.commentData.browser }}</el-tag>
      </div>
      <commentEditor
        v-if="isEditorShow"
        :type="props.commentData.type"
        :relevanceId="props.commentData.relevanceId"
        :parentId="props.commentData.id"
        @getList="getList"
        @closeEditor="showEditor"
      />
      <div v-if="props.commentData.children && props.commentData.children.length != 0">
        <div class="c-divider" style="margin: 15px 0 0 0; width: calc(100% - 50px)"></div>
        <commentItem
          v-for="item in props.commentData.children"
          :commentData="item"
          :parent-id="props.commentData.parentId"
          @getList="getList"
          :data="props.data"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import commentItem from '@/components/comment/components/commentItem.vue';
import commentEditor from './commentEditor.vue';
const emit = defineEmits(['getList']);
const props = defineProps({
  commentData: {
    default: {
      id: '',
      comment: '', //评论内容
      type: '0', //评论分类
      os: '', //操作系统
      browser: '', //浏览器
      nickName: '', //昵称
      email: '', //邮箱
      webUrl: '', //博客网站地址
      avatar: '', //头像
      parentId: '', //父级评论id
      relevanceId: '', //关联id
      createTime: '2024-10-1',
      userId: null,
      children: [] as any
    }
  },
  data: {
    default: {
      userId: null
    }
  }
});

const isEditorShow = ref(false) as any;

function getList() {
  console.log($t('刷新'));
  emit('getList');
}

function showEditor() {
  isEditorShow.value = !isEditorShow.value;
}

onMounted(() => {});
</script>
<style lang="scss" scoped>
@include theme() {
  .comment-item {
    display: flex;
    align-items: start;
    padding: 20px 20px 10px 20px;
    width: calc(100% - 40px);
    margin: 5px 0 0 0;
    .comment-item {
      margin-top: 15px;
    }
    // border-radius: 8px;
    background: get('back');
    .svg-icon {
      cursor: pointer;
    }
    .comment-editor {
      border: 1px solid #ccc;
      margin: 15px 0;
    }
    .comment-item {
      padding-left: 0;
      padding-top: 0;
      padding-bottom: 0;
      width: 100%;
    }
    .ct-left {
      width: 50px;
      height: 100%;
      .ct-avatar {
        width: 45px;
        height: 45px;
        border-radius: 6px;
      }
    }
    .ct-right {
      width: calc(100% - 70px);
      padding-left: 20px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .ct-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        .ct-base-info {
          display: flex;
          justify-content: start;
          align-items: center;
          .ct-commentator {
            font-weight: bold;
            font-size: 0.8rem;
            color: #ef794f;
            margin-right: 10px;
          }
          .ct-isBlogger {
            border-radius: 5px;
            background: #ff0000;
            color: white;
            padding: 3px 5px;
            font-size: 0.8rem;

            margin-right: 8px;
          }
          .ct-time {
            font-size: 0.8rem;
            opacity: 0.7;
          }
        }
        .ct-count-info {
          display: flex;
          justify-content: end;
          align-items: center;
          .reply {
            background: orange !important;
            margin: 0 !important;
            padding: 0.2rem 0.3rem !important;
            border: none !important;
            font-size: 0.75rem;
            :deep(.el-tag__content) {
              color: white !important;
            }
            cursor: pointer;
          }
          .svg-icon {
            width: 20px;
            height: 20px;
            margin-left: 5px;
            :deep(.theme-icon) {
              fill: white;
            }
          }
        }
      }
      .ct-main {
        border-radius: 6px;
        min-height: 100px;
        margin-bottom: 15px;
        background: #e1eaf988;
        padding: 10px;
        white-space: pre-wrap;
        word-wrap: break-word;
        text-align: left;
      }
      .ct-footer {
        display: flex;
        justify-content: start;
        align-items: center;
      }
    }
  }
}
</style>
