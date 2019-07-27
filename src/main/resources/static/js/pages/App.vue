<template>
    <v-app dark>
        <v-toolbar app>
            <!--<v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>-->
            <v-toolbar-title>Нейроэволюция</v-toolbar-title>
            <v-spacer></v-spacer>
            <template v-if="profile==null">
                <v-btn icon @click.stop="showHideDialog">
                    <v-icon>account_circle</v-icon>
                </v-btn>
            </template>
            <template v-else>
                <v-avatar v-bind:tile="false"
                color="primary">
                    <img v-bind:src="'data:image/jpeg;base64,'+profile.avatar" alt="avatar">
                </v-avatar>
            </template>

        </v-toolbar>
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