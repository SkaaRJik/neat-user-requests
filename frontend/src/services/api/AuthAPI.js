import Vue from 'vue'
import axiosInstance from "../../../settings/axios";

const controllerPath = ''
export default {
    signIn: signInDetails => {
        return Vue.prototype.$http.post(`${controllerPath}/login`, {
            ...signInDetails
        })
    },
    signUp: signUpDetails => {
        return Vue.prototype.$http.post(`${controllerPath}/signup`, {
            ...signUpDetails
        })
    },
    checkEmail: email => {
        return Vue.prototype.$http.get(`${controllerPath}/email-exist?email=${email}`)
    },
    checkUsername: username => {
        return Vue.prototype.$http.get(`${controllerPath}/username-exist?username=${username}`)
    },
    test: () => {
        console.log('[AuthApi].test headers:',Vue.prototype.$http.defaults.headers['Authorization'])
        return axiosInstance.get('/api/trainer/default-config')
    },

    refreshTokens(refreshToken) {
        return Vue.prototype.$http.get(`${controllerPath}/refresh-tokens?refreshToken=${refreshToken}`)
    }
}

/*export default {
    async signIn(signInDetails){

        const result = await this.$resource(authService+"/signin").save({}, signInDetails)
        const data = await result.json()
        console.log(data)
    },
    async signUp(signUpDetails){
        const result = await this.$resource(authService+"/signup").save({}, signUpDetails)
        const data = await result.json()
        console.log(data)
    }

    signUp: message => messages.update({id: message.id}, message),
    logOut: id => messages.remove({id},
    refreshToken: token => authService.
}*/