<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-stepper v-model="step" :alt-labels="true">

        <v-stepper-header>
            <v-stepper-step :complete="step > 1" step="1">Исходные данные</v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step :complete="step > 2" step="2">Обработка данных</v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step step="3">Имя проекта</v-stepper-step>
        </v-stepper-header>

        <v-stepper-items>
            <v-stepper-content step="1">
                <v-container>
                    <v-row>
                        <v-col cols="12">
                            <v-file-input
                                    v-model="file"
                                    class="py-2"
                                    counter
                                    :label="$t('Upload_File')"
                                    :placeholder="$t('Select_File')"
                                    prepend-icon="mdi-paperclip"
                                    outlined
                                    :show-size="1000"
                                    accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            >
                                <template v-slot:selection="{ index, text }">
                                    <v-chip
                                            color="deep-purple accent-4"
                                            label
                                            small
                                    >
                                        {{ text }}
                                    </v-chip>
                                </template>
                            </v-file-input>
                        </v-col>
                        <v-col cols="12">
                            <v-row
                                    align="center"
                                    justify="center"
                            >
                                <v-btn
                                        color="primary"
                                        @click="uploadXLSX"
                                        :disabled="nextPageDisabled"
                                >
                                    <v-progress-circular
                                            v-if="excelUploading"
                                            indeterminate
                                            color="primary"
                                    ></v-progress-circular>
                                    <span v-else>
                        {{$t('Continue')}}
                    </span>
                                </v-btn>
                                <v-btn text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                            </v-row>
                        </v-col>
                    </v-row>

                </v-container>
            </v-stepper-content>

            <v-stepper-content step="2">
                <div>
                    <v-btn
                            @click="step = 1"

                    >
                    <span>
                        {{$t('Back')}}
                    </span>
                    </v-btn>

                    <v-btn
                            color="primary"
                            @click="step = 3"

                    >
                    <span>
                        {{$t('Continue')}}
                    </span>
                    </v-btn>
                </div>
                <!-- <v-btn text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>-->
            </v-stepper-content>

            <v-stepper-content step="3">
                <v-card
                        class="mb-12"
                        color="grey lighten-1"
                        height="200px"
                ></v-card>

                <v-btn
                        @click="step = 1"

                >
                    <span>
                        {{$t('Back')}}
                    </span>
                </v-btn>
                <v-btn
                        color="primary"
                        @click="step = 2"
                >
                    <span>
                        {{$t('Continue')}}
                    </span>
                </v-btn>

                <!--<v-btn text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>-->
            </v-stepper-content>
        </v-stepper-items>
    </v-stepper>
</template>

<script>
    import ProjectsApi from '../services/api/ProjectsAPI'
    import router from "../router/vue-router";

    export default {
        name: "NewProject",
        props: {
            parsedData: Object,
            confirmedData: Object
        },
        created: () => {
            this.navigate()
        },
        data () {
            return {
                step: 1,
                file: null,
                excelUploading: false,
            }
        },
        methods: {
            navigate(){

                this.step =  router.query.step ? router.query.step : 1

                console.log('[NewProject].navigate() this.$route.query:', router.query.step)

                if(this.step === 2 && this.parsedData === undefined){
                    router.push({path: "/new-project?step=1"})
                }
            },
            async redirectToProjectsPage(){
                await this.$router.push({name:'projects'})
            },
            async uploadXLSX(){
                this.excelUploading = true
                try{
                    const parsedFile = (await ProjectsApi.parseExcelFile(this.file)).data

                } catch (e) {
                    console.error('[NewProject].uploadXLSX() EXCEPTION:',e)
                }

                console.log('[NewProject].uploadXLSX() this.parsedFile:', this.parsedFile)

                this.excelUploading = false
                this.step = 2
            },

        },
        computed: {
            nextPageDisabled: function () {
                return !this.file || this.excelUploading
            }
        }

    }
</script>

<style scoped>

</style>