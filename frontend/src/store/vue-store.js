import Vue from 'vue'
import Vuex from 'vuex'
import {auth} from "./modules/auth.module";
import {error} from "./modules/error-from-server.module";

Vue.use(Vuex)

const store = new Vuex.Store({

    modules: {
        auth,
        error,
    }
})

export default store;
