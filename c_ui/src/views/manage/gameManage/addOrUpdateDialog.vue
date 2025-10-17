<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="700" :modal="true">
    <el-form :model="form" label-width="70" ref="formRef" :rules="rules">
      <el-form-item label="游戏名" prop="name">
        <el-input v-model="form.name" clearable />
      </el-form-item>
      <el-form-item label="封面" prop="coverUrl">
        <upload v-model="form.coverUrl" path="game" />
      </el-form-item>
      <el-form-item label="分类" prop="type">
        <el-radio-group v-model="form.type" class="ml-4">
          <el-radio :label="'0'">单机</el-radio>
          <el-radio :label="'1'">手游</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="官网" prop="url">
        <el-input v-model="form.url" clearable />
      </el-form-item>
      <el-form-item label="评分" prop="rate">
        <el-input type="number" v-model="form.rate" clearable />
      </el-form-item>
      <el-form-item label="简介" prop="intro">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.intro"
          placeholder="写点什么吧。。。"
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
import { saveGame, updateGame, getGameById } from '@/api/game.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  coverUrl: [{ required: true, message: '请上传封面', trigger: 'blur' }],
  name: [{ required: true, message: '请输入游戏名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类', trigger: 'blur' }],
  rate: [{ required: true, message: '请输入评分', trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  id: null,
  coverUrl: null,
  url: null,
  intro: '',
  type: '0',
  name: '',
  rate: null
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getGameById(row.id);
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
    name: '',
    rate: null
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id ? await updateGame(form.value) : await saveGame(form.value);
    if (code === 200) {
      ElNotification.success(!form.value.id ? '新增成功' : '修改成功');
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
