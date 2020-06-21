import Vue from "vue";

const controllerPath = "/api/trainer";
export default {
  getDefaultConfig: () => {
    return Vue.prototype.$http.get(`${controllerPath}/default-config`);
  },

  getActivationFunctions: () => {
    return Vue.prototype.$http.get(`${controllerPath}/activation-functions`);
  }
};
