import Vue from 'vue'
import VueRouter from 'vue-router'
import Auth from "../components/auth/Auth";


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: Auth
    },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
    {
        path: '/projects',
        name: 'projects',
        component: () => import(/* webpackChunkName: "about" */ '../views/Projects.vue')
    },
]

const router = new VueRouter({
  /*mode: 'history',*/
  base: process.env.BASE_URL,
  routes
})

export default router
