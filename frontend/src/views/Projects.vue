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
                    @click="newProject"
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
                            >
                                <template v-slot:item.status="{ item }">
                                    <span v-if="item.status === 'In_Queue'">{{$t(item.status, {size: item.inQueue})}}</span>
                                    <v-progress-linear  v-else-if="item.status === 'In_Progress'"
                                            color="light-blue"
                                            height="10"
                                            :value="progress"
                                            striped
                                    ></v-progress-linear>
                                    <span v-else>{{$t(item.status)}}</span>

                                </template>

                            </v-data-table>
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
            },
            changeStatus() {
                if(this.projects[0].inQueue){
                    this.projects[0].inQueue--;
                    if(this.projects[0].inQueue === 0) {
                        delete this.projects[0].inQueue
                        this.projects[0].status = 'In_Progress'
                        this.progress = 0
                    }
                    return;
                }
                if(this.progress < 100 && this.projects[0].status === 'In_Progress'){
                    this.progress += 25
                    console.log('[Projects].changeStatus this.projects[0].progress:',this.progress)
                    return;
                }
                if(this.projects[0].trainingError === '??') {
                    this.projects[0].status = 'Done'
                    this.projects[0].trainingError = Math.random()
                    this.projects[0].predictionError = Math.random()
                    return;
                }
                this.projects = [
                    {
                        name: 'Frozen Yogurt',
                        trainingError: '??',
                        predictionError: '??',
                        inQueue: 1,
                        status: 'In_Queue',

                    },

                ]
                clearInterval(this.intervalId)
            },
            newProject() {
                    if(this.intervalId){
                        clearInterval(this.intervalId)
                        this.projects = [
                            {
                                name: 'Frozen Yogurt',
                                trainingError: '??',
                                predictionError: '??',
                                inQueue: 1,
                                status: 'In_Queue',

                            },

                        ]
                    }

                    this.intervalId = setInterval(this.changeStatus, 1000);
            }
        },
        data () {
            return {
                intervalId: null,
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
                progress: 0,
                projects: [
                    {
                        name: 'Frozen Yogurt',
                        trainingError: '??',
                        predictionError: '??',
                        inQueue: 1,
                        status: 'In_Queue',

                    },

                ],
            }
        },
    }
</script>

<style scoped>
    .v-progress-linear__bar, .v-progress-linear__bar__determinate {
        transition: none;
    }
</style>
