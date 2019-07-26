import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        profile: null,
        dialogShow: false,

    },
    mutations: {
        showHideDialog(state) {
            state.dialogShow = !state.dialogShow
        },
        setProfileMutation(state, profile){
            state.profile = profile
            state.profile.avatar=state.profile.avatar.data
            state.profile.token=state.profile.token.token
        }
    },
    actions:{
      showHideDialog({ commit }){
          commit('showHideDialog')
      },

      setProfile({commit, state}, profile){
          if(profile!=null){
              commit('setProfileMutation', profile)
          }
      }
    }
})