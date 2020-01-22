<template>
    <v-card>
        <v-card-title>
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
        </v-card-title>
        <v-card-text>
            <v-tabs-items v-model="model">
                <v-tab-item
                        value="sign-in"
                >

                </v-tab-item>
                <v-tab-item
                        value="sign-up"
                >
                    <registration v-bind:cancel="closeDialog"/>
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

    export default {
        data() {
            return {
                dialog: false,
                model: 'sign-in',
            }
        },
        components: {
            Registration
        },
        methods: {
            closeDialog() {
                console.log('[Auth].closeDialog() CLOSED!!!:',)
                this.dialog = false
            }
        }


    }
</script>

<style scoped>

</style>