<template>
  <!-- <div v-if="isExternal" :style="styleExternalIcon" class="svg-external-icon svg-icon" v-on="$listeners"/> -->
  <svg aria-hidden="true" v-on="$listeners" class="svg-icon">
    <use :xlink:href="`#${this.iconName}`" />
  </svg>
</template>

<script>
// import {isExternal} from '@/utils/validate'

export default {
  name: 'SvgIcon',
  props: {
    iconName: {
      type: String,
      default: ''
    },
    className: {
      type: String,
      default: ''
    }
  },
  computed: {
    // isExternal() {
    //   return isExternal(this.iconClass);
    // },
    // iconName() {
    //   return `#${this.iconName}`;
    // },
    // svgClass() {
    //   if (this.className) {
    //     return 'svg-icon ' + this.className;
    //   } else {
    //     return 'svg-icon';
    //   }
    // },
    styleExternalIcon() {
      return {
        mask: `url(${this.iconClass}) no-repeat 50% 50%`,
        '-webkit-mask': `url(${this.iconClass}) no-repeat 50% 50%`
      };
    }
  },
  mounted() {}
};
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.svg-external-icon {
  background-color: currentColor;
  mask-size: cover !important;
  display: inline-block;
}
</style>
