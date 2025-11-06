<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="600" :modal="true">
    <el-form :model="form" label-width="85" ref="formRef" :rules="rules">
      <el-form-item :label="$t('影视名称')" prop="name">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item :label="$t('封面')" prop="coverUrl">
        <upload v-model="form.coverUrl" path="drama" />
      </el-form-item>
      <el-form-item :label="$t('分类')" prop="type">
        <dict-select v-model="form.type" dict-type="drama_type" :placeholder="$t('请选择分类')" />
      </el-form-item>
      <el-form-item :label="$t('播放地址')" prop="url">
        <el-input v-model="form.url" clearable :placeholder="$t('请输入播放地址')" />
      </el-form-item>
      <el-form-item :label="$t('简介')">
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
import { ref, onMounted } from 'vue';
import { saveDrama, updateDrama, getDramaById } from '@/api/dramaSeries.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  coverUrl: [{ required: true, message: $t('请上传封面'), trigger: 'blur' }],
  name: [{ required: true, message: $t('请输入影视名称'), trigger: 'blur' }],
  type: [{ required: true, message: $t('请选择分类'), trigger: 'change' }],
  url: [{ required: true, message: $t('请输入播放地址'), trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  id: null,
  coverUrl: null,
  url: null,
  intro: '',
  type: '0',
  name: ''
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getDramaById(row.id);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    id: null,
    coverUrl: null,
    url: null,
    intro: '',
    type: '0',
    name: ''
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id ? await updateDrama(form.value) : await saveDrama(form.value);
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
