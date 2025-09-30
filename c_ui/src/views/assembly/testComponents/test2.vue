<!--
 * @Description: 
-->
<template>
  <!-- <div class="box">
    <div class="in">
      <div class="n1">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n2">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n3">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n4">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n5">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n6">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
    </div>
    <div class="out">
      <div class="t1">
        <img src="https://img-blog.csdnimg.cn/2020090116353966.png#pic_center" alt="" />
      </div>
      <div class="t2"></div>
      <div class="t3"></div>
      <div class="t4"></div>
      <div class="t5"></div>
      <div class="t6"></div>
    </div>
  </div> -->
  <div class="box">
    <div class="in">
      <div class="n1">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n2">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n3">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n4">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n5">
        <div class="k1">
          <img src="https://img-blog.csdnimg.cn/20200901163533997.png#pic_center" alt="" />
        </div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
      <div class="n6">
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
        <div class="k1"></div>
      </div>
    </div>
    <div class="out">
      <div class="t1"></div>
      <div class="t2"></div>
      <div class="t3"></div>
      <div class="t4"></div>
      <div class="t5"></div>
      <div class="t6"></div>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
<style>
/* html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  background: url(https://i02piccdn.sogoucdn.com/73750308f69893a3);
  background-repeat: repeat;
} */
.box {
  width: 100%;
  height: 400px;
  position: absolute;
  top: 19%;
  transform: rotateY(45deg) rotateX(-20deg);
  transform-style: preserve-3d;
}
/* .box:first-child {
  left: 24%;
}
.box:last-child {
  right: 24%;
} */
/*里面的盒子*/
.box > .in {
  width: 99px;
  height: 99px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  left: 50%;
  top: 50%;
  /*开启3d*/
  transform-style: preserve-3d;
  perspective: 100000px;
  transform: translate(-50%, -50%) rotateY(45deg) rotateX(-20deg);
  /*调用动画*/
  animation: mofan 10s linear 0s infinite normal;
  z-index: 100;
}

/*里面盒子六个面的共同值*/
.box > .in > div {
  width: 99px;
  height: 99px;
  position: absolute;
  opacity: 0.9;
  border: 1px solid white;
}

/*里面盒子的六个面*/
/*前*/
.n1 {
  background: #00ffff;
  transform: translateZ(51px);
}
/*后*/
.n2 {
  background: black;
  transform: translateZ(-51px);
}
/*左*/
.n3 {
  background: black;
  transform: translateX(-51px) rotateY(90deg);
}
/*右*/
.n4 {
  background: #00ffff;
  transform: translateX(51px) rotateY(90deg);
}
/*上*/
.n5 {
  background: #00ffff;
  transform: translateY(-51px) rotateX(90deg);
}
/*下*/
.n6 {
  background: black;
  transform: translateY(51px) rotateX(90deg);
}
/*里面盒子的六个面结束*/
/*动画*/
@keyframes mofan {
  from {
    transform: translate(-50%, -50%) rotateY(0deg) rotateX(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotateY(360deg) rotateX(0deg);
  }
}

@keyframes nixuan {
  from {
    transform: translate(-50%, -50%) rotateY(0deg) rotateX(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotateY(-360deg) rotateX(0deg);
  }
}

/*外面的盒子*/

.box > .out {
  width: 200px;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  left: 50%;
  top: 50%;
  transform-style: preserve-3d;
  transform: translate(-50%, -50%);
  /*调用动画*/
  animation: mofan 20s linear 0s infinite normal;
  color: get('font-color');
}

.box > .out > .t1,
.t2,
.t3,
.t4,
.t5,
.t6 {
  width: 200px;
  height: 200px;
  position: absolute;
  opacity: 0.7;
  border: 1px solid white;
  transition: all 0.3s;
  background: center / cover fixed no-repeat url('http://120.48.127.181/file/lol.jpg');
}
/*前*/
.t1 {
  background: #92c7dc;
  transform: translateZ(100px);
}
.t1 > img {
  height: 100%;
}
/*后*/
.t2 {
  background: #92c7dc;
  transform: translateZ(-100px);
}
/*左*/
.t3 {
  background: #92c7dc;
  transform: translateX(-100px) rotateY(90deg);
}
/*右*/
.t4 {
  background: #92c7dc;
  transform: translateX(100px) rotateY(90deg);
}
/*上*/
.t5 {
  background: #92c7dc;
  transform: translateY(-100px) rotateX(90deg);
}
/*下*/
.t6 {
  background: #92c7dc;
  transform: translateY(100px) rotateX(90deg);
}

.box:hover > .out > .t1 {
  transform: translateZ(300px);
}

.box:hover > .out > .t2 {
  transform: translateZ(-300px);
}
.box:hover > .out > .t3 {
  transform: translateX(-300px) rotateY(90deg);
}

.box:hover > .out > .t4 {
  transform: translateX(300px) rotateY(90deg);
}

.box:hover > .out > .t5 {
  transform: translateY(-300px) rotateX(90deg);
}
.box:hover > .out > .t6 {
  transform: translateY(300px) rotateX(90deg);
}

/*鼠标悬浮时外盒子逆转*/
.box:hover > .out {
  animation: nixuan 10s linear 0s infinite normal;
}

.n1,
.n4,
.n5 {
  background-image: linear-gradient(to right, #00ffff, black);
}

.k1 {
  width: 30%;
  height: 30%;
  border: 2px solid white;
  float: left;
  border-radius: 2px;
}
</style>
