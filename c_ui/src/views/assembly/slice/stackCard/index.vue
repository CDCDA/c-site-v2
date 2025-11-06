<!--
 * @Description: 管理页侧边栏
-->
<template>
  <div class="stackCard">
    <div class="food-card">
      <div class="food-card__image">
        <img src="https://www.jq22.com/tp/6372933017718194079607274.jpg" alt="Pizza" />
      </div>

      <div class="food-card__details">
        <span class="food-card__name">Pizza</span>
        <span class="food-card__desc">Fresh & sweet</span>

        <div class="food-card__price">￡50</div>
        <div class="food-card__rating">
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star"></div>
        </div>
      </div>
    </div>

    <div class="food-card">
      <div class="food-card__image">
        <img src="https://www.jq22.com/tp/6372933018122879962904493.jpg" alt="Chicken" />
      </div>

      <div class="food-card__details">
        <span class="food-card__name">Chicken</span>
        <span class="food-card__desc">Fried to crisp</span>

        <div class="food-card__price">￡82</div>
        <div class="food-card__rating">
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
        </div>
      </div>
    </div>

    <div class="food-card">
      <div class="food-card__image">
        <img src="https://www.jq22.com/tp/6372933018702569397400410.jpg" alt="Coffee" />
      </div>

      <div class="food-card__details">
        <span class="food-card__name">Coffee</span>
        <span class="food-card__desc">Anyhow</span>

        <div class="food-card__price">￡5</div>
        <div class="food-card__rating">
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star food-card__star--gold"></div>
          <div class="food-card__star"></div>
          <div class="food-card__star"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t: $t } = useI18n();
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const menuList = ref([]) as any;

onMounted(() => {});
</script>
<style lang="scss" scoped>
.stackCard {
  background: black;
  width: 100%;
  height: 100%;
}
.food-card {
  background-color: #fff;
  position: relative;
  width: 325px;
  overflow: hidden;
  border-radius: 30px;
  box-shadow: -30px 70px 150px -20px rgba(51, 51, 51, 0.25);
  transform-style: preserve-3d;
  transform: rotateX(22deg) rotate(-14deg) rotateY(17deg) translateZ(var(--z));
  position: absolute;
  transition: transform 0.3s;
}
.food-card:nth-child(1) {
  left: 40%;
  top: 29%;
  z-index: 1;
  --z: 30px;
}
.food-card:nth-child(1):hover {
  --z: 50px;
}
.food-card:nth-child(2) {
  left: 51%;
  top: 33%;
  z-index: 2;
  --z: 50px;
}
.food-card:nth-child(2):hover {
  --z: 70px;
}
.food-card:nth-child(3) {
  top: 37%;
  left: 35%;
  --z: -20px;
}
.food-card:nth-child(3):hover {
  --z: 0;
}
.food-card:hover {
  z-index: 5;
}
.food-card__image {
  position: relative;
  height: 160px;
}
.food-card__image > img {
  max-width: 100%;
  border-bottom-right-radius: 30px;
  transform: rotate(8deg) translate(-10px, -55px);
  position: absolute;
  height: 200px;
  object-fit: cover;
  object-position: center;
}
.food-card__details {
  display: grid;
  grid-row-gap: 8px;
  grid-template: 1fr 1fr / 1fr 1fr;
  padding: 8px 30px 20px;
}
.food-card__name {
  grid-column: 1;
  grid-row: 1;
  font-size: 0.9rem;
  font-weight: bold;
  color: #11313c;
}
.food-card__price {
  grid-column: 2;
  grid-row: 1;
  font-size: 0.9rem;
  font-weight: bold;
  color: #f7736e;
  text-align: right;
}
.food-card__desc {
  grid-column: 1;
  grid-row: 2;
  font-size: 14px;
  font-weight: 500;
  color: #949a98;
}
.food-card__rating {
  grid-column: 2;
  grid-row: 2;
  text-align: right;
}
.food-card__star {
  width: 16px;
  height: 16px;
  background-color: #dadada;
  display: inline-block;
  clip-path: polygon(
    50% 0%,
    61% 35%,
    98% 35%,
    68% 57%,
    79% 91%,
    50% 70%,
    21% 91%,
    32% 57%,
    2% 35%,
    39% 35%
  );
}
.food-card__star:not(:last-child) {
  margin-right: 6px;
}
.food-card__star--gold {
  background-color: #e6c356;
}
</style>
