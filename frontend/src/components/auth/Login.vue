<template>
  <v-card>
    <v-card-title>
      <v-alert
        :type="alertType"
        border="bottom"
        class="mb-0"
        dense
        elevation="2"
        style="width: 100%"
        transition="scale-transition"
        v-model="showAlert"
      >
        {{ dataToLogin.message }}
      </v-alert>
    </v-card-title>
    <v-card-text>
      <v-container grid-list-md>
        <v-layout wrap>
          <v-flex xs12>
            <v-text-field
              :label="$t('Login_Or_Email')"
              required
              type="email"
              v-model="dataToLogin.username"
              v-on:focusin="
                () => {
                  this.dataToLogin.message = '';
                }
              "
              v-on:focusout="checkUsername"
            />
          </v-flex>
          <v-flex xs12>
            <v-text-field
              :label="$t('Password')"
              required
              type="password"
              v-model="dataToLogin.password"
              v-on:focusin="
                () => {
                  checkUsername;
                }
              "
            />
          </v-flex>
        </v-layout>
      </v-container>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn :disabled="isLoading" @click="signIn" color="blue darken-1" text>
        <v-progress-circular
          color="amber"
          indeterminate
          v-show="isLoading"
        ></v-progress-circular>
        <div v-show="!isLoading">{{ $t("To_Sign_In") }}</div>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import {
  checkEmailExist,
  checkUsernameExist,
  isEmailValid,
  isUsernameValid
} from "../../services/utils/validators";

export default {
  name: "Login",

  props: {
    dataToLogin: Object
  },

  computed: {
    showAlert: function() {
      console.log("[Login].showAlert() :");
      return this.dataToLogin.message !== "";
    }
  },

  data() {
    return {
      isLoading: false,
      alertType: "error",
      alert_timer: null
    };
  },

  methods: {
    async signIn() {
      console.log("[Login].signIn() start:");
      const error = this.validateFields();
      console.log("[Login].signIn() error:", error);
      if (error) {
        this.alertType = "error";
        this.dataToLogin.message = error;
      } else {
        try {
          await this.$store.dispatch("auth/login", this.dataToLogin);
          await this.$router.push({ name: "projects" });
        } catch (e) {
          this.alertType = "error";
          if (e.response) {
            if (e.response.status === 401) {
              this.dataToLogin.message = this.$t(
                "Wrong_Password_Or_Login_Error"
              );
            } else {
              this.dataToLogin.message = e.response.data;
            }
          } else {
            this.dataToLogin.message = e.message;
          }
        }
      }
    },
    async checkUsername() {
      if (isUsernameValid(this.dataToLogin.username)) {
        const usernameExist = await checkUsernameExist(
          this.dataToLogin.username
        );
        console.log("[Login].checkUsername :", usernameExist);
        if (usernameExist.data === false) {
          this.alertType = "info";
          this.dataToLogin.message = this.$t(
            "User_With_Such_Username_Does_Not_Exist_Info"
          );
        } else {
          this.dataToLogin.message = "";
        }
      } else {
        if (isEmailValid(this.dataToLogin.username)) {
          const emailExists = await checkEmailExist(this.dataToLogin.username);
          console.log("[Login].checkUsername :", emailExists);
          if (emailExists.data === false) {
            this.alertType = "info";
            this.dataToLogin.message = this.$t(
              "User_With_Such_Email_Does_Not_Exist_Info"
            );
          } else {
            this.dataToLogin.message = "";
          }
        } else {
          this.alertType = "error";
          this.dataToLogin.message = this.$t("Email_Is_Not_Valid");
        }
      }
    },
    validateFields() {
      if (!this.dataToLogin.username)
        return this.$t("Fill_Field", { field: this.$t("Login_Or_Email") });
      if (!this.dataToLogin.password)
        return this.$t("Fill_Field", { field: this.$t("Password") });
      return null;
    }
  }
};
</script>

<style scoped></style>
