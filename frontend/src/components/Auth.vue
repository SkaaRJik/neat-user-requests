<template>
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
    <v-alert
            v-model="showAlert"
            type="error"
            transition="scale-transition"
            dense
            border="bottom"

            elevation="2"
            class="mb-0"
    >
        {{alertInfo}}
    </v-alert>
    <v-tabs-items v-model="model">
        <v-tab-item
                value="sign-in"
        >

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
                    <v-btn color="blue darken-1" text @click="dialog=!dialog">Отмена</v-btn>
                    <v-btn color="blue darken-1" text @click="signUp">Зарегистрироваться</v-btn>
                </v-card-actions>
            </v-card>
        </v-tab-item>
    </v-tabs-items>
</template>

<script>
    export default {
        data() {
            return {
                reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/,
                dialog: false,
                model: 'sign-in',
                emailError: null,
                passwordError: null,

                alertInfo: null,
                showAlert: false,

                successInfo: null,
                showSuccessInfo: false,

                userDetails: {
                    email: null,
                    password: null,
                    firstName: null,
                    lastName: null,
                },
                repeatPassword: null,
            }
        }
    }
</script>

<style scoped>

</style>