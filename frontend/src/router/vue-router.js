import Vue from 'vue'
import VueRouter from 'vue-router'
import Auth from "../components/auth/Auth";
import projects_routes from "./projects-routes";


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: Auth,
    },
    {
        path: '/login',
        name: 'login',
        component: Auth,
        props: {dataToLogin: {message: ''}},
        meta: {
            guest: true
        }
    },
    {
        path: '/about',
        name: 'about',
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    },
    ...projects_routes

]

const router = new VueRouter({
    /*mode: 'history',*/
    base: process.env.BASE_URL,
    routes
})



router.beforeEach((to, fromRoute, next) => {
    if(to.matched.some(record => record.meta.requiresAuth)) {
        if (localStorage.getItem('user') == null) {
            next({
                path: '/login',
                params: { nextUrl: to.fullPath }
            })
        } else {
            const user = JSON.parse(localStorage.getItem('user'))
            const roles = user.roles
            if(to.matched.some(record => record.meta.is_admin)) {
                if(roles.includes('ADMIN')){
                    next()
                }
                else{
                    next({ name: 'home'})
                }
            }else {
                next()
            }
        }
    } else if(to.matched.some(record => record.meta.guest)) {
        if(localStorage.getItem('user') == null){
            next()
        }
        else{
            next(fromRoute)
        }
    }else {
        next()
    }
})

export default router
