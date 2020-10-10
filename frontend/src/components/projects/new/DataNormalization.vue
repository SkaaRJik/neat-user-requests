<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
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

        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
          <v-text-field
            v-model="minRange"
            :label="$t('Min')"
            min="0"
            max="0.9"
            step="0.1"
            class="ma-3"
            type="number"
          />
        </v-col>
        <v-col sm="12" xs="12" md="4" lg="4" xl="4">
          <v-text-field
            v-model="maxRange"
            :label="$t('Max')"
            min="0.1"
            max="1.0"
            step="0.1"
            class="ma-3"
            type="number"
          />
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

    <v-col cols="12" v-if="chartData.length > 0">
      <reactive-chart :data="chartData" :layout="chartLayout"></reactive-chart>
    </v-col>
  </v-row>
</template>

<script>
import NormalizationMethodsContainer from "../../../services/NormalizationMethods";
import ReactiveChart from "../../charts/ReactiveChart";

export default {
  name: "DataNormalization",
  components: { ReactiveChart },
  props: {
    parsedData: Object,
    value: Object
  },
  data() {
    return {
      normalizationMethods: [],
      normalizationMethod: "",
      chartData: [],
      chartLayout: {
        xaxis: {
          title: this.$t("Range")
        },
        yaxis: {
          title: this.$t("Amount")
        }
      },

      minRange: 0.3,
      maxRange: 0.7
    };
  },
  methods: {
    normalize: async function() {
      if (!this.parsedData) return [];
      if (!this.parsedData.data) return [];
      if (!this.parsedData.data[0]) return [];

      this.minRange = Number(this.minRange);
      this.maxRange = Number(this.maxRange);

      try {
        const data = await NormalizationMethodsContainer[
          this.normalizationMethod
        ](this.parsedData, this.minRange, this.maxRange);
        console.log("[DataNormalization].normalize data:", data);
        const newValue = {
          ...this.value,
          normalizationServiceData: data.normalizationServiceData,
          columns: data.columns
        };

        this.$emit("input", newValue);
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

        this.chartData = [
          {
            x: chartLabels,
            y: data.statistic,
            type: "bar"
          }
        ];
      } catch (e) {
        console.error("[DataNormalizationVue].normalize error:", e);
      }
    }
  },
  computed: {},
  mounted() {
    this.normalizationMethods = Object.keys(NormalizationMethodsContainer);
    this.normalizationMethod = this.normalizationMethods[0];
  }
};
</script>

<style scoped></style>
