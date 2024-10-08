import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import Antd, {notification} from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import {useMemberStore} from "@/stores/index.js";

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

app.mount('#app')

// 全局使用图标
const icons= Icons;
for (const i in icons) {
    app.component(i, icons[i])
}

// axios拦截器
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config)
    const token = useMemberStore().member.token;
    if(token) {
        config.headers.token = token
        console.log("请求headers增加token:", token)
    }
    return config
}, error => {
    return Promise.reject(error)
})
axios.interceptors.response.use(function (response){
    console.log('返回结果：', response)
    return response
}, error => {
    console.log('返回错误：', error)
    const response = error.response
    const status = response.status
    if(status === 401) {
        console.log("未登录或登录超时, 跳转到登录页面")
        useMemberStore().setMember({})
        notification.error({description: "未登录或登录超时"})
        router.push('/login')
    }
    return Promise.reject(error)
})

axios.defaults.baseURL = import.meta.env.VITE_APP_SERVER
console.log('环境：',import.meta.env.VITE_NODE_ENV)
console.log('服务器：',import.meta.env.VITE_APP_SERVER)
