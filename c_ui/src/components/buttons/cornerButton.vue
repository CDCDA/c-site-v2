<template>
  <div class="button-wrap">
    <button class="corner-button">
      <span>Click me</span>
    </button>
  </div>
</template>
<script>
export default {
  name: 'neonRain',
  mounted() {}
};
</script>
<style scoped lang="scss">
.button-wrap {
  background: transparent;
}
.corner-button {
  cursor: pointer;
  background: white;
  border: 5px solid #ccc;
  padding: 5px 20px;
  font-size: 0.8rem;
  border-radius: 4px;
  color: black;
  position: relative;
  transition: color 0.3s;
}
.corner-button:hover {
  color: pink;
}
.corner-button:hover::before {
  width: 0;
}
.corner-button:hover::after {
  height: 0;
}
.corner-button:active {
  border-width: 0.25rem;
}
.corner-button span {
  position: relative;
  z-index: 2;
}
.corner-button::before,
.corner-button::after {
  content: '';
  position: absolute;
  background: white;
  z-index: 1;
  transition: all 0.3s;
}
.corner-button::before {
  width: calc(100% - 20px);
  height: calc(101% + 1rem);
  top: -0.5rem;
  left: 50%;
  transform: translateX(-50%);
}
.corner-button::after {
  height: calc(100% - 20px);
  width: calc(101% + 1rem);
  left: -0.5rem;
  top: 50%;
  transform: translateY(-50%);
}
</style>
