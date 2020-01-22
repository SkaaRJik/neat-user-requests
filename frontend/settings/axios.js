import Vue from 'vue'
import Axios from 'axios'
import {API_SERVER} from '../settings/constants'

Vue.config.productionTip = false
Axios.defaults.baseURL = API_SERVER
console.log('[Axios].() Axios.defaults.baseURL:', Axios.defaults.baseURL)


Vue.prototype.$http = Axios;
console.log('[Axios].() Vue.prototype.$http:', Vue.prototype.$http)

const token = localStorage.getItem('token')
if (token) {
    Vue.prototype.$http.defaults.headers.common['Authorization'] = token
}