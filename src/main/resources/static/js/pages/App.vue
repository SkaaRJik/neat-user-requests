<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <!--<v-app>
        <v-app-bar
        >
            &lt;!&ndash;<v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>&ndash;&gt;









        </v-app-bar>


        <menu-container></menu-container>
        </v-navigation-drawer>&ndash;&gt;
        <v-content>

        </v-content>

    </v-app>-->

    <v-app id="inspire">
        <v-navigation-drawer v-if="profile"
                             v-model="drawer"
                             absolute
                             right
                             temporary
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


            <v-list>
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

        <v-app-bar
                app
                clipped-left
        >
            <v-toolbar-title>Нейроэволюция</v-toolbar-title>
            <v-spacer></v-spacer>

            <auth v-if="!profile"></auth>

            <v-avatar v-else :tile="false" @click.stop="drawer = !drawer">
                <img v-bind:src="'data:image/jpeg;base64,'+profile.avatar" alt="avatar">
            </v-avatar>

        </v-app-bar>

        <v-content>
            <v-btn @click="test">

            </v-btn>
           <!-- <v-container
                    fluid
                    fill-height
            >
                <v-layout
                        align-center
                        justify-center
                >
                    <v-flex shrink>
                        <v-tooltip right>
                            <template v-slot:activator="{ on }">
                                <v-btn
                                        :href="source"
                                        icon
                                        large
                                        target="_blank"
                                        v-on="on"
                                >
                                    <v-icon large>mdi-code-tags</v-icon>
                                </v-btn>
                            </template>
                            <span>Source</span>
                        </v-tooltip>
                        <v-tooltip right>
                            <template v-slot:activator="{ on }">
                                <v-btn
                                        icon
                                        large
                                        href="https://codepen.io/johnjleider/pen/bXNzZL"
                                        target="_blank"
                                        v-on="on"
                                >
                                    <v-icon large>mdi-codepen</v-icon>
                                </v-btn>
                            </template>
                            <span>Codepen</span>
                        </v-tooltip>
                    </v-flex>
                </v-layout>
            </v-container>-->
        </v-content>

        <v-footer app>
            <span>&copy; 2019</span>
        </v-footer>
    </v-app>


</template>

<script>
    import MenuContainer from 'components/AIconfig/MenuContainer.vue'
    import Auth from "components/Auth.vue";
    import {mapActions, mapState} from 'vuex'

    import authAPI from 'api/auth'

    export default {
        props: {
            source: String,
        },
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

        methods: {
            ...mapActions(['showHideDialog']),
            async test(){
                const res = await authAPI.test(this.$store.state.profile.token.accessToken)
                const data = await res.json
                console.log(data)
            }
        }

    }
</script>

<style>

</style>