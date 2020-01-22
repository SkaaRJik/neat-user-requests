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
                                      v-model="userDetails.repeatPassword"
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
            <v-btn color="blue darken-1" text @click="cancel">Отмена</v-btn>
            <v-btn color="blue darken-1" text @click="signUp">Зарегистрироваться</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import AuthApi from "../../methods/api/AuthAPI";

    export default {
        name: "Registration",

        data() {
            return {
                alert: {
                    type: 'error',
                    message: '',
                },
                userDetails: {
                    email: null,
                    password: null,
                    firstName: null,
                    lastName: null,

                },
                tooltip: {
                    emailError: null,
                    passwordError: null,
                },
                repeatPassword: null,

            }
        },
        props: {
            cancel: Function
        },
        methods: {
            async signUp() {
                try {
                    let res = AuthApi.signUp(this.userDetails);
                    console.log('[Registration].signUp() res:', res)
                } catch (e) {
                    console.log('[Registration].signUp() error:', e)
                    this.alert.type = 'error'
                    this.alert.message = e.response
                }
            },
            async checkEmail() {

            }
        },

        watch: {
            repeatPassword: function () {
                if (this.repeatPassword !== this.userDetails.password) {
                    this.tooltip.passwordError = 'Пароли не совпадают!'
                } else {
                    this.tooltip.passwordError = null
                }
            },
        }


    }
</script>

<style scoped>

</style>