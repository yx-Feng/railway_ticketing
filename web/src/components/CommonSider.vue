<template>
  <div>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" mode="inline" :style="{ height: '100%', borderRight: 0 }">
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
    </a-layout-sider>
  </div>
</template>

<script setup>
import {ref, watch} from "vue";
import router from "@/router/index.js";

const openKeys = ref(['sub1']);
const selectedKeys = ref([]);

watch(() => router.currentRoute.value.path, (newValue) => {
  console.log('watch', newValue);
  selectedKeys.value = [];
  selectedKeys.value.push(newValue);
}, {immediate: true})
</script>
