<template>
  <div class="page-main chat-main" ref="messageContainer">
    <el-alert
      title="只是随便接了个api进来玩玩，有很多更成熟的AI网站"
      class="alert"
      type="warning"
      show-icon
    />
    <div class="chat-messages">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message-item', message.role]"
      >
        <div class="message-header" v-if="message.role !== 'user'">
          <div class="avatar">
            <el-avatar
              :icon="message.role === 'user' ? UserFilled : Service"
              :class="message.role"
            />
          </div>
          <div class="role-name">
            {{ message.role === 'user' ? '你' : 'DeepSeek' }}
          </div>
          <div class="message-content" v-if="loading && index === messages.length - 1">
            <span class="loading-dots">思考中</span>
          </div>
        </div>
        <md-preview
          v-if="message.content"
          :editorId="'preview-only'"
          :modelValue="message.content"
        />
        <div
          class="time-count-wrap"
          v-if="message.role !== 'user' && index != 0 && message.costTime"
        >
          <div class="time-count">
            {{ `本次回答共耗时${(message.costTime / 1000).toFixed(2)}s` }}
          </div>
        </div>
      </div>

      <!--      &lt;!&ndash; 加载状态 &ndash;&gt;-->
      <!--      <div class="message-item assistant" v-if="loading">-->
      <!--        <div class="message-header">-->
      <!--          <div class="avatar">-->
      <!--            <el-avatar :icon="Service" class="assistant" />-->
      <!--          </div>-->
      <!--          <div class="role-name">DeepSeek</div>-->
      <!--        </div>-->
      <!--      </div>-->
    </div>
  </div>
  <!-- 输入区域 -->
  <div class="input-wrapper">
    <!--    <rain></rain>-->
    <div class="input-area">
      <el-input
        v-model="userInput"
        type="textarea"
        :rows="3"
        placeholder="输入消息..."
        resize="none"
        :disabled="loading"
        @keyup.enter.exact.prevent="chatWithAI"
      />
      <div class="input-actions">
        <el-button
          type="primary"
          class="send-btn"
          :loading="loading"
          :disabled="!userInput.trim()"
          @click="chatWithAI"
        >
          发送
        </el-button>
      </div>
    </div>
    <div class="setting">
      <div class="setting-item">
        <div class="setting-item-label">自动滚动</div>
        <el-switch v-model="options.scroll"></el-switch>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { Service, UserFilled } from '@element-plus/icons-vue';
import { sendMessage } from '@/api/chat.ts';
import useThemeStore from '@/store/modules/theme.ts';
import { ElMessage, ElNotification } from 'element-plus';
import rain from '@/views/assembly/slice/rain/index.vue';
import { MdPreview } from 'md-editor-v3';
const page = ref(null) as any;
const options = ref({ scroll: true }) as any;
const themeStore = useThemeStore();
themeStore.isFooterShow = false;
import { debounce } from 'lodash';

themeStore.isFooterShow = false;
const userInput = ref('');
const loading = ref(false);
const messages = ref([
  {
    role: 'assistant',
    content: '你好！我是 AI小助手，有什么我可以帮你的吗？'
  }
]) as any;

// 发送消息
async function chatWithAI() {
  const start = performance.now();
  if (!userInput.value.trim() || loading.value) return;
  const userMessage = {
    role: 'user',
    content: userInput.value
  };
  messages.value.push(userMessage);
  userInput.value = '';
  loading.value = true;
  await scrollToBottom();

  try {
    const assistantMessage = {
      role: 'assistant',
      content: '' // 初始内容为空
    };
    const response = (await sendMessage(messages.value)) as any;
    if (response.status !== 200) {
      ElNotification.error('API returned an error');
    }
    if (!response.body) return;
    //接收数据流
    const decoder = new TextDecoder('utf-8');
    const reader = response.body.getReader();
    let index = 0;
    messages.value.push(assistantMessage);
    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        break;
      }
      const chunk = decoder.decode(value);
      const lines = chunk.split('data: ').slice(1); // 分组并处理掉返回中的data:
      const parsedLines = lines
        .map(line => line.trim()) // 去空格
        .filter(line => line !== ''); // 去掉空行
      for (const parsedLine of parsedLines) {
        index++;
        //循环返回的字段
        if (parsedLine.includes('choices')) {
          // 返回数据为合理数据判断
          const { choices } = JSON.parse(parsedLine);
          const { delta } = choices[0];
          const { content } = delta;
          if (content && index > 4) {
            // 文本 处理
            messages.value[messages.value.length - 1].content += content;
            console.log('文本:', content, options.value.scroll);
            if (options.value.scroll) {
              scrollToBottom();
            }
          }
        }
      }
    }
    if (!messages.value[messages.value.length - 1].content) {
      messages.value[messages.value.length - 1].content = 'api请求失败,请联系管理员';
    }
    loading.value = false;
    const end = performance.now();
    messages.value[messages.value.length - 1].costTime = end - start;
    // console.log(`函数执行时间为: ${timeDiff} 毫秒`);
  } catch (error) {
    ElMessage.error('消息发送失败，请重试');
    loading.value = false;
  }
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick();
  page.value.scrollTo({ top: page.value.scrollHeight, behavior: 'smooth' });
};

onMounted(() => {
  page.value = document.querySelector('.el-main') as any;
  scrollToBottom();
});
</script>

<style lang="scss" scoped>
@include theme() {
  .chat-main {
    max-width: 1000px !important;
    height: auto;
    display: flex;
    flex-direction: column;
    overflow: auto;
    background: get('back') !important;
  }
  .md-editor-previewOnly {
    border: 1px solid get('border-color');
    //box-shadow: get('box-shadow');
    padding: 0.5rem;
    border-radius: 6px;
    text-align: left;
  }
  .user {
    display: flex;
    justify-content: end;
    :deep(.md-editor-preview-wrapper) {
      padding: 0 20px !important;
    }
    .md-editor-previewOnly {
      text-align: right;
      background: #eff6ff;
      width: fit-content;
      border-radius: 15px;
      border: none;

      .md-editor-preview-wrapper {
        width: fit-content;
        padding: 0 20px !important;
      }
    }
  }
  .input-wrapper {
    //border-top: 1px solid #e5e7eb;
    box-shadow: get('box-shadow');
    background-color: #ffffff;
    position: fixed;
    bottom: 0;
    z-index: 10;
    width: 100vw;
    display: flex;
    justify-content: center;
    //.rain {
    //  position: absolute;
    //}
    .input-area {
      //box-shadow: inset 1px 1px 3px rgba(255, 255, 255, 0.3);
      overflow: hidden;
    }
  }
}
.alert {
  margin: 48px 48px 0px 48px;
  width: calc(100% - 96px);
}
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px 0;
  margin-bottom: 130px;
  .message-item {
    padding: 24px 48px;

    &.assistant {
      //background-color: #f9fafb;
    }

    .message-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 12px;
      position: relative;
      .avatar {
        .el-avatar {
          width: 24px;
          height: 24px;
          border-radius: 4px;

          &.user {
            background-color: #2970ff;
          }

          &.assistant {
            background-color: #10b981;
          }
        }
      }

      .role-name {
        font-size: 14px;
        font-weight: 500;
        color: #1f2937;
      }
    }

    .message-content {
      color: #1f2937;
      line-height: 1.6;
      font-size: 15px;
      position: absolute;
      left: calc(50% - 25px);
      :deep(p) {
        margin: 0.8em 0;

        &:first-child {
          margin-top: 0;
        }

        &:last-child {
          margin-bottom: 0;
        }
      }

      :deep(pre) {
        background-color: #f3f4f6;
        padding: 16px;
        border-radius: 8px;
        margin: 1em 0;
        position: relative;

        code {
          background: none;
          padding: 0;
          border-radius: 0;
          font-family: 'Fira Code', monospace;
        }

        &:hover {
          .copy-btn {
            opacity: 1;
          }
        }
      }

      :deep(code) {
        background-color: #f3f4f6;
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 0.9em;
        font-family: 'Fira Code', monospace;
      }
    }
  }
}

.input-area {
  //max-width: 768px;
  margin: 24px 0;
  display: flex;
  justify-content: center;
  position: relative;
  width: 920px;
  border-radius: 12px;
  :deep(.el-textarea__inner) {
    padding: 12px 100px 12px 16px;
    font-size: 15px;
    line-height: 1.6;
    border-radius: 12px;
    //border-color: #e5e7eb;
    resize: none;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    background: #f3f4f6 !important;
    //color: #fff !important;
    &:focus {
      border-color: #2970ff;
      box-shadow: 0 0 0 3px rgba(41, 112, 255, 0.1);
    }

    &:disabled {
      background-color: #f9fafb;
      cursor: not-allowed;
    }
  }

  .input-actions {
    position: absolute;
    right: 12px;
    bottom: 12px;

    .send-btn {
      height: 32px;
      padding: 0 16px;
      background-color: #2970ff;
      border: none;

      &:hover:not(:disabled) {
        background-color: #1b62ff;
      }

      &:disabled {
        background-color: #e5e7eb;
        cursor: not-allowed;
      }
    }
  }
}

.loading-dots {
  display: inline-block;
  color: #6b7280;

  &::after {
    content: '';
    animation: dots 1.4s infinite;
  }
}

@keyframes dots {
  0%,
  20% {
    content: '.';
  }
  40% {
    content: '..';
  }
  60%,
  100% {
    content: '...';
  }
}

// 自定义滚动条
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background-color: transparent;
}

::-webkit-scrollbar-thumb {
  background-color: #d1d5db;
  border-radius: 4px;

  &:hover {
    background-color: #9ca3af;
  }
}

// 代码块复制按钮
.copy-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  opacity: 0;
  transition: opacity 0.2s;
  padding: 4px 8px;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  font-size: 12px;
  color: #6b7280;
  cursor: pointer;

  &:hover {
    background-color: #f9fafb;
    color: #1f2937;
  }
}
.time-count-wrap {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: end;
  margin-top: 10px;
}
.time-count {
  border-radius: 5px;
  background: #827c83;
  width: fit-content;
  font-size: 0.7rem;
  padding: 5px 10px;
  color: white;
}
.setting {
  width: 120px;
  position: absolute;
  right: 20px;
  font-size: 0.8rem;
  bottom: 15px;
  .setting-item {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>
