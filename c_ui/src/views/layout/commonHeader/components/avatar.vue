<template>
  <el-dropdown trigger="click" @command="change" style="color: inherit !important">
    <el-image class="avatar" :src="userStore.avatar" />
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="profile">
          {{ $t('个人信息') }}
        </el-dropdown-item>
        <el-dropdown-item command="editPassword">
          {{ $t('修改密码') }}
        </el-dropdown-item>
        <el-dropdown-item command="logout">
          {{ $t('退出登录') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <c-dialog title="修改密码" v-model="isEditPasswordDialogShow" width="450" :modal="true">
    <el-form
      :model="editPasswordForm"
      :rules="editPasswordRules"
      ref="editPasswordFormRef"
      label-width="100px"
    >
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input
          v-model="editPasswordForm.oldPassword"
          type="password"
          placeholder="请输入旧密码"
          show-password
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="editPasswordForm.newPassword"
          type="password"
          placeholder="请输入新密码"
          show-password
        />
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmNewPassword">
        <el-input
          v-model="editPasswordForm.confirmNewPassword"
          type="password"
          placeholder="请确认新密码"
          show-password
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="footer" style="display: flex; justify-content: end">
        <el-button
          type="default"
          @click="handleEditPasswordCancel"
          style="margin-bottom: 10px; width: 80px"
        >
          {{ $t('取消') }}
        </el-button>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleEditPasswordSubmit"
          style="margin-bottom: 10px; width: 80px"
        >
          {{ $t('确定') }}
        </el-button>
      </div>
    </template>
  </c-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
const router = useRouter();
const { t: $t } = useI18n();
import { updatePassword } from '@/api/system/user.ts';
import useUserStore from '@/store/modules/user.ts';
import { ElMessageBox, ElNotification } from 'element-plus';
const userStore = useUserStore();
import { loadingService } from '@/components/loading/loading.ts';
const loading = ref(false);

function change(params: string) {
  if (params === 'profile') {
    toProfile();
  } else if (params === 'editPassword') {
    handleEditPassword();
  } else if (params === 'logout') {
    handleLogout();
  }
}

const isEditPasswordDialogShow = ref(false);
const editPasswordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: ''
});
const editPasswordRules = reactive({
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmNewPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }]
});

function handleEditPasswordCancel() {
  isEditPasswordDialogShow.value = false;
}
const editPasswordFormRef = ref(null) as any;

function handleEditPassword() {
  isEditPasswordDialogShow.value = true;
}

function handleEditPasswordSubmit() {
  loading.value = true;
  editPasswordFormRef.value.validate(async (valid: any) => {
    if (valid) {
      if (editPasswordForm.newPassword !== editPasswordForm.confirmNewPassword) {
        ElNotification.error($t('新密码和确认新密码不一致'));
        loading.value = false;
        return;
      }
      const { code } = await updatePassword(editPasswordForm);
      if (code === 200) {
        ElNotification.success($t('密码修改成功'));
        isEditPasswordDialogShow.value = false;
      }
      loading.value = false;
    } else {
      loading.value = false;
    }
  });
}

function toProfile() {
  loadingService.show({ type: 'loading', text: '加载中...' });
  router.push({ path: '/personalInfo' });
  loadingService.hide();
}

function handleLogout() {
  ElMessageBox.confirm($t('确定注销并退出系统吗？'), $t('提示'), {
    confirmButtonText: $t('确定'),
    cancelButtonText: $t('取消'),
    type: 'warning'
  })
    .then(() => {
      userStore.userId = '';
      userStore.userName = '';
      userStore.token = '';
      userStore.permission = [];
      window.localStorage.setItem('userData', '');
      router.push({ name: 'login' });
    })
    .catch(() => {});
}
</script>
<style scoped>
.avatar {
  height: 2rem;
  width: 2rem;
  border-radius: 2rem;
  margin-left: 0.5rem;
  border: 3px solid white;
  cursor: pointer;
}
</style>
