<!-- 新增或编辑 -->
<template>
  <c-dialog v-model="open" :title="title" width="450" :modal="true">
    <el-form :model="form" label-width="55" ref="formRef" :rules="rules">
      <el-form-item :label="$t('名称')" prop="dictName">
        <el-input v-model="form.dictName" clearable :placeholder="$t('请输入字典名称')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('类型')" prop="dictType">
        <el-input v-model="form.dictType" clearable :placeholder="$t('请输入字典类型')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('状态')" prop="status">
        <el-switch
          v-model="form.status"
          :active-text="$t('正常')"
          :inactive-text="$t('停用')"
          active-value="1"
          inactive-value="0"
        ></el-switch>
      </el-form-item>
      <el-form-item :label="$t('备注')">
        <el-input
          type="textarea"
          :rows="3"
          maxlength="100"
          show-word-limit
          v-model="form.remark"
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
import { saveDictType, updateDictType, getDictTypeById } from '@/api/system/dict/dictType.ts';
import { ElNotification } from 'element-plus';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  dictName: [{ required: true, message: $t('请输入字典名称'), trigger: 'blur' }],
  dictType: [{ required: true, message: $t('请输入字典类型'), trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  id: null,
  dictName: null,
  dictType: null,
  status: '1',
  remark: null
}) as any;

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getDictTypeById(row.id);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    id: null,
    dictName: null,
    dictType: null,
    status: '1',
    remark: null
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id
      ? await updateDictType(form.value)
      : await saveDictType(form.value);
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
