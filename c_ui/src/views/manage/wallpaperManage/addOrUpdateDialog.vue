<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" width="500" :title="title" :modal="true">
    <el-form :model="form" label-width="85" ref="formRef" :rules="rules">
      <el-form-item :label="$t('壁纸名称')" prop="name">
        <el-input v-model="form.name" clearable></el-input>
      </el-form-item>
      <el-form-item :label="$t('壁纸类型')" prop="type">
        <el-select v-model="form.type" @change="handleTypeChange">
          <el-option :label="$t('静态壁纸')" value="img" />
          <el-option :label="$t('动态壁纸')" value="video" />
          <el-option :label="$t('纯色壁纸')" value="color" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('壁纸路径')" prop="url">
        <el-input
          v-model="form.url"
          clearable
          :placeholder="$t('请输入壁纸URL或颜色值')"
        ></el-input>
      </el-form-item>
      <el-form-item
        :label="$t('封面')"
        prop="coverUrl"
        v-if="form.type === 'video'"
        :rules="form.type === 'video' ? rules.coverUrl : []"
      >
        <upload v-model="form.coverUrl" path="wallpaper"></upload>
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
import { saveWallpaper, updateWallpaper, getWallpaperById } from '@/api/system/wallpaper';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  name: [{ required: true, message: $t('请输入壁纸名称'), trigger: 'blur' }],
  url: [{ required: true, message: $t('请输入壁纸路径'), trigger: 'blur' }],
  type: [{ required: true, message: $t('请选择壁纸类型'), trigger: 'change' }],
  coverUrl: [{ required: true, message: $t('请上传视频封面'), trigger: 'change' }]
});

const title = ref('');

const form = ref({
  id: null,
  coverUrl: '',
  url: '',
  name: '',
  type: 'img'
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getWallpaperById(row.id);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    id: null,
    coverUrl: '',
    url: '',
    name: '',
    type: 'img'
  };
}

function handleTypeChange() {
  // 切换类型时清空相关字段
  if (form.value.type !== 'video') {
    form.value.coverUrl = '';
  }
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id
      ? await updateWallpaper(form.value)
      : await saveWallpaper(form.value);
    if (code === 200) {
      ElNotification.success({
        title: 'Success',
        message: !form.value.id ? $t('新增成功') : $t('修改成功'),
        offset: 100
      });
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
