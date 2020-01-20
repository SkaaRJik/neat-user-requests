<template>
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
        name: "Login"
    }
</script>

<style scoped>

</style>