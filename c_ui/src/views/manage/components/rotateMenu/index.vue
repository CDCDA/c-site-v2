<!--
 * @Description: 旋转导航
-->
<!--suppress CssInvalidFunction -->
<template>
  <div class="rotate-menu" id="perspective">
    <div id="wrap">
      <div class="rotate-menu-item" v-for="menu in menuList" @click="toManage(menu)">
        <c-image :lazy="false" class="rotate-img" :src="menu.meta.src" fit="contain" />
        <div class="rotate-menu-title">
          {{ $t(menu.meta.title) }}
        </div>
      </div>
      <p></p>
    </div>
    <router-view-dialog
      ref="routerDialog"
      type="filter"
      :modal="true"
      :title="title"
      @close="close"
    ></router-view-dialog>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import routerViewDialog from '@/components/routerViewDialog/index.vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const title = ref('') as any;
// 管理菜单
const menuList = ref([]) as any;
const activeMenu = ref({}) as any;
const routerDialog = ref(null) as any;
function toManage(menu: any) {
  // console.log(menu);
  title.value = $t(menu.meta.title);
  routerDialog.value.open();
  destroyEvent();
  router.push({ name: menu.name });
}
function close() {
  routerDialog.value.close();
  initMenu();
}
function destroyEvent() {
  document.onmousemove = null;
  document.onmouseup = null;
  document.onmousedown = null;
  document.onresize = null;
}

function initMenu() {
  let oWrap = document.getElementById('wrap') as any;
  let oImg = oWrap.querySelectorAll('.rotate-menu-item');
  let oImgLength = oImg.length;
  let Deg = 360 / oImgLength;
  let nowX,
    nowY,
    lastX = 0,
    lastY = 0,
    minusX = 0,
    minusY = 0;
  let roY = 0,
    roX = -10;
  let timer = 0 as any;

  for (let i = 0; i < oImgLength; i++) {
    oImg[i].style.transform = 'rotateY(' + i * Deg + 'deg) translateZ(350px)';
    oImg[i].style.transition = 'transform 1s ' + (oImgLength - 1 - i) * 0.1 + 's';
  }
  function mTop() {
    let wH = (document.querySelector('.page-main') as any).clientHeight;
    oWrap.style.marginTop = wH / 2 - 180 + 'px';
  }
  mTop();

  window.onresize = mTop;
  // 拖拽：三个事件-按下 移动 抬起
  //按下
  document.onmousedown = function (ev) {
    ev = ev || window.event;

    //鼠标按下的时候，给前一点坐标赋值，为了避免第一次相减的时候出错
    lastX = ev.clientX;
    lastY = ev.clientY;

    //移动
    this.onmousemove = function (ev) {
      ev = ev || window.event;

      clearInterval(timer);

      nowX = ev.clientX; // clientX 鼠标距离页面左边的距离
      nowY = ev.clientY; // clientY ………………………………顶部………………

      //当前坐标和前一点坐标差值
      minusX = nowX - lastX;
      minusY = nowY - lastY;

      //更新wrap的旋转角度，拖拽越快-> minus变化大 -> roY变化大 -> 旋转快
      roY += minusX * 0.2; // roY = roY + minusX*0.2;
      roX -= minusY * 0.1;

      oWrap.style.transform = 'rotateX(' + roX + 'deg) rotateY(' + roY + 'deg)';

      /*
          //生成div，让div跟着鼠标动
          let oDiv = document.createElement('div');
          oDiv.style.cssText = 'width:5px;height:5px;background:red;position:fixed;left:'+nowX+'px;top:'+nowY+'px';
          this.body.appendChild(oDiv);
          */

      //前一点的坐标
      lastX = nowX;
      lastY = nowY;
    };
    //抬起
    this.onmouseup = function () {
      this.onmousemove = null;
      timer = setInterval(function () {
        minusX *= 0.95;
        minusY *= 0.95;
        roY += minusX * 0.2; // roY = roY + minusX*0.2;
        roX -= minusY * 0.1;
        oWrap.style.transform = 'rotateX(' + roX + 'deg) rotateY(' + roY + 'deg)';
        if (Math.abs(minusX) < 0.1 && Math.abs(minusY) < 0.1) {
          clearInterval(timer);
        }
        // console.log(minusX);
      }, 13);
    };
    return false;
  };
}

function getMenuList() {
  menuList.value = (router.options.routes as any).find((x: any) => x.name == 'manage').children;
  console.log(menuList.value);
  setTimeout(() => {
    initMenu();
  }, 0);
}
onMounted(() => {
  getMenuList();
});
</script>

<style lang="scss" scoped>
@include theme() {
  #perspective {
    z-index: 100;
    perspective: 800px;
    margin: 0 auto;
    width: 100%;
    height: 100%;
    position: absolute;
    background: transparent;
  }
  #wrap {
    width: 120px; /*133:200  4:6  */
    height: 180px;
    margin: 0 auto;
    position: relative;
    /*搭建3D效果必须的两个属性：一个变换风格变3d，一个场景景深800px*/
    transform-style: preserve-3d;
    transform: rotateX(-10deg) rotateY(0deg) rotateZ(100px);
  }

  #wrap p {
    width: 1200px;
    height: 1200px;
    background: -webkit-radial-gradient(
      center center,
      600px 600px,
      rgba(244, 23, 234, 0.2),
      rgba(0, 0, 0, 0)
    );
    border-radius: 100%;
    position: absolute;
    left: 50%;
    top: 102%;
    margin-left: -600px;
    margin-top: -600px;
    transform: rotateX(90deg);
  }
  #wrap .rotate-menu-item {
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
    position: absolute;
    border-radius: 8px;
    cursor: pointer;
    overflow: hidden;
    left: calc(50% - 60px);
    transform: rotateY(0deg) translateZ(0px);
    /*倒影：朝向 偏移 遮盖*/ /*线性渐变(从哪里开始,开始时候的颜色,结束时候的颜色)*/
    -webkit-box-reflect: below 5px -webkit-linear-gradient(top, rgba(0, 0, 0, 0) 40%, rgba(
            0,
            0,
            0,
            0.5
          )
          100%);
    .rotate-img {
      width: 90px;
      height: 90px;
      backdrop-filter: blur(20px);
      padding: 30px 15px 0 15px;
      :deep(img) {
        filter: invert(100%);
      }
    }
    .rotate-menu-title {
      display: flex;
      align-items: center;
      height: 70px;
      color: white;
      backdrop-filter: blur(20px);
      justify-content: center;
    }
  }
}
</style>
