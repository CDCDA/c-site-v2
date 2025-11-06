<!--
 * @Description: 博客发布弹窗
-->
<template>
  <c-dialog
    class="blog-release"
    v-model="dialogVisible"
    :title="$t('发布')"
    width="700px"
    :modal="true"
  >
    <el-form class="blog-release-settings" :model="blogData" label-width="80px">
      <el-form-item :label="$t('博客标签')">
        <el-tag
          v-for="tag in blogData.tags"
          :key="tag"
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
          class="ml-1 w-20 button-new-tag"
          size="small"
          @keyup.enter="addTag"
          @blur="addTag"
        />
        <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
          + Tag
        </el-button>
        <el-icon
          @click="tagVisible = true"
          style="font-size: 1rem; margin-left: 10px; cursor: pointer"
          ><Grid
        /></el-icon>
      </el-form-item>
      <el-form-item :label="$t('博客分类')">
        <el-tree-select
          v-model="blogData.typeId"
          :data="typeList"
          check-strictly
          @node-click="typeChange"
          :props="defaultProps"
          :render-after-expand="false"
          ><template #default="{ data: { typeName } }"> {{ typeName }}</template>
        </el-tree-select>
      </el-form-item>
      <el-form-item :label="$t('添加封面')">
        <upload v-model="blogData.coverUrl" path="blogCover"></upload>
      </el-form-item>
      <el-form-item :label="$t('博客摘要')">
        <el-input
          v-model="blogData.blogAbstract"
          type="textarea"
          :rows="2"
          :placeholder="$t('请输入...')"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t('取消') }}</el-button>
        <!-- <el-button @click="dialogVisible = false">{{ $t('保存为草稿') }}</el-button>
        <el-button @click="dialogVisible = false">{{ $t('定时发布') }}</el-button> -->
        <el-button @click="submit">{{ $t('发布') }}</el-button>
      </span>
    </template>
    <c-dialog
      class="tag-dialog"
      v-model="tagVisible"
      :title="$t('标签选择')"
      width="500px"
      :modal="true"
    >
      <div class="tag-list">
        <el-tag
          v-for="tag in tagList"
          :key="tag"
          class="tag-item"
          :class="tag.isActive ? 'is-active' : ''"
          :disable-transitions="false"
          style="margin-bottom: 10px; cursor: pointer"
          @click="chooseTag(tag)"
        >
          {{ tag.tagName }}
        </el-tag>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="tagVisible = false">{{ $t('取消') }}</el-button>
          <el-button @click="setTags()">{{ $t('确定') }}</el-button>
        </span>
      </template>
    </c-dialog>
  </c-dialog>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, nextTick, onMounted } from 'vue';
import { saveBlog, updateBlog } from '@/api/blog';
import upload from '@/components/upload/index.vue';
import type { UploadProps } from 'element-plus';
import { ElMessage, ElNotification, ElMessageBox } from 'element-plus';
import { pageTypes } from '@/api/type';
import { pageTags } from '@/api/tag';
import { useRouter } from 'vue-router';
import type { UploadFile } from 'element-plus';
const router = useRouter();
const tagVisible = ref(false);

const dialogVisible = ref(false);
const props = defineProps({
  blogData: null as any
});
const emit = defineEmits(['resetBlogData']);

const defaultProps = {
  children: 'children',
  label: 'typeName',
  value: 'typeId'
};
const dialogImageUrl = ref('');
const inputValue = ref('');
const inputVisible = ref(false);
const InputRef = ref() as any;
const typeList = ref([]);
const imageUrl = ref(null) as any;
const tagList = ref([]) as any;
function close() {
  dialogVisible.value = false;
}
function open() {
  dialogVisible.value = true;
}

//添加标签
function addTag() {
  const { tags } = props.blogData;
  if (inputValue.value) {
    tags.push({ tagName: inputValue.value });
  }
  inputVisible.value = false;
  inputValue.value = '';
}

//删除标签
function tagDel(tag: string) {
  const { tags } = props.blogData;
  tags.splice(tags.indexOf(tag), 1);
}

function showInput() {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus();
  });
}

async function submit() {
  const { code, data } = props.blogData.blogId
    ? await updateBlog(props.blogData)
    : await saveBlog(props.blogData);
  if (code === 200) {
    // 清空博客本地缓存
    window.localStorage.setItem('blogData', '');
    ElMessageBox.confirm($t('博客发布成功'), 'success', {
      distinguishCancelAndClose: true,
      confirmButtonText: $t('前往博客'),
      cancelButtonText: $t('写新博客')
    })
      .then(() => {
        router.push({ name: 'blogDisplay', query: { blogId: data } });
      })
      .catch(() => {
        emit('resetBlogData');
      });
    close();
  }
}

function typeChange(val: any) {
  val;
  // console.log(val);
}

async function getTypeList() {
  const { code, rows, total } = (await pageTypes({})) as any;
  if (code === 200) {
    typeList.value = rows;
  }
}

async function getTagList() {
  const { code, rows, total } = (await pageTags({})) as any;
  if (code === 200) {
    tagList.value = rows;
  }
}

const handleRemove = (file: UploadFile) => {
  console.log(file);
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!;
  dialogVisible.value = true;
};

const handleDownload = (file: UploadFile) => {
  console.log(file);
};

const handleAvatarSuccess: UploadProps['onSuccess'] = (response: any) => {
  imageUrl.value = response.data;
};

const beforeAvatarUpload: UploadProps['beforeUpload'] = rawFile => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!');
    return false;
  }
  return true;
};

function chooseTag(tag: any) {
  console.log('tag', tag);
  tag.isActive = !tag.isActive;
}

function setTags() {
  props.blogData.tags = [];
  tagList.value.forEach((tag: any) => {
    if (tag.isActive) {
      props.blogData.tags.push(tag);
    }
  });
  tagVisible.value = false;
}

onMounted(() => {
  getTypeList();
  getTagList();
});
defineExpose({
  close,
  open
});
</script>
<style lang="scss">
@include theme() {
  .blog-settings {
    width: calc(100% - 20px);
    text-align: left;
    margin-top: 5px;
    border-radius: 3px;
    padding: 10px;
    color: get('font-color');
    border: 1px solid get('font-color');
  }

  .tag-item.is-active {
    background: get('bk') !important;
    border: 1px solid transparent !important;
    span {
      color: get('re-font-color') !important;
    }
  }
  .el-tag__close:hover {
    path {
      fill: get('re-font-color');
    }
  }
}

.el-upload {
  // border: 1px dashed get('font-color');
  background: transparent;
  height: 100px;
  width: 100px;
}
.el-upload-list__item.is-ready {
  height: 100px;
  width: 100px;
}
.button-new-tag {
  width: 60px !important;
}
.tag-item {
  cursor: pointer;
}
</style>
