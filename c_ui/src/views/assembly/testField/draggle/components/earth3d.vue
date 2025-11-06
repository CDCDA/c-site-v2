<!--
 * @Description: 3d地球
-->
<template>
  <baseChart class="earth-3d" :options="options"></baseChart>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, watch } from 'vue';
import baseChart from '@/components/charts/baseChart.vue';

const options = ref({}) as any;
const tempOptions = {
  backgroundColor: 'transparent',
  globe: {
    baseTexture: '/earth3d/world.topo.bathy.200401.jpg',
    heightTexture: '/earth3d/world.topo.bathy.200401.jpg',
    displacementScale: 0.04,
    shading: 'realistic',
    environment: '',
    realisticMaterial: {
      roughness: 0.9
    },
    postEffect: {
      enable: true
    },
    light: {
      main: {
        intensity: 5,
        shadow: true
      },
      ambientCubemap: {
        texture: '/earth3d/pisa.hdr',
        diffuseIntensity: 0.2
      }
    }
  }
};

onMounted(() => {
  options.value = tempOptions;
});
</script>
<style lang="scss" scoped>
@include theme() {
  .earth-3d {
    @include full();
    box-shadow: get('box-shadow');
    background: get('back');
    border-radius: 8px;
  }
}
</style>
