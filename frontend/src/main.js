import Vue from 'vue'
import App from './App.vue'
import router from './router/vue-router'
import store from './store/vue-store'
import Vuetify from 'vuetify/lib'
import vuetify from './plugins/vuetify';
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'
import '../settings/axios'
import i18n from './i18n'

Vue.use(Vuetify)

const vue = new Vue({
    router,
    store,
    vuetify,
    i18n,
    render: h => h(App)
}).$mount('#app')

