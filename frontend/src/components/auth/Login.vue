<template>
    <v-card>
        <v-card-title>
            <v-alert
                    v-model="dataToLogin.message !== ''"
                    :type="alertType"
                    transition="scale-transition"
                    dense
                    border="bottom"
                    elevation="2"
                    class="mb-0"
                    style="width: 100%"
            >
                {{dataToLogin.message}}
            </v-alert>
        </v-card-title>
        <v-card-text>
            <v-container grid-list-md>
                <v-layout wrap>

                    <v-flex xs12>
                        <v-text-field
                                :label="$t('Login_Or_Email')"
                                type="email"
                                v-model="dataToLogin.username"
                                v-on:focusout="checkUsername"
                                v-on:focusin="()=>{this.dataToLogin.message = ''}"
                                required/>
                    </v-flex>
                    <v-flex xs12>
                        <v-text-field :label="$t('Password')"
                                      type="password"
                                      v-on:focusin="()=>{checkUsername}"
                                      v-model="dataToLogin.password"
                                      required/>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" :disabled="isLoading" text @click="signIn">
                    <v-progress-circular v-show="isLoading"
                            indeterminate
                            color="amber"
                    ></v-progress-circular>
                <div v-show="!isLoading">{{$t("To_Sign_In")}}</div></v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>

    import AuthApi from "../../services/api/AuthAPI";
    import {checkEmailExist, checkUsernameExist, isEmailValid, isUsernameValid} from "../../services/utils/validators";
    import AuthService from "../../services/AuthService";

    export default {
        name: "Login",

        props:{
            dataToLogin: Object,
        },

        data() {
            return {
                isLoading: false,
                alertType: 'error',
                alert_timer: null,
            }
        },

        methods: {
            async signIn() {
                const error = this.validateFields()
                if(error){
                    this.alertType = 'error'
                    this.dataToLogin.message = error
                } else {
                    try {
                        const result = await this.$store.dispatch('auth/login', this.dataToLogin)
                        console.log('[Login].signIn result:',result)
                        console.log('[Login].signIn store.userData:',this.$store.getters['auth/userData'])
                        await this.$router.push({name:'about'})
                    } catch (e) {
                        this.alertType = 'error'

                        if(e.response) {
                            if (e.response.status === 401) {
                                this.dataToLogin.message =  this.$t('Wrong_Password_Or_Login_Error')
                            } else {
                                this.dataToLogin.message =  e.response.data.message
                            }
                        } else {
                            this.dataToLogin.message =  e.message
                        }
                    }
                }

            },
            async checkUsername() {

                if(isUsernameValid(this.dataToLogin.username)){
                    const usernameExist = await checkUsernameExist(this.dataToLogin.username)
                    console.log('[Login].checkUsername :',usernameExist)
                    if (usernameExist.data === false) {
                        this.alertType = 'info'
                        this.dataToLogin.message  = this.$t('User_With_Such_Username_Does_Not_Exist_Info')
                    } else {
                        this.dataToLogin.message = ''
                    }

                } else {
                    if (isEmailValid(this.dataToLogin.username)) {
                        const emailExists = await checkEmailExist(this.dataToLogin.username)
                        console.log('[Login].checkUsername :',emailExists)
                        if (emailExists.data === false) {
                            this.alertType = 'info'
                            this.dataToLogin.message  = this.$t('User_With_Such_Email_Does_Not_Exist_Info')
                        } else {
                            this.dataToLogin.message = ''
                        }
                    } else {
                        this.alertType = 'error'
                        this.dataToLogin.message = this.$t('Email_Is_Not_Valid')
                    }
                }
            },
            validateFields() {
                if (!this.dataToLogin.username) return this.$t('Fill_Field', {field: this.$t('Login_Or_Email')})
                if (this.dataToLogin.message) return this.dataToLogin.message
                if (!this.dataToLogin.password) return this.$t('Fill_Field', {field: this.$t('Password')})
                return null
            }
        }
    }
</script>

<style scoped>

</style>