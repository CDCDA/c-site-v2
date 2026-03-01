<!--
 * @Description: 联系我弹窗
-->
<template>
  <c-dialog v-model="visible" :title="$t('联系我')" width="500px" :before-close="handleClose">
    <el-descriptions :column="1" border label-width="80px">
      <el-descriptions-item :label="$t('姓名')">
        <div class="desc-item">
          <span>{{ contactInfo.name }}</span>
          <el-icon class="copy-icon" @click="copyText(contactInfo.name)"><CopyDocument /></el-icon>
        </div>
      </el-descriptions-item>
      <el-descriptions-item :label="$t('手机号')">
        <div class="desc-item">
          <span>{{ contactInfo.phone }}</span>
          <el-icon class="copy-icon" @click="copyText(contactInfo.phone)"><CopyDocument /></el-icon>
        </div>
      </el-descriptions-item>
      <el-descriptions-item :label="$t('邮箱')">
        <div class="desc-item">
          <span>{{ contactInfo.email }}</span>
          <el-icon class="copy-icon" @click="copyText(contactInfo.email)"><CopyDocument /></el-icon>
        </div>
      </el-descriptions-item>
      <el-descriptions-item :label="$t('微信')">
        <div class="desc-item">
          <span>{{ contactInfo.wechat }}</span>
          <svg-icon
            iconName="commonSvg-二维码"
            @click="showQrCode"
            style="cursor: pointer; font-size: 18px"
          />
          <el-icon class="copy-icon" @click="copyText(contactInfo.wechat)"
            ><CopyDocument
          /></el-icon>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </c-dialog>

  <c-dialog v-model="qrVisible" :title="$t('微信二维码')" width="300px" center>
    <div class="qr-container">
      <img :src="qrCodeUrl" alt="微信二维码" />
    </div>
  </c-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { CopyDocument } from '@element-plus/icons-vue';
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:modelValue']);

const visible = ref(false);
const qrVisible = ref(false);
const qrCodeUrl = ref('/images/wechat-qrcode.jpg');

const contactInfo = ref({
  name: '陈毅东',
  phone: '15260962898',
  email: '1205489124@qq.com',
  wechat: 'a1205489124'
});

watch(
  () => props.modelValue,
  val => {
    visible.value = val;
  }
);

watch(visible, val => {
  emit('update:modelValue', val);
});

function handleClose() {
  visible.value = false;
}

function copyText(text: string) {
  navigator.clipboard
    .writeText(text)
    .then(() => {
      ElMessage.success($t('复制成功'));
    })
    .catch(() => {
      ElMessage.error($t('复制失败'));
    });
}

function showQrCode() {
  qrVisible.value = true;
}
</script>

<style lang="scss" scoped>
.desc-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  position: relative;

  span {
    text-align: center;
  }

  .copy-icon {
    position: absolute;
    right: 10px;
    cursor: pointer;
    font-size: 16px;
    color: var(--el-color-primary);
    transition: all 0.3s;

    &:hover {
      transform: scale(1.2);
      color: var(--el-color-primary-light-3);
    }
  }

  .qr-icon {
    position: absolute;
    left: 10px;
    cursor: pointer;
    font-size: 18px;
    color: var(--el-color-success);
    transition: all 0.3s;

    &:hover {
      transform: scale(1.2);
      color: var(--el-color-success-light-3);
    }
  }
}

.qr-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;

  img {
    width: 200px;
    height: 200px;
    object-fit: contain;
  }
}
</style>
