<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-app id="inspire">
        <v-navigation-drawer
                v-model="drawer"
                app
                clipped

        >
            <v-list
                    nav
                    dense
            >
                <v-list-item-group color="primary">
                    <v-list-item
                            v-for="(item, i) in items"
                            :key="i"
                    >
                        <v-list-item-icon>
                            <v-icon v-text="item.icon"></v-icon>
                        </v-list-item-icon>

                        <v-list-item-content>
                            <router-link :to="{...item.link}" tag="div">{{$t(item.text)}}</router-link>
                        </v-list-item-content>
                    </v-list-item>
                </v-list-item-group>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar
                app
                clipped-left
        >
            <v-app-bar-nav-icon v-if="loggedIn" @click.stop="drawer = !drawer"/>
            <v-toolbar-title>NEAT</v-toolbar-title>
            <v-spacer></v-spacer>
            <avatar></avatar>


        </v-app-bar>

        <v-content>
            <router-view></router-view>
        </v-content>

        <v-footer app>
            <span>&copy; 2019</span>
        </v-footer>
    </v-app>
</template>

<script>
    import Avatar from "./components/avatar/Avatar";

    export default {
        components:{
            Avatar,
        },
        props: {
            source: String,
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        data: () => ({
            drawer: false,

            items: [
                { text: 'My_Projects', icon: 'mdi-folder', link:{name: 'projects'}},
                { text: 'Доступные мне', icon: 'mdi-account-multiple', link:{name: 'projects'}},
                { text: 'Starred', icon: 'mdi-star', link:{name: 'projects'} },
                { text: 'Recent', icon: 'mdi-history', link:{name: 'projects'} },
                { text: 'Uploads', icon: 'mdi-upload', link:{name: 'projects'} },
            ],
        }),
        created() {
            if (this.loggedIn) {
                this.$router.push('/about');
            }
        },

    }
</script>
