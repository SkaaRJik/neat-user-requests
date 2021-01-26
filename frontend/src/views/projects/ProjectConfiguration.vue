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
            <data-separation
              v-model="dataIndexes"
              :data="normalizedData.columns[0].data"
            />
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
                      !dataIndexes ||
                        !dataIndexes.trainEndIndex ||
                        !dataIndexes.testEndIndex
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

        <!-- <v-stepper-content step="3">
           <v-container>
             <columns-chooser
               v-model="normalizedData"
               :headers="parsedData.headers"
             />
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
                     :disabled="nodesAreNotChosen"
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
             <ai-params
               v-model="settings"
               :inputs="normalizedData.inputs"
               :outputs="normalizedData.outputs"
             />
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
                     :disabled="!settings"
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

                 <v-text-field :label="$t('Window_Size')" class="ma-3" v-model="windowSize"></v-text-field>

                 <v-text-field :label="$t('Prediction_Period')" class="ma-3" v-model="predictionPeriod"></v-text-field>
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
                     :disabled="!settings"
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
         </v-stepper-content>-->
      </v-stepper-items>
    </v-stepper>
  </v-container>
</template>

<script>
import DataNormalization from "../../components/projects/configure/DataNormalization";
import DataSeparation from "../../components/projects/configure/DataSeparation";
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
    DataSeparation
  },
  data() {
    return {
      parsedData: { headers: [] },
      normalizedData: {
        normalizationServiceData: {
          method: null
        },
        columns: []
      },
      settings: null,
      loading: false,
      dataIndexes: {},
      selectedColumns: {},
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
        console.log('[ProjectConfiguration].loadProjectData this.parsedData:', this.parsedData);
        this.normalizedData = {
          normalizationServiceData: {
            method: null
          },
          columns: []
        };
        


        this.parsedData.headers.forEach((value, index) => {
          this.normalizedData.columns.push({
            data: [],
            // eslint-disable-next-line no-undef
            columnName: value,
            columnType:
                index <= this.parsedData.headers.length - 2 ? "Input" : "Output",
            minValue: 0,
            maxValue: 0
          });
        })
      } catch (e) {
        console.error("[ProjectConfiguration.vue].loadProjectData error:", e);
      } finally {
        this.loading = false;
      }
      /*if (!this.normalizedData.data && this.step !== 1) {
        return this.$router.replace({
          name: "project-configure",
          params: { id: this.$route.params.id },
          query: { step: 1 }
        });
      }*/
    },
    async saveConfig() {
      const config = {
        normalizedData: this.normalizedData,
        settings: this.settings,
        dataIndexes: this.dataIndexes,
        selectedColumns: this.selectedColumns,
        predictionParams: {
          windowSize: this.windowSize,
          predictionPeriod: this.predictionPeriod
        }
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
    goToStep(step) {
      this.$router.replace({
        name: "project-configure",
        query: { step },
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
    }
  },
  mounted() {
    this.loadProjectData();
  },
  computed: {
    nodesAreNotChosen() {
      if (!this.selectedColumns.inputs || !this.selectedColumns.outputs) {
        return true;
      } else if (
        !!this.selectedColumns.outputs &&
        this.selectedColumns.outputs > 1
      ) {
        return true;
      }
      return false;
    },
    goToStep2IsDisabled() {
      return !this.normalizedData || (!this.normalizedData.columns && !this.normalizedData.columns.length === 0);
    }
  },
  watch: {
    projectId: function(newVal) {
      if (newVal) {
        this.loadProjectData();
      }
    },
   /* step: function(newVal, oldVal) {
      if (newVal === oldVal) {
        return;
      }

      if (!newVal) {
        return this.$router.replace({
          name: "project-configure",
          query: { step: 1 },
          params: { id: this.projectId }
        });
      }

      if (newVal === 2) {
        if (
          !this.normalizedData.data &&
          this.normalizedData.data.length === 0
        ) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 1 }
          });
        }
      }

      if (newVal === 3) {
        if (
          !this.dataIndexes &&
          !this.dataIndexes.trainEndIndex &&
          !this.dataIndexes.testEndIndex
        ) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 2 }
          });
        }
      }

      if (newVal === 4) {
        if (
          (!this.selectedColumns.inputs && !this.selectedColumns.outputs) ||
          (this.selectedColumns.outputs && this.selectedColumns.outputs > 1)
        ) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 3 }
          });
        }
      }
    }*/
  }
};
</script>

<style scoped></style>
