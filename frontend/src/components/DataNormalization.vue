<template>
  <v-row>
    <v-col cols="12">
      <v-select
        :items="normalizationMethods"
        :label="$t('Normalization_Method')"
        v-model="normalizationMethod"
      >
        <template v-slot:selection="{ item }">
          <span>{{ $t(item) }}</span>
        </template>
        <template v-slot:item="{ item }">
          <span>{{ $t(item) }}</span>
        </template>
      </v-select>
      <v-text-field v-model="minRange" :label="$t('Min')" />
      <v-text-field v-model="maxRange" :label="$t('Max')" />
      <v-btn :disabled="!normalizationMethod" @click="normalize">{{
        $t("Normalize")
      }}</v-btn>
    </v-col>
    <v-col cols="12"> </v-col>
  </v-row>
</template>

<script>
  import NormalizationMethodsContainer from "../services/NormalizationMethods";

  export default {
  name: "DataNormalization",
  props: {
    parsedData: Object
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
      } catch (e) {
        console.error("[DataNormalizationVue].normalize error:", e);
      }
      /*const func = NormalizationMethodsContainer[this.normalizationMethod];
      console.log(
        "[DataNormalizationVue].normalize func, func(this.parsedData.data, this.minRange, this.maxRange):",
        func,
        func(this.parsedData.data, this.minRange, this.maxRange)
      );
      func(this.parsedData.data, this.minRange, this.maxRange);*/
      /*const normalizedData = NormalizationMethodsContainer[
        this.normalizationMethod
      ](this.parsedData.data, this.minRange, this.maxRange);
      console.log(
        "[DataNormalizationVue].normalize normalizedData:",
        normalizedData
      );*/
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
