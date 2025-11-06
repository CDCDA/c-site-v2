<!--
 * @Description: 搜索弹窗
-->
<template>
  <c-dialog
    class="search-dialog"
    v-model="dialogVisible"
    :title="$t('搜索')"
    width="30rem"
    @close="emit('close')"
    style="height: 400px"
    :modal="true"
  >
    <div class="finder">
      <div class="finder__outer">
        <div class="finder__inner">
          <div class="finder__icon" ref="icon"></div>
          <input
            class="finder__input"
            type="text"
            name="q"
            v-model="searchText"
            @input="findRoute"
          />
        </div>
      </div>
      <div class="c-divider"></div>
      <div class="finder__outer finder-list">
        <div class="finder-list-item" v-for="item in resultRoutes" @click="routeTo(item)">
          <span>{{ $t(item.meta.title) }}</span>
          <svg-icon iconName="commonSvg-右" />
        </div>
      </div>
    </div>
  </c-dialog>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, nextTick, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { autoClearTimer } from '@/utils/timer';
const dialogVisible = ref(true) as any;
const router = useRouter();
const routes = router.getRoutes().filter((e: any) => {
  return e.meta.isHidden !== true && e.meta.title;
}) as any;
var resultRoutes = JSON.parse(JSON.stringify(routes));
const searchText = ref('' as String);
const emit = defineEmits(['close']);
function findRoute() {
  resultRoutes = routes.filter((r: any) => {
    if (r.meta.title) return r.meta.title.includes(searchText.value);
  });
}

function routeTo(item: any) {
  router.push({ name: item.name });
  emit('close');
}

onMounted(() => {
  var input = document.querySelector('.finder__input') as any;
  var finder = document.querySelector('.finder') as any;
  var form = document.querySelector('form') as any;
  autoClearTimer(() => {
    input = document.querySelector('.finder__input') as any;
    finder = document.querySelector('.finder') as any;
    form = document.querySelector('form') as any;

    input.addEventListener('focus', () => {
      finder.classList.add('active');
    });

    input.addEventListener('blur', () => {
      if (input.value.length === 0) {
        finder.classList.remove('active');
      }
    });
    if (form)
      form.addEventListener('submit', (ev: any) => {
        ev.preventDefault();
        finder.classList.add('processing');
        finder.classList.remove('active');
        input.disabled = true;
        autoClearTimer(() => {
          finder.classList.remove('processing');
          input.disabled = false;
          if (input.value.length > 0) {
            finder.classList.add('active');
          }
        }, 1000);
      });
  }, 1000);
});
</script>
<style lang="scss" scoped>
@include theme() {
  .finder__inner {
    border: 2px solid get('border-color');
  }
}

.finder {
  border-radius: 8px;
  width: 100%;
  height: 100%;
}

.finder__outer {
  display: flex;
  width: 100%;
  border-radius: 10px;
}
#app .theme-icon {
}
.finder-list {
  width: 100%;
  height: calc(100% - 75px);
  margin-top: 10px;
  overflow: auto;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  // padding: 10px 5px;
  .finder-list-item {
    width: calc(100% - 2rem);
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.5rem 1rem;
    border-bottom: 1px solid #ccc;
    font-size: 0.9rem;
    .svg-icon {
      font-size: 1rem;
    }
  }
}

.finder__inner {
  display: flex;

  border-radius: 40px;
  padding: 5px 30px;
  align-items: center;
  position: relative;
  flex: 1;
}

.finder__form {
  flex: 1;
  height: calc(100% + 3rem);
}

.finder__input {
  height: calc(100% + 3rem);
  border: none;
  background-color: transparent;
  outline: none;
  font-size: 1.2rem;
  letter-spacing: 0.75px;
}

.finder__icon {
  width: 24px;
  height: 24px;
  margin-right: 1rem;
  transition: all 0.2s;
  box-shadow: inset 0 0 0 20px #292929;
  border-radius: 50%;
  position: relative;
}
.finder__icon:after,
.finder__icon:before {
  display: block;
  content: '';
  position: absolute;
  transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.finder__icon:after {
  width: 8px;
  height: 8px;
  background-color: #292929;
  border: 3px solid #f6f5f0;
  top: 50%;
  position: absolute;
  transform: translateY(-50%);
  left: 0;
  right: 0;
  margin: auto;
  border-radius: 50%;
}
.active .finder__icon:after {
  border-width: 5px;
  background-color: #f6f5f0;
}
.finder__icon:before {
  width: 4px;
  height: 13px;
  background-color: #f6f5f0;
  top: 50%;
  left: 17px;
  transform: rotateZ(45deg) translate(-50%, 0);
  transform-origin: 0 0;
  border-radius: 4px;
}
.active .finder__icon:before {
  background-color: #292929;
  width: 4px;
  top: 20%;
  left: 22px;
  transform: rotateZ(45deg) translate(-50%, 25px);
}
.processing .finder__icon {
  transform-origin: 50%;
  animation: spinner 0.3s linear infinite;
  animation-delay: 0.5s;
}
.active .finder__icon {
  transform: translateY(-5px);
}

@keyframes spinner {
  0% {
    transform: rotateZ(45deg);
  }
  100% {
    transform: rotateZ(405deg);
  }
}
</style>
<style lang="scss">
.search-dialog {
  .el-dialog__body {
    height: calc(100% - 78px) !important;
  }
}
</style>
