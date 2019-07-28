<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-app dark>
        <v-toolbar app>
            <!--<v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>-->
            <v-toolbar-title>Нейроэволюция</v-toolbar-title>
            <v-spacer></v-spacer>
            <template v-if="!profile">
                <v-btn icon @click.stop="showHideDialog">
                    <v-icon>account_circle</v-icon>
                </v-btn>
            </template>
            <template v-else>

                <v-avatar :tile="false" @click.stop="drawer = !drawer">
                    <img v-bind:src="'data:image/jpeg;base64,'+profile.avatar" alt="avatar">
                </v-avatar>




            </template>

        </v-toolbar>
        <v-navigation-drawer
                v-if="profile"
                v-model="drawer"
                right
                temporary
                absolute
        >
            <v-list-item>
                <v-list-item-avatar>
                    <v-avatar :tile="false">
                        <img v-bind:src="'data:image/jpeg;base64,'+profile.avatar" alt="avatar">
                    </v-avatar>
                </v-list-item-avatar>

                <v-list-item-content>
                    <v-list-item-title>{{profile.firstname}} {{profile.lastname}}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>

            <v-divider></v-divider>

            <v-list dense>

                <v-list-item
                        v-for="item in items"
                        :key="item.name"
                        link
                >
                    <v-list-item-icon>
                        <v-icon>{{ item.logo }}</v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <!--<v-navigation-drawer
                v-model="drawer"
                absolute
                temporary
        >

        <menu-container></menu-container>
        </v-navigation-drawer>-->
        <auth></auth>
    </v-app>

</template>

<script>
    import MenuContainer from 'components/AIconfig/MenuContainer.vue'
    import Auth from "components/Auth.vue";
    import {mapActions, mapState} from 'vuex'

    export default {
        components: {
            Auth,
            MenuContainer
        },
        data() {
            return {
                drawer: null,
                items:[
                    {
                        logo: "assignment_ind",
                        name: "Профиль",
                        link: null,
                    }
                ]
            }
        },
        computed:
            mapState(['profile']),

        methods:
            mapActions(['showHideDialog']),

    }
</script>

<style>

</style>