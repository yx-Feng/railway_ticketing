<template>
  <div>
    <a-layout class="homePage">
      <CommonHeader/>
      <a-layout>
        <CommonSider/>
        <a-layout style="padding: 0 24px 24px">
          <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <router-view></router-view>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup>
import CommonHeader from "@/components/CommonHeader.vue";
import CommonSider from "@/components/CommonSider.vue";
</script>

<style scoped>
.homePage {
  height: 100vh;
}
</style>
