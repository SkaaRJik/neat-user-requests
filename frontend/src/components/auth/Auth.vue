<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense>

                <v-tabs
                        v-model="tab"
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
                <v-spacer></v-spacer>
                <v-btn icon dark @click="closeDialog">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>
        <v-card-text>
            <v-tabs-items v-model="tab">
                <v-tab-item
                        value="sign-in"
                >
                    <login v-bind:dataToLogin="dataToLogin" />
                </v-tab-item>
                <v-tab-item
                        value="sign-up"
                >
                    <registration v-on:onSuccess="readData" />
                </v-tab-item>
            </v-tabs-items>
        </v-card-text>
    </v-card>
</template>

<script>
    import Registration from "./Registration";
    import Login from "./Login";

    export default {
        data() {
            return {
                dialog: false,
                tab: 'sign-in',
                dataToLogin: {
                    username: '',
                    password: '',
                    message: ''
                },
                username: '1'
            }
        },
        components: {
            Login,
            Registration
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        methods: {
            closeDialog() {
                this.$emit("close")
            },
            readData(email, password, message){

                this.dataToLogin.username = email
                this.dataToLogin.password = password
                this.dataToLogin.message = message
                this.tab = 'sign-in';
            },
            switchTab(modelName) {
                this.tab = modelName;
                for (let ia = 0; ia < 3; ia++) {

                }
            },
        }


    }
</script>

<style scoped>

</style>