<template>
  <div>
    <h1>欢迎使用仿12306售票系统</h1>
    所有会员总数：{{ count }}
  </div>
</template>

<script setup>
import { ref } from 'vue';
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