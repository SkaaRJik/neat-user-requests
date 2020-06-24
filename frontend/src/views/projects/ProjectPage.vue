<template>
  <v-card>
    <v-card-title
      >{{ $t("Project") }}:
      {{ projectInfo.name ? projectInfo.name : "" }}</v-card-title
    >
    <v-card-text>
      <!--<page-loading v-if="loading" id="loading-card"></page-loading>-->
      <v-main>
        <v-row>
          <v-col cols="12">
            <v-card>
              <v-card-title>
                Прогноз: Экологоемкость по воде (общий объем сточных вод на душу
                населения), куб.м/чел.
              </v-card-title>
              <v-card-text>
                <reactive-chart
                  :data="predictionData"
                  :layout="predictionLayout"
                ></reactive-chart>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12">
            <v-row align="center" justify="center">
              <span class="ma-3">
                Ошибка обучения: 0.003
              </span>
              <span class="ma-3">
                Ошибка тестирования: 0.021
              </span>
              <span class="ma-3">
                Ошибка прогнозирования: 0.07
              </span>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col sm="12" xs="12" md="12" lg="6" xl="6">
            <v-card>
              <v-card-title>Ошибка обучения</v-card-title>
              <v-card-text>
                <reactive-chart
                  :data="trainErrorChartData"
                  :layout="errorLayout"
                ></reactive-chart>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col sm="12" xs="12" md="12" lg="6" xl="6">
            <v-card>
              <v-card-title>Ошибка прогнозирования</v-card-title>
              <v-card-text>
                <reactive-chart
                  :data="testErrorChartData"
                  :layout="errorLayout"
                ></reactive-chart>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-main>
    </v-card-text>
  </v-card>
</template>

<script>
  import ProjectsAPI from "../../services/api/ProjectsAPI";
  import ReactiveChart from "../../components/charts/ReactiveChart";
  import {getRandomValue} from "../../utils/generate.utils";

  export default {
  name: "ProjectPage",
  components: {
    ReactiveChart
  },
  props: { projectId: String },
  data() {
    return {
      loading: false,
      projectInfo: {},
      chartsData: [],
      errorLayout: {
        xaxis: {
          title: this.$t('Epoch')
        },
        yaxis: {
          title: this.$t('Error')
        }
      },
      testErrorChartData: [],
      trainErrorChartData: [],
      predictionData: [],
      predictionLayout: {
        xaxis: {
          title: this.$t('Values')
        },
        yaxis: {
          title: this.$t('Years')
        }
      }
    };
  },
  watch: {
    projectId: function() {
      this.loadProjectInfo();
    }
  },
  methods: {
    generateRandomCharts() {
      const years = 40;

      const trace1 = {
        x: [],
        y: [],
        type: "scatter",
        mode: "lines+markers",
        name: "Факт"
      };

      const trace2 = {
        x: [],
        y: [],
        type: "scatter",
        mode: "lines+markers",
        name: "Прогноз"
      };

      for (let i = 0; i < years; i++) {
        const val = getRandomValue(50, 300);
        if (i <= years - 10) {
          trace1.x.push(1990 + i);
          trace1.y.push(val);
        }
        trace2.x.push(1990 + i);
        trace2.y.push(val + getRandomValue(0, 50));
      }

      this.predictionData = [trace1, trace2];

      const trainData = {
        x: [],
        y: [],
        type: "scatter",
        mode: "lines+markers",
        name: "Ошибка"
      };

      const testData = {
        x: [],
        y: [],
        type: "scatter",
        mode: "lines+markers",
        name: "Ошибка"
      };

      for (let i = 0; i < 100; i++) {
        testData.x.push(i + 1);
        testData.y.push(1 / (i + 2 + Math.random()));
        trainData.x.push(i + 1);
        trainData.y.push(1 / (i + 2 + Math.random()));
      }

      this.testErrorChartData = [testData];
      this.trainErrorChartData = [trainData];

      const newChartData = [];

      for (let i = 0; i < 3; i++) {
        newChartData.push([trace1, trace2]);
      }

      this.chartsData = newChartData;
    },

    async loadProjectInfo() {
      this.loading = true;
      const projectId = this.$route.params.id;
      console.log("[ProjectPageVue].loadProjectInfo params:", projectId);
      try {
        const res = await ProjectsAPI.getProjectDetails(projectId);
        this.projectInfo = res.data;
        if (this.projectInfo.status === "NEW") {
          await this.$router.push({
            name: "project-configure",
            params: { id: this.projectId }
          });
        }
      } catch (e) {
        console.log("[ProjectPageVue].loadProjectInfo e:", e);
      }
      this.loading = false;
      this.generateRandomCharts();
    }
  },
  mounted() {
    this.loadProjectInfo();
  }
};
</script>

<style scoped>
#loading-card {
  height: 500px;
}
</style>
