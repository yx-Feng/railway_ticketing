<template>
  <a-row class="login">
    <a-col :span="6" :offset="9" class="login-main">
      <h1 style="text-align: center">仿12306售票系统</h1>
      <a-form :model="loginForm" name="basic" autocomplete="off">
        <a-form-item label="" name="mobile" :rules="[{ required: true, message: '请输入手机号!' }]">
          <a-input v-model:value="loginForm.mobile" />
        </a-form-item>

        <a-form-item label="" name="code" :rules="[{ required: true, message: '请输入验证码!' }]">
          <a-input v-model:value="loginForm.code" >
            <template #addonAfter>
              <a @click="sendCode">获取验证码</a>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </a-col>
  </a-row>
</template>

<script setup>
import { reactive } from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {useRouter} from 'vue-router';
import {useMemberStore} from "@/stores/index.js";

const loginForm = reactive({
  mobile: '11118888888',
  code: '',
});
const router = useRouter();
const memberStore = useMemberStore();
const sendCode = () => {
  axios.post("/member/member/send-code", {
    mobile: loginForm.mobile
  }).then(response => {
    // console.log(response);
    let data = response.data;
    if(data.success) {
      notification.success({description: '发送验证码成功!'})
      loginForm.code = "8888"
    } else {
      notification.error({description: data.message})
    }
  })
};

const login = () => {
  axios.post("/member/member/login",loginForm).then(response => {
    let data = response.data;
    if(data.success) {
      notification.success({description: '登录成功!'})
      // 登录成功，跳转到控制台主页
      router.push("/welcome")
      memberStore.setMember(data.content);
      // console.log("登录成功：",data.content);
    } else {
      notification.error({description: data.message})
    }
  })
}
</script>

<style>
.login h1 {
  margin-bottom: 50px;
}

.login-main {
  margin-top: 200px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
}
</style>