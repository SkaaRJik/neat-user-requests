import Vue from 'vue'
import App from './App.vue'
import router from './router/vue-router'
import store from './store/vue-store'
import Vuetify from 'vuetify/lib'
import vuetify from './plugins/vuetify';
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'
import Axios from 'axios'


Vue.config.productionTip = false
Vue.prototype.$http = Axios;

const token = localStorage.getItem('token')
if (token) {
    Vue.prototype.$http.defaults.headers.common['Authorization'] = token
}


Vue.use(Vuetify)

new Vue({

    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app')
