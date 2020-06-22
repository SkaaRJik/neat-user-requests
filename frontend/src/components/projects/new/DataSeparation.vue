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


  </v-row>
</template>

<script>
export default {
  name: "DataSeparation",
  props: {
    value: Object,
  },
  data: () => {
    return {
      trainPercentage: 70,
      testPercentage: 30,
      trainEndIndex: null,
      testEndIndex: null,
    };
  },
  methods: {
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
        };
        this.$emit("input", newValue);
      }
    }
  },
};
</script>

<style scoped></style>
