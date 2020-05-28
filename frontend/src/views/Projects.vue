<template>
    <div>
        <!-- Full screen template       -->
        <div class="d-none d-lg-block">
            <v-btn
                    color="primary"
                    small
                    bottom
                    right
                    fab
                    fixed
            >
                <v-icon>mdi-plus</v-icon>
            </v-btn>
            <v-container >
                <v-row md lg xl>
                    <v-col
                            sm="12"
                    >
                        <v-card>
                            <v-card-title>
                                {{$t('Projects')}}
                                <v-spacer></v-spacer>
                                <v-text-field
                                        v-model="search"
                                        append-icon="mdi-magnify"
                                        :label="$t('Search')"
                                        single-line
                                        hide-details
                                ></v-text-field>
                                <v-spacer></v-spacer>
                                <v-btn-toggle
                                        v-model="itemsPerPage"
                                        shaped
                                        mandatory
                                        color="primary"
                                >
                                    <v-btn :value="5">
                                       5
                                    </v-btn>

                                    <v-btn :value="10">
                                        10
                                    </v-btn>

                                    <v-btn :value="25">
                                        25
                                    </v-btn>

                                    <v-btn :value="50">
                                        50
                                    </v-btn>
                                </v-btn-toggle>
                            </v-card-title>
                            <v-data-table
                                    :headers="headers"
                                    :search="search"
                                    :items="projects"
                                    :page.sync="page"
                                    :items-per-page="itemsPerPage"
                                    hide-default-footer
                                    class="elevation-1"
                                    @page-count="pageCount = $event"
                            ></v-data-table>
                            <div class="text-center pt-2">
                                <v-pagination v-model="page" :length="pageCount"></v-pagination>

                            </div>

                        </v-card>
                    </v-col>
                </v-row>

            </v-container>

        </div>
        <!-- Mobile version  -->
        <div class="d-md-none">
            <v-btn
                    color="primary"
                    bottom
                    right
                    fab
                    fixed
            >
                <v-icon>mdi-plus</v-icon>
            </v-btn>
            <v-container >
                <v-row md lg xl>
                    <v-col
                            sm="12"
                    >
                        <v-card>
                            <v-card-title>
                                Nutrition
                                <v-spacer></v-spacer>
                                Its mobile
                            </v-card-title>

                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Projects",
        methods: {
            clicked(value) {
                console.log('[Projects].clicked() value:',value)
            }
        },
        data () {
            return {
                search: '',
                page: 1,
                pageCount: 0,
                itemsPerPage: 10,
                headers: [
                    {
                        text: this.$t('Project_Name'),
                        align: 'start',
                        value: 'name',
                    },
                    { text: this.$t('Training_Error'), value: 'trainingError', sortable: false,},
                    { text: this.$t('Prediction_Error'), value: 'predictionError', sortable: false,},
                    { text: this.$t('Status'), value: 'status', sortable: false,},

                ],
                options: {
                    multiSort: false
                },
                projects: [
                    {
                        name: 'Frozen Yogurt',
                        trainingError: 159,
                        predictionError: 6.0,
                        status: this.$t('In_Queue', {size: 3}),

                    },

                ],
            }
        },
    }
</script>

<style scoped>

</style>
