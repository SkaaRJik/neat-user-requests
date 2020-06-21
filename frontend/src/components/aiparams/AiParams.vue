<template>
  <v-container>
    <v-switch
      :label="$t('Advanced_mode')"
      :true-value="true"
      :false-value="false"
      v-model="isAdvanced"
    ></v-switch>
    <v-list class="pa-1">
      <div v-for="item in aiConfig" :key="item.header">
        <v-subheader v-if="item.show && showSublist(item.params)">{{
          $t(item.header)
        }}</v-subheader>

        <v-list-item-group v-if="item.show && showSublist(item.params)">
          <v-list-item
            v-for="(param, i) in item.params"
            :key="i"
            v-show="!isHidden(param)"
          >
            <v-list-item-content>
              <v-switch
                v-if="isFieldBoolean(param.value)"
                :label="$t(param.name)"
                :true-value="true"
                :false-value="false"
                v-model="param.value"
              ></v-switch>

              <v-text-field
                v-if="isFieldString(param.value) || isFieldNumber(param.value)"
                :type="isFieldNumber(param.value) ? 'number' : 'text'"
                :label="$t(param.name)"
                v-model="param.value"
              >
              </v-text-field>




            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </div>
    </v-list>
  </v-container>
</template>

<script>
  import AIAPI from "../../services/api/AIAPI";
  import _ from "lodash";

  export default {
  name: "AiParams",
  props: {
    config: Object
  },
  data: function() {
    return {
      aiConfig: [],
      isAdvanced: false,
      functions: [],
    };
  },
  methods: {

    async loadFunctions(){
      try {
        const res = await AIAPI.getActivationFunctions();
        this.functions = res.data;
      } catch (e) {
        console.error("[AiParams.vue].loadDefaultConfig error:", e);
      }
    },

    async loadDefaultConfig() {
      this.loading = true;
      try {
        const res = await AIAPI.getDefaultConfig();
        this.aiConfig = res.data;
        console.log(
          "[AiParams.vue].loadDefaultConfig this.aiConfig:",
          this.aiConfig
        );
      } catch (e) {
        console.error("[AiParams.vue].loadDefaultConfig error:", e);
      } finally {
        this.loading = false;
      }
    },

    isFieldBoolean(value) {
      return _.isBoolean(value);
    },

    isFieldString(value) {
      return _.isString(value);
    },

    isFieldNumber(value) {
      return _.isNumber(value);
    },

    isFieldArray(value) {
      return _.isArray(value);
    },

    showSublist(elements) {
      let hidden = true;
      for (const value of elements) {
        hidden = hidden && this.isHidden(value);
      }
      return !hidden;
    },

    isHidden(param) {
      if (param.showInGui) {
        if (param.isAdvanced) {
          return !this.isAdvanced;
        } else if (!param.isAdvanced) {
          return false;
        }
      }

      return true;
    }
  },
  mounted() {
    this.loadFunctions()
    this.loadDefaultConfig();
  }
};
</script>

<style scoped></style>
