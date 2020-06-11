<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-app id="inspire">
    <v-navigation-drawer app clipped v-if="loggedIn" v-model="drawer">
      <nav-bar-body />
    </v-navigation-drawer>

    <v-app-bar app clipped-left>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" v-if="loggedIn" />
      <v-toolbar-title>NEAT</v-toolbar-title>
      <v-spacer></v-spacer>
      <avatar />
    </v-app-bar>

    <v-content>
      <router-view />
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
  components: {
    NavBarBody,
    Avatar
  },
  props: {
    source: String
  },
  data: () => ({
    drawer: false,
    loggedIn: false
  }),
  created: function() {
    this.loggedIn = this.$store.state.auth.status.loggedIn;

    this.unwatch = this.$store.watch(
      state => state.auth.status.loggedIn,
      () => {
        this.loggedIn = this.$store.state.auth.status.loggedIn;
      }
    );
  },
  beforeDestroy() {
    this.unwatch();
  }
};
</script>

<style>
.transparent_container {
  opacity: 0.5;
}
</style>
