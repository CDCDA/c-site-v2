<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="500" :modal="true">
    <el-form :model="form" label-width="55" ref="formRef" :rules="rules">
      <el-form-item label="名称" prop="tagName">
        <el-input v-model="form.tagName" clearable></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="blogTag"></upload>
      </el-form-item>
      <el-form-item label="简介">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.intro"
          placeholder="写点什么吧~(￣▽￣)~*"
          clearable
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submit"> 确定 </el-button>
      </span>
    </template>
  </c-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { saveTag, updateTag, getTagById } from '@/api/tag.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  tagId: null,
  coverUrl: null,
  intro: '',
  tagName: ''
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getTagById(row.tagId);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    tagId: null,
    coverUrl: null,
    intro: '',
    tagName: ''
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = !form.value.tagId ? await saveTag(form.value) : await updateTag(form.value);
    if (code === 200) {
      ElNotification.success(!form.value.tagId ? '新增成功' : '修改成功');
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
