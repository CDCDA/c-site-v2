<!--
 * @Description: 评论发送模块
-->
<template>
  <div class="comment-editor">
    <el-form class="comment-editor-header" :inline="true">
      <c-image class="comment-editor-avatar" :src="commentData.avatar"></c-image>
      <el-form-item label="昵称">
        <el-input v-model="commentData.nickName"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="commentData.email"></el-input>
      </el-form-item>
      <el-form-item label="网址">
        <el-input v-model="commentData.webUrl"></el-input>
      </el-form-item>
    </el-form>
    <editor ref="editorRef" v-model="commentData.comment" />
    <div class="comment-editor-footer">
      <div class="footer-left flex-start">
        <el-popover
          placement="right"
          width="400"
          trigger="hover"
          @before-enter="beforeEnter"
          @before-leave="beforeLeave"
        >
          <template #default>
            <div class="expression-wrap">
              <div class="expression-main">
                <div class="expression-list">
                  <div class="expression" v-for="e in selectTab.list" @click="insertExpression(e)">
                    <div v-if="e.type == 'text'" class="expression-text">{{ e.text }}</div>
                    <c-image v-else :src="e.src" class="expression-img"></c-image>
                  </div>
                </div>
              </div>
              <div class="expression-tabs">
                <div
                  class="tab"
                  v-for="item in expressionTabs"
                  :class="selectTab.value == item.value ? 'active' : ''"
                  @click="tabClick(item)"
                >
                  {{ item.label }}
                </div>
              </div>
            </div>
          </template>
          <template #reference>
            <svg-icon
              id="expression-icon"
              :class="rotate ? 'rotate' : ''"
              :iconName="`commonSvg-${expressionIcon}`"
              slot="reference"
            />
          </template>
        </el-popover>
      </div>
      <div class="footer-right flex-end">
        <el-button class="sent" @click="submit">发送</el-button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import editor from '@/components/editor/index.vue';
import { getOSAndBrowser } from '@/utils/common.ts';
import { saveComment } from '@/api/comment.ts';
import { ElNotification } from 'element-plus';
import { expressionTabs } from '../expression/data.js';
import useUserStore from '@/store/modules/user';
const editorRef = ref(null) as any;
const userStore = useUserStore();
const emit = defineEmits(['getList', 'closeEditor']);
const props = defineProps(['relevanceId', 'type', 'parentId']);
const oSAndBrowser = getOSAndBrowser();
const selectTab = ref({}) as any;
const commentData = ref({
  comment: '', //评论内容
  type: props.type, //评论分类
  os: oSAndBrowser.os, //操作系统
  browser: oSAndBrowser.browser, //浏览器
  nickName: '', //昵称
  email: '', //邮箱
  webUrl: '', //博客网站地址
  avatar: '', //头像
  relevanceId: props.relevanceId, //关联id
  userId: userStore.userId,
  parentId: props.parentId
});
const expressionIcon = ref('表情-魔鬼怒');
const rotate = ref(false);
function beforeEnter() {
  rotate.value = true;
  setTimeout(() => {
    rotate.value = false;
    expressionIcon.value = '表情-魔鬼笑';
  }, 200);
}

function beforeLeave() {
  rotate.value = true;
  setTimeout(() => {
    rotate.value = false;
    expressionIcon.value = '表情-魔鬼怒';
  }, 200);
}

function tabClick(item: any) {
  selectTab.value = item;
}

function insertExpression(item: any) {
  if (item.type) {
    editorRef.value.insertText(item.text);
  } else {
    editorRef.value.insertNode({
      type: 'image',
      src: item.src,
      style: {
        width: '35px',
        height: '35px'
      },
      children: [{ text: '' }]
    });
  }
}

async function submit() {
  commentData.value.relevanceId = props.relevanceId;
  commentData.value.type = props.type;
  commentData.value.parentId = props.parentId;
  const { code, data } = (await saveComment(commentData.value)) as any;
  if (code === 200) {
    ElNotification.success('评论发布成功');
    commentData.value.comment = '';
    emit('getList');
    emit('closeEditor');
  }
}
onMounted(() => {
  commentData.value.avatar = userStore.avatar;
  commentData.value.nickName = userStore.nickName;
  commentData.value.email = userStore.email;
  selectTab.value = expressionTabs[0];
  // initListener();
});
</script>
<style lang="scss">
.expression-wrap {
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  .expression-main {
    height: calc(100% - 40px);
    overflow: auto;
    border: 1px solid #ccc;
    border-radius: 3px;
    .expression-list {
      display: flex;
      flex-wrap: wrap;
      justify-content: start;
    }
    .expression {
      height: fit-content;
      width: fit-content;
    }
    .expression-img {
      width: 36px;
      height: 36px;
      padding: 3px 7px;
      margin: 5px;
      cursor: pointer;
    }
    .expression-text {
      padding: 4px 8px;
      margin: 5px;
      cursor: pointer;
    }
    .expression-img:hover,
    .expression-text:hover {
      border-radius: 6px;
      background: #f1f1f2;
    }
  }
  .expression-tabs {
    display: flex;
    flex-wrap: wrap;
    justify-content: start;
    .tab {
      width: fit-content;
      margin: 1px 5px;
      padding: 3px 10px;
      border-radius: 4px;
    }
    .tab.active,
    .tab:hover {
      background: get('border-color');
      color: white;
    }
  }
}
</style>
<style lang="scss" scoped>
@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.rotate {
  animation: rotate 0.2s ease;
}
@include theme() {
  .comment-editor {
    height: 288px;
    background: get('back');
    border-radius: 8px;
    padding: 20px;

    :deep(.c-editor) {
      height: 160px !important;
    }
    .sent {
      background: rgb(131, 123, 199) !important;
      color: white !important;
      border: none !important;
    }
    :deep(.w-e-scroll) {
      * {
        text-align: left;
      }
    }
    .comment-editor-avatar {
      width: 40px;
      height: 40px;
      border-radius: 20px;
      margin-right: 10px;
      margin-left: 4px;
      display: flex;
      align-items: center;
    }
    .comment-editor-header {
      display: flex;
      justify-content: start;
      margin-bottom: 3px;
      padding-bottom: 10px;
      align-items: center;
      :deep(.el-form-item) {
        margin-right: 0;
        margin-left: 10px;
        margin-bottom: 0 !important;
        width: calc(33.33% - 18px);
      }
    }

    .comment-editor-footer {
      margin-top: 12px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 5px;
      .footer-left {
        position: relative;
        .svg-icon {
          width: 25px;
          height: 25px;
          cursor: pointer;
        }
        .expression-pack-wrap:hover {
          .expression-pack {
            opacity: 1;
            display: block;
          }
        }
        .expression-pack {
          display: none;
          width: 100px;
          height: 100px;
          box-shadow: get('box-shadow');
          position: absolute;
          left: 30px;
          bottom: 5px;
          opacity: 0;
        }
      }
      button {
        height: 28px;
      }
    }
  }
}
</style>
