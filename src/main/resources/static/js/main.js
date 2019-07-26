import Vue from 'vue'
import Vuetify from 'vuetify'
import store from 'store/vuex-store'
import router from 'router/vue-router'
import '@babel/polyfill'
import 'api/resource'
import App from 'pages/App.vue'
import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader

Vue.use(Vuetify)


var app = new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App)
})