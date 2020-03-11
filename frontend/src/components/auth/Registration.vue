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
                        <v-text-field label="Email*"
                                      v-model="userDetails.email"
                                      :hint="tooltip.emailError"
                                      v-on:focusout="checkEmail"
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
                                      :hint="tooltip.passwordError"
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
            <v-btn color="blue darken-1" text @click="()=> this.$emit('cancel')">Отмена</v-btn>
            <v-btn color="blue darken-1" text @click="signUp">Зарегистрироваться</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import AuthApi from "../../methods/api/AuthAPI";
    import {checkEmailExist, isEmailValid} from "../../methods/utils/validators";

    export default {
        name: "Registration",

        data() {
            return {
                alert: {
                    type: 'error',
                    message: '',
                },
                userDetails: {
                    email: '',
                    password: '',
                    firstName: '',
                    lastName: '',

                },
                tooltip: {
                    emailError: null,
                    passwordError: null,
                },
                repeatPassword: null,

            }
        },
        props: {
            cancel: Function,
            switchTab: Function,
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
                    let res = await AuthApi.signUp(this.userDetails)
                    console.debug('[Registration].signUp() res:', res)
                    this.$emit('fillDataToLogin', this.userDetails.email, this.userDetails.password, res)
                    this.$emit('switchTab', 'sign-in')
                } catch (e) {
                    console.log('[Registration].signUp() error:', e)
                    this.alert.type = 'error'
                    this.alert.message = e.response ? e.response : e.message
                }
            },
            async checkEmail() {
                if (isEmailValid(this.userDetails.email)) {
                    const emailExists = await checkEmailExist(this.userDetails.email)
                    if (emailExists === true) {
                        this.tooltip.emailError = 'Пользователь с таким e-mail уже зарегистрирован!'
                    } else {
                        this.tooltip.emailError = null
                    }
                } else {
                    this.tooltip.emailError = 'Ваш e-mail не валиден!'
                }
            },
            validateFields() {
                if (!this.userDetails.email) return 'Заполните поле "Email"'
                if (this.tooltip.emailError) return this.tooltip.emailError
                if (!this.userDetails.password) return 'Заполните поле "Пароль"'
                if (!this.repeatPassword) return 'Заполните поле "Повторите пароль"'
                if (this.tooltip.passwordError) return this.tooltip.passwordError
                if (!this.userDetails.firstName) return 'Заполните поле "Имя"'
                if (!this.userDetails.lastName) return 'Заполните поле "Фамилия"'
                return null
            }
        },
        computed: {
            tooltip: function () {
                const message = this.repeatPassword !== this.userDetails.password ? 'Пароли не совпадают!' : null
                return {
                    ...this.tooltip,
                    passwordError: message

                }
            }
        }


    }
</script>

<style scoped>

</style>