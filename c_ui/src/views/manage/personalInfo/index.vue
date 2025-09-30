<!--
 * @Description: 个人信息管理
-->
<template>
  <div
    class="personal-main"
    style="background: url('http://120.48.127.181/file/樱花古风.jpg') center 28% / cover no-repeat"
  >
    <div class="personal-page page-main">
      <div class="user-left">
        <div class="info">
          <!--          <c-image class="user-avatar" :src="userInfo.avatar" />-->
          <upload class="user-avatar-upload" v-model="userInfo.avatar" path="avatar" />
          <div class="user-left-detail">
            <el-form ref="formRef" class="info-form" label-width="100" :rules="rules" :model="form">
              <el-form-item label="入站时间">
                <span>{{ userInfo.createTime }}</span>
              </el-form-item>
              <el-form-item label="昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称">
                <el-input v-model="form.nickName"></el-input>
              </el-form-item>
              <el-form-item label="账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号">
                <span>{{ userInfo.userName }}</span>
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="form.phone"></el-input>
              </el-form-item>
              <el-form-item label="邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱">
                <el-input v-model="form.email"></el-input>
              </el-form-item>
              <el-form-item label="性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别">
                <el-radio-group v-model="form.sex" style="margin-left: 15px">
                  <el-radio label="0" size="small">男</el-radio>
                  <el-radio label="1" size="small">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input type="textarea" v-model="form.introduction"></el-input>
              </el-form-item>
              <div class="user-footer">
                <el-button @click="submit" v-permission="'operate'">保存</el-button>
                <el-button @click="cancel">取消</el-button>
              </div>
            </el-form>
          </div>
        </div>
      </div>
      <div class="user-right"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue';
import { formatDate } from '@/utils/date';
import useUserStore from '@/store/modules/user';
import { getUserById, saveUser } from '@/api/system/user';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/upload.vue';
const userInfo = ref({} as any);
const userStore = useUserStore();
import useThemeStore from '@/store/modules/theme.ts';
const themeStore = useThemeStore();
themeStore.isFooterShow = false;
const isEdit = ref(false as any);
const form = ref({} as any);
const formRef = ref(null as any);
const rules = ref({
  nickName: [{ required: true, message: 'Please input Activity name', trigger: 'blur' }]
});
async function getUserInfo(userId: any) {
  const { code, msg, rows, total } = (await getUserById(userId)) as any;
  if (code === 200 && data) {
    Object.assign(userInfo.value, data);
    form.value = JSON.parse(JSON.stringify(userInfo.value));
  }
}

async function submit() {
  await formRef.value.validate(async (valid: any) => {
    if (valid) {
      const { code, msg, rows, total } = (await saveUser(form.value)) as any;
      if (code === 200) {
        ElNotification({
          title: 'Success',
          message: '修改成功',
          type: 'success'
        });
        getUserInfo(userStore.userId);
      }
    }
  });
}

function cancel() {
  form.value = JSON.parse(JSON.stringify(userInfo.value));
}

function showEdit() {
  isEdit.value = !isEdit.value;
}

onMounted(() => {
  getUserInfo(userStore.userId);
});
</script>
<style lang="scss" scoped>
.personal-main {
  position: fixed;
  width: 100vw;
  height: 100vh;
}
@include theme() {
  .personal-page {
    height: calc(100% - 2.8rem - 30px);
    margin-top: 1rem;
    color: get('font-color');
    background: transparent !important;
    backdrop-filter: none !important;
    overflow: hidden;
    display: flex;

    :deep(.el-upload) {
      height: 100%;
    }
    :deep(.el-image) {
      height: 100%;
      border-radius: 50%;
    }

    .user-left {
      width: 50%;
      height: calc(100% - 2rem);
      background: hsla(0, 0%, 100%, 0.7);
      display: flex;
      flex-direction: column;
      align-items: center;
      overflow-y: auto;
      padding: 1rem;
      color: get(font-color);
      font-size: 1rem;
      .user-left-back {
        position: absolute;
        width: 100%;
        filter: brightness(0.6);
        height: 100%;
      }
      .user-left-detail {
        text-align: left;
        justify-content: start;
        display: flex;
        flex-direction: column;
      }
      .user-avatar,
      :deep(.c-uploader) {
        border-radius: 5rem;
        width: 5rem;
        height: 5rem;
        margin-bottom: 1rem;
        margin-top: 4rem;
      }
      .info {
        display: flex;
        flex-direction: column;
        width: 100%;
        align-items: center;
        justify-content: start;
      }
    }
    .user-right {
      width: 50%;
      height: 100%;
      overflow-y: auto;
      background: hsla(0, 0%, 100%, 0.3);
      padding: 20px;
    }
    .info-form {
      margin: 20px;
      position: relative;
      height: calc(100% - 30vh - 80px);
      span {
        font-size: 0.8rem;
      }
      :deep(.el-input),
      :deep(.el-textarea) {
        width: 250px !important;
        margin-left: 15px;
        background: transparent !important;
      }
      :deep(.el-input__wrapper),
      :deep(.el-textarea__inner) {
        //background-color: hsla(0, 0%, 100%, 0.3);
        background: hsla(0, 0%, 100%, 0.3) !important;
        border: none !important;
      }
      .el-textarea {
        width: calc(100% - 100px);
      }
      .el-icon {
        font-size: 1.2rem;
        margin-left: 10px;
        // visibility: hidden;
      }
      .el-icon:hover {
        visibility: visible;
      }
      .el-form-item__content > span {
        margin-left: 15px;
      }
      // .el-button {
      //   background: transparent;
      //   color: get('font-color');
      // }
      .user-footer {
        margin-top: 2rem;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .el-pagination {
        display: flex;
        justify-content: center;
      }
    }
  }
  // .info-column {
  //   width: 30%;
  //   height: 100%;
  //   display: flex;
  //   flex-direction: column;
  //   justify-content: space-between;
  // }
  // .info-column:nth-child(1) {
  //   box-shadow: 0 1px 2px rgba(195, 195, 195, 0.6);
  //   background: transparent;
  //   backdrop-filter: blur(10px);
  //   border-radius: 8px;
  // }
  .blog-count,
  .type-count,
  .tag-count,
  .essay-count {
    width: calc(100% - 10px);
    height: calc(50% - 10px);
    box-shadow: 0 1px 2px rgba(195, 195, 195, 0.6);
    background: transparent;
    backdrop-filter: blur(10px);
    border-radius: 8px;
  }
  .blog-count {
    margin: 0 10px 5px 10px;
  }
  .type-count {
    margin: 5px 10px 0 10px;
  }
  .tag-count {
    margin: 0 10px 5px 10px;
  }
  .essay-count {
    margin: 5px 10px 0 10px;
  }
}
</style>
