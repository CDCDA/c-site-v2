<template>
  <div class="login-container">
    <div class="login-main">
      <div class="login" :class="pageType === 'register' ? 'login-hidden' : ''">
        <h3 class="login-title">{{ $t('登录') }}</h3>
        <div class="login-input">
          <input
            type="text"
            :placeholder="$t('用户名/邮箱')"
            class="userName"
            v-model="loginForm.userName"
          />
          <input
            type="password"
            :placeholder="$t('密码')"
            class="password"
            v-model="loginForm.password"
          />
        </div>
        <div class="login-edit">
          <span class="edit-pw">{{ $t('修改密码?') }}</span>
        </div>
        <div class="login-btn">
          <el-button @click="handleLogin" :loading="loading" :disabled="loading">{{
            $t('登录')
          }}</el-button>
        </div>
      </div>
      <div class="register" :class="pageType === 'register' ? 'register-show' : ''">
        <h3 class="register-title">{{ $t('注册') }}</h3>
        <div class="register-input">
          <input
            type="text"
            :placeholder="$t('昵称')"
            class="nickName"
            v-model="registerForm.nickName"
          />
          <input
            type="password"
            :placeholder="$t('密码')"
            class="password"
            v-model="registerForm.password"
          />
          <input type="text" :placeholder="$t('邮箱')" class="email" v-model="registerForm.email" />
          <input type="text" :placeholder="$t('验证码')" class="code" v-model="registerForm.code" />
        </div>
        <div class="register-edit">
          <span class="edit-pw" @click="getRegisterCode">{{ $t('获取验证码') }}</span>
        </div>
        <div class="register-btn">
          <el-button @click="handleRegister" :loading="loading" :disabled="loading">{{
            $t('暂不开放注册')
          }}</el-button>
        </div>
      </div>
      <div class="register-pre" :class="pageType === 'register' ? 'login-pre' : ''">
        <h3 class="register-title">
          {{ `${pageType === 'login' ? $t('没有账号') : $t('已有账号')}?` }}
        </h3>
        <div class="register-tip">
          {{ `${pageType === 'login' ? $t('立即注册') : '请登录🚀'}` }}
        </div>
        <div class="register-btn">
          <el-button
            @click="openRegister"
            :class="pageType === 'register' ? 'login-btn' : 'register-btn'"
            >{{ `${pageType === 'login' ? $t('注册') : $t('登录')}` }}</el-button
          >
          <el-button @click="handleTouristLogin" :loading="touristLoading" :disabled="loading">{{
            $t('游客登录')
          }}</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="login">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import jwtDecode from 'jwt-decode';
import { autoClearTimer } from '@/utils/timer';
import { ElMessage, ElNotification } from 'element-plus';
import { login, touristLogin, register, getCode } from '@/api/system/auth.ts';
import { useRouter } from 'vue-router';
import useUserStore from '@/store/modules/user';
import Cookies from 'js-cookie';
import useThemeStore from '@/store/modules/theme.ts';
const themeStore = useThemeStore();
themeStore.isShow = false;
themeStore.isFooterShow = false;
const userStore = useUserStore();
const router = useRouter();
const loading = ref(false);
const loginForm = ref({
  password: '',
  userName: ''
});
const registerForm = ref({
  nickName: '',
  password: '',
  email: '',
  code: ''
});
const pageType = ref('login');

function resetLoginForm() {
  loginForm.value = {
    password: '',
    userName: ''
  };
}

function resetRegisterForm() {
  registerForm.value = {
    nickName: '',
    password: '',
    email: '',
    code: ''
  };
}

//切换注册页面
function openRegister() {
  if (pageType.value === 'register') pageType.value = 'login';
  else pageType.value = 'register';
}

const touristLoading = ref(false);

// 游客登录
async function handleTouristLogin() {
  touristLoading.value = true;
  let form = {
    userName: 'CCCC',
    password: '1'
  };
  const { code, data } = (await login(form)) as any;
  if (code === 200) {
    //缓存用户数据
    const { token, user } = data;
    const userData = {
      token,
      userId: user.userId,
      userName: user.username,
      email: user.email,
      nickName: user.nickName,
      avatar: user.avatar,
      permission: ['show']
    };
    Object.assign(userStore, userData);
    console.log('用户信息', userData);
    try {
      window.localStorage.setItem('userData', JSON.stringify(userData));
    } catch (error) {
      console.log(error);
    }
    console.log('跳转到首页');
    router.push('/');
  }
  touristLoading.value = false;
}

// 登录
async function handleLogin() {
  if (!loginForm.value.userName) {
    ElNotification.warning($t('请输入账号/邮箱'));
    return;
  }
  if (!loginForm.value.userName) {
    ElNotification.warning($t('请输入密码'));
    return;
  }
  loading.value = true;
  const { code, data } = await login(loginForm.value);
  console.log('登录结果', code, data);
  if (code === 200) {
    //缓存用户数据
    const { token, user } = data;
    const userData = {
      token,
      userId: user.userId,
      userName: user.username,
      email: user.email,
      nickName: user.nickName,
      avatar: user.avatar,
      permission: ['add', 'delete', 'show', 'operate']
    };
    Object.assign(userStore, userData);
    console.log('用户信息', userData);
    try {
      window.localStorage.setItem('userData', JSON.stringify(userData));
    } catch (error) {
      console.log(error);
    }
    console.log('跳转到首页');
    router.push('/');
  }
  loading.value = false;
}

//注册
async function handleRegister() {
  ElNotification.warning($t('暂不开放注册'));
  return;
  if (!registerForm.value.nickName) {
    ElNotification.warning($t('请输入昵称'));
    return;
  }
  if (!registerForm.value.password) {
    ElNotification.warning($t('请输入密码'));
    return;
  }
  if (!registerForm.value.email) {
    ElNotification.warning($t('请输入邮箱'));
    return;
  }
  if (!registerForm.value.code) {
    ElNotification.warning($t('请输入验证码'));
    return;
  }
  loading.value = true;
  const { code } = (await register(registerForm.value)) as any;
  if (code === 200) {
    ElNotification.success($t('注册成功'));
    loginForm.value.userName = registerForm.value.email;
    loginForm.value.password = registerForm.value.password;
    resetRegisterForm();
    openRegister();
  }
  loading.value = false;
}

//获取验证码
async function getRegisterCode() {
  let params = { email: registerForm.value.email };
  const { code } = (await getCode(params)) as any;
  if (code === 200) {
    ElNotification.success($t('验证码发送成功,请注意查收'));
  }
}
async function handleTouristLogIn() {
  const { code } = (await touristLogin()) as any;
  if (code === 200) {
    const token = jwtDecode(Cookies.get('token')) as any;
    userStore.token = Cookies.get('token');
    userStore.userId = token.aud;
    userStore.userName = token.username;
    userStore.permission = [];
    window.localStorage.setItem(
      'userData',
      JSON.stringify({
        token: userStore.token,
        userId: userStore.userId,
        userName: userStore.userName,
        permission: userStore.permission
      })
    );

    router.push('/');
  }
}
onMounted(() => {
  // loginForm.value.userName = 'CCCC';
  // loginForm.value.password = '1';
});
</script>
<style lang="scss" scoped>
@include theme() {
  .login-container {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
  .login-main {
    opacity: 0.9;
    border-radius: 10px;
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15), 0 10px 10px rgba(0, 0, 0, 0.15);
    position: relative;
    overflow: hidden;
    width: 750px;
    max-width: 100%;
    min-height: 450px;
    margin: 10px;
    height: 50vh;
    display: flex;
    align-items: center;
  }
  .login {
    width: 50%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    transition: all 0.5s ease-in-out;
    background: white;
    .login-title {
      margin-top: 22%;
      margin-bottom: 10%;
      font-size: 1.6rem;
    }
    .login-input {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }
    .login-edit {
      color: black;
      font-size: 0.7rem;
      text-decoration: none;
      margin: 15px 0;
    }
  }
  .login-hidden.login {
    opacity: 0;
    top: 100%;
  }
  .login-pre.register-pre {
    left: 0;
  }
  .register-show.register {
    opacity: 1;
    left: 50%;
    top: 0;
    z-index: 0;
  }
  .register {
    width: 50%;
    height: 100%;
    position: absolute;
    opacity: 0;
    left: 0;
    top: 0;
    z-index: -1;
    transition: all 0.5s ease-in-out;
    background: white;
    .register-title {
      margin-top: 10%;
      margin-bottom: 4%;
      font-size: 1.6rem;
    }
    .register-input {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }
    .register-edit {
      color: black;
      font-size: 0.7rem;
      text-decoration: none;
      margin: 15px 0;
    }
  }
  .register-pre {
    width: 50%;
    height: 100%;
    position: absolute;
    left: 50%;
    top: 0;
    transition: all 0.5s ease-in-out;
    background: linear-gradient(90deg, #ff4b2b, #ff416c);
    color: white;
    .register-title {
      margin-top: 40%;
      margin-bottom: 13%;
      font-size: 1.6rem;
    }
    .register-tip {
      font-size: 0.7rem;
      letter-spacing: 1px;
      margin: 20px 0 30px;
    }
  }
  .el-button {
    border-radius: 2rem !important;
    border: none;
    background: linear-gradient(90deg, #ff4b2b, #ff416c) !important;
    color: white !important;
    font-size: 16px;
    font-weight: 700;
    padding: 20px 45px !important;
    letter-spacing: 2px;
    cursor: pointer;
    border: 1px solid #fafaf9;
  }
  .el-button:hover {
    transform: scale(1.05);
  }
  .el-button:active {
    transform: translateY(2px);
  }

  input {
    background-color: #eee !important;
    border-radius: 2px;
    border: none;
    padding: 12px 15px;
    margin: 10px 0;
    width: calc(100% - 80px);
    outline: none;
    &:active {
      background-color: #eee;
    }
    &::selection {
      background-color: #eee;
    }
  }
}
</style>
