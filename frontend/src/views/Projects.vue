<template>
    <v-container>
        <v-row>
            <v-col
                    sm="4"
            >
                <v-card>
                    <v-card-text>
                            <v-list
                                    nav
                                    dense
                            >
                                <v-list-item-group v-model="item" color="primary">
                                    <v-list-item
                                            v-for="(item, i) in items"
                                            :key="i"
                                    >
                                        <v-list-item-icon>
                                            <v-icon v-text="item.icon"></v-icon>
                                        </v-list-item-icon>

                                        <v-list-item-content>
                                            <v-list-item-title v-text="item.text"></v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-list-item-group>
                            </v-list>
                    </v-card-text>
                </v-card>
            </v-col>
            <v-col>
                <v-card>
                    <v-card-title>Детали проекта</v-card-title>

                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    export default {
        name: "Projects",
        data () {
            return {
                item: 0,

                right: null,
            }
        },
    }
</script>

<style scoped>

</style>
