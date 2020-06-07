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
                    </v-row>
                    <v-row>
                        <v-col
                                cols="auto"
                                class="mr-auto"
                        >
                            <v-btn class="ma-3" text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                        </v-col>
                        <v-col cols="auto">
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
                            >
                                <span slot="append" color="green">{{$t('Items', {size: parsedHeaders.length})}}</span>
                            </v-select>
                        </v-col>
                        <v-col cols="12">
                            <v-select
                                    v-model="parsedLegend"
                                    :items="parsedLegend"
                                    readonly
                                    chips
                                    multiple
                                    :label="$t('Legend')"
                            >
                                <span slot="append" color="green">{{$t('Items', {size: parsedLegend.length})}}</span>
                            </v-select>
                        </v-col>


                        <v-col cols="12">

                            <v-autocomplete
                                    v-model="textFormat"
                                    :items="formats"
                                    :filter="customFilter"
                                    item-text="name"
                                    :label="$t('Date_format_inside_the_document')"
                            >
                                <template v-slot:selection="{ attr, on, item, selected }">
                                    <span v-text="item.text"></span>
                                    <v-chip
                                            v-bind="attr"
                                            :input-value="selected"
                                            color="blue-grey"
                                            class="white--text"
                                            v-on="on"
                                    >
                                        {{formatDate(item.format)}}

                                    </v-chip>
                                </template>
                                <template v-slot:item="{ item }">

                                    <v-list-item-content>
                                        <v-list-item-title v-text="item.text"></v-list-item-title>
                                        <v-spacer></v-spacer>
                                        <v-list-item-title v-text="formatDate(item.format)"></v-list-item-title>
                                    </v-list-item-content>
                                </template>

                            </v-autocomplete>


                        </v-col>
                        <v-col cols="12" v-if="shouldRenderDataErrors">
                            <v-expansion-panels :value="0">
                                <v-expansion-panel
                                >
                                    <v-expansion-panel-header disable-icon-rotate>
                                        Следующие поля содержат строку вместо числа:
                                        <template v-slot:actions>
                                            <v-icon color="error">mdi-alert-circle</v-icon>
                                        </template>
                                    </v-expansion-panel-header>
                                    <v-expansion-panel-content>
                                        <template v-for="(item,index) in parsedErrors">
                                            <v-list-item  :id="index" :key="index">
                                                {{item.error}} {{parsedData.headers[item.column] ? parsedData.headers[item.column] : item.column}} {{parsedData.legend[item.row] ? parsedData.legend[item.row] : item.row}}
                                            </v-list-item>
                                        </template>
                                    </v-expansion-panel-content>
                                </v-expansion-panel>
                            </v-expansion-panels>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col
                                cols="auto"
                                class="mr-auto"
                                xs="12"
                        >
                            <v-btn class="ma-3" text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                        </v-col>
                        <v-col cols="auto" xs="12">
                            <v-card-actions>
                                <v-btn
                                        @click="step = 1"
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
                                    <v-icon> mdi-arrow-right</v-icon>
                                </v-btn>
                            </v-card-actions>

                        </v-col>

                    </v-row>
                </v-container>

                <!-- <v-btn text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>-->
            </v-stepper-content>

            <v-stepper-content step="3">
                <v-container>
                    <v-row>
                        <v-col cols="12">

                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col
                                cols="auto"
                                class="mr-auto"
                        >
                            <v-btn class="ma-3" text @click="redirectToProjectsPage">{{$t('Cancel')}}</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-card-actions>
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
                                    <v-icon v-else right> mdi-check</v-icon>
                                </v-btn>
                            </v-card-actions>
                        </v-col>
                    </v-row>
                </v-container>
            </v-stepper-content>
        </v-stepper-items>
    </v-stepper>
</template>

<script>
    import parseExcel from "../parser/ExcelParser";
    import Vue from "vue";
    import moment from 'moment'

    export default {
        name: "NewProject",
        methods: {

            formatDate(format){
              return  moment(this.date).format(format)
            },

            customFilter (item, queryText, itemText) {
                const textOne = item.name.toLowerCase()
                const textTwo = item.abbr.toLowerCase()
                const searchText = queryText.toLowerCase()

                return textOne.indexOf(searchText) > -1 ||
                    textTwo.indexOf(searchText) > -1
            },

            redirectToProjectsPage(){
                this.$router.push({name:'projects'})
            },

            async uploadXLSX(){
                this.excelUploading = true
                try{
                    this.parsedData = await parseExcel(this.file)
                    //this.parsedData = (await ProjectsApi.parseExcelFile(this.file)).data
                    this.step = 2;
                } catch (e) {
                    console.error('[NewProject].uploadXLSX() EXCEPTION:',e)
                    await Vue.$toast.open({
                        message: `${this.$t(e)}`,
                        type: 'error',
                        position: 'top-right',
                        dismissible: true,
                    });
                }

                this.excelUploading = false

            },

            async back() {
                this.step--;
            },



        },
        data () {
            return {
                file: null,
                excelUploading: false,
                parsedData: null,
                step: 1,
                textFormat: null,
                date: new Date(),
                formats: [
                    { format: 'DD.MM.YYYY', text: 'ДД.ММ.ГГГГ' },
                    { format: 'DD.MMM.YYYY' , text: 'ДД.МММ.ГГГГ' },
                    { format: 'DD.MMMM.YYYY', text: 'ДД.ММММ.ГГГГ' },
                    { format: 'DD.MM.YY', text: 'ДД.ММ.ГГ' },
                    { format: 'DD.MMM.YY', text: 'ДД.МММ.ГГ' },
                    { format: 'DD.MMMM.YY', text: 'ДД.ММММ.ГГ' },
                    { format: 'DD.MM', text: 'ДД.ММ' },
                    { format: 'DD.MMM', text: 'ДД.МММ' },
                    { format: 'DD.MMMM', text: 'ДД.ММММ' },
                    { format: 'DD-MM-YYYY', text: 'ДД-ММ-ГГГГ' },
                    { format: 'DD-MMM-YYYY', text: 'ДД-МММ-ГГГГ' },
                    { format: 'DD-MMMM-YYYY', text: 'ДД-МММ-ГГГГ' },
                    { format: 'DD-MM-YY', text: 'ДД-ММ-ГГ' },
                    { format: 'DD-MMM-YY', text: 'ДД-МММ-ГГ' },
                    { format: 'DD-MMMM-YY', text: 'ДД-ММММ-ГГ' },
                    { format: 'DD/MM/YYYY', text: 'ДД/ММ/ГГГГ' },
                    { format: 'DD/MMM/YYYY', text: 'ДД/МММ/ГГГГ' },
                    { format: 'DD/MMMM/YYYY', text: 'ДД/ММММ/ГГГГ' },
                    { format: 'DD/MM/YY', text: 'ДД/ММ/ГГГГ' },
                    { format: 'DD/MMM/YY', text: 'ДД/ММ/ГГГГ' },
                    { format: 'DD/MMMM/YY', text: 'ДД/ММ/ГГГГ' },
                ],
            }
        },

        computed: {
            nextPageDisabled: function () {
                return !this.file || this.excelUploading
            },
            shouldRenderDataErrors() {
                if(this.parsedData){
                    return this.parsedData.dataErrors.length > 0
                }
                return false
            },
            parsedErrors() {
                if(this.parsedData) {
                    return this.parsedData.dataErrors
                }
                return []
            },
            parsedHeaders() {
                if(this.parsedData) {
                    return [this.parsedData.legendHeader, ...this.parsedData.headers]
                }
                return []
            },

            parsedLegend() {
                if(this.parsedData) {
                    return this.parsedData.legend
                }
                return []
            }
        },
    }
</script>

<style scoped>

</style>
