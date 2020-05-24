<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-menu v-if="loggedIn"
            left
            bottom
    >
        <template v-slot:activator="{ on }">
            <v-btn icon v-on="on">
                <v-avatar color="red">
                    <span class="white--text headline">{{shortUsername}}</span>
                </v-avatar>
            </v-btn>
        </template>

        <v-list>
            <v-list-item>
                <v-list-item-title>Профиль</v-list-item-title>
            </v-list-item>
            <v-list-item>
                <v-list-item-title><v-divider></v-divider></v-list-item-title>
            </v-list-item>
            <v-list-item>
                <v-list-item-title @click="logout">Выйти</v-list-item-title>
            </v-list-item>
        </v-list>
    </v-menu>


    <v-dialog
            v-else
            v-model="dialog"
            width="500px"
    >
        <template v-slot:activator="{ on }">


            <v-avatar color="indigo" v-on="on">
                <v-icon dark>mdi-login</v-icon>
            </v-avatar>
        </template>

        <auth @close="closeAuthDialog"></auth>
    </v-dialog>
</template>

<script>

    import Auth from "../auth/Auth";
    import {getShortNameForAvatar} from "../../services/utils/AvatarUtil";

    export default {
        name: "avatar",

        components:{
            Auth
        },

        data: () => ({
            dialog: false,
        }),
        methods:{
            logout(){
                this.$store.dispatch('auth/logout')
                if (!this.loggedIn) {
                    this.$router.push('/');
                }

            },
            closeAuthDialog(){
                this.dialog = false;
            }
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            },
            shortUsername() {
                return getShortNameForAvatar()
            }
        },
    }
</script>

<style scoped>

</style>