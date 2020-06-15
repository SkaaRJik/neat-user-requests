<template>
  <v-row>
    <v-col cols="12">
      <v-select
        :items="parsedHeaders"
        :label="$t('Headers')"
        chips
        multiple
        readonly
        v-model="parsedHeaders"
      >
        <div class="span-in-row__container" slot="append">
          <span class="span-in-row">
            {{ $t("Items", { size: parsedHeaders.length }) }}
          </span>
        </div>
      </v-select>
    </v-col>
    <v-col cols="12">
      <v-select
        :items="parsedLegend"
        :label="$t('Legend')"
        chips
        multiple
        readonly
        v-model="parsedLegend"
      >
        <div class="span-in-row__container" slot="append">
          <span class="span-in-row">
            {{ $t("Items", { size: parsedLegend.length }) }}
          </span>

          <span class="span-in-row">
            {{ $t("Increment", { increment: calculateIncrement() }) }}
          </span>
        </div>
      </v-select>
    </v-col>

    <v-col cols="12" v-if="shouldRenderDataErrors">
      <v-expansion-panels :value="0">
        <v-expansion-panel>
          <v-expansion-panel-header disable-icon-rotate>
            {{ $t("Errors") }}
            <template v-slot:actions>
              <v-icon color="error">mdi-alert-circle</v-icon>
            </template>
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            <template v-for="(item, index) in parsedErrors">
              <v-list-item :id="index" :key="index">
                {{ $t(item.error, item) }}
              </v-list-item>
            </template>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-col>
  </v-row>
</template>

<script>
    import moment from "moment";

    export default {
  name: "ProjectDetails",
  props: {
    parsedData: Object
  },
  methods: {
    calculateIncrement() {
      if (this.parsedData.isDate) {
        const diff = moment.duration(this.parsedData.increment, "milliseconds");
        if (diff.asYears()) return `${diff.asYears()} ${this.$t("Years")}`;
        if (diff.asMonths()) return `${diff.asMonths} ${this.$t("Months")}`;
        if (diff.asDays()) return `${diff.asDays} ${this.$t("Days")}`;
        if (diff.asHours()) return `${diff.asHours} ${this.$t("Hours")}`;
        if (diff.asMinutes()) return `${diff.asMinutes} ${this.$t("Minutes")}`;
        if (diff.asSeconds())
          return `${diff.asSeconds()} ${this.$t("Seconds")}`;
      }
      return this.parsedData.increment.toFixed(3);
    }
  },
  computed: {
    parsedErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors;
      }
      return [];
    },
    parsedHeaders() {
      if (this.parsedData.headers) {
        return [this.parsedData.legendHeader, ...this.parsedData.headers];
      }
      return [];
    },
    parsedLegend() {
      if (this.parsedData.legend) {
        if (this.parsedData.isDate) {
          return this.parsedData.legend.map(value =>
            moment(value).format("DD.MM.YYYY HH:mm")
          );
        } else {
          return this.parsedData.legend;
        }
      }
      return [];
    },
    shouldRenderDataErrors() {
      if (this.parsedData.dataErrors) {
        return this.parsedData.dataErrors.length > 0;
      }
      return false;
    }
  }
};
</script>

<style scoped>
.span-in-row__container {
  font-size: 12px;
}

.span-in-row {
  display: flow-root;
}
</style>
