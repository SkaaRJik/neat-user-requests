<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
          <v-text-field
            v-model="trainPercentage"
            @change="testPercentage = 100 - trainPercentage"
            :label="$t('Train_Percentage')"
            suffix="%"
            class="ma-3"
            type="number"
            min="1"
            max="100"
          />
        </v-col>
        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
          <v-text-field
            v-model="testPercentage"
            :label="$t('Test_Percentage')"
            class="ma-3"
            type="number"
            suffix="%"
            min="1"
            max="100"
          />
        </v-col>
        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
          <v-btn @click="calculatePercentage">{{ $t("Submit") }}</v-btn>
        </v-col>
      </v-row>
      <v-row
        align="center"
        justify="center"
        v-if="trainEndIndex && testEndIndex"
      >
        <v-col>
          <span>{{
            `${$t("Train_Elements", { elements: trainEndIndex + 1 })},
            ${$t("Test_Elements", { elements: testEndIndex + 1 })},
            ${$t("Total", {
              elements: `${trainEndIndex + testEndIndex + 2} / ${
                value.output.length
              }`
            })}`
          }}</span>
        </v-col>
      </v-row>
    </v-col>

    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-list width="100%">
          <v-list-item
            two-line
            v-for="(item, index) in dataTypes"
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
                        :items="headerTypes"
                        :label="$t('Type_node')"
                        @change="handleDataType(item, $event)"
                        dense
                        outlined
                      >
                        <template v-slot:selection="{ item: dataType }">
                          <span>{{ $t(dataType) }}</span>
                        </template>
                        <template v-slot:item="{ item: dataType }">
                          <span>{{ $t(dataType) }}</span>
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
  name: "DataSeparation",
  props: {
    value: Object,
    parsedData: Object
  },
  data: () => {
    return {
      trainPercentage: 70,
      testPercentage: 30,
      trainEndIndex: null,
      testEndIndex: null,
      outputsIndex: [],
      dataTypes: [],
      inputs: 0,
      outputs: 0,
      headerTypes: ["Input", "Output", "Unused"]
    };
  },
  methods: {
    handleDataType(item, newValue) {
      console.log(
        "[DataSeparation].handleDataType oldValue, newValue:",
        item.type,
        newValue
      );
      if (item.type !== newValue) {
        console.log(
          "[DataSeparation].handleDataType BEFORE this.inputs, this.outputs:",
          this.inputs,
          this.outputs
        );
        if (newValue === "Input") {
          this.inputs++;
        } else if (newValue === "Output") {
          this.outputs++;
        }
        console.log(
          "[DataSeparation].handleDataType after ++ this.inputs, this.outputs:",
          this.inputs,
          this.outputs
        );

        if (item.type === "Input") {
          this.inputs--;
        } else if (item.type === "Output") {
          this.outputs--;
        }
        console.log(
          "[DataSeparation].handleDataType after -- this.inputs, this.outputs:",
          this.inputs,
          this.outputs
        );

        item.type = newValue;
        if (this.value.output) {
          const newValue = {
            ...this.value,
            trainEndIndex: this.trainEndIndex,
            testEndIndex: this.trainEndIndex + this.testEndIndex,
            inputs: this.inputs,
            outputs: this.outputs
          };
          console.log("[DataSeparation].handleDataType newValue:", newValue);
          this.$emit("input", newValue);
        }
      }
    },
    calculatePercentage() {
      if (this.trainPercentage + this.testPercentage > 100) {
        this.$toast.open({
          message: `${this.$t("ERROR_SUM_OF_PERCENTAGE_GREATER_100")}`,
          type: "error",
          position: "bottom-right",
          duration: 5000
        });
      }
      if (this.trainPercentage + this.testPercentage <= 0) {
        this.$toast.open({
          message: `${this.$t("ERROR_SUM_OF_PERCENTAGE_LESS_OR_EQUAL_0")}`,
          type: "error",
          position: "bottom-right",
          duration: 5000
        });
      }
      this.trainEndIndex = Math.round(
        (this.value.output.length - 1) * (this.trainPercentage / 100)
      );
      this.testEndIndex = Math.floor(
        (this.value.output.length - 1) * (this.testPercentage / 100)
      );

      if (this.value.output) {
        const newValue = {
          ...this.value,
          trainEndIndex: this.trainEndIndex,
          testEndIndex: this.trainEndIndex + this.testEndIndex,
          inputs: this.inputs,
          outputs: this.outputs
        };
        this.$emit("input", newValue);
      }
    }
  },
  watch: {
    parsedData: function(newValue) {
      if (newValue) {
        if (newValue.headers) {
          this.dataTypes = newValue.headers.map((val, index) => {
            return {
              name: val,
              type: newValue.headers.length - 1 === index ? "Output" : "Input"
            };
          });
          this.inputs = newValue.headers.length - 1;
          this.outputs = 1;
        }
      }
    }
  }
};
</script>

<style scoped></style>
