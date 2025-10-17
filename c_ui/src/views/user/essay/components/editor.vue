<!--
 * @Description: 随笔编辑
-->
<template>
  <div class="essay-manage-editor">
    <el-form
      :model="essayData"
      label-width="85px"
      class="essay-form"
      ref="formEl"
      label-position="right"
      @submit.native.prevent
      :rules="rules"
    >
      <el-form-item label="随笔内容" prop="content">
        <el-input
          v-model="essayData.content"
          type="textarea"
          maxlength="300"
          show-word-limit
          :rows="5"
          placeholder="写点什么吧。。。"
        ></el-input>
      </el-form-item>
      <el-form-item label="随笔图片">
        <upload v-model="essayData.imageRelations" path="essay"></upload>
        <c-dialog v-model="dialogVisible">
          <img :src="dialogImageUrl" alt="Preview Image" />
        </c-dialog>
      </el-form-item>
      <el-form-item label="常用标签">
        <el-tag
          v-for="tag in commonUseTags"
          class="tag-item"
          :effect="tag.effect"
          :type="tag.tagType"
          @click.native="addTag(tag)"
        >
          {{ tag.tagName }}
        </el-tag>
      </el-form-item>
      <el-form-item label="博客标签">
        <el-tag
          v-for="tag in essayData.tags"
          :key="tag.tagName"
          :effect="tag.effect"
          :type="tag.tagType"
          class="tag-item"
          closable
          :disable-transitions="false"
          @close="tagDel(tag)"
        >
          {{ tag.tagName }}
        </el-tag>
        <el-input
          v-if="inputVisible"
          ref="InputRef"
          v-model="inputValue"
          class="button-new-input"
          size="small"
          @keyup.enter="addTag(inputValue)"
          @blur="addTag(inputValue)"
        />
        <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
          + Tag
        </el-button>
      </el-form-item>
      <el-form-item label="发布时间">
        <el-date-picker v-model="essayData.createTime"></el-date-picker>
      </el-form-item>
    </el-form>

    <div class="essay-editor-footer">
      <el-button type="success" @click="save()">保存</el-button>
      <el-button type="primary" @click="submit()" v-permission="['operate']">发布</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { saveEssay, updateEssay, getEssayById } from '@/api/essay.ts';
import useUserStore from '@/store/modules/user';
import { ElNotification } from 'element-plus';
import upload from '@/components/upload/index.vue';
import { formatDate } from '@/utils/date.ts';
const router = useRouter();
const formEl = ref(null) as any;
const userStore = useUserStore();
const commonUseTags = ref([
  { tagName: '游戏', tagType: 'primary', effect: 'dark' },
  { tagName: '吐槽', tagType: 'success', effect: 'dark' },
  { tagName: '分享', tagType: 'info', effect: 'dark' },
  { tagName: '生活', tagType: 'warning', effect: 'dark' },
  { tagName: '音乐', tagType: 'danger', effect: 'dark' },
  { tagName: '电影', tagType: 'primary', effect: 'dark' },
  { tagName: '开发', tagType: 'success', effect: 'dark' }
]) as any;
const rules = ref({
  content: [{ required: true, message: '请输入随笔内容', trigger: 'blur' }]
}) as any;
const essayData = ref({
  name: '',
  content: '',
  imageRelations: [],
  tags: [] as any,
  createTime: new Date()
}) as any;
const inputValue = ref('');
const inputVisible = ref(false);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const InputRef = ref() as any;
function showInput() {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus();
  });
}

function resetEssay() {
  essayData.value = {
    name: '',
    content: '',
    imageRelations: [],
    tags: [],
    createTime: new Date()
  };
}

//添加标签
function addTag(tag: any) {
  inputVisible.value = true;
  const { tags } = essayData.value;
  let tagName = tag.tagName || inputValue.value;
  if (tagName) {
    let tempTag = tags.filter((x: any) => x.tagName == tagName);
    const typeArr = ['primary', 'success', 'info', 'warning', 'danger'];
    const tagType = tag.tagType || typeArr[Math.floor(Math.random() * typeArr.length)];
    if (tempTag.length == 0) {
      essayData.value.tags.push({ tagName, tagType, effect: 'dark' });
      console.log(essayData.value);
    } else {
      ElNotification.warning({
        message: '已经选过了 ╮(╯▽╰)╭',
        offset: 100
      });
    }
  }
  inputVisible.value = false;
  inputValue.value = '';
}
//删除标签
function tagDel(tag: string) {
  const { tags } = essayData.value;
  tags.splice(tags.indexOf(tag), 1);
}

async function submit() {
  let form = JSON.parse(JSON.stringify(essayData.value));
  form.createTime = formatDate(new Date(form.createTime));
  form.tags = JSON.stringify(essayData.value.tags);
  if (!form.content) {
    ElNotification.warning('别发个空随笔');
  }
  await formEl.value.validate(async (valid: any) => {
    if (valid) {
      const { code, rows, total } = form.blogId ? await updateEssay(form) : await saveEssay(form);
      if (code === 200) {
        resetEssay();
        ElNotification.success({
          title: '随笔发布成功',
          message: '点击前往随笔',
          offset: 100,
          onClick: () => {
            router.push('essay');
          }
        });
      }
    }
  });
}

async function getEssayData(id: any) {
  const { code, data } = await getEssayById(id);
  if (code === 200) {
    Object.assign(essayData.value, data);
    essayData.value.imageRelations = [];
    if (data.images && data.images.length)
      data.images.forEach((img: any) => {
        let spArr = img.split('/');
        essayData.value.imageRelations.push({ name: spArr[spArr.length - 1], url: img, id: null });
      });
    if (data.tags) essayData.value.tags = JSON.parse(data.tags);
    console.log(essayData.value);
  }
}

//保存随笔到本地
function save() {
  window.localStorage.setItem('essayData', JSON.stringify(essayData.value));
  ElNotification.success({
    title: '保存成功',
    offset: 100
  });
}

onMounted(() => {
  essayData.value.createTime = new Date();
  const { id } = router.currentRoute.value.query;
  let tempEssayData = window.localStorage.getItem('essayData');
  if (id) {
    getEssayData(id);
  } else if (tempEssayData) {
    essayData.value = JSON.parse(tempEssayData);
  }
});
</script>
<style lang="scss" scoped>
@include theme() {
  .essay-manage-editor {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 6px;
    background: get('back') !important;
    color: get('font-color');
    flex-direction: column;
    overflow: auto;
    overflow-x: hidden;
    padding: 15px;
    margin: 15px;
    width: calc(100%);
  }
}

.essay-form {
  width: 100%;
  height: calc(100% - 40px);
}
:deep(.el-form-item .el-form-item__label) {
  font-size: 0.9rem;
}
:deep(.el-textarea__inner) {
  background-image: url('@/assets/png/pikachu-sit.png') !important;
  background-repeat: no-repeat !important;
  object-fit: cover !important;
  background-size: 7% auto !important;
  background-position: 99.5% 96% !important;
}
.essay-form {
}
.essay-editor-footer {
  width: calc(100% - 40px);
  display: flex;
  margin: 20px;
  justify-content: center;
  .el-button:nth-child(1) {
    margin-right: 10px;
    width: 100px;
    height: 35px;
    font-size: 16px;
  }

  .el-button:nth-child(2) {
    margin-left: 10px;
    width: 100px;
    height: 35px;
    font-size: 16px;
  }
}
.button-new-tag {
  //width: 60px !important;
  padding: 2px 12px;
  font-size: 0.75rem !important;
  height: 1.3rem;
}
.button-new-input {
  width: min-content !important;
  min-width: 65px !important;
  font-size: 0.7rem !important;
  height: 1.2rem !important;
  border-radius: 4px !important;
  :deep(.el-input__wrapper) {
    height: 100% !important;
    padding: 0 4px;
  }
}
.tag-item {
  cursor: pointer;
}
</style>
