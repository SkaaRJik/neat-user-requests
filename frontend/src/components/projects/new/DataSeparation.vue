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
                value.data.length
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
  name: "DataSeparation",
  props: {
    value: Object,
    headers: Array
  },
  data: () => {
    return {
      trainPercentage: 70,
      testPercentage: 30,
      trainEndIndex: null,
      testEndIndex: null,
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

        item.type = newValue;
        if (this.value.data) {
          const newValue = {
            ...this.value,
            trainEndIndex: this.trainEndIndex,
            testEndIndex: this.trainEndIndex + this.testEndIndex,
            inputs: this.inputs,
            outputs: this.outputs,
            headers: this.headerTypes
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
      console.log(
        "[DataSeparation.vue].calculatePercentage this.value:",
        this.value
      );
      this.trainEndIndex = Math.round(
        (this.value.data.length - 1) * (this.trainPercentage / 100)
      );
      this.testEndIndex = Math.floor(
        (this.value.data.length - 1) * (this.testPercentage / 100)
      );

      if (this.value.data) {
        const newValue = {
          ...this.value,
          trainEndIndex: this.trainEndIndex,
          testEndIndex: this.trainEndIndex + this.testEndIndex,
          inputs: this.inputs,
          outputs: this.outputs,
          headers: this.headerTypes
        };
        this.$emit("input", newValue);
      }
    }
  },
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
      }
    }
  }
};
</script>

<style scoped></style>
