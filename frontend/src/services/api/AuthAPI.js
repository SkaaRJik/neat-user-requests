import Vue from "vue";

const controllerPath = "";
export default {
  signIn: signInDetails => {
    return Vue.prototype.$http.post(`${controllerPath}/login`, {
      ...signInDetails
    });
  },
  signUp: signUpDetails => {
    return Vue.prototype.$http.post(`${controllerPath}/signup`, {
      ...signUpDetails
    });
  },
  checkEmail: email => {
    return Vue.prototype.$http.get(
      `${controllerPath}/email-exist?email=${email}`
    );
  },
  checkUsername: username => {
    return Vue.prototype.$http.get(
      `${controllerPath}/username-exist?username=${username}`
    );
  },
  test: () => {
    console.log(
      "[AuthApi].test headers:",
      Vue.prototype.$http.defaults.headers["Authorization"]
    );
    return Vue.prototype.$http.get("/api/trainer/default-config");
  },

  refreshTokens(refreshToken) {
    return Vue.prototype.$http.get(
      `${controllerPath}/refresh-tokens?refreshToken=${refreshToken}`
    );
  }
};
