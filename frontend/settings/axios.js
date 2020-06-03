import axios from 'axios';
import {API_SERVER} from '../settings/constants'
import Vue from "vue";
import router from "../src/router/vue-router";
import store from "../src/store/vue-store";

Vue.config.productionTip = false

const user = JSON.parse(localStorage.getItem('user'))
let token = null

if (user) {
    console.log('[Axios]. :',user)
    token = user.tokens.accessToken
}

console.log('[Axios]. :',token)

const axiosInstance = axios.create({
    baseURL: API_SERVER,
    headers: {
        Authorization: token
    }
})

Vue.prototype.$http = axiosInstance;


// Axios.defaults.baseURL = API_SERVER
axiosInstance.interceptors.response.use(response => response, async error => {
    const status = error.response ? error.response.status : null
    const text = error.response ? error.response.data : null
    console.log('[axios.js]interceptors. : status',status)
    if (status === 401) {
        // will loop if refreshToken returns 401
        try{
            console.log('[axios].refreshTokens OLD error.config.headers[Authorization]:',error.config.headers['Authorization'])
            let tokens = await store.dispatch('auth/refreshTokens')
            error.config.headers['Authorization'] = tokens.accessToken
            console.log('[axios].refreshTokens new error.config.headers[Authorization]:',error.config.headers['Authorization'])
            return Vue.prototype.$http.request(error.config);
        } catch (e) {
            console.log('[axios.js]. status === 401 error:',e)
            return Promise.reject(error);
        }



        // return refreshToken(store).then(_ => {
        //     error.config.headers['Authorization'] = 'Bearer ' + store.state.auth.token;
        //     error.config.baseURL = undefined;
        //     return Axios.request(error.config);
        // })
        // Would be nice to catch an error here, which would work, if the interceptor is omitted

    }
    if (status === 406) {
        await router.push('/login')
    }

    else if(status) {
        console.log('[Axios]. status text:',status, text)
        await store.dispatch({ type:'error/setError', status, text} )
    }

    return Promise.reject(error);
});

export default axiosInstance





