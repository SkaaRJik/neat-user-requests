<template>
  <v-row>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-alert
          border="bottom"
          class="mb-0"
          dense
          elevation="2"
          style="width: 100%"
          transition="scale-transition"
          type="info"
        >
          {{ $t("Legend_Column_As_Date_Info") }}
        </v-alert>
      </v-row>
    </v-col>
    <v-col cols="12">
      <v-row align="center" justify="center">
        <v-btn class="ma-3" color="primary">
          <v-icon left>mdi-arrow-down-bold</v-icon>
          {{ $t("Template") }}
        </v-btn>
        <v-btn class="ma-3">
          <v-icon left>mdi-arrow-down-bold</v-icon>
          {{ $t("Example", { number: 1 }) }}
        </v-btn>
        <v-btn class="ma-3">
          <v-icon left>mdi-arrow-down-bold</v-icon>
          {{ $t("Example", { number: 2 }) }}
        </v-btn>
      </v-row>
    </v-col>
    <v-col cols="12">
      <v-file-input
        :label="$t('Upload_File')"
        :placeholder="$t('Select_File')"
        :show-size="1000"
        accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        class="py-2"
        counter
        outlined
        prepend-icon="mdi-paperclip"
        :value="value"
        @change="$emit('input', $event)"
      >
        <template v-slot:selection="{ index, text }">
          <v-chip color="deep-purple accent-4" label small>
            {{ text }}
          </v-chip>
        </template>
      </v-file-input>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "UploadProjectData",
  props: ["value"]
};
</script>

<style scoped></style>
