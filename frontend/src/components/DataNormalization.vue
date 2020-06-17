<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-col>
          <v-select
            :items="normalizationMethods"
            :label="$t('Normalization_Method')"
            v-model="normalizationMethod"
            class="ma-3"
          >
            <template v-slot:selection="{ item }">
              <span>{{ $t(item) }}</span>
            </template>
            <template v-slot:item="{ item }">
              <span>{{ $t(item) }}</span>
            </template>
          </v-select>
        </v-col>

        <v-col>
          <v-text-field v-model="minRange" :label="$t('Min')" class="ma-3" />
        </v-col>
        <v-col>
          <v-text-field v-model="maxRange" :label="$t('Max')" class="ma-3" />
        </v-col>
      </v-row>
    </v-col>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-btn
          :disabled="!normalizationMethod"
          @click="normalize"
          class="ma-3"
          >{{ $t("Normalize") }}</v-btn
        ></v-row
      >
    </v-col>

    <v-col cols="12">
      <div id="normalizationChart"></div>
    </v-col>
  </v-row>
</template>

<script>
  import NormalizationMethodsContainer from "../services/NormalizationMethods";
  import Plotly from "plotly.js-dist";

  export default {
  name: "DataNormalization",
  props: {
    parsedData: Object,
    value: Object
  },
  data() {
    return {
      normalizationMethods: [],
      normalizationMethod: "",
      minRange: 0.3,
      maxRange: 0.7
    };
  },
  methods: {
    normalize: async function() {
      if (!this.parsedData) return [];
      if (!this.parsedData.data) return [];
      if (!this.parsedData.data[0]) return [];
      try {
        const data = await NormalizationMethodsContainer[
          this.normalizationMethod
        ](this.parsedData.data, this.minRange, this.maxRange);
        console.log("[DataNormalizationVue].normalize data:", data);
        this.$emit("input", data);
        const chartLabels = [];

        const chartXInterval = (this.maxRange - this.minRange) / 10;

        for (
          let i = this.minRange;
          i < this.maxRange - chartXInterval;
          i += chartXInterval
        ) {
          chartLabels.push(
            `[${i.toFixed(3)}, ${(i + chartXInterval).toFixed(3)})`
          );
        }
        chartLabels.push(
          `[${(this.maxRange - chartXInterval).toFixed(3)}, ${this.maxRange}]`
        );
        chartLabels.push(`${this.minRange} < || > ${this.maxRange}`);

        const chartOptions = [
          {
            x: chartLabels,
            y: data.statistic,
            type: "bar"
          }
        ];
        const layout = {
          plot_bgcolor: "#303030",
          paper_bgcolor: "#303030",
          font: {
            color: "#FFF"
          }
        };
        Plotly.newPlot("normalizationChart", chartOptions, layout);
      } catch (e) {
        console.error("[DataNormalizationVue].normalize error:", e);
      }
    }
  },
  computed: {
    separatedData() {
      if (!this.parsedData) return [];
      if (!this.parsedData.data) return [];
      if (!this.parsedData.data[0]) return [];

      const data = this.parsedData.data[0].map((col, i) =>
        this.parsedData.data.map(row => row[i])
      );
      return data;
    }
  },
  mounted() {
    this.normalizationMethods = Object.keys(NormalizationMethodsContainer);
    this.normalizationMethod = this.normalizationMethods[0];
  }
};
</script>

<style scoped></style>
