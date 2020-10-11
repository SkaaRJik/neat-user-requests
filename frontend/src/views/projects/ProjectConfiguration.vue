<template>
  <v-container>
    <page-loading v-if="loading"></page-loading>

    <v-stepper :alt-labels="true" v-model="step" v-else>
      <v-stepper-header>
        <v-stepper-step :complete="step > 1" step="1">{{
          $t("Data_Normalization")
        }}</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :complete="step > 2" step="2">{{
          $t("Data_Separation")
        }}</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :complete="step > 3" step="3">{{
          $t("Choose_Params")
        }}</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :complete="step > 4" step="4">{{
          $t("AI_Config")
        }}</v-stepper-step>

        <v-divider></v-divider>

        <v-stepper-step :complete="step > 5" step="5">{{
          $t("Prediction_Params")
        }}</v-stepper-step>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content step="1">
          <v-container>
            <data-normalization
              :parsed-data="parsedData"
              v-model="normalizedData"
            />
            <v-row>
              <v-col class="mr-auto" cols="auto" xs="12">
                <v-btn @click="redirectToProjectsPage" class="ma-3" text
                  >{{ $t("Cancel") }}
                </v-btn>
              </v-col>
              <v-col cols="auto" xs="12">
                <v-card-actions>
                  <v-btn
                    :disabled="goToStep2IsDisabled"
                    @click="goToStep(2)"
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
        </v-stepper-content>

        <v-stepper-content step="2">
          <v-container>
            <data-separation v-model="normalizedData" />
            <v-row>
              <v-col class="mr-auto" cols="auto" xs="12">
                <v-btn @click="redirectToProjectsPage" class="ma-3" text
                  >{{ $t("Cancel") }}
                </v-btn>
              </v-col>
              <v-col cols="auto" xs="12">
                <v-card-actions>
                  <v-btn @click="back" class="ma-3">
                    <v-icon left> mdi-arrow-left</v-icon>
                    {{ $t("Back") }}
                  </v-btn>
                  <v-btn
                    @click="goToStep(3)"
                    :disabled="
                      !normalizedData ||
                        !normalizedData.trainEndIndex ||
                        !normalizedData.testEndIndex
                    "
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
        </v-stepper-content>

        <v-stepper-content step="3">
          <v-container>
            <columns-chooser v-model="normalizedData" />
            <v-row>
              <v-col class="mr-auto" cols="auto" xs="12">
                <v-btn @click="redirectToProjectsPage" class="ma-3" text
                  >{{ $t("Cancel") }}
                </v-btn>
              </v-col>
              <v-col cols="auto" xs="12">
                <v-card-actions>
                  <v-btn @click="back" class="ma-3">
                    <v-icon left> mdi-arrow-left</v-icon>
                    {{ $t("Back") }}
                  </v-btn>
                  <v-btn
                    @click="goToStep(4)"
                    :disabled="goToStep4IsDisabled"
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
        </v-stepper-content>

        <v-stepper-content step="4">
          <v-container>
            <ai-params v-model="settings" />
            <v-row>
              <v-col class="mr-auto" cols="auto" xs="12">
                <v-btn @click="redirectToProjectsPage" class="ma-3" text
                  >{{ $t("Cancel") }}
                </v-btn>
              </v-col>
              <v-col cols="auto" xs="12">
                <v-card-actions>
                  <v-btn @click="back" class="ma-3">
                    <v-icon left> mdi-arrow-left</v-icon>
                    {{ $t("Back") }}
                  </v-btn>
                  <v-btn
                    :disabled="goToStep5IsDisabled"
                    class="ma-3"
                    color="primary"
                    @click="goToStep(5)"
                  >
                    {{ $t("Continue") }}
                    <v-icon> mdi-arrow-right</v-icon>
                  </v-btn>
                </v-card-actions>
              </v-col>
            </v-row>
          </v-container>
        </v-stepper-content>

        <v-stepper-content step="5">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-row align="center" justify="center">
                  <v-text-field
                    :label="$t('Window_Size')"
                    class="ma-3"
                    v-model="windowSize"
                  ></v-text-field>

                  <v-text-field
                    :label="$t('Prediction_Period')"
                    class="ma-3"
                    v-model="predictionPeriod"
                  ></v-text-field>
                </v-row>
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
                  <v-btn @click="back" class="ma-3">
                    <v-icon left> mdi-arrow-left</v-icon>
                    {{ $t("Back") }}
                  </v-btn>
                  <v-btn
                    :disabled="goToStep5IsDisabled"
                    class="ma-3"
                    color="primary"
                    @click="saveConfig"
                  >
                    {{ $t("Continue") }}
                    <v-icon> mdi-arrow-right</v-icon>
                  </v-btn>
                </v-card-actions>
              </v-col>
            </v-row>
          </v-container>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
  </v-container>
</template>

<script>
import DataNormalization from "../../components/projects/new/DataNormalization";
import DataSeparation from "../../components/projects/new/DataSeparation";
import ColumnsChooser from "../../components/projects/new/ColumnsChooser";
import AiParams from "../../components/aiparams/AiParams";

import ProjectsAPI from "../../services/api/ProjectsAPI";
import PageLoading from "../../components/loading/PageLoading";

export default {
  name: "ProjectEdit",
  props: {
    step: Number,
    projectId: Number
  },
  components: {
    PageLoading,
    DataNormalization,
    DataSeparation,
    ColumnsChooser,
    AiParams
  },
  data() {
    return {
      parsedData: { headers: [] },
      normalizedData: {
        normalizationServiceData: {
          method: null
        },
        columns: [],
        trainEndIndex: null,
        testEndIndex: null,
        totalRows: null,
        inputs: 0,
        outputs: 0,
        headers: []
      },
      settings: null,
      loading: false,
      windowSize: 3,
      predictionPeriod: 10
    };
  },
  methods: {
    async loadProjectData() {
      this.loading = true;
      try {
        const res = await ProjectsAPI.getProjectData(this.projectId);
        this.parsedData = res.data;
        this.parsedData.headers.forEach((value, index) => {
          this.normalizedData.columns.push({
            data: [],
            columnName: value,
            columnType:
              index <= this.parsedData.headers.length - 2 ? "Input" : "Output",
            minValue: 0,
            maxValue: 0
          });
        });
      } catch (e) {
        console.error("[ProjectConfiguration.vue].loadProjectData error:", e);
      } finally {
        this.loading = false;
        if (this.step !== 1) {
          this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 1 }
          });
        }
      }
    },
    async saveConfig() {
      const config = {
        normalizedData: this.normalizedData,
        settings: this.settings,
        windowSize: this.windowSize,
        predictionPeriod: this.predictionPeriod
      };
      try {
        await ProjectsAPI.saveProjectConfiguration(this.projectId, config);
        this.$router.push({ name: "projects" });
      } catch (e) {
        console.error("[ProjectConfiguration].saveConfig e:", e);
      }
    },
    redirectToProjectsPage() {
      this.$router.push({ name: "projects" });
    },
    goToStep(nextStep) {
      if (nextStep === 4) {
        this.normalizedData.columns.forEach((value, index) => {
          value.columnType = this.normalizedData.headers[index].type;
        });
      }

      if (nextStep === 5) {
        const nodesConfig = this.settings[this.settings.length - 1].params;
        nodesConfig.forEach(val => {
          if (val.name === "INPUT.NODES") {
            val.value = this.normalizedData.inputs;
          }
          if (val.name === "OUTPUT.NODES") {
            val.value = this.normalizedData.outputs;
          }
        });
      }

      this.$router.replace({
        name: "project-configure",
        query: { step: nextStep },
        params: { id: this.projectId }
      });
      window.scrollTo(0, 0);
    },
    back() {
      this.$router.replace({
        name: "project-configure",
        query: { step: this.step - 1 },
        params: { id: this.projectId }
      });
      window.scrollTo(0, 0);
    },
    checkNavigation(step) {
      if (!step) {
        return this.$router.replace({
          name: "project-configure",
          query: { step: 1 },
          params: { id: this.projectId }
        });
      }

      if (step === 2) {
        if (this.goToStep2IsDisabled) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 1 }
          });
        }
      }

      if (step === 3) {
        if (this.goToStep3IsDisabled) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 2 }
          });
        }
      }

      if (step === 4) {
        if (this.goToStep4IsDisabled) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 3 }
          });
        }
      }

      if (step === 5) {
        if (this.goToStep5IsDisabled) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 4 }
          });
        }
      }
    }
  },
  mounted() {
    this.loadProjectData();
  },
  computed: {
    goToStep2IsDisabled() {
      return (
        !this.normalizedData.columns ||
        (!!this.normalizedData.columns.length &&
          this.normalizedData.columns[0].data.length === 0)
      );
    },
    goToStep3IsDisabled() {
      return (
        !this.normalizedData.trainEndIndex || !this.normalizedData.testEndIndex
      );
    },
    goToStep4IsDisabled() {
      return (
        this.normalizedData.inputs === 0 ||
        this.normalizedData.outputs !== 1 ||
        this.normalizedData.headers.length === 0
      );
    },
    goToStep5IsDisabled() {
      return !this.settings;
    }
  },
  watch: {
    projectId: function(newVal) {
      if (newVal) {
        this.loadProjectData();
      }
    },
    step: function(newVal, oldVal) {
      if (newVal === oldVal) {
        return;
      }
      this.checkNavigation(newVal);
    }
  }
};
</script>

<style scoped></style>
