import AuthAPI from "./api/AuthAPI";
import Vue from "vue";

class AuthService {
  async login(user) {
    const response = await AuthAPI.signIn(user);
    if (response.data.username) {
      localStorage.setItem("user", JSON.stringify(response.data));
    }
    Vue.prototype.$http.defaults.headers.common["Authorization"] =
      response.data.tokens.accessToken;
    return response.data;
  }

  logout() {
    localStorage.removeItem("user");
  }

  async register(user) {
    const res = await AuthAPI.signUp(user);
    return res.data;
  }

  async refreshTokens(user) {
    try {
      const res = await AuthAPI.refreshTokens(user.tokens.refreshToken);
      const tokens = res.data;
      user.tokens = tokens;
      localStorage.setItem("user", JSON.stringify(user));
      Vue.prototype.$http.defaults.headers.common["Authorization"] =
        user.tokens.accessToken;
      return Promise.resolve(tokens);
    } catch (e) {
      console.log('[AuthService].uploadAvatar e:', e);
      this.logout();
      return Promise.reject(e);
    }
  }

  async uploadAvatar(avatar) {
    try {

      await AuthAPI.uploadAvatar(avatar);
      return Promise.resolve(true);
    } catch (e) {
      console.log('[AuthService].uploadAvatar e:', e);
      return Promise.reject(e);
    }
  }
}

export default new AuthService();
