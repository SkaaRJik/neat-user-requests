<template>
  <div :id="uuid"></div>
</template>

<script>
import Plotly from "plotly.js-dist";
import { uuidv4 } from "../../utils/generate.utils";

export default {
  name: "ReactiveChart",
  props: { data: Array, layout: Object },
  data: function() {
    return {
      uuid: uuidv4(),
      baseLayout: {
        plot_bgcolor: "#303030",
        paper_bgcolor: "#303030",
        font: {
          color: "#FFF"
        }
      }
    };
  },
  mounted() {
    Plotly.newPlot(this.uuid, this.data ? this.data : [], {
      ...this.baseLayout,
      ...this.layout
    });
  },
  watch: {
    data: function(newValue) {
      if (newValue) {
        Plotly.newPlot(this.uuid, newValue, {
          ...this.baseLayout,
          ...this.layout
        });
      }
    },
    layout: function(newValue) {
      Plotly.newPlot(this.uuid, newValue, {
        ...this.baseLayout,
        ...this.layout
      });
    }
  }
};
</script>

<style scoped></style>
