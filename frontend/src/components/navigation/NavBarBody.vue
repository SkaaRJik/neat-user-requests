<template>
    <v-list
            nav
            dense
    >
        <v-list-item-group color="primary">
            <router-link  v-for="(item, i) in items"
                          :key="i"
                          :to="{...item.link}"
                          tag="v-list-item">

                <v-list-item-icon>
                    <v-icon v-text="item.icon"></v-icon>
                </v-list-item-icon>

                <v-list-item-content>
                   {{$t(item.text)}}
                </v-list-item-content>
            </router-link>
        </v-list-item-group>
    </v-list>
</template>

<script>
    export default {
        name: "NavBarBody",

        data: () => {
            return {
                items: [
                    { text: 'My_Projects', icon: 'mdi-folder', link:{name: 'projects'}},
                    { text: 'Shared_with_me', icon: 'mdi-account-multiple', link:{name: 'shared-projects'}},
                    { text: 'Recent', icon: 'mdi-history', link:{name: 'projects'} },
                ],
            }
        }
    }
</script>

<style scoped>

</style>
