import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomePage.vue'),
      children: [{
        path: 'welcome',
        component: () => import('../views/main/Welcome.vue'),
      },{
        path: 'about',
        component: () => import('../views/main/about.vue'),
      }]
    },
    {
      path: '',
      redirect: '/welcome'
    }
  ]
})

export default router
