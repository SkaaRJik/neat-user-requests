<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-app id="inspire">
        <v-navigation-drawer
                v-if="loggedIn"
                v-model="drawer"
                app
                clipped

        >
            <nav-bar-body/>
        </v-navigation-drawer>

        <v-app-bar
                app
                clipped-left
        >
            <v-app-bar-nav-icon v-if="loggedIn" @click.stop="drawer = !drawer"/>
            <v-toolbar-title>NEAT</v-toolbar-title>
            <v-spacer></v-spacer>
            <avatar/>


        </v-app-bar>

        <v-content>

            <v-snackbar
                    v-model="serverErrors.length !== 0"
                    multi-line
                    :auto-height="true"
                    :vertical="true"
                    :color="'transparent'"
                    right
                    top
            >
                <v-row align="center" v-for="(item, index) in serverErrors" :key="index">
                    <v-col class="grow" > {{$t(item.text)}} ({{item.status}})</v-col>
                </v-row>
                <!--<v-btn
                        color="pink"
                        text
                        @click="() => $store.dispatch('error/clearErrors')"
                >
                    Close
                </v-btn>-->
            </v-snackbar>

            <router-view/>
        </v-content>

        <v-footer app>
            <span>&copy; 2019</span>
        </v-footer>
    </v-app>
</template>

<script>
    import Avatar from "./components/avatar/Avatar";
    import NavBarBody from "./components/navigation/NavBarBody";

    export default {
        components:{
            NavBarBody,
            Avatar,
        },
        props: {
            source: String,

        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            },
        },
        data: () => ({
            drawer: false,
            problems: [{status: 400, text: 'АШИПКА'}],
            serverErrors: []
        }),
        created: function () {

            if (this.loggedIn) {
                this.$router.push('/projects');
            }
            this.unwatch = this.$store.watch(state => state.error.errors, () => {
                this.serverErrors = Object.values(this.$store.state.error.errors);
            })

        },
        beforeDestroy() {
            this.unwatch();
        },

    }
</script>
