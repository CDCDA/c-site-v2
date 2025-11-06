<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="28rem" :modal="true">
    <el-form :model="form" label-width="90" ref="formRef" :rules="rules">
      <el-form-item :label="$t('更新内容')" prop="operation">
        <el-input
          type="textarea"
          :rows="4"
          maxlength="500"
          show-word-limit
          v-model="form.operation"
          :placeholder="$t('请输入更新内容...')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="$t('更新时间')" prop="operateTime">
        <el-date-picker
          v-model="form.operateTime"
          type="date"
          :placeholder="$t('请选择')"
          size="small"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="open = false">{{ $t('取消') }}</el-button>
        <el-button type="primary" @click="submit"> {{ $t('确定') }}</el-button>
      </span>
    </template>
  </c-dialog>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { saveLog, updateLog, getLogById } from '@/api/system/updateLog';
import { ElNotification } from 'element-plus';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  operation: [{ required: true, message: $t('请输入更新内容'), trigger: 'blur' }],
  operateTime: [{ required: true, message: $t('请选择更新时间'), trigger: 'change' }]
});

const title = ref('');

const form = ref({
  id: null,
  operation: '',
  operateTime: new Date()
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getLogById(row.id);
  if (code === 200) {
    form.value = data;
    // 确保时间格式正确
    if (form.value.operateTime) {
      form.value.operateTime = new Date(form.value.operateTime);
    }
  }
};

function handleReset() {
  form.value = {
    id: null,
    operation: '',
    operateTime: new Date()
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id ? await updateLog(form.value) : await saveLog(form.value);
    if (code === 200) {
      ElNotification.success(!form.value.id ? $t('新增成功') : $t('修改成功'));
      emit('getList');
      open.value = false;
    }
  });
}

const emit = defineEmits(['getList']);
defineExpose({
  init
});
</script>
