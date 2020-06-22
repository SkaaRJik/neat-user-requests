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
                        :label="$t('Type_node')"
                        @change="handleDataType(item, $event)"
                        dense
                        outlined
                        :append-icon="
                          item.type === 'Output' && outputs > 1
                            ? 'mdi-alert'
                            : 'mdi-menu-down'
                        "
                        :error="item.type === 'Output' && outputs > 1"
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
    headers: Array
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
        console.log(
          "[DataSeparation].handleDataType newValue:",
          newNormalizedData
        );
        this.$emit("input", newNormalizedData);
      }
    }
  },
  mounted() {},
  watch: {
    headers: function(newValue) {
      if (newValue) {
        this.headerTypes = newValue.map((val, index) => {
          return {
            name: val,
            type: newValue.length - 1 === index ? "Output" : "Input"
          };
        });
        this.inputs = newValue.length - 1;
        this.outputs = 1;
        const newNormalizedData = {
          ...this.value,
          inputs: this.inputs,
          outputs: this.outputs,
          headers: this.headerTypes
        };
        console.log("[ColumnsChooser].watch headers:", newNormalizedData);
        this.$emit("input", newNormalizedData);
      }
    },
    normalizedData: function(newVal) {
      if (newVal) {
        const newNormalizedData = {
          ...newVal,
          inputs: this.inputs,
          outputs: this.outputs,
          headers: this.headerTypes
        };
        console.log(
          "[ColumnsChooser].watch normalizedData:",
          newNormalizedData
        );
        this.$emit("input", newNormalizedData);
      }
    }
  }
};
</script>

<style scoped></style>
