<template>
  <v-card>
    <v-card-title>
      <v-alert
        :type="alert.type"
        border="bottom"
        class="mb-0"
        dense
        elevation="2"
        style="width: 100%"
        transition="scale-transition"
        v-if="alert.message"
        v-model="alert.message"
      >
        {{ alert.message }}
      </v-alert>
    </v-card-title>
    <v-card-text>
      <v-container grid-list-md>
        <v-layout wrap>
          <v-flex xs12>
            <v-text-field
              :hint="emailError"
              :label="$t('Email') + ' *'"
              persistent-hint
              required
              v-model="userDetails.email"
              v-on:focusout="checkEmail"
            >
            </v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field
              :hint="usernameError"
              :label="$t('Login') + ' *'"
              persistent-hint
              required
              v-model="userDetails.username"
              v-on:focusout="checkUsername"
            >
            </v-text-field>
          </v-flex>
          <v-flex sm6 xs12>
            <v-text-field
              :label="$t('Password') + ' *'"
              required
              type="password"
              v-model="userDetails.password"
            ></v-text-field>
          </v-flex>
          <v-flex sm6 xs12>
            <v-text-field
              :hint="passwordError"
              :label="$t('Password_Repeat') + ' *'"
              persistent-hint
              required
              type="password"
              v-model="repeatPassword"
            >
            </v-text-field>
          </v-flex>
          <v-flex sm6 xs12>
            <v-text-field
              :label="$t('First_Name') + ' *'"
              required
              v-model="userDetails.firstName"
            ></v-text-field>
          </v-flex>
          <v-flex sm6 xs12>
            <v-text-field
              :label="$t('Last_Name') + ' *'"
              required
              v-model="userDetails.lastName"
            ></v-text-field>
          </v-flex>
        </v-layout>
      </v-container>
      <small>{{ $t("*_Is_Required") }}</small>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn @click="signUp" color="blue darken-1" text
        >{{ $t("To_Sign_Up") }}
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
  name: "Registration",

  data() {
    return {
      alert: {
        type: "error",
        message: "",
        timer: null
      },
      userDetails: {
        email: "",
        username: "",
        password: "",
        firstName: "",
        lastName: ""
      },

      emailError: null,
      usernameError: null,
      repeatPassword: null
    };
  },
  props: {
    onSuccess: Function,
    cancel: Function
  },
  methods: {
    async signUp() {
      try {
        this.alert.message = "";
        const err = this.validateFields();
        if (err) {
          console.log("[Registration].signUp() validation error:", err);
          this.alert.type = "error";
          this.alert.message = err;
          return;
        }
        console.debug("[Registration].signUp() userDetails:", this.userDetails);
        const message = await this.$store.dispatch(
          "auth/register",
          this.userDetails
        );
        console.debug("[Registration].signUp() res:", message);
        this.$emit(
          "onSuccess",
          this.userDetails.email,
          this.userDetails.password,
          message
        );
      } catch (e) {
        console.log("[Registration].signUp() error:", e);
        this.alert.type = "error";
        this.alert.message = e.response ? e.response : e.message;
      }
    },
    async checkEmail() {
      this.emailError = null;
      if (isEmailValid(this.userDetails.email)) {
        const emailExists = await checkEmailExist(this.userDetails.email);
        if (emailExists.data === true) {
          this.emailError = this.$t("Email_Already_Taken_Info");
        }
      } else {
        this.emailError = this.$t("Email_Is_Not_Valid");
      }
    },
    async checkUsername() {
      this.usernameError = null;
      if (isUsernameValid(this.userDetails.username)) {
        const usernameExist = await checkUsernameExist(
          this.userDetails.username
        );
        if (usernameExist.data === true) {
          this.usernameError = this.$t("Login_Already_Taken_Info");
        }
      } else {
        this.usernameError = this.$t("Login_is_not_valid", {
          symbols: "@ , ."
        });
      }
    },
    validateFields() {
      if (!this.userDetails.email)
        return this.$t("Fill_Field", { field: this.$t("Email") });
      if (this.emailError) return this.emailError;
      if (!this.userDetails.username)
        return this.$t("Fill_Field", { field: this.$t("Login") });
      if (this.usernameError) return this.usernameError;
      if (!this.userDetails.password)
        return this.$t("Fill_Field", { field: this.$t("Password") });
      if (!this.repeatPassword)
        return this.$t("Fill_Field", { field: this.$t("Password_Repeat") });
      if (this.passwordError) return this.passwordError;
      if (!this.userDetails.firstName)
        return this.$t("Fill_Field", { field: this.$t("First_Name") });
      if (!this.userDetails.lastName)
        return this.$t("Fill_Field", { field: this.$t("Last_Name") });
      return null;
    }
  },
  computed: {
    passwordError: function() {
      if (
        this.repeatPassword &&
        this.repeatPassword.length !== 0 &&
        this.repeatPassword !== this.userDetails.password
      ) {
        return this.$t("Passwords_Mismatch");
      }
      return null;
    }
  }
};
</script>

<style scoped></style>
