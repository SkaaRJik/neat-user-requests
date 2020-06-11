<template>
  <v-card>
    <v-card-actions>
      <v-btn :disabled="isLoading" @click="refreshToken">
        REFRESH TOKEN
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import AuthAPI from "../services/api/AuthAPI";

export default {
  data() {
    return {
      isLoading: false
    };
  },

  methods: {
    async refreshToken() {
      this.isLoading = true;
      try {
        const tokens = await AuthAPI.test();
        console.log("[About].refreshToken tokens:", tokens);
      } catch (e) {
        console.log("[About].refreshToken error:", e);
      }

      this.isLoading = false;
    }
  }
};
</script>
