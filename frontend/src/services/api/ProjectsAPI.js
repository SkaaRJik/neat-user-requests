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
  }
};