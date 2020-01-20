import Vue from 'vue'

import store from 'vue-modules/vuex-store'
import router from 'vue-modules/vue-router.js'
import vuetify from 'vue-modules/vuetify'

import '@babel/polyfill'
import App from 'App.vue'
// import 'frontend/src/api/resource'


const app = new Vue({
    el: '#app',
    store,
    router,
    vuetify,
    render: a => a(App)
})