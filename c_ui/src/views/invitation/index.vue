<template>
  <div class="invitation-container">
    <!-- 星空背景 -->
    <div class="starry-background">
      <img src="@/assets/images/star.webp" alt="星空背景" class="star-image" />
    </div>

    <!-- 主要内容 -->
    <div class="content-wrapper animated fade-in">
      <h1 class="title">检测到移动端访问</h1>
      <p class="description">
        由于本站点包含高负载看板交互与动画效果，为了不压榨您的手机 CPU，请移步 PC 端开启完整体验。
      </p>

      <!-- 功能按钮 -->
      <div class="button-container">
        <el-button
          type="primary"
          size="large"
          class="action-button github-button"
          @click="openGitHub"
        >
          <svg-icon icon-class="socialSvg-github" class="button-icon" />
          查看 GitHub 源码
        </el-button>

        <el-button
          type="success"
          size="large"
          class="action-button pdf-button"
          @click="downloadResume"
        >
          <svg-icon icon-class="commonSvg-文档" class="button-icon" />
          下载简历 PDF
        </el-button>

        <el-button type="info" size="large" class="action-button copy-button" @click="copyPCLink">
          <svg-icon icon-class="commonSvg-复制" class="button-icon" />
          一键复制 PC 链接
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';

// 打开 GitHub 源码
const openGitHub = () => {
  window.open('https://github.com/cccc1495/c-site-v2', '_blank');
};

// 下载简历 PDF
const downloadResume = () => {
  // 这里需要替换为实际的简历 PDF 链接
  const resumeUrl = 'https://example.com/resume.pdf';
  window.open(resumeUrl, '_blank');
};

// 一键复制 PC 链接
const copyPCLink = () => {
  const pcLink = window.location.origin + window.location.pathname;
  navigator.clipboard
    .writeText(pcLink)
    .then(() => {
      ElMessage.success('PC 链接已复制到剪贴板');
    })
    .catch(err => {
      ElMessage.error('复制失败，请手动复制链接');
      console.error('复制失败:', err);
    });
};
</script>

<style scoped lang="scss">
.invitation-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.starry-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;

  .star-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    filter: brightness(0.7);
  }
}

.content-wrapper {
  background: rgba(0, 0, 0, 0.7);
  border-radius: 16px;
  padding: 2rem;
  max-width: 90%;
  width: 500px;
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.title {
  color: #ffffff;
  font-size: 24px;
  margin-bottom: 20px;
  font-weight: 600;
}

.description {
  color: #e0e0e0;
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 2rem;
}

.button-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 20px;

  .action-button {
    width: 100%;
    padding: 15px;
    font-size: 16px;
    border-radius: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    }

    .button-icon {
      margin-right: 10px;
      font-size: 18px;
    }
  }

  .github-button {
    background: #333333;
    border-color: #333333;

    &:hover {
      background: #444444;
      border-color: #444444;
    }
  }

  .pdf-button {
    background: #28a745;
    border-color: #28a745;

    &:hover {
      background: #218838;
      border-color: #1e7e34;
    }
  }

  .copy-button {
    background: #17a2b8;
    border-color: #17a2b8;

    &:hover {
      background: #138496;
      border-color: #117a8b;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .content-wrapper {
    padding: 30px;
    width: calc(90% - 60px);
  }

  .title {
    font-size: 20px;
  }

  .description {
    font-size: 14px;
  }

  .action-button {
    padding: 12px;
    font-size: 14px;
    margin: 0;
  }
}
</style>
