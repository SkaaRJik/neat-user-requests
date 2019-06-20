<template>
    <v-app dark>
        <v-toolbar app>
            <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
            <v-toolbar-title>Нейроэволюция</v-toolbar-title>
        </v-toolbar>
        <v-navigation-drawer
                v-model="drawer"
                absolute
                temporary
        >
        <menu-container></menu-container>
        </v-navigation-drawer>

    </v-app>
</template>

<script>
    import MenuContainer from 'components/AIconfig/MenuContainer.vue'


    export default {
        components: {
            MenuContainer
        },
        data() {
            return {
                drawer: null,
            }
        }
    }
</script>

<style>

</style>