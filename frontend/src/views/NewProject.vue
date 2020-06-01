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
                            <v-row
                                    align="center"
                                    justify="center"
                            >
                                <v-btn
                                        class="ma-3"
                                        color="primary"
                                >
                                    <v-icon left>mdi-arrow-down-bold</v-icon> {{$t("Template")}}
                                </v-btn>
                                <v-btn
                                        class="ma-3"
                                >
                                    <v-icon left>mdi-arrow-down-bold</v-icon> {{$t("Example", {number: 1})}}
                                </v-btn>
                                <v-btn
                                        class="ma-3"
                                >
                                    <v-icon left>mdi-arrow-down-bold</v-icon> {{$t("Example", {number: 2})}}
                                </v-btn>
                            </v-row>
                        </v-col>
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
                                <v-btn class="ma-3" text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                                <v-btn
                                        class="ma-3"
                                        color="primary"
                                        @click="uploadXLSX"
                                        :disabled="nextPageDisabled"
                                >
                                    {{$t('Continue')}}
                                    <v-progress-circular
                                            v-if="excelUploading"
                                            indeterminate
                                            color="primary"
                                    ></v-progress-circular>
                                    <v-icon v-else right> mdi-arrow-right</v-icon>
                                </v-btn>

                            </v-row>
                        </v-col>
                    </v-row>

                </v-container>
            </v-stepper-content>

            <v-stepper-content step="2">

                <v-container>
                    <v-row>
                        <v-col cols="12">
                            <v-select
                                    :items="parsedHeaders"
                                    v-model="parsedHeaders"
                                    readonly
                                    chips
                                    multiple
                                    :label="$t('Headers')"
                            />
                        </v-col>
                        <v-col cols="12">
                            <v-select
                                    v-model="parsedLegend"
                                    :items="parsedLegend"
                                    readonly
                                    chips
                                    multiple
                                    :label="$t('Legend')"
                            />
                        </v-col>
                        <v-col cols="12">
                            <v-card-actions>
                                <v-btn class="ma-3" text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                                <v-spacer></v-spacer>

                                <v-btn
                                    @click="back()"
                                    class="ma-3"

                            >
                                <v-icon left> mdi-arrow-left</v-icon>
                                {{$t('Back')}}
                            </v-btn>

                                <v-btn
                                        class="ma-3"
                                        color="primary"
                                        @click="step = 3"

                                >
                                    {{$t('Continue')}}
                                    <v-progress-circular
                                            v-if="excelUploading"
                                            indeterminate
                                            color="primary"
                                    ></v-progress-circular>
                                    <v-icon v-else right> mdi-arrow-right</v-icon>
                                </v-btn></v-card-actions>
                            <v-row
                                    align="center"
                                    justify="center"
                            >

                            </v-row>
                        </v-col>
                    </v-row>

                </v-container>

                <!-- <v-btn text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>-->
            </v-stepper-content>

            <v-stepper-content step="3">
                <v-card
                        class="mb-12"
                        color="grey lighten-1"
                        height="200px"
                ></v-card>

                <v-btn
                        @click="step = 2"

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
        methods: {

            redirectToProjectsPage(){
                this.$router.push({name:'projects'})
            },

            async uploadXLSX(){
                this.excelUploading = true
                try{
                    this.parsedData = (await ProjectsApi.parseExcelFile(this.file)).data
                    await this.$router.push({name: "new-project", query:{step: 2}})
                } catch (e) {
                    console.error('[NewProject].uploadXLSX() EXCEPTION:',e)
                }


                this.excelUploading = false

            },

            async back() {
                await this.$router.go(-1)
            },



        },
        data () {
            return {
                file: null,
                excelUploading: false,
                parsedData: null,
                headers: {items: [], values:[]},
                legend: {items: [], values:[]}
            }
        },

        computed: {
            nextPageDisabled: function () {
                return !this.file || this.excelUploading
            },
            step: function () {
                const query_step = this.$route.query.step

                if(!query_step) {
                    return 1;
                }

                if(query_step == 2){
                    if(!this.parsedData) {
                        this.$router.push({name: "new-project", query: {step: 1}})
                        return 1;
                    }
                }

                if(query_step == 3){
                    if(!this.parsedData) {
                        this.$router.push({name: "new-project", query: {step: 1}})
                        return 1;
                    }
                }

                return query_step;
            },
            parsedHeaders() {
                if(this.parsedData) {
                    return this.parsedData.headers
                }
                return []
            },

            parsedLegend() {
                if(this.parsedData) {
                    return this.parsedData.legend
                }
                return []
            }
        }

    }
</script>

<style scoped>

</style>
