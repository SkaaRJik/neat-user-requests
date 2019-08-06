import Vue from 'vue'





export default {
    signIn: signInDetails => {return Vue.resource('/api/auth/signin').save({}, signInDetails)},
    signUp: signUpDetails => {return Vue.resource('/api/auth/signup').save({}, signUpDetails)},
    checkEmail: email => {return Vue.resource('/api/auth/check-email').get({email: email})},
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