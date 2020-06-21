<template>
  <v-card>
    <v-card-title>Hello</v-card-title>
    <v-card-text>
      <page-loading v-if="loading" id="loading-card"></page-loading>
    </v-card-text>
  </v-card>
</template>

<script>
  import PageLoading from "../../components/loading/PageLoading";
  import ProjectsAPI from "../../services/api/ProjectsAPI";

  export default {
  name: "ProjectPage",
  components: { PageLoading },
  props: { projectId: String },
  data() {
    return {
      loading: false,
      projectInfo: {}
    };
  },
  watch: {
    projectId: function() {
      this.loadProjectInfo();
    }
  },
  methods: {
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
