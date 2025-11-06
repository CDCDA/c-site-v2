import i18n from '@/locales/i18n';
/*
 * @Description:接口
 */
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import axios from 'axios';
import useUserStore from '@/store/modules/user';
import { ElMessageBox, ElNotification } from 'element-plus';
var user = null as any;
import router from '@/router/index';

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? '/dev-api' : '/prod-api',
  timeout: 150000
}) as any;

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    if (!user) {
      user = useUserStore();
    }
    config.headers.Authorization = `Bearer ${user.token}`;
    return config as any;
  },
  error => {
    ElNotification.error(i18n.global.t('请求配置错误'));
    return Promise.reject(error) as any;
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 检查响应是否存在
    if (!response) {
      ElNotification.error(i18n.global.t('无法连接到服务器'));
      return Promise.reject(new Error('No response received')) as any;
    }

    const { data } = response;

    // 如果响应数据是空字符串（如你示例中的情况）
    if (data === '') {
      ElNotification.error(i18n.global.t('服务器返回空响应'));
      return Promise.reject(new Error('Empty response from server')) as any;
    }

    // 处理业务状态码
    if (data.code === 401) {
      ElMessageBox.alert(
        '登录状态已过期，您可以继续留在该页面，或者重新登录',
        i18n.global.t('系统提示'),
        {
          confirmButtonText: i18n.global.t('重新登录'),
          type: 'warning',
          showClose: false
        }
      )
        .then(() => {
          user.clearToken();
          router.push({ name: 'login' });
        })
        .catch(() => {});
      return Promise.reject(new Error('Unauthorized')) as any;
    }

    if (data.code === 500) {
      const errorMsg = data.msg || i18n.global.t('服务器内部错误');
      ElNotification.error(errorMsg);
      return Promise.reject(new Error(errorMsg)) as any;
    }

    if (data.code === 403) {
      ElNotification.error(data.msg || i18n.global.t('没有权限访问'));
      return Promise.reject(new Error('Forbidden')) as any;
    }

    // 请求成功，返回数据
    return data as any;
  },
  error => {
    // 统一错误处理
    handleError(error);
    return Promise.reject(error) as any;
  }
);

// 统一的错误处理函数
function handleError(error: any) {
  if (error.response) {
    // 服务器返回了错误状态码
    const { status, data } = error.response;

    switch (status) {
      case 400:
        ElNotification.error(data?.msg || i18n.global.t('请求参数错误'));
        break;
      case 401:
        ElNotification.error(data?.msg || i18n.global.t('未授权，请重新登录'));
        user?.clearToken?.();
        router.push({ name: 'login' });
        break;
      case 403:
        ElNotification.error(data?.msg || i18n.global.t('没有权限访问'));
        break;
      case 404:
        ElNotification.error(i18n.global.t('请求的资源不存在'));
        break;
      case 500:
        // 处理500错误，特别是空响应的情况
        if (data === '' || !data) {
          ElNotification.error(i18n.global.t('服务器内部错误，请稍后重试'));
        } else {
          ElNotification.error(data?.msg || i18n.global.t('服务器内部错误'));
        }
        break;
      case 502:
        ElNotification.error(i18n.global.t('网关错误'));
        break;
      case 503:
        ElNotification.error(i18n.global.t('服务不可用'));
        break;
      case 504:
        ElNotification.error(i18n.global.t('网关超时'));
        break;
      default:
        ElNotification.error(data?.msg || i18n.global.t('请求失败'));
    }
  } else if (error.request) {
    // 请求发送了但没有收到响应
    if (error.code === 'ECONNABORTED') {
      ElNotification.error(i18n.global.t('请求超时，请检查网络连接'));
    } else {
      ElNotification.error(i18n.global.t('网络错误，请检查网络连接'));
    }
  } else {
    // 其他错误
    ElNotification.error(error.message || i18n.global.t('未知错误'));
  }
}

export default service as any;
