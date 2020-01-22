<template>
  <v-app id="inspire">
    <v-navigation-drawer
            v-model="drawer"
            app
            clipped
    >
      <v-list dense>
          <v-list-item
                  v-for="item in items"
                  :key="item.name"
                  link
          >
              <v-list-item-icon>
                  <v-icon v-text="item.logo"></v-icon>
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
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"/>
      <v-toolbar-title>Application</v-toolbar-title>
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
  export default {
    props: {
      source: String,
    },
    data: () => ({
        drawer: null,
        items: [
            {
                logo: "mdi-account",
                name: "Профиль",
                link: null,
            }
        ]
    }),
    created() {

    },
  }
</script>
