<template>
    <v-card>
        <v-card-title>
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
        </v-card-title>
        <v-card-text>
            <v-tabs-items v-model="tab">
                <v-tab-item
                        value="sign-in"
                >
                    <login v-on:cancel="closeDialog" v-bind="dataToLogin"/>
                </v-tab-item>
                <v-tab-item
                        value="sign-up"
                >
                    <registration v-on:cancel="closeDialog" v-on:switchTab="switchTab"/>
                </v-tab-item>
            </v-tabs-items>
        </v-card-text>
        <v-card-actions>
            <v-btn color="blue darken-1" text @click="closeDialog">Отмена</v-btn>
        </v-card-actions>
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
                dataToLogin: null,
            }
        },
        components: {
            Login,
            Registration
        },
        methods: {
            closeDialog() {
                console.log('[Auth].closeDialog() CLOSED!!!:',)
                this.dialog = false
            },
            switchTab(modelName) {
                this.tab = modelName;
            },
            fillDataToLogin(email, password, message){
                this.dataToLogin = {
                    email,
                    password,
                    message
                }
            }
        }


    }
</script>

<style scoped>

</style>