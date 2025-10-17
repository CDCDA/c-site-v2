<!--
 * @Description: 评论组件
-->
<template>
  <div class="comment">
    <div class="comment-title"><svg-icon iconName="commonSvg-评论1"></svg-icon>评论</div>
    <commentEditor :type="props.type" @getList="getList" :relevanceId="props.relevanceId" />
    <div v-if="commentList.length != 0" v-cLoading="loading">
      <div class="comment-header">
        <span class="comment-count">{{ total }}条评论</span>
        <svg-icon iconName="commonSvg-刷新" @click="getList"></svg-icon>
      </div>
      <!-- <div class="c-divider" style="margin: 20px 20px -10px 20px"></div> -->
      <div class="comment-list">
        <div v-for="(item, i) in commentList">
          <commentItem :commentData="item" @getList="getList" :data="props.data" />
          <div
            v-if="i != commentList.length - 1"
            class="c-divider"
            style="margin: 15px 20px 0 20px"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import commentEditor from './components/commentEditor.vue';
import commentItem from './components/commentItem.vue';
import { ref, onMounted, watch } from 'vue';
import { listTreeComments } from '@/api/comment';
const props = defineProps(['relevanceId', 'type', 'data']);
const commentList = ref([]);
const loading = ref(false);
const total = ref(0);
const queryParams = ref({
  type: props.type,
  relevanceId: props.relevanceId
});

async function getList() {
  loading.value = true;
  const { code, rows, total: totalCount } = await listTreeComments(queryParams.value);
  if (code === 200) {
    commentList.value = rows;
    total.value = totalCount;
  }
  loading.value = false;
}

watch(
  () => props.relevanceId,
  (val: any) => {
    if (!val) return;
    queryParams.value.relevanceId = val;
    getList();
  },
  { deep: true }
);
</script>
<style lang="scss" scoped>
@include theme() {
  .comment {
    border-radius: 12px;
    // background: get('back');
    // padding: 20px;
    padding-bottom: 20px;
    width: 100%;
  }
  .comment-title {
    padding: 0 22px;
    font-weight: bold;
    font-size: 22px;
    display: flex;
    width: 100%;
    justify-content: start;
    align-items: center;
    .svg-icon {
      height: 25px;
      width: 25px;
      margin-right: 10px;
    }
    // margin-bottom: 10px;
  }
  .comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: get('back');
    padding: 15px 22px 0 22px;
    .comment-count {
      font-size: 1rem;
      font-weight: bold;
    }
    .svg-icon {
      width: 20px;
      height: 20px;
      cursor: pointer;
    }
    .svg-icon:active {
      transform: translateY(2px);
    }
  }
  .comment-list {
    .comment-item {
    }
  }
}
</style>
