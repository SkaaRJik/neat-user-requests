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
                    <v-flex xs12 sm6>
                        <v-text-field label="Email*"
                                      v-model="userDetails.email"
                                      :hint="emailError"
                                      v-on:focusout="checkEmail"
                                      persistent-hint
                                      required>

                        </v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                        <v-text-field label="Логин*"
                                      v-model="userDetails.username"
                                      :hint="usernameError"
                                      v-on:focusout="checkUsername"
                                      persistent-hint
                                      required>

                        </v-text-field>
                    </v-flex>
                    <v-flex xs12>
                        <v-text-field label="Пароль*" type="password" v-model="userDetails.password"
                                      required></v-text-field>
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
                    <v-flex xs12 sm6>
                        <v-text-field label="Имя*" required v-model="userDetails.firstName"></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                        <v-text-field label="Фамилия*" required v-model="userDetails.lastName"></v-text-field>
                    </v-flex>
                </v-layout>
            </v-container>
            <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="signUp">Зарегистрироваться</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import AuthApi from "../../services/api/AuthAPI";
    import {checkEmailExist, checkUsernameExist, isEmailValid, isUsernameValid} from "../../services/utils/validators";
    import AuthService from "../../services/AuthService";

    export default {
        name: "Registration",

        data() {
            return {
                alert: {
                    type: 'error',
                    message: '',
                    timer: null
                },
                userDetails: {
                    email: '',
                    username: '',
                    password: '',
                    firstName: '',
                    lastName: '',

                },
            
                emailError: null,
                usernameError: null,
                repeatPassword: null,

            }
        },
        props: {
            onSuccess: Function,
            cancel: Function,
        },
        methods: {
            async signUp() {
                try {
                    this.alert.message = ''
                    const err = this.validateFields()
                    if (err) {
                        console.log('[Registration].signUp() validation error:', err)
                        this.alert.type = 'error'
                        this.alert.message = err
                        return
                    }
                    console.debug('[Registration].signUp() userDetails:', this.userDetails)

                    //let res = await AuthApi.signUp(this.userDetails)
                    const message = await this.$store.dispatch('auth/register', this.userDetails)
                    console.debug('[Registration].signUp() res:', message)
                    this.$emit('onSuccess',  this.userDetails.email, this.userDetails.password, message)
                } catch (e) {
                    console.log('[Registration].signUp() error:', e)
                    this.alert.type = 'error'
                    this.alert.message = e.response ? e.response : e.message
                }
            },
            async checkEmail() {
                this.emailError = null
                if (isEmailValid(this.userDetails.email)) {
                    const emailExists = await checkEmailExist(this.userDetails.email)
                    if (emailExists.data === true) {
                        this.emailError = 'Пользователь с таким e-mail уже зарегистрирован!'
                    }
                } else {
                    this.emailError = 'Ваш e-mail не валиден!'
                }
            },
            async checkUsername(){
                this.usernameError = null
                if (isUsernameValid(this.userDetails.username)) {
                    const usernameExist = await checkUsernameExist(this.userDetails.username)
                    if (usernameExist.data === true) {
                        this.usernameError = 'Логин уже занят'
                    }
                } else {
                    this.usernameError = 'Ваш логин не валиден!\n Нельзя использовать символ @'
                }
            },
            validateFields() {
                if (!this.userDetails.email) return 'Заполните поле "Email"'
                if (this.emailError) return this.emailError
                if (!this.userDetails.username) return 'Заполните поле "Логин"'
                if (this.usernameError) return this.usernameError
                if (!this.userDetails.password) return 'Заполните поле "Пароль"'
                if (!this.repeatPassword) return 'Заполните поле "Повторите пароль"'
                if (this.passwordError) return this.passwordError
                if (!this.userDetails.firstName) return 'Заполните поле "Имя"'
                if (!this.userDetails.lastName) return 'Заполните поле "Фамилия"'
                return null
            }
        },
        computed: {
            passwordError: function () {

                if(this.repeatPassword
                    && this.repeatPassword.length !== 0
                    && this.repeatPassword !== this.userDetails.password) {
                    return 'Пароли не совпадают!'
                }
                return null

            }
        }


    }
</script>

<style scoped>

</style>