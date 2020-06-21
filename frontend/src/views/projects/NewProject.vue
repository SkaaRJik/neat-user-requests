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

      <v-stepper-step :complete="step > 3" step="3">Имя проекта</v-stepper-step>
    </v-stepper-header>

    <v-stepper-items>
      <v-stepper-content step="1">
        <v-container>
          <upload-project-data v-model="file"></upload-project-data>
          <v-row>
            <v-col class="mr-auto" cols="auto">
              <v-btn @click="redirectToProjectsPage" class="ma-3" text
                >{{ $t("Cancel") }}
              </v-btn>
            </v-col>
            <v-col cols="auto">
              <v-btn
                :disabled="!file || excelUploading"
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
          <project-details :parsed-data="parsedData" />
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
                  :disabled="shouldRenderDataErrors || !parsedData.increment"
                  @click="goToStep(3)"
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
                <v-btn @click="back" class="ma-3">
                  <v-icon left> mdi-arrow-left</v-icon>
                  {{ $t("Back") }}
                </v-btn>
                <!--<v-btn
                  :disabled="
                    !projectName || !projectName.trim() || excelUploading
                  "
                  @click="handleSaveProject"
                  class="ma-3"
                  color="primary"
                >
                  {{ $t("Continue") }}
                  <v-progress-circular
                    v-if="excelUploading"
                    color="primary"
                    indeterminate
                  ></v-progress-circular>
                  <v-icon v-else right> mdi-arrow-right</v-icon>
                </v-btn>-->
                <v-btn
                  :disabled="
                    !projectName || !projectName.trim() || excelUploading
                  "
                  @click="handleSaveProject"
                  class="ma-3"
                  color="primary"
                >
                  {{ $t("Create") }}
                  <v-progress-circular
                    color="primary"
                    indeterminate
                    v-show="excelUploading"
                  ></v-progress-circular>
                  <v-icon right v-show="!excelUploading"> mdi-check</v-icon>
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
  import parseExcel from "../../parser/ExcelParser";
  import Vue from "vue";
  import ProjectsAPI from "../../services/api/ProjectsAPI";
  import UploadProjectData from "../../components/projects/new/UploadProjectData";
  import ProjectDetails from "../../components/projects/new/ProjectDetails";

  export default {
  name: "NewProject",
  props: {
    step: Number
  },
  components: {
    ProjectDetails,
    UploadProjectData
  },
  data() {
    return {
      projectName: null,
      file: null,
      excelUploading: false,
      legendError: false,
      parsedData: { increment: 0, headers: [] }
    };
  },
  methods: {
    async handleSaveProject() {
      this.excelUploading = true;

      try {
        const projectFromTheServer = await ProjectsAPI.findProject({
          projectName: this.projectName
        });
        console.log(
          "[NewProject.vue].handleSaveProject projectFromTheServer:",
          projectFromTheServer
        );
        if (projectFromTheServer.data) {
          //TODO Create a confirmation dialog
          await Vue.$toast.open({
            message: `${this.$t("PROJECT_WITH_SUCH_NAME_ALREADY_EXISTS")}`,
            type: "error",
            position: "top-right",
            dismissible: true
          });
          return;
        }

        const data = {
          ...this.parsedData,
          name: this.projectName
        };
        const res = await ProjectsAPI.saveProject(data);
        if (res && res.data) {
          this.projectId = res.data;
          await this.$router.push({
            name: "project-configure",
            params: { id: this.projectId }
          });
        }
      } catch (e) {
        console.error("[NewProject].handleSaveProject error:", e);
        await Vue.$toast.open({
          message: `${this.$t(e)}`,
          type: "error",
          position: "top-right",
          dismissible: true
        });
      } finally {
        this.excelUploading = false;
      }
    },

    async uploadXLSX() {
      this.excelUploading = true;
      try {
        console.log("[NewProject].uploadXLSX this.file:", this.file);
        this.parsedData = await parseExcel(this.file);
        //this.parsedData = (await ProjectsApi.parseExcelFile(this.file)).data
        this.goToStep(2);
      } catch (e) {
        console.error("[NewProject].uploadXLSX() EXCEPTION:", e);
        await Vue.$toast.open({
          message: `${this.$t(e)}`,
          type: "error",
          position: "top-right",
          dismissible: true
        });
      } finally {
        console.log(
          "[NewProject].uploadXLSX this.parsedData:",
          this.parsedData
        );
        this.excelUploading = false;
      }
    },

    redirectToProjectsPage() {
      this.$router.push({ name: "projects" });
    },

    goToStep(step) {
      this.$router.push({ name: "new-project", query: { step } });
    },

    back() {
      this.$router.go(-1);
    }
  },
  computed: {
    shouldRenderDataErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors.length > 0;
      }
      return false;
    }
  },
  mounted() {
    if (this.step !== 1) {
      this.$router.replace({ name: "new-project", query: { step: 1 } });
    }
  },
  watch: {
    step: function(newVal, oldVal) {
      if (newVal === oldVal) {
        return;
      }

      if (!newVal) {
        return this.$router.replace({
          name: "new-project",
          query: { step: 1 }
        });
      }

      if (newVal === 2) {
        if (!this.parsedData.data) {
          return this.$router.replace({
            name: "new-project",
            query: { step: 1 }
          });
        }
      }

      if (newVal === 3) {
        if (!this.parsedData.data && !this.parsedData.increment) {
          this.$router.replace({ name: "new-project", query: { step: 1 } });
        }
      }
    }
  }
};
</script>

<style scoped></style>
