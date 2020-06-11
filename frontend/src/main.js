import Vue from "vue";
import App from "./App.vue";
import router from "./router/vue-router";
import store from "./store/vue-store";
import Vuetify from "vuetify/lib";
import vuetify from "./plugins/vuetify";
import ScrollLoader from "vue-scroll-loader";
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";
import "../settings/axios";
import i18n from "./i18n";
import VueToast from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";

Vue.use(Vuetify);
Vue.use(ScrollLoader);
Vue.use(VueToast);

// eslint-disable-next-line no-unused-vars
const vue = new Vue({
  router,
  store,
  vuetify,
  i18n,
  render: h => h(App)
}).$mount("#app");
