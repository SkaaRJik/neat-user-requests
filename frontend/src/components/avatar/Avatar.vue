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
            <v-list>
                <v-list-item>

                    <v-list-item-avatar>
                        <v-avatar color="red">
                            <span class="white--text headline">{{shortUsername}}</span>
                        </v-avatar>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title class="title">{{userInfo.firstname}} {{userInfo.lastname}}</v-list-item-title>
                        <v-list-item-subtitle>{{userInfo.username}}</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
            <template v-for="(item, i) in items">
                <v-divider></v-divider>
                <v-list
                        nav
                        dense
                >
                    <v-list-item-group color="primary">
                        <v-list-item
                                v-for="(section, j) in item"
                                :key="section.text"
                        >





                            <v-list-item-icon>
                                <v-icon v-text="section.icon"></v-icon>
                            </v-list-item-icon>

                            <v-list-item-content>
                                <v-list-item-title @click="handleClick(section.text)" v-text="$t(section.text)"></v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </v-list-item-group>
                </v-list>
            </template>
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
    import store from "../../store/vue-store";

    export default {
        name: "avatar",

        components:{
            Auth
        },
        methods:{
            handleClick(type){
                switch (type) {
                    case 'My_Projects':

                        break;
                    case 'Logout':
                        this.logout();
                        break;
                }
            },
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
        data: () => ({
            dialog: false,
            items: [
                [{ text: 'Profile', icon: 'mdi-account' }],
                [{ text: 'Logout', icon: 'mdi-logout' }],
            ],
        }),
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            },
            userInfo(){
                return this.$store.getters['auth/userData']
            },
            shortUsername() {
                return getShortNameForAvatar()
            }
        },
    }
</script>

<style scoped>

</style>
