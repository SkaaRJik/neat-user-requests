import Vue from 'vue'
import Axios from 'axios'
import {API_SERVER} from '../settings/constants'

Vue.config.productionTip = false
Axios.defaults.baseURL = API_SERVER


Vue.prototype.$http = Axios;


const token = localStorage.getItem('token')
if (token) {
    Vue.prototype.$http.defaults.headers.common['Authorization'] = token
}