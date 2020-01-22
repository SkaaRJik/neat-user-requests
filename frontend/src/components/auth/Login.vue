<template>
    <v-card>
        <v-card-title>
            <v-alert
                    v-model="showSuccessInfo"
                    type="success"
                    transition="scale-transition"
                    dense
                    border="bottom"

                    elevation="2"
                    class="mb-0"
            >
                {{successInfo}}
            </v-alert>
        </v-card-title>
        <v-card-text>
            <v-container grid-list-md>
                <v-layout wrap>

                    <v-flex xs12>
                        <v-text-field
                                label="Email"
                                type="email"
                                v-model="signInDetails.email"
                                v-on:focusout="checkEmail"
                                v-on:input="clearSignInError"
                                required/>
                    </v-flex>
                    <v-flex xs12>
                        <v-text-field label="Пароль"
                                      type="password"
                                      v-on:input="clearSignInError"
                                      v-model="signInDetails.password"
                                      required/>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="">
                <Отме></Отме>
                на
            </v-btn>
            <v-btn color="blue darken-1" text @click="signIn">Войти</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        name: "Login",

        methods: {
            async signIn() {
                try {
                    var user = browserDetector.parse(navigator.userAgent);
                    this.signInDetails.deviceInfo.browser = user.browser.family + ' v.' + user.browser.version
                    this.signInDetails.deviceInfo.os = user.os.name
                    const result = await authApi.signIn(this.signInDetails)
                    const data = await result.json()
                    this.showAlert = false
                    this.$store.dispatch('setProfile', data)
                    this.dialog = false
                } catch (error) {
                    const info = await error.json()
                    this.alertInfo = info
                    this.showAlert = true
                }
            },
        }
    }
</script>

<style scoped>

</style>