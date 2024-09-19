<template>
  <div>
    <a-layout>
      <CommonHeader/>
      <a-layout>
        <CommonSider/>
        <a-layout style="padding: 0 24px 24px">
          <a-breadcrumb style="margin: 16px 0">
            <a-breadcrumb-item>Home</a-breadcrumb-item>
            <a-breadcrumb-item>List</a-breadcrumb-item>
            <a-breadcrumb-item>App</a-breadcrumb-item>
          </a-breadcrumb>
          <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            所有会员总数：{{ count }}
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import CommonHeader from "@/components/CommonHeader.vue";
import CommonSider from "@/components/CommonSider.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

axios.get("/member/member/count").then((response) =>{
  let data = response.data;
  if(data.success) {
    count.value = data.content
  } else {
    notification.error({description: data.content})
  }
})
const count = ref(0)

</script>

<style scoped>
.site-layout-background {
  background: #fff;
}
</style>
