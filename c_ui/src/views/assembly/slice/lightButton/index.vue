<!--
 * @Description:
-->
<template>
  <div class="light-button-wrap">
    <cornerButton class="button" />
    <lightButton class="button" />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import cornerButton from '@/components/buttons/cornerButton.vue';
import lightButton from '@/components/buttons/lightButton.vue';
</script>

<style>
.light-button-wrap {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  .button {
    margin: 10px;
  }
}
</style>
