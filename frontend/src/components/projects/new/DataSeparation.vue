<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-col sm="12" xs="12" md="12" lg="4" xl="4">
          <v-text-field
            v-model="trainPercentage"
            @change="testPercentage = 100 - trainPercentage"
            min="1"
            max="99"
            :label="$t('Train_Percentage')"
            suffix="%"
            class="ma-3"
            type="number"
          />
        </v-col>
        <v-col sm="12" xs="12" md="12" lg="4" xl="4">
          <v-text-field
            v-model="testPercentage"
            :label="$t('Test_Percentage')"
            class="ma-3"
            type="number"
            suffix="%"
            min="1"
            max="99"
          />
        </v-col>
        <v-col sm="12" xs="12" md="12" lg="4" xl="4">
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
            `${$t("Train_Elements", { elements: trainEndIndex })},
            ${$t("Test_Elements", { elements: testEndIndex - trainEndIndex })},
            ${$t("Total", {
              elements: `${trainEndIndex + testEndIndex - trainEndIndex} / ${
                data.length
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
    data: Array
  },
  data: () => {
    return {
      trainPercentage: 70,
      testPercentage: 30,
      trainEndIndex: null,
      testEndIndex: null
    };
  },
  methods: {
    calculatePercentage() {

      this.trainPercentage = Number(this.trainPercentage);
      this.testPercentage = Number(this.testPercentage);

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
        this.data.length * (this.trainPercentage / 100)
      );
      this.testEndIndex = Math.floor(
        this.data.length * (this.testPercentage / 100)
      );

      const newValue = {
        trainEndIndex: this.trainEndIndex,
        testEndIndex: this.trainEndIndex + this.testEndIndex
      };
      this.$emit("input", newValue);
    }
  },
  watch: {
    value: function(newVal, oldVal) {
      if (newVal) {
        if (newVal !== oldVal) {
          this.trainEndIndex = newVal.trainEndIndex;
          this.testEndIndex = newVal.testEndIndex;
        }
      }
    }
  }
};
</script>

<style scoped></style>
