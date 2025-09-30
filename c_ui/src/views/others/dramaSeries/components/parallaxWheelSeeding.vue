<!--
 * @Description:  视差轮播
-->
<template>
  <div class="container">
    <input type="radio" name="slider" id="item-1" checked />
    <input type="radio" name="slider" id="item-2" />
    <input type="radio" name="slider" id="item-3" />
    <div class="cards">
      <label class="card" for="item-1" id="song-1">
        <img
          src="https://images.unsplash.com/photo-1530651788726-1dbf58eeef1f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=882&q=80"
          alt="song"
        />
      </label>
      <label class="card" for="item-2" id="song-2">
        <img
          src="https://images.unsplash.com/photo-1559386484-97dfc0e15539?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1234&q=80"
          alt="song"
        />
      </label>
      <label class="card" for="item-3" id="song-3">
        <img
          src="https://images.unsplash.com/photo-1533461502717-83546f485d24?ixlib=rb-1.2.1&auto=format&fit=crop&w=900&q=60"
          alt="song"
        />
      </label>
    </div>
  </div>
</template>
<script lang="ts" setup></script>
<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css?family=DM+Sans:400,500,700&display=swap');

.card {
  position: absolute;
  width: 60%;
  height: 100%;
  left: 0;
  right: 0;
  margin: auto;
  -webkit-transition: -webkit-transform 0.4s ease;
  transition: -webkit-transform 0.4s ease;
  transition: transform 0.4s ease;
  transition: transform 0.4s ease, -webkit-transform 0.4s ease;
  cursor: pointer;
}

.container {
  width: 800px;
  height: 600px;
  -webkit-transform-style: preserve-3d;
  transform-style: preserve-3d;
  display: -webkit-box;
  display: flex;
  -webkit-box-pack: center;
  justify-content: center;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  -webkit-box-align: center;
  align-items: center;
}

.cards {
  position: relative;
  width: 100%;
  height: 100%;
  margin-bottom: 20px;
}

img {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  -o-object-fit: cover;
  object-fit: cover;
}

#item-1:checked ~ .cards #song-3,
#item-2:checked ~ .cards #song-1,
#item-3:checked ~ .cards #song-2 {
  -webkit-transform: translatex(-40%) scale(0.8);
  transform: translatex(-40%) scale(0.8);
  opacity: 0.4;
  z-index: 0;
}

#item-1:checked ~ .cards #song-2,
#item-2:checked ~ .cards #song-3,
#item-3:checked ~ .cards #song-1 {
  -webkit-transform: translatex(40%) scale(0.8);
  transform: translatex(40%) scale(0.8);
  opacity: 0.4;
  z-index: 0;
}

#item-1:checked ~ .cards #song-1,
#item-2:checked ~ .cards #song-2,
#item-3:checked ~ .cards #song-3 {
  -webkit-transform: translatex(0) scale(1);
  transform: translatex(0) scale(1);
  opacity: 1;
  z-index: 1;
}
#item-1:checked ~ .cards #song-1 img,
#item-2:checked ~ .cards #song-2 img,
#item-3:checked ~ .cards #song-3 img {
  box-shadow: 0 0 5px 0 rgba(81, 81, 81, 0.47);
}
</style>
