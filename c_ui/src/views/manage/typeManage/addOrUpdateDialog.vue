<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="500" :modal="true">
    <el-form :model="form" label-width="90px" ref="formRef" :rules="rules">
      <el-form-item label="分类名称" prop="typeName">
        <el-input
          v-model="form.typeName"
          placeholder="请输入分类名称"
          clearable
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="分类简介" prop="intro">
        <el-input
          v-model="form.intro"
          type="textarea"
          :rows="3"
          placeholder="请输入分类简介"
          maxlength="100"
          show-word-limit
          clearable
        />
      </el-form-item>
      <el-form-item label="封面图片">
        <upload v-model="form.coverUrl" path="blogType"></upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </span>
    </template>
  </c-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { saveType, updateType } from '@/api/type.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  typeName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  intro: [
    { required: true, message: '请输入分类简介', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ]
});

const title = ref('');

const form = ref({
  typeId: null,
  typeName: '',
  intro: '',
  coverUrl: ''
}) as any;

const init = (options: any) => {
  if (options.operation === 'add') {
    handleReset();
  } else {
    form.value = { ...options.row };
  }
  console.log('options', options);
  title.value = options.title;
  open.value = true;
};

function handleReset() {
  form.value = {
    typeId: null,
    typeName: '',
    intro: '',
    coverUrl: ''
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;

    const { code } = form.value.typeId ? await updateType(form.value) : await saveType(form.value);

    if (code === 200) {
      ElNotification.success(form.value.typeId ? '修改成功' : '新增成功');
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
