<!--
 * @Description:  
-->
<template>
  <div class="blankTemplate"></div>
</template>

<script>
import { useRouter, onBeforeRouteUpdate } from 'vue-router';
const router = useRouter();
// export default {
//   beforeRouteEnter(to, from, next) {
//     next(vm => {
//       router.replace(from.fullPath);
//     });
//   },
//   setup() {}
// };
</script>
<style>
.blankTemplate {
  height: 100%;
  width: 100%;
}
</style>
