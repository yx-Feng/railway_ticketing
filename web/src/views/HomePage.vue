<template>
  <div>
    <a-layout>
      <CommonHeader/>
      <CommonSider/>
    </a-layout>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import CommonHeader from "@/components/CommonHeader.vue";
import CommonSider from "@/components/CommonSider.vue";


</script>

<style scoped>

.site-layout-background {
  background: #fff;
}
</style>
