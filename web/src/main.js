import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';

import * as Icons from '@ant-design/icons-vue';

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