import { useWebSocketStore } from '@/store/modules/webSocket.ts';
// 前端订阅管理
export default class ChannelManager {
  private webSocketStore = useWebSocketStore();

  // 订阅频道
  subscribeChannel(channel: string) {
    this.webSocketStore.sendJsonMessage({
      type: 'subscribe',
      action: 'subscribe',
      channel: channel
    });
  }

  // 取消订阅频道
  unsubscribeChannel(channel: string) {
    this.webSocketStore.sendJsonMessage({
      type: 'subscribe',
      action: 'unsubscribe',
      channel: channel
    });
  }

  // 获取可用频道
  getAvailableChannels() {
    this.webSocketStore.sendJsonMessage({
      type: 'get_available_channels'
    });
  }

  // 获取用户订阅
  getUserSubscriptions() {
    this.webSocketStore.sendJsonMessage({
      type: 'get_subscriptions'
    });
  }
}
