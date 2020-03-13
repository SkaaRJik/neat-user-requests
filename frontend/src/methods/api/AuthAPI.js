import Vue from 'vue'

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
    test: token => {
        return Vue.http.get('/api/trainer/default-config', {
            headers: {
                'Authorization': token
            }
        })
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