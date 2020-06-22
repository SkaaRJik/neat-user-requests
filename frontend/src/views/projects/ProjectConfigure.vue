<template>
  <v-stepper :alt-labels="true" v-model="step">
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
        $t("Choose_params")
      }}</v-stepper-step>

      <v-divider></v-divider>

      <v-stepper-step :complete="step > 4" step="4">{{
        $t("AI_Config")
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
                    !normalizedData.trainEndIndex &&
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
                <v-btn :disabled="!settings" class="ma-3" color="primary">
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
</template>

<script>
  import DataNormalization from "../../components/projects/new/DataNormalization";
  import DataSeparation from "../../components/projects/new/DataSeparation";
  import ProjectsAPI from "../../services/api/ProjectsAPI";
  import AiParams from "../../components/aiparams/AiParams";
  import ColumnsChooser from "../../components/projects/new/ColumnsChooser";

  export default {
  name: "ProjectEdit",
  props: {
    step: Number,
    projectId: Number
  },
  components: { ColumnsChooser, AiParams, DataNormalization, DataSeparation },
  data() {
    return {
      parsedData: { headers: [] },
      normalizedData: {},
      settings: null,
      loading: false
    };
  },
  methods: {
    async loadProjectData() {
      this.loading = true;
      try {
        const res = await ProjectsAPI.getProjectData(this.projectId);
        this.parsedData = res.data;
      } catch (e) {
        console.error("[ProjectConfigure.vue].loadProjectData error:", e);
      } finally {
        this.loading = false;
      }
      if (!this.normalizedData.data && this.step !== 1) {
        return this.$router.replace({
          name: "project-configure",
          params: { id: this.$route.params.id },
          query: { step: 1 }
        });
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
      if (!this.normalizedData.inputs || !this.normalizedData.outputs) {
        return true;
      } else if (
        !!this.normalizedData.outputs &&
        this.normalizedData.outputs > 1
      ) {
        return true;
      }
      return false;
    },
    goToStep2IsDisabled() {
      return !this.normalizedData || !this.normalizedData.data;
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
          !this.normalizedData.trainEndIndex &&
          !this.normalizedData.testEndIndex
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
          (!this.normalizedData.inputs && !this.normalizedData.outputs) ||
          (this.normalizedData.outputs && this.normalizedData.outputs > 1)
        ) {
          return this.$router.replace({
            name: "project-configure",
            params: { id: this.$route.params.id },
            query: { step: 3 }
          });
        }
      }
    }
  }
};
</script>

<style scoped></style>
