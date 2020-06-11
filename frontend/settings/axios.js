import axios from "axios";
import { API_SERVER } from "../settings/constants";
import Vue from "vue";
import router from "../src/router/vue-router";
import store from "../src/store/vue-store";

Vue.config.productionTip = false;

const user = JSON.parse(localStorage.getItem("user"));
let token = null;

console.log("[Axios].() user:", user);

if (user) {
  console.log("[Axios]. :", user);
  token = user.tokens.accessToken;
}

console.log("[Axios]. token:", token);

Vue.prototype.$http = axios;

Vue.prototype.$http.defaults.baseURL = API_SERVER;
Vue.prototype.$http.defaults.headers.common["Authorization"] = token;

// Axios.defaults.baseURL = API_SERVER
axios.interceptors.response.use(
  response => response,
  async error => {
    const status = error.response ? error.response.status : null;
    const text = error.response ? error.response.data : null;
    console.log("[axios.js]interceptors. : status", status);
    if (status === 401) {
      // will loop if refreshToken returns 401
      try {
        console.log(
          "[axios].refreshTokens OLD error.config.headers[Authorization]:",
          error.config.headers["Authorization"]
        );
        const tokens = await store.dispatch("auth/refreshTokens");
        Vue.prototype.$http.defaults.headers.common["Authorization"] =
          tokens.accessToken;
        error.config.headers["Authorization"] = tokens.accessToken;
        console.log(
          "[axios].refreshTokens new error.config.headers[Authorization]:",
          error.config.headers["Authorization"]
        );
        return Vue.prototype.$http.request(error.config);
      } catch (e) {
        console.log("[axios.js]. status === 401 error:", e);
        return Promise.reject(error);
      }
    }

    if (status === 406 || status === 400) {
      await store.dispatch("auth/logout");
      Vue.$toast.open({
        message: `${Vue.$t("YOU_WERE_LOGGED_OUT")}`,
        type: "error",
        position: "bottom-right"
      });
      await router.push("/login");
    } else if (status) {
      console.log("[Axios]. status text:", status, text);
      Vue.$toast.open({
        message: `${Vue.$t(text)} (${status})`,
        type: "error",
        position: "bottom-right"
      });
      /* await store.dispatch({ type:'error/setError', status, text} )*/
    }

    return Promise.reject(error);
  }
);
