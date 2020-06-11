<template>
  <div id="full_screen_table">
    <v-btn @click="newProject" bottom color="primary" fab fixed right small>
      <v-icon>mdi-plus</v-icon>
    </v-btn>
    <v-container>
      <v-row lg md xl>
        <v-col sm="12">
          <v-card>
            <v-card-title>
              {{ $t("Projects") }}
              <v-spacer></v-spacer>
              <v-text-field
                :label="$t('Search')"
                append-icon="mdi-magnify"
                hide-details
                single-line
                v-model="search"
              ></v-text-field>
              <v-spacer></v-spacer>
              <v-btn-toggle
                color="primary"
                mandatory
                shaped
                v-model="itemsPerPage"
              >
                <v-btn :value="5">
                  5
                </v-btn>

                <v-btn :value="10">
                  10
                </v-btn>

                <v-btn :value="25">
                  25
                </v-btn>

                <v-btn :value="50">
                  50
                </v-btn>
              </v-btn-toggle>
            </v-card-title>
            <v-data-table
              :headers="headers"
              :items="projects"
              :items-per-page="itemsPerPage"
              :page.sync="page"
              :search="search"
              @click:row="clicked"
              @page-count="pageCount = $event"
              class="elevation-1"
              hide-default-footer
            >
              <template v-slot:item.trainingError="{ item }">
                <v-fab-transition>
                  <span>
                    {{ item.trainingError }}
                  </span>
                </v-fab-transition>
              </template>
              <template v-slot:item.predictionError="{ item }">
                <span>{{ item.predictionError }}</span>
              </template>
              <template v-slot:item.status="{ item }">
                <v-fab-transition>
                  <span v-if="item.status === 'In_Queue'">{{
                    $t(item.status, { size: item.inQueue })
                  }}</span>
                  <v-progress-linear
                    :value="progress[item.name]"
                    color="light-blue"
                    height="10"
                    striped
                    v-else-if="item.status === 'In_Progress'"
                  ></v-progress-linear>
                  <v-icon
                    color="success"
                    v-else-if="item.status === 'Done'"
                    x-large
                    >mdi-check
                  </v-icon>
                </v-fab-transition>
              </template>
            </v-data-table>
            <div class="text-center pt-2">
              <v-pagination :length="pageCount" v-model="page"></v-pagination>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "ProjectsViewFullScreen",
  methods: {
    clicked(value) {
      console.log("[Projects].clicked() value:", value);
    },
    async newProject() {
      await this.$router.push({ name: "new-project" });
    }
  },
  data() {
    return {
      intervalId: null,
      search: "",
      page: 1,
      pageCount: 0,
      itemsPerPage: 10,
      headers: [
        {
          text: this.$t("Project_Name"),
          align: "start",
          value: "name"
        },
        {
          text: this.$t("Training_Error"),
          value: "trainingError",
          sortable: false
        },
        {
          text: this.$t("Prediction_Error"),
          value: "predictionError",
          sortable: false
        },
        { text: this.$t("Status"), value: "status", sortable: false }
      ],
      options: {
        multiSort: false
      },
      progress: {},
      projects: [
        {
          name: "Frozen Yogurt",
          trainingError: "",
          predictionError: "",
          inQueue: 1,
          status: "In_Queue"
        },
        {
          name: "Hello",
          trainingError: "",
          predictionError: "",
          inQueue: 1,
          status: "In_Queue"
        },
        {
          name: "Its me",
          trainingError: Math.random(),
          predictionError: Math.random(),
          status: "Done"
        }
      ]
    };
  }
};
</script>

<style scoped></style>
