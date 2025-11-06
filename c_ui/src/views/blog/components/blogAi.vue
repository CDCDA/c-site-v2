<!--ai摘要-->
<template>
  <div class="blog-ai-wrap">
    <div class="blog-ai-header">
      <div class="blog-ai-header-left">
        <svg-icon iconName="commonSvg-AI" style="font-size: 1.2rem" />
        <span class="blog-ai-icon">AI-摘要</span>
        <svg-icon iconName="commonSvg-刷新" style="cursor: pointer" @click="getAbstract" />
      </div>
      <div class="blog-ai-header-right">
        <div class="loading-dots" v-if="loading">{{ $t('总结中') }}</div>
      </div>
    </div>
    <div class="blog-ai-abstract" v-cLoading="loading">
      <md-preview :editorId="'preview-only'" :modelValue="abstract" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import SvgIcon from '@/components/icon-component.vue';
import { sendMessage } from '@/api/chat.ts';
import { ElMessage, ElNotification } from 'element-plus';
import { onMounted, ref } from 'vue';
import { MdPreview } from 'md-editor-v3';
const props = defineProps({
  blogContent: {
    type: String,
    default: null
  }
});
const abstract = ref('') as any;
const loading = ref(false);
//获取摘要
async function getAbstract() {
  if (!props.blogContent) return;
  let messages = [
    {
      role: 'user',
      content: `${$t('用一段尽量简短的话概括一下博客内容： ')}${props.blogContent}`
    }
  ];
  loading.value = true;
  try {
    abstract.value = '';
    const response = (await sendMessage(messages)) as any;
    if (response.status !== 200) {
      ElNotification.error('API returned an error');
    }
    if (!response.body) return;
    //接收数据流
    const decoder = new TextDecoder('utf-8');
    const reader = response.body.getReader();
    let index = 0;
    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        break;
      }
      const chunk = decoder.decode(value);
      const lines = chunk.split('data: ').slice(1); // 分组并处理掉返回中的data:
      const parsedLines = lines
        .map(line => line.trim()) // 去空格
        .filter(line => line !== ''); // 去掉空行
      for (const parsedLine of parsedLines) {
        index++;
        //循环返回的字段
        if (parsedLine.includes('choices')) {
          // 返回数据为合理数据判断
          const { choices } = JSON.parse(parsedLine);
          const { delta } = choices[0];
          const { content } = delta;
          if (content && index > 4) {
            // 文本 处理
            abstract.value += content;
          }
        }
      }
    }
    if (!abstract.value) {
      abstract.value = 'api请求失败,请联系管理员';
    }
    loading.value = false;
  } catch (error) {
    ElMessage.error($t('获取博客摘要失败，请重试'));
    loading.value = false;
  }
}

onMounted(() => {
  setTimeout(() => {
    getAbstract();
  }, 2000);
});
</script>
<style scoped lang="scss">
.blog-ai-wrap {
  border: 1px solid #d4d4d4;
  margin: 25px 25px 0 25px;
  border-radius: 12px;
  overflow: hidden;
  background: #f7f7f9;
  .blog-ai-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 20px 10px 20px;
    font-size: 0.8rem;
    .blog-ai-header-left {
      display: flex;
      align-items: center;
    }
    .blog-ai-icon {
      margin-right: 10px;
      margin-left: 5px;
    }
  }
  .blog-ai-abstract {
    border: 1px solid #d4d4d4;
    margin: 5px 15px 15px 15px;
    border-radius: 8px;
    overflow: hidden;
    background: white;
    min-height: 3rem;
  }
  .loading-dots {
    display: inline-block;
    color: #6b7280;
    &::after {
      content: '';
      animation: dots 1.4s infinite;
    }
  }
  @keyframes dots {
    0%,
    20% {
      content: '.';
    }
    40% {
      content: '..';
    }
    60%,
    100% {
      content: '...';
    }
  }
}
</style>
