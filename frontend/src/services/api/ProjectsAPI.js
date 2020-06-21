import Vue from "vue";

const controllerPath = "/api/projects";
export default {
  parseExcelFile: file => {
    const formData = new FormData();
    formData.append("file", file, file.name);

    return Vue.prototype.$http.post(`${controllerPath}/parse`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });
  },

  saveProject: projectDetails => {
    return Vue.prototype.$http.post(`${controllerPath}/save`, projectDetails);
  },

  getMyProjects: (page, itemsPerPage) => {
    return Vue.prototype.$http.get(`${controllerPath}/my`, {
      query: { page, itemsPerPage }
    });
  },

  getProjectDetails: id => {
    return Vue.prototype.$http.get(`${controllerPath}/${id}`);
  },

  getProjectData: id => {
    return Vue.prototype.$http.get(`${controllerPath}/${id}/data`);
  },

  findProject: params => {
    console.log("[ProjectsAPI.js].findProject query:", params);
    return Vue.prototype.$http.get(`${controllerPath}/find`, {
      params
    });
  }
};
