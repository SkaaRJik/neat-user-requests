import AuthService from "../../services/AuthService";

const user = JSON.parse(localStorage.getItem('user'));
console.log('[AuthModule]. user :',user)
const initialState = user
    ? { status: { loggedIn: true }, user }
    : { status: { loggedIn: false }, user: null };

export const auth = {
    namespaced: true,
    state: initialState,
    actions: {
        async login({ commit }, user) {
            try{
                const userData = await AuthService.login(user)
                commit('loginSuccess', userData);
                return Promise.resolve(userData);
            } catch (error) {
                commit('loginFailure');
                return Promise.reject(error);
            }
        },
        logout({ commit }) {

            commit('logout');
        },
        async register({ commit }, user) {

            try {
                const message = await AuthService.register(user)
                commit('registerSuccess');
                return Promise.resolve(message);
            } catch (error) {
                commit('registerFailure');
                return Promise.reject(error);
            }
        },
        async refreshTokens({ commit, state }) {
            console.log('[AuthModule].refreshTokens state:',state)
            try {
                if(state.user){
                    const tokens = await AuthService.refreshTokens(state.user)

                    commit('refreshTokenSuccess', tokens);
                    return Promise.resolve(tokens);
                }
                console.log('[AuthModule].refreshTokens REJECT:',)
                return Promise.reject(new Error('User is not present in vuex store'));

            } catch (error) {
                //commit('refreshTokenFailure');
                console.log('[AuthModule].refreshTokens REJECT:',)
                return Promise.reject(error);
            }
        }

    },
    mutations: {
        loginSuccess(state, user) {
            state.status.loggedIn = true;
            state.user = user;
        },
        loginFailure(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
        logout(state) {
            AuthService.logout();
            state.status.loggedIn = false;
            state.user = null;
        },
        registerSuccess(state) {
            state.status.loggedIn = false;
        },
        registerFailure(state) {
            state.status.loggedIn = false;
        },
        refreshTokenSuccess(state, tokens) {
            state.status.loggedIn = true;
            state.user.tokens = tokens;
        },
        refreshTokenFailure(state) {
            state.status.loggedIn = true;
            state.user = null;
        }
    },
    getters: {
        userData: state => {
            return state.user
        }
    }
};
