// loading.ts
import { createApp, App } from 'vue';
import Loading from './loadingFullscreen.vue';

// 定义 Loading 配置接口
interface LoadingOptions {
  type?: string;
  text?: string;
  background?: string;
  [key: string]: any;
}

// 定义 Loading 实例接口（根据你的 Loading 组件实际结构调整）
interface LoadingInstance {
  updateInfo: (options: LoadingOptions) => void;
  $el: HTMLElement;
}

class LoadingService {
  private instance: LoadingInstance | null = null;
  private container: HTMLElement | null = null;
  private app: App<Element> | null = null;
  private isShowing: boolean = false;

  constructor() {
    this.init();
  }

  private init(): void {
    // 创建 Loading 实例
    this.app = createApp(Loading);
    this.container = document.createElement('div');
    this.container.className = 'global-loading-container' as any;
    this.container.style.width = '100vw';
    this.container.style.height = '100vh';
    this.container.style.position = 'fixed';
    this.container.style.top = '0';
    this.container.style.left = '0';
    this.container.style.zIndex = '9999';
    this.container.style.color = '#fff';
    // 添加到 body
    document.body.appendChild(this.container);
    // 挂载实例
    this.instance = this.app.mount(this.container) as any;
    // 默认隐藏
    this.hide();
  }

  /**
   * 显示 Loading
   * @param options Loading 配置选项
   */
  public show(options: LoadingOptions = {}): void {
    if (!this.instance || !this.container) {
      console.warn('Loading instance not initialized');
      return;
    }

    this.isShowing = true;

    // 更新配置
    this.instance.updateInfo(options);

    // 显示
    this.container.style.display = 'block';

    // 防止背景滚动
    document.body.style.overflow = 'hidden';
  }

  /**
   * 隐藏 Loading
   */
  public hide(): void {
    if (!this.container) return;

    this.isShowing = false;
    this.container.style.display = 'none';

    // 恢复背景滚动
    document.body.style.overflow = '';
  }

  /**
   * 异步操作包装器 - 自动显示/隐藏 Loading
   * @param promise 异步操作
   * @param options Loading 配置选项
   * @returns Promise<T>
   */
  public async wrapAsync<T>(promise: Promise<T>, options: LoadingOptions = {}): Promise<T> {
    this.show(options);

    try {
      const result = await promise;
      return result;
    } catch (error) {
      throw error;
    } finally {
      this.hide();
    }
  }

  /**
   * 获取当前 Loading 状态
   */
  public get isLoading(): boolean {
    return this.isShowing;
  }

  /**
   * 销毁实例（在应用卸载时调用）
   */
  public destroy(): void {
    if (this.container && this.container.parentNode) {
      this.container.parentNode.removeChild(this.container);
    }
    this.instance = null;
    this.container = null;
    this.app = null;
  }
}

// 创建全局单例
export const loadingService = new LoadingService();

// 默认导出
export default loadingService;
