<!--
 * @Description: 架构设计
-->
<template>
  <div class="page-main architecture-page">
    <div class="glass-container">
      <!-- 头部：增加渐变文字和动态副标题 -->
      <div class="page-header">
        <h1 class="gradient-text">{{ $t('项目架构') }}</h1>
        <p class="typing-text">
          {{ $t('一个全栈个人网站，包含博客、管理后台、组件实验室等模块') }}
        </p>
      </div>

      <!-- 技术栈：图标化展示 -->
      <section class="section tech-section">
        <h2 class="section-title"><svg-icon name="实验" /> {{ $t('技术栈') }}</h2>
        <div class="tech-card-wrapper">
          <div v-for="(group, title) in techGroups" :key="title" class="tech-card">
            <div class="card-header">{{ $t(title) }}</div>
            <div class="tech-icons">
              <div v-for="tech in group" :key="tech.name" class="tech-item" :title="tech.name">
                <svg-icon :name="tech.icon" class="tech-icon-svg" />
                <span>{{ tech.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 核心架构：左右对比布局 -->
      <el-row :gutter="30" class="structure-row">
        <el-col :md="12" :sm="24">
          <section class="section">
            <h2 class="section-title"><svg-icon name="分类" /> {{ $t('前端架构') }}</h2>
            <color-border>
              <div class="inner-content">
                <el-tree :data="frontendTree" :props="defaultProps" default-expand-all>
                  <template #default="{ node, data }">
                    <span class="custom-tree-node">
                      <svg-icon :name="data.children ? '文件夹' : '随笔'" />
                      <span>{{ node.label }}</span>
                    </span>
                  </template>
                </el-tree>
              </div>
            </color-border>
          </section>
        </el-col>

        <el-col :md="12" :sm="24">
          <section class="section">
            <h2 class="section-title"><svg-icon name="设置" /> {{ $t('后端架构') }}</h2>
            <div class="backend-info">
              <div class="module-group" v-for="m in backendModules" :key="m.title">
                <h4 class="primary-color">{{ m.title }}</h4>
                <div class="module-tags">
                  <span v-for="tag in m.tags" :key="tag" class="mini-tag">{{ tag }}</span>
                </div>
              </div>
            </div>
          </section>
        </el-col>
      </el-row>

      <!-- 数据流：动效连接线 -->
      <section class="section">
        <h2 class="section-title"><svg-icon name="切换" /> {{ $t('数据流示意') }}</h2>
        <div class="flow-container">
          <div v-for="(step, index) in flowSteps" :key="index" class="flow-step-wrapper">
            <div class="flow-node">
              <div class="node-icon"><svg-icon :name="step.icon" /></div>
              <div class="node-name">{{ $t(step.name) }}</div>
              <div class="node-desc">{{ step.sub }}</div>
            </div>
            <div v-if="index < flowSteps.length - 1" class="flow-line">
              <div class="line-active"></div>
            </div>
          </div>
        </div>
      </section>

      <!-- 特性：卡片悬浮效果 -->
      <section class="section">
        <h2 class="section-title"><svg-icon name="礼花" /> {{ $t('关键特性') }}</h2>
        <div class="feature-grid">
          <div class="feature-item" v-for="feature in features" :key="feature.title">
            <div class="feature-icon-box">
              <svg-icon :name="feature.icon" />
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.desc }}</p>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import ColorBorder from '@/components/colorBorder.vue';

const frontendTech = ['Vue 3', 'TypeScript', 'Vite', 'Pinia', 'Element Plus', 'Swiper', 'Uni-app'];
const backendTech = [
  'Java 21',
  'Spring Boot 3',
  'MyBatis-Plus',
  'JWT',
  'WebSocket',
  'Spring Security'
];
const dbTech = ['PostgreSQL', 'Redis', 'Docker'];

const frontendTree = [
  {
    label: 'src',
    children: [
      { label: 'api' },
      { label: 'assets' },
      { label: 'components' },
      { label: 'router' },
      { label: 'store' },
      { label: 'utils' },
      { label: 'views' }
    ]
  }
];

const defaultProps = {
  children: 'children',
  label: 'label'
};

const techGroups = {
  前端: [
    { name: 'Vue 3', icon: 'vue' },
    { name: 'TS', icon: 'ts' },
    { name: 'Vite', icon: 'vite' },
    { name: 'Pinia', icon: 'pinia' }, // 如果没有pinia图标可以用js
    { name: 'Uni-app', icon: 'uniapp' }
  ],
  后端: [
    { name: 'Java 21', icon: 'java' },
    { name: 'Spring Boot', icon: 'springboot' },
    { name: 'Postgres', icon: 'postgres' },
    { name: 'Redis', icon: 'redis' },
    { name: 'Docker', icon: 'docker' }
  ]
};

const flowSteps = [
  { name: '客户端', sub: 'Vue3 / App', icon: '手机' },
  { name: '网关层', sub: 'Nginx / SSL', icon: '分析' },
  { name: '业务层', sub: 'Spring Boot', icon: '控制台' },
  { name: '持久层', sub: 'PostgreSQL', icon: '字典' }
];

const features = [
  { title: $t('响应式设计'), desc: $t('适配PC、平板、手机等多种设备'), icon: '全屏' },
  { title: $t('暗色主题'), desc: $t('支持明暗主题切换，适配系统偏好'), icon: '皮肤1' },
  { title: $t('RBAC权限'), desc: $t('基于角色的访问控制，精细化权限管理'), icon: '个人中心' },
  { title: $t('实时通信'), desc: $t('WebSocket支持即时消息和在线状态'), icon: '铃铛' },
  { title: $t('双语支持'), desc: $t('基于 i18n 的中英文实时切换'), icon: '语言切换' },
  { title: $t('自动化流'), desc: $t('集成 Node-RED 可视化自动化逻辑'), icon: '风车' }
];
</script>

<style lang="scss" scoped>
.architecture-page {
  @include theme();
  min-height: 100vh;
  padding: 40px 20px;

  .glass-container {
    max-width: 1200px;
    margin: 0 auto;
    background: get('back-90'); // 使用带透明度的背景
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }

  .gradient-text {
    background: linear-gradient(120deg, get('primary'), #64f38c);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-weight: 800;
  }

  .tech-card-wrapper {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;

    .tech-card {
      background: get('back');
      border-radius: 12px;
      padding: 15px;
      border: 1px solid get('border-color');

      .tech-icons {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        margin-top: 15px;
      }

      .tech-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 60px;
        transition: transform 0.3s;
        &:hover {
          transform: translateY(-5px);
        }

        .tech-icon-svg {
          width: 32px;
          height: 32px;
          margin-bottom: 5px;
        }
        span {
          font-size: 12px;
          color: get('re-font-color');
        }
      }
    }
  }

  .flow-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 40px 0;

    .flow-node {
      text-align: center;
      z-index: 2;
      .node-icon {
        width: 60px;
        height: 60px;
        background: get('primary');
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 10px;
        box-shadow: 0 0 20px get('primary');
        svg {
          width: 30px;
          height: 30px;
          color: #fff;
        }
      }
      .node-name {
        font-weight: bold;
        color: get('re-font-color');
      }
      .node-desc {
        font-size: 12px;
        opacity: 0.6;
      }
    }

    .flow-line {
      flex: 1;
      height: 2px;
      background: get('border-color');
      margin: 0 -20px;
      position: relative;
      overflow: hidden;

      .line-active {
        position: absolute;
        width: 40px;
        height: 100%;
        background: linear-gradient(90deg, transparent, get('primary'), transparent);
        animation: flowMove 2s infinite linear;
      }
    }
  }

  @keyframes flowMove {
    0% {
      left: -40px;
    }
    100% {
      left: 100%;
    }
  }

  .feature-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;

    .feature-item {
      padding: 20px;
      border-radius: 15px;
      background: rgba(255, 255, 255, 0.05);
      transition: all 0.3s;
      border: 1px solid transparent;
      &:hover {
        background: rgba(255, 255, 255, 0.1);
        border-color: get('primary');
        transform: translateY(-5px);
      }
      .feature-icon-box {
        font-size: 24px;
        margin-bottom: 10px;
        color: get('primary');
      }
    }
  }
}

// 适配移动端
@media (max-width: 768px) {
  .flow-container {
    flex-direction: column;
    gap: 30px;
    .flow-line {
      display: none;
    }
  }
}
</style>
