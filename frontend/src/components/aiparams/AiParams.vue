<template>
  <v-container>
    <v-tooltip bottom>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          @click="setAiConfigWithDeafultValues"
          bottom
          color="primary"
          fab
          fixed
          right
          small
          v-bind="attrs"
          v-on="on"
        >
          <v-icon>mdi-reload</v-icon>
        </v-btn>
      </template>
      <span>{{ $t("Reset_to_default_configuration") }}</span>
    </v-tooltip>

    <v-row>
      <v-col cols="12">
        <v-switch
          :label="$t('Advanced_mode')"
          :true-value="true"
          :false-value="false"
          v-model="isAdvanced"
        ></v-switch>
      </v-col>
    </v-row>
    <v-row>
      <template v-for="item in aiConfig">
        <v-col
          sm="12"
          xs="12"
          md="4"
          lg="4"
          xl="4"
          :key="item.header"
          v-if="item.show && showSublist(item.params)"
        >
          <v-card>
            <v-card-title>
              {{ $t(item.header) }}
            </v-card-title>
            <v-card-title>
              <v-list>
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
                      @blur="updateVModel"
                    ></v-switch>

                    <v-text-field
                      v-if="
                        isFieldString(param.value) || isFieldNumber(param.value)
                      "
                      style="width: 100%"
                      type="number"
                      :label="$t(param.name)"
                      v-model="param.value"
                      :max="param.maxValue ? param.maxValue : undefined"
                      :min="param.maxValue ? param.minValue : undefined"
                      :step="param.maxValue && param.maxValue > 1 ? 1 : 0.1"
                      @blur="updateVModel(param)"
                      :append-icon="
                        param.name === 'GENERATOR.SEED'
                          ? 'mdi-refresh'
                          : undefined
                      "
                      @click:append="
                        param.name === 'GENERATOR.SEED'
                          ? generateNewSeed(param)
                          : undefined
                      "
                    >
                    </v-text-field>

                    <v-expansion-panels
                      :multiple="true"
                      v-if="isFieldArray(param.value)"
                    >
                      <v-expansion-panel>
                        <v-expansion-panel-header>{{
                          $t(param.name)
                        }}</v-expansion-panel-header>
                        <v-expansion-panel-content>
                          <template v-for="(func, index) in functions">
                            <v-switch
                              :label="$t(func)"
                              :true-value="func"
                              false-value=""
                              v-model="param.value[index]"
                              :key="index"
                              @blur="updateVModel"
                            ></v-switch>
                          </template>
                        </v-expansion-panel-content>
                      </v-expansion-panel>
                    </v-expansion-panels>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card-title>
          </v-card>
        </v-col>
      </template>
    </v-row>
  </v-container>
</template>

<script>
import AIAPI from "../../services/api/AIAPI";
import _ from "lodash";

export default {
  name: "AiParams",
  props: {
    value: null,
  },
  data: function() {
    return {
      aiConfig: [],
      isAdvanced: false,
      functions: [],
      defaultConfig: []
    };
  },
  methods: {
    async loadFunctions() {
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
        this.defaultConfig = res.data;
        this.setAiConfigWithDeafultValues();
      } catch (e) {
        console.error("[AiParams.vue].loadDefaultConfig error:", e);
      } finally {
        this.loading = false;
      }
    },

    setAiConfigWithDeafultValues() {
      this.aiConfig = this.defaultConfig;
      this.updateVModel();
    },

    isFieldBoolean(value) {
      return _.isBoolean(value);
    },

    isFieldString(value) {
      return _.isString(value);
    },

    isFieldNumber(value) {
      if (_.isNumber(value)) {
        return true;
      }
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
    },

    generateNewSeed(item) {
      item.value = new Date().getTime();
    },

    updateVModel(param) {
      if (param) {
        if (param.maxValue || param.minValue) {
          if (param.value > param.maxValue) {
            param.value = param.maxValue;
          }
          if (param.value < param.minValue) {
            param.value = param.minValue;
          }
        }
      }
      this.$emit("input", this.aiConfig);
    }
  },
  mounted() {
    this.loadFunctions();
    this.loadDefaultConfig();
  },

};
</script>

<style scoped></style>
