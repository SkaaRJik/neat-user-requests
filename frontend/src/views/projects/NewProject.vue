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

      <v-divider></v-divider>

      <v-stepper-step step="4">{{ $t("Data_Normalization") }}</v-stepper-step>
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
          <project-details :parsed-data="parsedData" />
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
                  @click="step = 4"
                  class="ma-3"
                  color="primary"
                >
                  {{ $t("Continue") }}
                  <v-icon> mdi-arrow-right</v-icon>
                </v-btn>
                <!--<v-btn
                  :disabled="projectName.length === 0"
                  @click="step = 4"
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
                </v-btn>-->
              </v-card-actions>
            </v-col>
          </v-row>
        </v-container>
      </v-stepper-content>

      <v-stepper-content step="4">
        <v-container>
          <data-normalization :parsed-data="parsedData" />
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
  import DataNormalization from "../../components/DataNormalization";

  export default {
  name: "NewProject",
  components: {
    ProjectDetails,
    UploadProjectData,
    DataNormalization
  },
  data() {
    return {
      projectName: "",
      file: null,
      excelUploading: false,
      legendError: false,
      parsedData: { increment: 0 },
      step: 1
    };
  },
  methods: {
    async handleSaveProject() {
      const data = {
        ...this.parsedData,
        name: this.projectName
      };
      const res = await ProjectsAPI.saveProject(data);
      console.log("[NewProject].handleSaveProject res:", res.data);
      await this.$router.push({
        name: "project-page",
        params: { id: res.data }
      });
    },

    redirectToProjectsPage() {
      this.$router.push({ name: "projects" });
    },

    async uploadXLSX() {
      this.excelUploading = true;
      try {
        console.log("[NewProject].uploadXLSX this.file:", this.file);
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
    }
  },
  computed: {
    nextPageDisabled: function() {
      console.log("[NewProjectVue].nextPageDisabled this.file:", this.file);
      return !this.file || this.excelUploading;
    },
    shouldRenderDataErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors.length > 0;
      }
      return false;
    }
  }
};
</script>

<style scoped></style>
