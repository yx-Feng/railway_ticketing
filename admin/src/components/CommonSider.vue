<template>
  <div>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" mode="inline" :style="{ height: '100%', borderRight: 0 }">
        <a-menu-item key="/welcome">
          <router-link to="/welcome">
            <HomeOutlined /> 欢迎
          </router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">
            <UserOutlined /> 关于
          </router-link>
        </a-menu-item>
        <a-sub-menu key="member">
          <template #title>
            <span><UnorderedListOutlined />&nbsp;会员管理</span>
          </template>
          <a-menu-item key="/member/ticket">
            <router-link to="/member/ticket">
              <FileTextOutlined /> &nbsp;会员车票
            </router-link>
          </a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="batch">
          <template #title>
            <span><UnorderedListOutlined />&nbsp;跑批管理</span>
          </template>
          <a-menu-item key="/batch/job">
            <router-link to="/batch/job">
              <MenuUnfoldOutlined /> &nbsp;任务管理
            </router-link>
          </a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="business">
          <template #title>
            <span><UnorderedListOutlined />&nbsp;业务管理</span>
          </template>
          <a-menu-item key="/business/sk-token">
            <router-link to="/business/sk-token">
              <MenuUnfoldOutlined /> &nbsp;令牌余量
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/confirm-order">
            <router-link to="/business/confirm-order">
              <MenuUnfoldOutlined /> &nbsp;订单信息
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/daily-train-ticket">
            <router-link to="/business/daily-train-ticket">
              <MenuUnfoldOutlined /> &nbsp;余票信息
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/daily-train">
            <router-link to="/business/daily-train">
              <MenuUnfoldOutlined /> &nbsp;每日车次
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/daily-train-station">
            <router-link to="/business/daily-train-station">
              <MenuUnfoldOutlined /> &nbsp;每日车站
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/daily-train-carriage">
            <router-link to="/business/daily-train-carriage">
              <MenuUnfoldOutlined /> &nbsp;每日车箱
            </router-link>
          </a-menu-item>
          <a-menu-item key="/business/daily-train-seat">
            <router-link to="/business/daily-train-seat">
              <MenuUnfoldOutlined /> &nbsp;每日座位
            </router-link>
          </a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="base">
          <template #title>
            <span><UnorderedListOutlined />&nbsp;基础数据</span>
          </template>
          <a-menu-item key="/base/station">
            <router-link to="/base/station">
              <MenuUnfoldOutlined /> 车站管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/base/train">
            <router-link to="/base/train">
              <MenuUnfoldOutlined /> 火车管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/base/train-station">
            <router-link to="/base/train-station">
              <MenuUnfoldOutlined /> 火车车站管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/base/train-carriage">
            <router-link to="/base/train-carriage">
              <MenuUnfoldOutlined /> 火车车箱管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/base/train-seat">
            <router-link to="/base/train-seat">
              <MenuUnfoldOutlined /> 火车座位管理
            </router-link>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
  </div>
</template>

<script setup>
import {ref, watch} from "vue";
import router from "@/router/index.js";

const openKeys = ref(['batch', 'base', 'business', 'member']);
const selectedKeys = ref([]);

watch(() => router.currentRoute.value.path, (newValue) => {
  console.log('watch', newValue);
  selectedKeys.value = [];
  selectedKeys.value.push(newValue);
}, {immediate: true})
</script>
