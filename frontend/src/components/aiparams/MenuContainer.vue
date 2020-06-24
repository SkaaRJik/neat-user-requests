<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-list class="pa-1">
    <v-list-group :key="i" v-for="(item, i) in items">
      <template v-slot:activator>
        <v-list-tile>
          <v-list-tile-title>{{ item.header }}</v-list-tile-title>
        </v-list-tile>
      </template>
      <template v-if="item.header === 'Настройки активационных функций:'">
        <v-list-group
          :key="j"
          no-action
          sub-group
          v-for="(layer, j) in item.layers"
        >
          <template v-slot:activator>
            <v-list-tile>
              <v-list-tile-title>{{ layer.head }}</v-list-tile-title>
            </v-list-tile>
          </template>

          <v-list-tile v-for="(field, k) in layer.fields" :key="k" >
            <v-switch
              :label="field.placeholderText"
              :true-value="field.selected"
              color="red"
              false-value=""
              v-model="field.model"
            ></v-switch>
          </v-list-tile>
        </v-list-group>
      </template>
      <template v-else>
        <v-list-tile :key="j" class="my-4" v-for="(field, j) in item.fields">
          <template>
            <template v-if="field.type === 'text'">
              <v-layout
                align-space-between
                class="my-4"
                fill-heigh
                justify-start
                row
                t
                v-if="field.placeholderText === 'Семя генератора случ. чисел'"
              >
                <v-text-field
                  :disabled="field.disabled"
                  :label="field.placeholderText"
                  clearable
                  outline
                  v-model="field.model"
                  v-show="!field.show"
                />

                <v-btn
                  @click="generateNewSeed"
                  flat
                  icon
                  v-if="field.placeholderText === 'Семя генератора случ. чисел'"
                >
                  <v-icon>
                    refresh
                  </v-icon>
                </v-btn>
              </v-layout>
              <template v-else>
                <v-text-field
                  :disabled="field.disabled"
                  :label="field.placeholderText"
                  clearable
                  outline
                  v-model="field.model"
                  v-show="!field.show"
                />
              </template>
            </template>
            <template v-else-if="field.type === 'toggle'">
              <v-switch
                :false-value="false"
                :label="field.placeholderText"
                :true-value="true"
                color="red"
                hide-details
                v-model="field.model"
                v-show="!field.show"
              ></v-switch>
            </template>
          </template>
        </v-list-tile>
      </template>
    </v-list-group>
  </v-list>
</template>

<script>
export default {
  props: ["config"],
  data() {
    return {
      defaultConfig: null,

      items: [
        {
          header: "Параметры генетического алгоритма",
          fields: [
            {
              placeholderText: "Семя генератора случ. чисел",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность мутации",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность мутации актив. функции",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность новой связи",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность нового нейрона",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность мутации байеса",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность включ/исключ. связи",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Вероятность изменения веса",
              type: "text",
              model: ""
            }
          ]
        },

        {
          header: "Контроль эпох",
          fields: [
            {
              placeholderText: "Размер популяции",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Количество эпох",
              type: "text",
              model: ""
            },

            {
              placeholderText: "Дополнительные аллели",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Всегда сохранять лучшего",
              type: "toggle",
              model: Boolean
            },
            {
              placeholderText: "Включить прерывание",
              type: "toggle",
              model: Boolean
            },
            {
              placeholderText: "Значение остановки",
              type: "text",
              show: false,
              model: ""
            }
          ]
        },
        {
          header: "Настройки активационных функций:",
          layers: [
            {
              head: "Входной слой",
              fields: [
                {
                  placeholderText: "y=x",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.LinearFunction",
                  disabled: ""
                },
                {
                  placeholderText: "sin(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.SigmoidFunction",
                  disabled: ""
                },
                {
                  placeholderText: "tanh(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.TanhFunction",
                  disabled: ""
                }
              ]
            },
            {
              head: "Скрытые слои",
              fields: [
                {
                  placeholderText: "y=x",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.LinearFunction",
                  disabled: ""
                },
                {
                  placeholderText: "sin(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.SigmoidFunction",
                  disabled: ""
                },
                {
                  placeholderText: "tanh(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.TanhFunction",
                  disabled: ""
                }
              ]
            },
            {
              head: "Выходной слой",
              fields: [
                {
                  placeholderText: "y=x",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.LinearFunction",
                  disabled: ""
                },
                {
                  placeholderText: "sin(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.SigmoidFunction",
                  disabled: ""
                },
                {
                  placeholderText: "tanh(x)",
                  type: "toggle",
                  model: "",
                  selected: "org.neat4j.neat.nn.core.functions.TanhFunction",
                  disabled: ""
                }
              ]
            }
          ]
        },
        {
          header: "Настройки нейросети",
          fields: [
            {
              placeholderText: "Входов сети",
              type: "text",
              disabled: true,
              model: ""
            },
            {
              placeholderText: "Выходов сети",
              type: "text",
              disabled: true,
              model: ""
            },
            {
              placeholderText: "Макс. смещение весов",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Макс. смещение байеса",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Отбор фич",
              type: "toggle",
              model: ""
            },
            {
              placeholderText: "Разрешить рекурентность",
              type: "toggle",
              model: ""
            }
          ]
        },
        {
          header: "Настройки нишинга",
          fields: [
            {
              placeholderText: "Коэф-нт дополнительных генов (c1)",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Коэф-нт непересекающихся генов (c2)",
              type: "text",
              model: ""
            },
            {
              placeholderText:
                "Коэф-нт средней разницы весов совпадающих генов",
              type: "text",
              model: ""
            }
          ]
        },
        {
          header: "Контроль видообразования",
          fields: [
            {
              placeholderText:
                "Пороговое значение соответствия особей внутри вида",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Значение совместимости мутаций",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Макс. количество видов",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Пороговое значение выживания",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Макс. время жизни вида",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Порог, до которого особь считается молодой",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Штраф для старой особи",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Ускорение для молодой особи",
              type: "text",
              model: ""
            },
            {
              placeholderText: "Максимальное значение приспособленности",
              type: "text",
              model: ""
            }
          ]
        },
        {
          header: "Контроль",
          fields: [
            {
              placeholderText: "Включить контроль вымирания",
              type: "toggle",
              model: ""
            },
            {
              placeholderText: "Оставить особей в живых (штук)",
              type: "text",
              model: "",
              show: false
            },
            {
              placeholderText: "Время жизни",
              type: "text",
              model: "",
              show: false
            }
          ]
        }
      ]
    };
  },
  methods: {
    getDefaultConfig() {
      this.$resource("api/trainer/default-config")
        .get()
        .then(res => {
          if (res.ok) {
            res.json().then(data => {
              this.defaultConfig = data;
              console.log(this.defaultConfig);
              this.items[0].fields[0].model = this.defaultConfig.settings.GENERATOR_SEED;
              this.items[0].fields[1].model = this.defaultConfig.settings.PROBABILITY_MUTATION;
              this.items[0].fields[2].model = this.defaultConfig.settings.PROBABILITY.NEWACTIVATIONFUNCTION;
              this.items[0].fields[3].model = this.defaultConfig.settings.PROBABILITY_ADDLINK;
              this.items[0].fields[4].model = this.defaultConfig.settings.PROBABILITY.ADDNODE;
              this.items[0].fields[5].model = this.defaultConfig.settings.PROBABILITY.MUTATEBIAS;
              this.items[0].fields[6].model = this.defaultConfig.settings.PROBABILITY.TOGGLELINK;
              this.items[0].fields[7].model = this.defaultConfig.settings.PROBABILITY.WEIGHT.REPLACED;

              this.items[1].fields[0].model = this.defaultConfig.settings.POP.SIZE;
              this.items[1].fields[1].model = this.defaultConfig.settings.NUMBER.EPOCHS;
              this.items[1].fields[2].model = this.defaultConfig.settings.EXTRA.FEATURE.COUNT;
              this.items[1].fields[3].model = this.defaultConfig.settings.KEEP.BEST.EVER;
              this.items[1].fields[4].model = this.defaultConfig.settings.TERMINATION.VALUE.TOGGLE;
              this.items[1].fields[5].model = this.defaultConfig.settings.TERMINATION.VALUE;

              let newArr = [];
              for (var i = 0; i < this.items[2].layers.length; i++) {
                if (i === 0) {
                  newArr = this.defaultConfig.settings.INPUT.ACTIVATIONFUNCTIONS.split(
                    ";"
                  );
                }
                if (i === 1) {
                  newArr = this.defaultConfig.settings.HIDDEN.ACTIVATIONFUNCTIONS.split(
                    ";"
                  );
                }
                if (i === 2) {
                  newArr = this.defaultConfig.settings.OUTPUT.ACTIVATIONFUNCTIONS.split(
                    ";"
                  );
                }
                for (var j = 0; j < newArr.length; j++) {
                  this.items[2].layers[i].fields[j].model = newArr[j];
                }
              }

              this.items[3].fields[0].model = this.defaultConfig.settings.INPUT_NODES;
              this.items[3].fields[1].model = this.defaultConfig.settings.OUTPUT_NODES;
              this.items[3].fields[2].model = this.defaultConfig.settings.MAX.PERTURB;
              this.items[3].fields[3].model = this.defaultConfig.settings.MAX.BIAS.PERTURB;
              this.items[3].fields[4].model = this.defaultConfig.settings.FEATURE.SELECTION;
              this.items[3].fields[5].model = this.defaultConfig.settings.RECURRENCY.ALLOWED;

              this.items[4].fields[0].model = this.defaultConfig.settings.EXCESS.COEFFICIENT;
              this.items[4].fields[1].model = this.defaultConfig.settings.DISJOINT.COEFFICIENT;
              this.items[4].fields[2].model = this.defaultConfig.settings.WEIGHT.COEFFICIENT;

              this.items[5].fields[0].model = this.defaultConfig.settings.COMPATABILITY.THRESHOLD;
              this.items[5].fields[1].model = this.defaultConfig.settings.COMPATABILITY.CHANGE;
              this.items[5].fields[2].model = this.defaultConfig.settings.SPECIE.COUNT;
              this.items[5].fields[3].model = this.defaultConfig.settings.SURVIVAL.THRESHOLD;
              this.items[5].fields[4].model = this.defaultConfig.settings.SPECIE.AGE.THRESHOLD;
              this.items[5].fields[5].model = this.defaultConfig.settings.SPECIE.YOUTH.THRESHOLD;
              this.items[5].fields[6].model = this.defaultConfig.settings.SPECIE.OLD.PENALTY;
              this.items[5].fields[7].model = this.defaultConfig.settings.SPECIE.YOUTH.BOOST;
              this.items[5].fields[8].model = this.defaultConfig.settings.SPECIE.FITNESS.MAX;

              this.items[6].fields[0].model = this.defaultConfig.settings.ELE.EVENTS;
              this.items[6].fields[1].model = this.defaultConfig.settings.ELE.SURVIVAL.COUNT;
              this.items[6].fields[2].model = this.defaultConfig.settings.ELE.EVENT.TIME;
            });
          }
        });
    },
    send() {
      this.defaultConfig.settings.GENERATOR_SEED = this.items[0].fields[0].model;
      this.defaultConfig.settings.PROBABILITY_MUTATION = this.items[0].fields[1].model;
      this.defaultConfig.settings.PROBABILITY.NEWACTIVATIONFUNCTION = this.items[0].fields[2].model;
      this.defaultConfig.settings.PROBABILITY_ADDLINK = this.items[0].fields[3].model;
      this.defaultConfig.settings.PROBABILITY.ADDNODE = this.items[0].fields[4].model;
      this.defaultConfig.settings.PROBABILITY.MUTATEBIAS = this.items[0].fields[5].model;
      this.defaultConfig.settings.PROBABILITY.TOGGLELINK = this.items[0].fields[6].model;
      this.defaultConfig.settings.PROBABILITY.WEIGHT.REPLACED = this.items[0].fields[7].model;

      this.defaultConfig.settings.POP.SIZE = this.items[1].fields[0].model;
      this.defaultConfig.settings.NUMBER.EPOCHS = this.items[1].fields[1].model;
      this.defaultConfig.settings.EXTRA.FEATURE.COUNT = this.items[1].fields[2].model;
      this.defaultConfig.settings.KEEP.BEST.EVER = this.items[1].fields[3].model;
      this.defaultConfig.settings.TERMINATION.VALUE.TOGGLE = this.items[1].fields[4].model;
      this.defaultConfig.settings.TERMINATION.VALUE = this.items[1].fields[5].model;

      this.defaultConfig.settings.INPUT.ACTIVATIONFUNCTIONS =
        this.items[2].layers[0].fields[0].model +
        ";" +
        this.items[2].layers[0].fields[1].model +
        ";" +
        this.items[2].layers[0].fields[2].model;
      this.defaultConfig.settings.OUTPUT.ACTIVATIONFUNCTIONS =
        this.items[2].layers[2].fields[0].model +
        ";" +
        this.items[2].layers[2].fields[1].model +
        ";" +
        this.items[2].layers[2].fields[2].model;
      this.defaultConfig.settings.HIDDEN.ACTIVATIONFUNCTIONS =
        this.items[2].layers[1].fields[0].model +
        ";" +
        this.items[2].layers[1].fields[1].model +
        ";" +
        this.items[2].layers[1].fields[2].model;

      this.defaultConfig.settings.INPUT_NODES = this.items[3].fields[0].model;
      this.defaultConfig.settings.OUTPUT_NODES = this.items[3].fields[1].model;
      this.defaultConfig.settings.MAX.PERTURB = this.items[3].fields[2].model;
      this.defaultConfig.settings.MAX.BIAS.PERTURB = this.items[3].fields[3].model;
      this.defaultConfig.settings.FEATURE.SELECTION = this.items[3].fields[4].model;
      this.defaultConfig.settings.RECURRENCY.ALLOWED = this.items[3].fields[5].model;

      this.defaultConfig.settings.EXCESS.COEFFICIENT = this.items[4].fields[0].model;
      this.defaultConfig.settings.DISJOINT.COEFFICIENT = this.items[4].fields[1].model;
      this.defaultConfig.settings.WEIGHT.COEFFICIENT = this.items[4].fields[2].model;

      this.defaultConfig.settings.COMPATABILITY.THRESHOLD = this.items[5].fields[0].model;
      this.defaultConfig.settings.COMPATABILITY.CHANGE = this.items[5].fields[1].model;
      this.defaultConfig.settings.SPECIE.COUNT = this.items[5].fields[2].model;
      this.defaultConfig.settings.SURVIVAL.THRESHOLD = this.items[5].fields[3].model;
      this.defaultConfig.settings.SPECIE.AGE.THRESHOLD = this.items[5].fields[4].model;
      this.defaultConfig.settings.SPECIE.YOUTH.THRESHOLD = this.items[5].fields[5].model;
      this.defaultConfig.settings.SPECIE.OLD.PENALTY = this.items[5].fields[6].model;
      this.defaultConfig.settings.SPECIE.YOUTH.BOOST = this.items[5].fields[7].model;
      this.defaultConfig.settings.SPECIE.FITNESS.MAX = this.items[5].fields[8].model;

      this.defaultConfig.settings.ELE.EVENTS = this.items[6].fields[0].model;
      this.defaultConfig.settings.ELE.SURVIVAL.COUNT = this.items[6].fields[1].model;
      this.defaultConfig.settings.ELE.EVENT.TIME = this.items[6].fields[2].model;

      console.log(this.defaultConfig);

      this.$resource("/trainer/save")
        .save({}, this.defaultConfig)
        .then(res => {
          if (res.ok) {
            console.log("save ok!");
          }
        });
    },
    generateNewSeed() {
      this.items[0].fields[0].model = new Date().getTime();
    }
  },
  watch: {
    items: {
      handler() {
        this.items[1].fields[5].show = !this.items[1].fields[4].model;
        this.items[6].fields[1].show = !this.items[6].fields[0].model;
        this.items[6].fields[2].show = !this.items[6].fields[0].model;
      },
      deep: true
    }
  },
  created() {
    this.getDefaultConfig();
  }
};
</script>

<style></style>
