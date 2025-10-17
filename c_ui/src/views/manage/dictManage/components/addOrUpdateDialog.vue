<template>
  <c-dialog v-model="open" :title="title" width="700" :modal="true">
    <el-form :model="form" label-width="85" ref="formRef" :rules="rules">
      <el-row>
        <el-col :span="12">
          <el-form-item label="字典类型" prop="dictType">
            <el-input v-model="form.dictType" clearable disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="回显样式" prop="listClass">
            <dict-select v-model="form.listClass" dict-type="tag_css_type" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="字典键值" prop="dictValue">
            <el-input v-model="form.dictValue" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="字典标签" prop="dictLabel">
            <el-input v-model="form.dictLabel" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-switch
              v-model="form.status"
              active-text="正常"
              inactive-text="停用"
              active-value="1"
              inactive-value="0"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示顺序" prop="dictSort">
            <el-input-number v-model="form.dictSort" :min="0"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item label="备注" prop="remark" style="width: 100%">
          <el-input
            type="textarea"
            :rows="3"
            maxlength="100"
            show-word-limit
            v-model="form.remark"
            placeholder="写点什么吧。。。"
            clearable
          />
        </el-form-item>
      </el-row>
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
import { saveDictData, updateDictData, getDictDataById } from '@/api/system/dict/dictData.ts';
import { ElNotification } from 'element-plus';

const formRef = ref(null) as any;
const open = ref(false) as any;

const rules = ref({
  listClass: [{ required: true, message: '请选择回显样式', trigger: 'change' }],
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典键值', trigger: 'blur' }],
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  dictSort: [{ required: true, message: '请输入字典排序', trigger: 'blur' }]
});

const title = ref('');

const form = ref({
  id: null,
  dictLabel: null,
  dictType: null,
  dictValue: null,
  dictSort: 0,
  status: '1',
  listClass: 'default',
  remark: null
}) as any;

const dictType = ref(null);

const init = (options: any) => {
  options.operation == 'add' ? handleReset() : getInfo(options.row);
  title.value = options.title;
  form.value.dictType = options.dictType;
  open.value = true;
};

const getInfo = async (row: any) => {
  const { code, data } = await getDictDataById(row.id);
  if (code === 200) {
    form.value = data;
  }
};

function handleReset() {
  form.value = {
    id: null,
    dictLabel: null,
    dictType: null,
    dictValue: null,
    dictSort: 0,
    status: '1',
    listClass: 'default',
    remark: null
  };
}

function submit() {
  formRef.value.validate(async (valid: any) => {
    if (!valid) return;
    const { code } = form.value.id
      ? await updateDictData(form.value)
      : await saveDictData(form.value);
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
