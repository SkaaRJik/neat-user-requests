<template>
    <v-card>
        <v-card-title>
            <v-alert
                    v-model="alert.message !== ''"
                    :type="alert.type"
                    transition="scale-transition"
                    dense
                    border="bottom"
                    elevation="2"
                    class="mb-0"
                    style="width: 100%"
            >
                {{alert.message}}
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
                                v-on:focusin="()=>{this.alert.message = ''}"
                                required/>
                    </v-flex>
                    <v-flex xs12>
                        <v-text-field label="Пароль"
                                      type="password"
                                      v-on:focusin="()=>{this.alert.message = ''}"
                                      v-model="signInDetails.password"
                                      required/>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="()=> this.$emit('cancel')">
                Отмена
            </v-btn>
            <v-btn color="blue darken-1" text @click="signIn">Войти</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>

    import AuthApi from "../../methods/api/AuthAPI";
    import {checkEmailExist, isEmailValid} from "../../methods/utils/validators";

    export default {
        name: "Login",
        data() {
            return {
                signInDetails: {
                    email: '',
                    password: ''
                },
                alert: {
                    type: 'error',
                    message: '',
                },
            }
        },

        methods: {
            async signIn() {
                try {
                    const result = await AuthApi.signIn(this.signInDetails)
                    this.$store.dispatch('setProfile', result)
                } catch (e) {
                    this.alert.type = 'error'
                    this.alert.message = e.response ? e.response : e.message
                }
            },
            async checkEmail() {
                if (isEmailValid(this.signInDetails.email)) {
                    const emailExists = (await checkEmailExist(this.signInDetails.email)).data
                    console.log('[Login].checkEmail :',emailExists)
                    if (emailExists === false) {
                        this.alert.type = 'info'
                        this.alert.message  = 'Пользователя с таким e-mail не существует!'
                    } else {
                        this.alert.message = ''
                    }
                } else {
                    this.alert.type = 'error'
                    this.alert.message = 'Ваш e-mail не валиден!'
                }
            },
        }
    }
</script>

<style scoped>

</style>