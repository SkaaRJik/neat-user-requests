import Vue from 'vue'
import VueResource from 'vue-resource'
import Vuetify from 'vuetify'
import App from 'pages/App.vue'
import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader

Vue.use(Vuetify)
Vue.use(VueResource)


var app = new Vue({
    el: '#app',
    render: a => a(App)
})