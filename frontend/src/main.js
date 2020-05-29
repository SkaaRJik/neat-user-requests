import Vue from 'vue'
import App from './App.vue'
import router from './router/vue-router'
import store from './store/vue-store'
import Vuetify from 'vuetify/lib'
import vuetify from './plugins/vuetify';
import ScrollLoader from 'vue-scroll-loader'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'
import '../settings/axios'
import i18n from './i18n'

Vue.use(Vuetify)
Vue.use(ScrollLoader)

const vue = new Vue({
    router,
    store,
    vuetify,
    i18n,
    render: h => h(App)
}).$mount('#app')

