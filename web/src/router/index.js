import { createRouter, createWebHistory } from 'vue-router'
import HomePage from "@/views/HomePage.vue";
import {useMemberStore} from "@/stores/index.js";
import {notification} from "ant-design-vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomePage.vue'),
      meta: {
        loginRequire: true
      },
      children: [{
        path: 'welcome',
        component: () => import('../views/main/Welcome.vue'),
      },{
        path: 'passenger',
        component: () => import('../views/main/Passenger.vue'),
      },{
        path: 'ticket',
        component: () => import('../views/Ticket.vue'),
      }]
    },
    {
      path: '',
      redirect: '/welcome'
    },
    {
      path: '/login',
      name: 'login',
      // 懒加载
      component: () => import('../views/UserLogin.vue')
    }
  ]
})

// 路由跳转拦截
router.beforeEach((to, from, next) => {
  if(to.matched.some(function (item){
    console.log(item, "是否需要登录校验：", item.meta.loginRequire || false);
    return item.meta.loginRequire
  })) {
    const user = useMemberStore().member;
    console.log("页面登录校验开始：", user);
    if(!user.token) {
      console.log("用户未登录或登录超时！");
      notification.error({description: "未登录或登录超时"})
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
