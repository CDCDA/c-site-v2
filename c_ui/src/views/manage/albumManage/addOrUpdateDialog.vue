<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="50%" style="height: 70%" :modal="true">
    <el-form
      :model="form"
      label-width="55"
      style="height: calc(100% - 40px); overflow: auto"
      ref="formRef"
      :rules="rules"
    >
      <el-form-item :label="$t('名称')" prop="name">
        <el-input v-model="form.name" clearable></el-input>
      </el-form-item>
      <el-form-item :label="$t('简介')">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.intro"
          :placeholder="$t('写点什么吧~(￣▽￣)~*')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="$t('封面')">
        <upload v-model="form.coverUrl" path="album"></upload>
      </el-form-item>
      <el-form-item :label="$t('图片')" prop="images">
        <upload v-model="form.images" path="album"></upload>
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
import { saveAlbum, updateAlbum, getAlbumById } from '@/api/album.ts';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';
const formRef = ref(null) as any;
const open = ref(false) as any;
const rules = ref({
  name: [{ required: true, message: $t('请输入相册名称'), trigger: 'blur' }]
});
const title = ref('');

const form = ref({
  id: null,
  coverUrl: null,
  intro: '',
  name: '',
  images: []
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getAlbumById(row.id);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    id: null,
    coverUrl: null,
    intro: '',
    name: '',
    images: []
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id ? await updateAlbum(form.value) : await saveAlbum(form.value);
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
