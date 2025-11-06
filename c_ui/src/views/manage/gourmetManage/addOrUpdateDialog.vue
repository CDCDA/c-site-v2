<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="500" :modal="true">
    <el-form :model="form" label-width="60" ref="formRef" :rules="rules">
      <el-form-item :label="$t('美食名')" prop="name">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item :label="$t('封面')" prop="coverUrl">
        <div style="display: flex; align-items: center; gap: 10px">
          <el-radio-group v-model="fromNet" size="small">
            <el-radio-button :label="true">{{ $t('网络图片') }}</el-radio-button>
            <el-radio-button :label="false">{{ $t('本地上传') }}</el-radio-button>
          </el-radio-group>
          <upload v-if="!fromNet" v-model="form.coverUrl" path="gourmet" />
          <el-input v-else v-model="form.coverUrl" clearable :placeholder="$t('请输入图片URL')" />
        </div>
      </el-form-item>
      <el-form-item :label="$t('做法')" prop="url">
        <el-input v-model="form.url" clearable :placeholder="$t('请输入做法链接或描述')" />
      </el-form-item>
      <el-form-item :label="$t('简介')" prop="intro">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.intro"
          :placeholder="$t('写点什么吧。。。')"
          clearable
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
import { ref, onMounted, watch } from 'vue';
import { saveCate, updateCate, getCateById } from '@/api/cate.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;
const fromNet = ref(true) as any;

const rules = ref({
  name: [{ required: true, message: $t('请输入美食名称'), trigger: 'blur' }],
  coverUrl: [{ required: true, message: $t('请上传封面或输入图片URL'), trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  id: null,
  coverUrl: null,
  url: null,
  intro: '',
  name: ''
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getCateById(row.id);
  if (code === 200) {
    form.value = data;
    // 判断封面类型
    if (form.value.coverUrl && form.value.coverUrl.startsWith('http')) {
      fromNet.value = true;
    } else {
      fromNet.value = false;
    }
  }
};

function handleReset() {
  form.value = {
    id: null,
    coverUrl: null,
    url: null,
    intro: '',
    name: ''
  };
  fromNet.value = true;
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = !form.value.id ? await saveCate(form.value) : await updateCate(form.value);
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
