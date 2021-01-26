export const error = {
  namespaced: true,
  state: { errors: {} },
  actions: {
    setError({ commit }, { status, text, delay }) {
      const timerId = setTimeout(function() {
        commit("removeError", { timerId });
      }, delay || 5000);
      commit("addError", { timerId, status, text });
    },
    clearErrors({ commit }) {
      commit("clearErrors");
    }
  },
  mutations: {
    addError(state, { timerId, status, text }) {
      state.errors = {
        ...state.errors,
        [timerId]: { status, text }
      };
    },
    removeError(state, { timerId }) {
      const newError = { ...state.errors };
      delete newError[timerId];
      state.errors = newError;
    },
    clearErrors(state) {
      Object.keys(state.errors).forEach(key =>
        clearInterval(Number.parseInt(key))
      );
      state.errors = {};
    }
  },
  getters: {
    errors: state => {
      return state.errors;
    }
  },
  beforeDestroy() {
    this.unwatch();
  }
};
