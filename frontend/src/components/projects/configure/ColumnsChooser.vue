<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-list width="100%">
          <v-list-item
            two-line
            v-for="(item, index) in headerTypes"
            :key="`list-${index}`"
          >
            <v-list-item-content>
              <v-list-item-title>
                <v-container>
                  <v-row>
                    <v-col>
                      {{ item.name }}
                    </v-col>
                    <v-col sm="12" xs="12" md="3" lg="3" xl="3">
                      <v-select
                        :value="item.type"
                        :items="nodeTypes"
                        :label="$t('Type_Node')"
                        @change="handleDataType(item, $event)"
                        dense
                        outlined
                        :append-icon="
                          (item.type === 'Output' && outputs > 1) ||
                          outputs == 0 ||
                          inputs == 0
                            ? 'mdi-alert'
                            : 'mdi-menu-down'
                        "
                        :error="
                          (item.type === 'Output' && outputs > 1) ||
                            outputs == 0 ||
                            inputs == 0
                        "
                        :error-messages="(item.type === 'Output' && outputs > 1)
                        ? $t('ERROR_OUTPUTS_MUST_BE_ONLY_1') : outputs == 0
                        ? $t('ERROR_OUTPUTS_NODE_CANT_BE_0') : inputs == 0
                        ? $t('ERROR_INPUTS_NODE_CANT_BE_0') : ''"
                      >
                        <template v-slot:selection="{ item: headerType }">
                          <span>{{ $t(headerType) }}</span>
                        </template>
                        <template v-slot:item="{ item: headerType }">
                          <span>{{ $t(headerType) }}</span>
                        </template>
                      </v-select>
                    </v-col>
                  </v-row>
                </v-container>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "ColumnsChooser",
  props: {
    value: Object,
  },
  data: () => {
    return {
      outputsIndex: [],
      inputs: 0,
      outputs: 0,
      headerTypes: [],
      nodeTypes: ["Input", "Output", "Unused"]
    };
  },
  methods: {
    handleDataType(item, newValue) {
      if (item.type !== newValue) {
        if (newValue === "Input") {
          this.inputs++;
        } else if (newValue === "Output") {
          this.outputs++;
        }

        if (item.type === "Input") {
          this.inputs--;
        } else if (item.type === "Output") {
          this.outputs--;
        }

        if (this.outputs > 1) {
          this.$toast.open({
            message: `${this.$t("ERROR_OUTPUTS_MUST_BE_ONLY_1")}`,
            type: "error",
            position: "bottom-right",
            duration: 3000
          });
        }

        if (this.outputs === 0) {
          this.$toast.open({
            message: `${this.$t("ERROR_OUTPUTS_NODE_CANT_BE_0")}`,
            type: "error",
            position: "bottom-right",
            duration: 3000
          });
        }

        if (this.inputs === 0) {
          this.$toast.open({
            message: `${this.$t("ERROR_INPUTS_NODE_CANT_BE_0")}`,
            type: "error",
            position: "bottom-right",
            duration: 3000
          });
        }

        item.type = newValue;

        const newNormalizedData = {
          ...this.value,
          inputs: this.inputs,
          outputs: this.outputs,
          headers: this.headerTypes
        };
        this.$emit("input", newNormalizedData);
      }
    },
    initHeaders(headers){
      this.inputs = 0;
      this.outputs = 0;
      this.headerTypes = headers.map((val, index) => {
        this.inputs += val.columnType === "Input" ? 1 : 0
        this.outputs += val.columnType === "Output" ? 1 : 0
        return {
          name: val.columnName,
          type: headers.length - 1 === index ? "Output" : "Input"
        };
      });
      const newNormalizedData = {
        ...this.value,
        inputs: this.inputs,
        outputs: this.outputs,
        headers: this.headerTypes
      };
      this.$emit("input", newNormalizedData);
    }
  },
  mounted() {
    this.initHeaders(this.value.columns)
  },
  watch: {
    value: function(newValue, oldValue) {
      if (newValue) {
        if(newValue !== oldValue) {
          if(newValue.columns !== oldValue.columns){
            this.initHeaders(newValue.columns);
          }
        }
      }
    }
  }
};
</script>

<style scoped></style>
