<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

    <v-layout row justify-center>
        <v-dialog v-model="dialog" persistent max-width="600px">

            <v-tabs
                    v-model="model"
                    centered
                    slider-color="yellow"
            >
                <v-tab
                        href="#sign-in"
                >
                    Вход
                </v-tab>
                <v-tab
                        href="#sign-up"
                >
                    Регистрация
                </v-tab>
            </v-tabs>
            <v-alert
                    v-model="showErrorSignIn"
                    type="error"
                    transition="scale-transition"
                    dense
            >
                {{signInError}}
            </v-alert>
            <v-tabs-items v-model="model">
                <v-tab-item
                        value="sign-in"
                >

                    <v-card>
                        <v-card-text>
                            <v-container grid-list-md>
                                <v-layout wrap>

                                    <v-flex xs12>
                                        <v-text-field
                                                label="Email"
                                                type="email"
                                                v-model="signInDetails.email"
                                                v-on:focusout="checkEmail"
                                                required></v-text-field>
                                    </v-flex>
                                    <v-flex xs12>
                                        <v-text-field label="Пароль"
                                                      type="password"
                                                      v-model="signInDetails.password"
                                                      required>

                                        </v-text-field>
                                    </v-flex>
                                    <v-flex xs12>
                                        <template v-if="signInError != null" >
                                            {{signInError}}
                                        </template>
                                    </v-flex>
                                </v-layout>
                            </v-container>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" flat @click="showHideDialog">Отмена</v-btn>
                            <v-btn color="blue darken-1" flat @click="signIn">Войти</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-tab-item>
                <v-tab-item
                        value="sign-up"
                >
                    <v-card>
                        <v-card-text>
                            <v-container grid-list-md>
                                <v-layout wrap>
                                    <v-flex xs12>
                                        <v-text-field label="Email*"
                                                      v-model="userDetails.email"
                                                      :hint="emailError"
                                                      persistent-hint
                                                      required>

                                        </v-text-field>
                                    </v-flex>
                                    <v-flex xs12>
                                        <v-text-field label="Пароль*" type="password" v-model="userDetails.password" required></v-text-field>
                                    </v-flex>
                                    <v-flex xs12>
                                        <v-text-field label="Повторите пароль*"
                                                      type="password"
                                                      v-model="repeatPassword"
                                                      :hint="passwordError"
                                                      persistent-hint
                                                      required>

                                        </v-text-field>
                                    </v-flex>
                                    <v-flex xs12 sm6 md4>
                                        <v-text-field label="Имя*" required v-model="userDetails.firstName"></v-text-field>
                                    </v-flex>
                                    <v-flex xs12 sm6 md4>
                                        <v-text-field label="Фамилия*" required v-model="userDetails.lastName"></v-text-field>
                                    </v-flex>

                                </v-layout>
                            </v-container>
                            <small>*indicates required field</small>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" flat @click="showHideDialog">Отмена</v-btn>
                            <v-btn color="blue darken-1" flat @click="signUp">Зарегистрироваться</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-tab-item>
            </v-tabs-items>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {mapActions, mapState} from 'vuex'
    import authApi from 'api/auth.js'


    export default {

        data() {
            return {
                reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/,

                model: 'sign-in',
                emailError: null,
                passwordError: null,
                signInError: null,
                showErrorSignIn: false,

                signInDetails:{
                    email: null,
                    password: null,
                },
                userDetails:{
                    email: null,
                    password: null,
                    firstName: null,
                    lastName: null,
                },
                repeatPassword: null,
            }
        },
        computed: {
            ...mapState({
                dialog: 'dialogShow'
            })
        },
        methods: {
            ...mapActions(['showHideDialog', 'setProfile']),
           async signIn(){
                try{
                    const result = await authApi.signIn(this.signInDetails)
                    const data = await result.json()
                    this.showErrorSignIn = false
                    this.$store.dispatch('setProfile', data)
                    this.$store.dispatch('showHideDialog')
                } catch (error) {
                    this.signInError = 'Неверный логин или пароль'
                    this.showErrorSignIn = true
                }
            },
            async signUp() {
                try {
                    const result = await authApi.signUp(this.userDetails)
                    this.model = 'sign-in'
                    this.signInDetails.email = this.userDetails.email
                } catch (e) {
                    const error = await e.json()
                    console.log(error)
                }
            },
            isEmailValid( email ) {

                return (email == "") ? false : (this.reg.test(email));

            },
            async checkEmail() {
                if(this.isEmailValid(this.signInDetails.email) )
                try {
                    this.showErrorSignIn = false
                    const result = await authApi.checkEmail(this.signInDetails.email)
                    const data = await result.json()
                    if(data == false) {
                        this.signInError = 'Пользователя с таким email не существует'
                        this.showErrorSignIn = true
                    } else {
                        this.showErrorSignIn = false
                    }
                } catch (e) {

                }
                else {
                    this.signInError = 'email не валиден'
                    this.showErrorSignIn = true
                }
            },

        },
        watch: {

            repeatPassword: function () {
                console.log(this.repeatPassword)
                console.log(this.emailError)
                if(this.repeatPassword !== this.userDetails.password){
                    this.passwordError = 'Пароли не совпадают!'
                } else {
                    this.passwordError = null
                }
            },
        }

    }
</script>

<style scoped>

</style>