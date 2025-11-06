<template>
  <div class="message">
    <el-popover
      placement="bottom"
      append-to=".popper-base"
      :width="330"
      trigger="click"
      @show="getData"
    >
      <template #reference>
        <ElBadge :value="msgCount" :hidden="msgCount === 0" class="item">
          <i class="svg-icon-wrap" style="margin: 0px 1px">
            <svg-icon
              iconName="commonSvg-铃铛"
              style="font-size: 1.2rem; margin: 0 6px 5px 6px; cursor: pointer"
              class="header-icon bell"
            />
          </i>
        </ElBadge>
      </template>
      <div class="message-tools">
        <el-button type="text" size="mini" @click="handleReadAll">
          {{ $t('全部标记已读') }}
        </el-button>
      </div>
      <div class="message-list" v-if="msgList.length > 0">
        <div class="message-item" v-for="item in msgList" :key="item.id" @click="handleView(item)">
          <div class="message-content">
            <div class="message-content-top">
              <span class="message-title no-wrap">{{ item.title }}</span>
              <span class="message-time">{{
                formatDate(new Date(item.createTime), 'MM-dd hh-mm')
              }}</span>
            </div>
            <div class="message-content-bottom">
              <span class="message-detail no-wrap">{{ item.content }}</span>
              <span class="message-read">{{ item.isRead === '0' ? $t('未读') : $t('已读') }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="message-empty">
        <!-- <img src="@/assets/images/notData.png" alt="notData" /> -->
        <div>{{ $t('暂无消息') }}</div>
      </div>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { onMounted, ref, nextTick } from 'vue';
import { Bell } from '@element-plus/icons-vue';
import mittBus from '@/utils/mittBus';
import { formatDate } from '@/utils/date';
import { ElNotification, ElMessageBox } from 'element-plus';
import { listUserMessages, batchReadMessages } from '@/api/system/message';
import { useRouter } from 'vue-router';
import { useWebSocketStore } from '@/store/modules/webSocket';
import ChannelManager from '@/utils/channelManage';
const currentMessage = ref({}) as any;
const router = useRouter();
const activeName = ref('msg');
const allCount = ref(0);
const msgCount = ref(0);
const todoCount = ref(0);

const msgList = ref<any[]>([]);
const todoList = ref<any[]>([]);

const messageIds = ref([]) as any;

const handleRead = async () => {
  const { code } = await batchReadMessages(messageIds.value);
  if (code === 200) {
    getMsgList();
  }
};

const handleReadAll = () => {
  messageIds.value = msgList.value.map((item: any) => item.id);
  handleRead();
};

const getMsgList = async () => {
  const { code, rows } = await listUserMessages();
  if (code === 200) {
    msgList.value = rows;
    msgCount.value = rows.filter((item: any) => item.isRead === '0').length;
  }
};

const getTodoList = () => {
  // getTodoMessageListApi().then(res => {
  //   todoList.value = res.data;
  // });
};

const handleView = (item: any) => {
  if (item.isRead === '0') {
    messageIds.value = [item.id];
    handleRead();
  }
  ElMessageBox.confirm(item.content, item.title, {
    confirmButtonText: $t('确定'),
    cancelButtonText: $t('取消')
  });
};

const getData = () => {
  if (activeName.value === 'msg') {
    getMsgList();
  } else {
    getTodoList();
  }
};

function initWebSocketSubscriptions() {
  const webSocketStore = useWebSocketStore();
  const channelManager = new ChannelManager();

  webSocketStore.connectWebSocket({
    onopen: () => {
      // 订阅磁盘信息和系统通知
      channelManager.subscribeChannel('disk_info');
      channelManager.subscribeChannel('todo_list');
      channelManager.subscribeChannel('system_notice');

      // 所有用户都订阅用户消息
      channelManager.subscribeChannel('user_message');
    },

    onmessage: (event: any) => {
      let data = null;
      try {
        data = JSON.parse(event.data);
      } catch (error) {
        console.log('解析消息失败:', error);
        data = event.data;
      }
      switch (data.type) {
        case 'channel_message':
          handleChannelMessage(data.channel, data.data);
          break;
        case 'subscribed':
          console.log('订阅成功:', data.channel);
          break;
        case 'user_subscriptions':
          console.log('当前订阅:', data.channels);
          break;
      }
    }
  });
}

// 处理不同频道的消息
function handleChannelMessage(channel: string, data: any) {
  switch (channel) {
    case 'disk_info':
      console.log('📊 磁盘信息更新:', data);
      showMessage({ ...data, channel, title: `📊 ${$t('磁盘信息')}` });
      break;
    case 'todo_list':
      console.log('📝 待办事项通知:', data);
      showMessage({ ...data, channel, title: `📝 ${$t('待办事项')}` });
      break;
    case 'system_notice':
      console.log('🔔 系统通知:', data);
      showMessage({ ...data, channel, title: `🔔 ${$t('系统通知')}` });
      break;
    case 'user_message':
      console.log('💬 用户消息:', data);
      showMessage({ ...data, channel, title: `💬 ${$t('用户消息')}` });
      break;
  }
}

function showMessage(message: any) {
  ElNotification({
    title: message.title || '',
    message: message.content || '',
    duration: 15 * 1000
  });
}

onMounted(() => {
  getMsgList();
  initWebSocketSubscriptions();
});
</script>

<style lang="scss" scoped>
.svg-icon-wrap::before {
  top: -3px;
}
:deep(.el-badge__content.is-fixed) {
  right: 1rem;
}
.message-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  line-height: 45px;
}

.message-list {
  display: flex;
  flex-direction: column;
  max-height: 400px;
  overflow-y: auto;

  .message-item {
    display: flex;
    align-items: center;
    padding: 5px 0;
    border-bottom: 1px solid var(--el-border-color-light);
    cursor: pointer;

    &:last-child {
      border: none;
    }

    .message-content {
      width: 100%;
      display: flex;
      overflow: hidden;
      padding: 2px 6px;
      flex-direction: column;
      .message-content-top {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 5px;
      }
      .message-content-bottom {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@include theme() {
  .message-read {
    width: 1.3rem;
    font-size: 0.7rem !important;
    color: get('placeholder') !important;
  }
  .message-detail {
    flex: 1;
    font-size: 0.7rem;
    color: get('font-color');
  }
  .message-title {
    width: calc(100% - 6rem);
    font-weight: bold;
    font-size: 0.75rem;
  }

  .message-time {
    font-size: 0.7rem;
    width: 4.5rem;
  }
  .message-tools {
    display: flex;
    justify-content: end;
  }
}
</style>
