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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
]

const router = new VueRouter({
  /*mode: 'history',*/
  base: process.env.BASE_URL,
  routes
})

export default router
