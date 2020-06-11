<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-stepper :alt-labels="true" v-model="step">
    <v-stepper-header>
      <v-stepper-step :complete="step > 1" step="1"
        >Исходные данные
      </v-stepper-step>

      <v-divider></v-divider>

      <v-stepper-step :complete="step > 2" step="2"
        >Обработка данных
      </v-stepper-step>

      <v-divider></v-divider>

      <v-stepper-step step="3">Имя проекта</v-stepper-step>
    </v-stepper-header>

    <v-stepper-items>
      <v-stepper-content step="1">
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-row align="center" justify="center">
                <v-alert
                  border="bottom"
                  class="mb-0"
                  dense
                  elevation="2"
                  style="width: 100%"
                  transition="scale-transition"
                  type="info"
                >
                  {{ $t("Legend_column_as_date_info") }}
                </v-alert>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row align="center" justify="center">
                <v-btn class="ma-3" color="primary">
                  <v-icon left>mdi-arrow-down-bold</v-icon>
                  {{ $t("Template") }}
                </v-btn>
                <v-btn class="ma-3">
                  <v-icon left>mdi-arrow-down-bold</v-icon>
                  {{ $t("Example", { number: 1 }) }}
                </v-btn>
                <v-btn class="ma-3">
                  <v-icon left>mdi-arrow-down-bold</v-icon>
                  {{ $t("Example", { number: 2 }) }}
                </v-btn>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-file-input
                :label="$t('Upload_File')"
                :placeholder="$t('Select_File')"
                :show-size="1000"
                accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                class="py-2"
                counter
                outlined
                prepend-icon="mdi-paperclip"
                v-model="file"
              >
                <template v-slot:selection="{ index, text }">
                  <v-chip color="deep-purple accent-4" label small>
                    {{ text }}
                  </v-chip>
                </template>
              </v-file-input>
            </v-col>
          </v-row>
          <v-row>
            <v-col class="mr-auto" cols="auto">
              <v-btn @click="redirectToProjectsPage" class="ma-3" text
                >{{ $t("Cancel") }}
              </v-btn>
            </v-col>
            <v-col cols="auto">
              <v-btn
                :disabled="nextPageDisabled"
                @click="uploadXLSX"
                class="ma-3"
                color="primary"
              >
                {{ $t("Continue") }}
                <v-progress-circular
                  color="primary"
                  indeterminate
                  v-if="excelUploading"
                ></v-progress-circular>
                <v-icon right v-else> mdi-arrow-right</v-icon>
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
                :label="$t('Headers')"
                chips
                multiple
                readonly
                v-model="parsedHeaders"
              >
                <div class="span-in-row__container" slot="append">
                  <span class="span-in-row">
                    {{ $t("Items", { size: parsedHeaders.length }) }}
                  </span>
                </div>
              </v-select>
            </v-col>
            <v-col cols="12">
              <v-select
                :items="parsedLegend"
                :label="$t('Legend')"
                chips
                multiple
                readonly
                v-model="parsedLegend"
              >
                <div class="span-in-row__container" slot="append">
                  <span class="span-in-row">
                    {{ $t("Items", { size: parsedLegend.length }) }}
                  </span>

                  <span class="span-in-row">
                    {{ $t("Increment", { increment: calculateIncrement() }) }}
                  </span>
                </div>
              </v-select>
            </v-col>

            <v-col cols="12" v-if="shouldRenderDataErrors">
              <v-expansion-panels :value="0">
                <v-expansion-panel>
                  <v-expansion-panel-header disable-icon-rotate>
                    {{ $t("Errors") }}
                    <template v-slot:actions>
                      <v-icon color="error">mdi-alert-circle</v-icon>
                    </template>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <template v-for="(item, index) in parsedErrors">
                      <v-list-item :id="index" :key="index">
                        {{ $t(item.error, item) }}
                      </v-list-item>
                    </template>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>
          </v-row>
          <v-row>
            <v-col class="mr-auto" cols="auto" xs="12">
              <v-btn @click="redirectToProjectsPage" class="ma-3" text
                >{{ $t("Cancel") }}
              </v-btn>
            </v-col>
            <v-col cols="auto" xs="12">
              <v-card-actions>
                <v-btn @click="step = 1" class="ma-3">
                  <v-icon left> mdi-arrow-left</v-icon>
                  {{ $t("Back") }}
                </v-btn>
                <v-btn
                  :disabled="shouldRenderDataErrors || !parsedData.increment"
                  @click="step = 3"
                  class="ma-3"
                  color="primary"
                >
                  {{ $t("Continue") }}
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
              <v-text-field
                :label="$t('Project_Name')"
                clearable
                counter="100"
                outlined
                v-model="projectName"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col class="mr-auto" cols="auto">
              <v-btn @click="redirectToProjectsPage" class="ma-3" text
                >{{ $t("Cancel") }}
              </v-btn>
            </v-col>
            <v-col cols="auto">
              <v-card-actions>
                <v-btn @click="back()" class="ma-3">
                  <v-icon left> mdi-arrow-left</v-icon>
                  {{ $t("Back") }}
                </v-btn>
                <v-btn
                  :disabled="projectName.length === 0"
                  @click="handleSaveProject"
                  class="ma-3"
                  color="primary"
                >
                  {{ $t("Create") }}
                  <v-progress-circular
                    color="primary"
                    indeterminate
                    v-if="excelUploading"
                  ></v-progress-circular>
                  <v-icon right v-else> mdi-check</v-icon>
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
import moment from "moment";
import ProjectsAPI from "../services/api/ProjectsAPI";

export default {
  name: "NewProject",
  methods: {
    async handleSaveProject() {
      const data = {
        ...this.parsedData,
        name: this.projectName
      };
      const res = await ProjectsAPI.saveProject(data);
      console.log("[NewProject].handleSaveProject res:", res.data);
    },

    redirectToProjectsPage() {
      this.$router.push({ name: "projects" });
    },

    async uploadXLSX() {
      this.excelUploading = true;
      try {
        this.parsedData = await parseExcel(this.file);
        //this.parsedData = (await ProjectsApi.parseExcelFile(this.file)).data
        this.step = 2;
      } catch (e) {
        console.error("[NewProject].uploadXLSX() EXCEPTION:", e);
        await Vue.$toast.open({
          message: `${this.$t(e)}`,
          type: "error",
          position: "top-right",
          dismissible: true
        });
      }
      console.log("[NewProject].uploadXLSX this.parsedData:", this.parsedData);
      this.excelUploading = false;
    },

    async back() {
      this.step--;
    },

    calculateIncrement() {
      if (this.parsedData.isDate) {
        const diff = moment.duration(this.parsedData.increment, "milliseconds");
        if (diff.asYears()) return `${diff.asYears()} ${this.$t("Years")}`;
        if (diff.asMonths()) return `${diff.asMonths} ${this.$t("Months")}`;
        if (diff.asDays()) return `${diff.asDays} ${this.$t("Days")}`;
        if (diff.asHours()) return `${diff.asHours} ${this.$t("Hours")}`;
        if (diff.asMinutes()) return `${diff.asMinutes} ${this.$t("Minutes")}`;
        if (diff.asSeconds())
          return `${diff.asSeconds()} ${this.$t("Seconds")}`;
      }
      return this.parsedData.increment.toFixed(3);
    }
  },
  data() {
    return {
      projectName: "",
      file: null,
      excelUploading: false,
      legendError: false,
      parsedData: { increment: 0 },
      step: 1,
      textFormat: "DD.MM.YYYY",
      date: new Date()
    };
  },
  computed: {
    nextPageDisabled: function() {
      return !this.file || this.excelUploading;
    },
    shouldRenderDataErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors.length > 0;
      }
      return false;
    },
    parsedErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors;
      }
      return [];
    },
    parsedHeaders() {
      if (this.parsedData.headers) {
        return [this.parsedData.legendHeader, ...this.parsedData.headers];
      }
      return [];
    },
    parsedLegend() {
      if (this.parsedData.legend) {
        if (this.parsedData.isDate) {
          return this.parsedData.legend.map(value =>
            moment(value).format("DD.MM.YYYY HH:mm")
          );
        } else {
          return this.parsedData.legend;
        }
      }
      return [];
    }
  }
};
</script>

<style scoped>
.span-in-row__container {
  font-size: 12px;
}

.span-in-row {
  display: flow-root;
}
</style>
