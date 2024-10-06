<template>
  <div>
    <a-layout-header class="header" id="components-layout-demo-top-side-2">
      <div class="logo"/>
      <div style="float: right; color: white">
        您好：{{member.mobile}} &nbsp; &nbsp;
        <router-link to="/login">
          登录
        </router-link>
      </div>
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
        <a-menu-item key="/welcome">
          <router-link to="/welcome">
            <HomeOutlined /> 欢迎
          </router-link>
        </a-menu-item>
        <a-menu-item key="/passenger">
          <router-link to="/passenger">
            <UserOutlined /> 乘车人管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/ticket">
          <router-link to="/ticket">
            <UserOutlined /> 余票查询
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-header>
  </div>
</template>

<script setup>
import {ref, watch} from "vue";
import {useMemberStore} from "@/stores/index.js";
import router from "@/router/index.js";
const member = useMemberStore().member
const selectedKeys = ref([]);

watch(() => router.currentRoute.value.path, (newValue) => {
  console.log('watch', newValue);
  selectedKeys.value = [];
  selectedKeys.value.push(newValue);
}, {immediate: true})
</script>

<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
</style>