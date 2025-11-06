<!--
 * @Description: 自定义弹窗
-->
<template>
  <el-dialog
    class="c-dialog"
    :id="id"
    :draggable="draggable"
    :class="[isLessen ? 'is-lessen' : '', isFull ? 'is-full' : '', dialogType]"
    v-model="props.modelValue"
    :title="props.title"
    :width="props.width"
    :modal="props.modal"
    :fullscreen="isFull"
    destroy-on-close
    :show-close="false"
    append-to=".dialog-base"
    @close="close"
  >
    <template #header>
      <div class="c-dialog-header" @click="restore" @dblclick="lessen">
        <div class="c-dialog-title">{{ props.title }}</div>
        <div class="c-dialog-bt-group">
          <svg-icon v-if="!isLessen" iconName="commonSvg-缩小" @click="lessen" />
          <svg-icon iconName="commonSvg-刷新" @click="changeDialogType" />
          <svg-icon iconName="commonSvg-缩小窗口" @click="blowUp" v-if="isFull" />
          <svg-icon iconName="commonSvg-放大窗口" @click="blowUp" v-else />
          <svg-icon iconName="commonSvg-关闭" @click="close" />
        </div>
      </div>
    </template>
    <slot></slot>
    <slot name="footer"></slot>
  </el-dialog>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted, watch } from 'vue';
import { autoClearTimer } from '@/utils/timer';
import { generateRandomId } from '@/utils/common';
import useThemeStore from '@/store/modules/theme.ts';
const themeStore = useThemeStore();
const id = generateRandomId();
const props = defineProps({
  options: {
    default: {
      isDefaultFull: false
    }
  },
  title: {
    default: ''
  },
  width: {
    default: ''
  },
  modelValue: {
    default: false
  },
  drag: {
    default: false
  },
  isFull: {
    default: false
  },
  modal: {
    default: true
  },
  draggable: {
    default: true
  },
  type: {
    default: null
  }
});
const isFull = ref(false);
const draggable = ref(false);
const isLessen = ref(false);
const emit = defineEmits(['update:modelValue', 'close']);
const dialogType = ref('');

function changeDialogType() {
  if (dialogType.value == 'normal') {
    dialogType.value = 'filter';
  } else {
    dialogType.value = 'normal';
  }
}
function close() {
  emit('close');
  emit('update:modelValue', false);
}
function open() {
  emit('update:modelValue', true);
}

var restore = () => {
  return;
};

let tempDraggle = ref(false);
let tempOverlay = {} as any;
let tempOverlayDialog = {} as any;

function lessen() {
  isLessen.value = !isLessen.value;
  let dialog = document.getElementById(id) as any;
  let overlayDialog = dialog.parentNode;
  let overlay = overlayDialog.parentNode;
  if (isLessen.value) {
    tempOverlayDialog = {
      width: overlayDialog.style.width,
      height: overlayDialog.style.height,
      top: overlayDialog.style.top
    };
    tempOverlay = {
      width: overlay.style.width,
      height: overlay.style.height,
      bottom: overlay.style.bottom,
      backdropFilter: overlay.style.backdropFilter,
      background: overlay.style.background
    };
    overlay.style.width = '250px';
    overlay.style.height = '35px';
    overlay.style.bottom = '0px';
    overlay.style.backdropFilter = 'none';
    overlay.style.background = 'transparent';
    overlayDialog.style.width = '250px';
    overlayDialog.style.height = '35px';
    overlayDialog.style.top = 'initial';
    tempDraggle.value = JSON.parse(JSON.stringify(draggable.value));
    draggable.value = false;
  } else {
    overlay.style.width = tempOverlay.width;
    overlay.style.height = tempOverlay.height;
    overlay.style.bottom = tempOverlay.bottom;
    overlay.style.backdropFilter = tempOverlay.backdropFilter;
    overlay.style.background = tempOverlay.background;
    overlayDialog.style.width = tempOverlayDialog.width;
    overlayDialog.style.height = tempOverlayDialog.height;
    overlayDialog.style.top = tempOverlayDialog.top;
    draggable.value = tempDraggle.value;
  }
}

function blowUp() {
  if (isLessen.value) {
    isFull.value = false;
    lessen();
  } else {
    isFull.value = !isFull.value;
    if (isFull.value) {
      tempDraggle.value = JSON.parse(JSON.stringify(draggable.value));
      draggable.value = false;
    } else {
      draggable.value = tempDraggle.value;
    }
  }
}

function init() {
  isFull.value = props.isFull;
  draggable.value = props.draggable;
  dialogType.value = themeStore.options.dialogType;
  if (props.type) {
    dialogType.value = props.type;
  }
}
onMounted(() => {
  init();
});
defineExpose({
  close,
  open
});
</script>
<style lang="scss" scoped>
@include theme() {
  .c-dialog-header {
    width: 100%;
    margin: 0 0.5rem -3px 0.8rem;
    height: 100%;
    display: flex;
    font-size: 0.9rem;
    justify-content: space-between;
    align-items: center;
    .c-dialog-bt-group {
      display: flex;
      font-size: 1rem;
    }
    .svg-icon {
      font-size: inherit;
    }
    .svg-icon:hover {
      color: #4ca4ff !important;
    }
    .svg-icon:active {
      transform: translateY(1px);
    }
  }
}
</style>
