
export const error = {
    namespaced: true,
    state: {errors:{}},
    actions: {
        setError({ commit, state }, {status, text, delay}) {
            let timerId = setTimeout(function() {commit('removeError', {timerId})}, delay || 5000)
            console.log('[ErrorFromServerModule].setError timerId:',timerId)
            commit('addError', {timerId, status, text});
        },


    },
    mutations: {
        addError(state, {timerId, status, text}) {
            state.errors[timerId] = {status, text};
        },
        removeError(state, timerId) {
            delete state.errors[timerId]
        },
    },
    getters: {
        errors: state => {
            return state.errors
        }
    }
};
