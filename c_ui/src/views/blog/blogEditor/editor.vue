<template>
  <div class="blog-editor">
    <el-form :model="blogData" :rules="rules" ref="formEl">
      <el-form-item label="标题" prop="blogTitle" label-width="70px">
        <el-input class="blog-title-editor" placeholder="请输入标题" v-model="blogData.blogTitle" />
      </el-form-item>
      <MdEditor
        ref="editorRef"
        v-model="blogData.content"
        @onSave="blogSave"
        @onUploadImg="onUploadImg"
      />
      <el-form-item label="博客标签">
        <el-tag
          v-for="tag in blogData.tags"
          :key="tag"
          class="tag-item"
          :type="tag.tagType"
          :effect="tag.effect"
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
          @keyup.enter="addTag"
          @blur="addTag"
        />
        <el-button v-else class="button-new-tag" size="small" @click="showInput"> + Tag </el-button>
        <el-icon
          @click="tagVisible = true"
          style="font-size: 1rem; margin-left: 10px; cursor: pointer"
          ><Grid
        /></el-icon>
      </el-form-item>
      <el-form-item label="博客分类" prop="typeId">
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
      <el-form-item label="博客类型" prop="publishStatus">
        <el-switch
          v-model="blogData.isOriginal"
          active-value="1"
          inactive-value="0"
          active-text="原创"
          inactive-text="转载"
        />
      </el-form-item>
      <el-form-item label="是否推荐" prop="isRecommend">
        <el-switch
          v-model="blogData.isRecommend"
          active-value="1"
          inactive-value="0"
          active-text="推荐"
          inactive-text="不推荐"
        />
      </el-form-item>
      <el-form-item label="添加封面">
        <upload v-model="blogData.coverUrl" path="blogCover"></upload>
      </el-form-item>
      <el-form-item label="博客摘要">
        <el-input
          v-model="blogData.blogAbstract"
          type="textarea"
          :rows="2"
          placeholder="请输入..."
        ></el-input>
      </el-form-item>
    </el-form>
    <div class="blog-editor-header">
      <el-button type="success" @click="blogSave" class="bt-save">保存</el-button>
      <el-button type="primary" @click="submit" class="bt-release">发布</el-button>
    </div>
  </div>
  <c-dialog class="tag-dialog" v-model="tagVisible" title="标签选择" width="500px" :modal="true">
    <div class="tag-list">
      <el-tag
        v-for="tag in tagList"
        :key="tag"
        class="tag-item"
        :type="tag.tagType"
        :effect="tag.effect"
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
        <el-button type="danger" @click="tagVisible = false">取消</el-button>
        <el-button type="primary" @click="setTags()" v-permission="['operate']">确定</el-button>
      </span>
    </template>
  </c-dialog>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted, watch } from 'vue';
import { pageTypes } from '@/api/type';
import { pageTags } from '@/api/tag';
import { loadingService } from '@/components/loading/loading.ts';
import { saveBlog, updateBlog } from '@/api/blog';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import { ElMessageBox, ElNotification } from 'element-plus';
import useUserStore from '@/store/modules/user';
import { useRouter } from 'vue-router';
import { getBlogById } from '@/api/blog';
import upload from '@/components/upload/index.vue';
const tagVisible = ref(false);

const rules = ref({
  blogTitle: [{ required: true, message: '请输入博客标题', trigger: 'blur' }],
  typeId: [{ required: true, message: '请选择博客分类', trigger: 'blur' }]
});
const formEl = ref(null) as any;
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

//添加标签
function addTag(tag: any) {
  inputVisible.value = true;
  const { tags } = blogData.value;
  let tagName = tag.tagName || inputValue.value;
  if (tagName) {
    let tempTag = tags.filter((x: any) => x.tagName == tagName);
    const typeArr = ['primary', 'success', 'info', 'warning', 'danger'];
    const tagType = tag.tagType || typeArr[Math.floor(Math.random() * typeArr.length)];
    if (tempTag.length == 0) {
      blogData.value.tags.push({ tagName, tagType, effect: 'dark' });
      console.log(blogData.value);
    } else {
      ElNotification.warning({
        message: '已经选过了 ╮(╯▽╰)╭',
        offset: 100
      });
    }
  }
  // inputVisible.value = false;
  inputValue.value = '';
}

//删除标签
function tagDel(tag: string) {
  const { tags } = blogData.value;
  tags.splice(tags.indexOf(tag), 1);
}

function showInput() {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus();
  });
}

async function submit() {
  formEl.value.validate(async (valid: any) => {
    if (valid) {
      const { code, data } = blogData.value.blogId
        ? await updateBlog(blogData.value)
        : await saveBlog(blogData.value);
      if (code === 200) {
        //清空博客本地缓存
        window.localStorage.setItem('blogData', '');
        ElMessageBox.confirm('博客发布成功', 'success', {
          distinguishCancelAndClose: true,
          confirmButtonText: '前往博客',
          cancelButtonText: '写新博客',
          appendTo: '.messageBox-base'
        })
          .then(async () => {
            loadingService.show({ type: 'loading', text: '正在前往博客...' });
            await router.push({
              name: 'blogDisplay',
              query: { blogId: data || blogData.value.blogId }
            });
            loadingService.hide();
          })
          .catch(() => {
            resetBlogData();
          });
      }
    }
  });
}

function typeChange(val: any) {
  val;
  // console.log(val);
}

async function getTypeTree() {
  const params = {
    userId: blogData.value.userId
  };
  const { code, rows, total } = (await pageTypes(params)) as any;
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

function chooseTag(tag: any) {
  // console.log('tag', tag);
  tag.isActive = !tag.isActive;
}

function setTags() {
  blogData.value.tags = [];
  tagList.value.forEach((tag: any) => {
    if (tag.isActive) {
      blogData.value.tags.push(tag);
    }
  });
  tagVisible.value = false;
}

// import App from '@/App.vue';

//绑定值          modelValue:''
//主题            theme:'light' | 'dark'
//类名            class:''
//语言            language:'zh-CN','en-US'
//编辑器唯一标识   editorId:'md-editor-v3'
//是否显示行号     showCodeRowNumber:false | true
//预览内容主题     previewTheme: 'default' | 'github' | 'vuepress' | 'mk-cute' | 'smart-blue' | 'cyanosis'
//编辑器内联样式   style:''
//图表展示         noMermaid:false | true
//数学公式         noKatex :false | true
//代码块高亮样式   codeTheme:'atom'|'a11y'|'github'|'gradient'|'kimbie'|'paraiso'|'qtcreator'|'stackoverflow'
//编辑器内联样式   style:''
//图表展示         noMermaid:false | true
import axios from 'axios';

const router = useRouter() as any;
const userStore = useUserStore();
const loading = ref('rotate') as any;
const fullStatus = ref(false) as any;
const editorRef = ref(null) as any;
const editorOptions = ref({
  codeTheme: 'stackoverflow',
  theme: 'dark',
  previewTheme: 'default'
});
async function onUploadImg(files: any, callback: any) {
  const res = await Promise.all(
    files.map((file: any) => {
      return new Promise((rev, rej) => {
        const form = new FormData();
        form.append('file', file);
        form.append('path', 'blogMaterial');
        axios
          .post(
            ` ${process.env.NODE_ENV === 'development' ? '/dev-api' : '/prod-api'}/files`,
            form,
            {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }
          )
          .then(res => rev(res))
          .catch(error => rej(error));
      });
    })
  );
  console.log(res);
  callback(res.map(item => item.data.data));
}

const blogData = ref({
  tags: [],
  userId: userStore.userId,
  content: '',
  typeId: '',
  coverUrl: '',
  blogAbstract: '',
  isRecommend: '0',
  isOriginal: '1'
}) as any;

function resetBlogData() {
  blogData.value = ref({
    tags: [],
    userId: userStore.userId,
    content: '',
    typeId: '',
    coverUrl: '',
    blogAbstract: '',
    isRecommend: '0',
    isOriginal: '1'
  }) as any;
}

//保存博客数据到本地
function blogSave() {
  window.localStorage.setItem('blogData', JSON.stringify(blogData.value));
  ElNotification.success('保存成功');
}

async function getBlogInfo(id: string) {
  if (!id) return;
  const { code, data } = (await getBlogById(id)) as any;
  if (code === 200) {
    blogData.value = data;
  }
}

watch(
  () => router,
  val => {
    const { blogId } = val.currentRoute.value.query;
    if (blogId) {
      getBlogInfo(blogId);
    }
  },
  { deep: true, immediate: true }
);

onMounted(() => {
  getTypeTree();
  getTagList();
  let { blogId } = router.currentRoute.value.query;
  let tempBlogData = window.localStorage.getItem('blogData');
  if (blogId) {
    getBlogInfo(blogId);
  } else if (tempBlogData) {
    blogData.value = JSON.parse(tempBlogData);
  }

  editorRef.value?.on('pageFullscreen', (status: any) => {
    fullStatus.value = status;
    if (status === true) {
      (document.querySelector('.mavon-editor-container') as any).classList.add('full-screen');
    } else {
      (document.querySelector('.mavon-editor-container') as any).classList.remove('full-screen');
    }
  });
});
</script>
<style lang="scss">
@include theme() {
  .blog-editor {
    border-radius: 6px;
    background: get('back') !important;
    color: get('font-color');
    display: flex;
    flex-direction: column;
    overflow: auto;
    overflow-x: hidden;
    padding: 15px;
    margin: 15px;
    min-height: calc(100% - 60px);
    .md-editor__toc-nav-title {
      color: get('font-color');
    }

    .md-editor--fullscreen {
      background: get('back');
      box-shadow: get('box-shadow');
      backdrop-filter: blur(20px);
      border-radius: 0 !important;
    }

    #md-editor-v3 {
      border-radius: 5px;
      overflow: hidden;
    }

    .md-editor {
      height: calc(100vh - 3.7rem - 45px);
      border-radius: 5px;
      overflow: hidden;
      box-shadow: get('box-shadow');
      margin-bottom: 20px;
    }

    .cm-content.cm-lineWrapping,
    .md-editor-preview-wrapper {
      text-align: left;
    }

    .ml-1 {
      width: 100px;
    }
    .content-input-wrapper,
    .content-input,
    .auto-textarea-input,
    .v-show-content {
      background: transparent !important;
      text-align: left;
    }

    .blog-editor-footer {
      margin-top: 15px;
    }

    .blog-editor-header {
      display: flex;
      margin: 30px;
      justify-content: center;
      .bt-save {
        margin-right: 10px;
        width: 100px;
        height: 35px;
        font-size: 16px;
      }

      .bt-release {
        margin-left: 10px;
        width: 100px;
        height: 35px;
        font-size: 16px;
      }
    }
  }
  .tag-item.is-active {
    background: get('border-color') !important;
    border: 1px solid transparent !important;
    :deep(.el-tag__content) {
      color: get('re-font-color') !important;
    }
  }
}

.button-new-tag {
  padding: 2px 12px;
  font-size: 0.75rem !important;
  height: 1.3rem;
}
.tag-item {
  cursor: pointer;
}

.button-new-input {
  width: min-content !important;
  min-width: 65px !important;
  font-size: 0.7rem !important;
  height: 1.2rem !important;
  border-radius: 4px !important;
  border: 1px solid #dcdcdc !important;
  .el-input__wrapper {
    height: 100% !important;
    border: none;
    padding: 0 4px;
  }
}
</style>
