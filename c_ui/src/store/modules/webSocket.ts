import { defineStore } from 'pinia';
import { autoClearTimer } from '@/utils/timer.ts';
import useUserStore from '@/store/modules/user.ts';

// 定义类型
interface WebSocketFunctions {
  onopen?: (event: Event) => void;
  onmessage?: (event: MessageEvent) => void;
  onclose?: (event: CloseEvent) => void;
  onerror?: (event: Event) => void;
  onsend?: (event: Event) => void;
}

interface WebSocketState {
  socket: WebSocket | null;
  isClose: boolean;
  reconnectNum: number;
  functions?: WebSocketFunctions;
}

// 默认的 WebSocket 函数
const defaultFunctions: WebSocketFunctions = {
  onopen: null as any,
  onmessage: null as any,
  onerror: null as any,
  onclose: null as any,
  onsend: null as any
};

export const useWebSocketStore = defineStore('websocket', {
  state: (): WebSocketState => ({
    socket: null,
    isClose: false,
    reconnectNum: 0,
    functions: undefined
  }),

  actions: {
    // 连接 webSocket
    connectWebSocket(functions?: WebSocketFunctions): void {
      if (this.socket) {
        console.log('socket连接已建立，先关闭再重连');
        this.isClose = true;
        this.socket.close();
      }

      // 合并默认函数和传入的函数
      const mergedFunctions: WebSocketFunctions = {
        ...defaultFunctions,
        ...functions
      };

      this.functions = mergedFunctions;

      autoClearTimer(() => {
        const userStore = useUserStore();
        // 使用新的 WebSocket 路径
        const url = 'ws://120.48.127.181:7000/ws?userId=' + userStore.userId;
        console.log('尝试连接到:', url);

        try {
          this.socket = new WebSocket(url);
          console.log('初始化socket连接，状态:', this.getConnectionState());

          // 连接成功
          this.socket.onopen = (event: Event) => {
            console.log('✅ socket连接成功');
            this.isClose = false;
            this.reconnectNum = 0;

            // 连接成功后立即发送订阅消息
            if (mergedFunctions.onsend) {
              console.log('发送初始订阅消息');
              this.sendJsonMessage({ type: 'subscribe', data: 'AAA' });
              mergedFunctions.onsend(event);
            }

            if (mergedFunctions.onopen) {
              mergedFunctions.onopen(event);
            }
          };

          // 收到消息
          this.socket.onmessage = (event: MessageEvent) => {
            // console.log('📨 socket接收信息:', event.data);

            if (mergedFunctions.onmessage) {
              mergedFunctions.onmessage(event);
            }
          };

          // 连接关闭
          this.socket.onclose = (event: CloseEvent) => {
            console.log('❌ socket连接关闭, 代码:', event.code, '原因:', event.reason);
            console.log('当前状态:', this.getConnectionState());

            if (!this.isClose) {
              console.log('非手动关闭,开始重连');
              this.reconnectNum++;
              this.reconnect(this.reconnectNum);
            }

            if (mergedFunctions.onclose) {
              mergedFunctions.onclose(event);
            }
          };

          // 连接错误
          this.socket.onerror = (event: Event) => {
            console.log('💥 socket连接错误', event);
            console.log('当前状态:', this.getConnectionState());
            this.reconnectNum++;
            this.reconnect(this.reconnectNum);

            if (mergedFunctions.onerror) {
              mergedFunctions.onerror(event);
            }
          };
        } catch (error) {
          console.error('🚨 WebSocket 创建失败:', error);
          this.reconnectNum++;
          this.reconnect(this.reconnectNum);
        }
      }, 1000);
    },

    // 关闭 websocket
    closeWebSocket(): void {
      if (this.socket) {
        this.isClose = true;
        this.socket.close();
        this.socket = null;
        this.functions = undefined;
        console.log('关闭socket连接');
      }
    },

    // 发送消息
    sendMessage(data: string | ArrayBuffer | Blob | ArrayBufferView): boolean {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(data);
        return true;
      } else {
        console.warn('WebSocket 未连接，无法发送消息');
        return false;
      }
    },

    // 发送 JSON 消息
    sendJsonMessage(data: any): boolean {
      try {
        const jsonString = JSON.stringify(data);
        return this.sendMessage(jsonString);
      } catch (error) {
        console.error('JSON 序列化失败:', error);
        return false;
      }
    },

    // 获取连接状态
    getConnectionState(): string {
      if (!this.socket) return 'CLOSED';

      switch (this.socket.readyState) {
        case WebSocket.CONNECTING:
          return 'CONNECTING';
        case WebSocket.OPEN:
          return 'OPEN';
        case WebSocket.CLOSING:
          return 'CLOSING';
        case WebSocket.CLOSED:
          return 'CLOSED';
        default:
          return 'UNKNOWN';
      }
    },

    // 重连方法
    async reconnect(num: number): Promise<void> {
      if (num >= 4 && num <= 6) {
        // 这里可以添加 token 验证逻辑
        // const { code } = await verifyToken();
        // if (code === 401) {
        //   // 处理 token 过期逻辑
        //   console.log('token 过期，需要重新登录');
        // }
      } else if (num >= 6) {
        console.log('socket连接错误超过5次,终止重连');
        return;
      } else {
        console.log(`进行第${num}次重连`);
        // 使用保存的函数进行重连
        this.connectWebSocket(this.functions);
      }
    }
  },

  getters: {
    // 判断是否已连接
    isConnected: (state): boolean => {
      return state.socket?.readyState === WebSocket.OPEN;
    },

    // 获取重连次数
    getReconnectNum: (state): number => {
      return state.reconnectNum;
    },

    // 判断是否手动关闭
    isManuallyClosed: (state): boolean => {
      return state.isClose;
    }
  }
});

export default useWebSocketStore;
