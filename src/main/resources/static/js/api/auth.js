import Vue from 'vue'





export default {
    signIn: signInDetails => Vue.resource('/api/auth/signin').save({}, signInDetails),
    signUp: signUpDetails => Vue.resource('/api/auth/signup').save({}, signUpDetails),

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