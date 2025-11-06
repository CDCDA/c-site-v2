import i18n from '@/locales/i18n';
import { defineAsyncComponent } from 'vue';

// 动态加载脚本
export function loadScript(url: any, callBack?: any) {
  const script = document.createElement('script');
  script.src = url;
  script.type = 'text/javascript';
  document.body.appendChild(script);
  script.addEventListener('load', function () {
    callBack ? callBack() : '';
  });
}

// 动态删除脚本
export function deleteScripts() {
  var scr = document.getElementsByTagName('script');
}

// 生成懒加载组件
export function getSyncComponent(options: Object) {
  const { loader, loading, error, delay, timeout, suspensible, retryTimes } = options as any;
  return defineAsyncComponent({
    // 工厂函数
    loader: loader || null,
    // 加载过程中显示的组件Loading，用来占位的，用普通组件的方式导入即可
    loadingComponent: loading,
    // 加载失败时显示的组件Error
    errorComponent: error,
    // 在显示loadingComponent组件之前的延迟，等待多长时间 ｜ 默认值200ms
    delay: delay || 2000,
    // 如果提供了timerout，并且加载组件时超过了设定值，将显示错误组件，默认值 infinity == 永不超时 ms
    timeout: timeout || 3000,
    // 定义组件是否可挂起 默认true
    suspensible: suspensible || true,
    /**
     * err : 错误信息
     * retry : 函数，调用retry尝试重新加载
     * fail : 函数，指示加载程序结束退出
     * attempts : 记录尝试的次数
     */
    onError(err, retry, fail, attempts) {
      if (err.message.match(/fetch/) && attempts <= (retryTimes || 3)) {
        // 请求发生错误时重试，最多尝试3次
        retry();
      } else {
        // 像promise的resove|reject一样，必须调用一个，否则就卡在这里了
        fail();
      }
    }
  });
}

// 复制
export function copyText(text: String) {
  const input = document.createElement('input') as any;
  input.value = text;
  document.body.appendChild(input);
  input.select();
  document.execCommand('copy');
  document.body.removeChild(input);
}

//随机id
export function generateRandomId() {
  return Math.random().toString(36).substr(2, 9);
}

// 获取操作系统和浏览器信息
export function getOSAndBrowser() {
  const userAgent = navigator.userAgent;
  let os = '';
  let osVersion = '';
  let browser = '';
  let browserVersion = '';

  // 获取操作系统及版本
  if (/Windows NT/.test(userAgent)) {
    os = 'Windows';
    const versionMatch = userAgent.match(/Windows NT (\d+\.\d+)/);
    if (versionMatch) {
      osVersion = versionMatch[1];
    }
  } else if (/Mac OS/.test(userAgent)) {
    os = 'Mac OS';
    const versionMatch = userAgent.match(/Mac OS X (\d+\.\d+)/);
    if (versionMatch) {
      osVersion = versionMatch[1];
    }
  } else if (/Linux/.test(userAgent)) {
    os = 'Linux';
  }

  // 获取浏览器及版本
  if (/Chrome/.test(userAgent)) {
    browser = 'Chrome';
    const versionMatch = userAgent.match(/Chrome\/(\d+\.\d+)/);
    if (versionMatch) {
      browserVersion = versionMatch[1];
    }
  } else if (/Firefox/.test(userAgent)) {
    browser = 'Firefox';
    const versionMatch = userAgent.match(/Firefox\/(\d+\.\d+)/);
    if (versionMatch) {
      browserVersion = versionMatch[1];
    }
  } else if (/Safari/.test(userAgent)) {
    browser = 'Safari';
    const versionMatch = userAgent.match(/Version\/(\d+\.\d+)/);
    if (versionMatch) {
      browserVersion = versionMatch[1];
    }
  }
  return { os: `${os} ${osVersion}`, browser: `${browser} ${browserVersion}` };
}
