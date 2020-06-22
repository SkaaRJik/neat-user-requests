/*
 * Created on Sep 29, 2004
 *
 */
package ru.filippov.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Holds an object representation of the NEAT settings
 * @author MSimmerson
 *
 */
@Data
public class NEATConfig implements AIConfig {
	private Map settings;



	//Make copy of settings
	public NEATConfig(NEATConfig originalConfig){
		this.settings = new HashMap(originalConfig.settings);
	}


	public NEATConfig(Map config) {
		this.settings = new HashMap(config);

	}

	// default
	public NEATConfig() {
		this.settings = new HashMap(50);
	}

	public NEATConfig(String filePath) {
		this.settings = new HashMap();
	}

	public String configElement(String elementKey) {
		return ((String)this.settings.get(elementKey));
	}

	public void updateConfig(String elementKey, String elementValue) {
		this.settings.put(elementKey, elementValue);
	}

	@Override
	public boolean saveConfig(File dest) throws IOException {
		if(dest != null) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
			writer.write("");
			this.settings.entrySet().stream().forEach(o -> {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) o;
				try {
					writer.append(entry.getKey() +"="+entry.getValue()+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
			writer.flush();
			writer.close();
			return true;
		} else {
			return false;
		}
	}

	public List<String> getActivationFunctionsByElementKey(String elementKey){
		StringTokenizer stringTokenizer = new StringTokenizer(this.configElement(elementKey), ";");
		List<String> activationFunctions = new ArrayList<>(stringTokenizer.countTokens());
		while (stringTokenizer.hasMoreTokens()){
			activationFunctions.add(stringTokenizer.nextToken());
		}
		return activationFunctions;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class GUIAIConfig {
		private String name;
		private Object value;
		private Boolean allowedToChangeByUser;
		private Boolean showInGui;
		private Boolean isAdvanced;
		private Object maxValue;
		private Object minValue;

	}

	public static List<Map> getDefaultConfig(){

		List<Map> config = new ArrayList<>();

		Map<String, Object> section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_GENETIC_ALGORITHM");
		section.put("show", true);
		List<GUIAIConfig> params = new ArrayList<>(9);
		section.put("params", params);
		params.add(new GUIAIConfig(
				"GENERATOR.SEED",
				1548235723799L,
				true,
				true,
				false,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"PROBABILITY.MUTATION",
				0.25,
				true,
				true,
				false,
				1,
				0

		));
		params.add(new GUIAIConfig(
				"PROBABILITY.CROSSOVER",
				0.5,
				true,
				true,
				true,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.ADDLINK",
				0.1,
				true,
				true,
				false,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.ADDNODE",
				0.03,
				true,
				true,
				false,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.NEWACTIVATIONFUNCTION",
				0.1,
				true,
				true,
				false,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.MUTATEBIAS",
				0.3,
				true,
				true,
				true,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.TOGGLELINK",
				0.1,
				true,
				true,
				false,
				1,
				0
		));
		params.add(new GUIAIConfig(
				"PROBABILITY.WEIGHT.REPLACED",
				0.5,
				true,
				true,
				false,
				1,
				0
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_NICHE_SETTING");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
        "EXCESS.COEFFICIENT",
				1,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "DISJOINT.COEFFICIENT",
				1,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "WEIGHT.COEFFICIENT",
				0.4,
				true,
				true,
				true,
				null,
				null
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_SPECIES_CONTROL");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
        "COMPATABILITY.THRESHOLD",
				0.5,
				true,
				true,
				true,
				1,
				0
		));
		params.add(new GUIAIConfig(
        "COMPATABILITY.CHANGE",
				0.1,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.COUNT",3,
				true,
				true,
				true,
				5,
				1
		));
		params.add(new GUIAIConfig(
        "SURVIVAL.THRESHOLD",
				0.2,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.AGE.THRESHOLD",
				80,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.YOUTH.THRESHOLD",
				10,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.OLD.PENALTY",
				1.2,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.YOUTH.BOOST",
				0.7,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "SPECIE.FITNESS.MAX",
				15,
				true,
				true,
				true,
				null,
				null
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_NETWORK_SETTING");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);


		params.add(new GUIAIConfig(
        "MAX.PERTURB",
				0.5,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "MAX.BIAS.PERTURB",
				0.1,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "FEATURE.SELECTION",
				false,
				true,
				true,
				false,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "RECURRENCY.ALLOWED",
				false,
				true,
				true,
				false,
				null,
				null
		));



		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_ACTIVATION_FUNCTIONS");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
        "INPUT.ACTIVATIONFUNCTIONS",
				List.of("org.neat4j.neat.nn.core.functions.LinearFunction","",""),
				true,
				true,
				false,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "OUTPUT.ACTIVATIONFUNCTIONS",
				List.of("","org.neat4j.neat.nn.core.functions.SigmoidFunction",""),
				true,
				true,
				false,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "HIDDEN.ACTIVATIONFUNCTIONS",
				List.of("","","org.neat4j.neat.nn.core.functions.TanhFunction"),
				true,
				true,
				false,
				null,
				null
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_LIFE_CONTROL");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
        "ELE.EVENTS",
				false,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "ELE.SURVIVAL.COUNT",
				0.1,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "ELE.EVENT.TIME",
				1000,
				true,
				true,
				true,
				null,
				null
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "HEADER_EPOCH_CONTROL");
		section.put("show", true);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
        "KEEP.BEST.EVER",
				true,
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
        "EXTRA.FEATURE.COUNT",
				0,
				true,
				true,
				false,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "POP.SIZE",
				150,
				true,
				true,
				true,
				300,
				1
		));
		params.add(new GUIAIConfig(
        "NUMBER.EPOCHS",
				100,
				true,
				true,
				false,
				1000,
				1
		));
		params.add(new GUIAIConfig(
        "TERMINATION.VALUE.TOGGLE",
				false,
				true,
				true,
				true,
				null,
				null
		));
		params.add(new GUIAIConfig(
        "TERMINATION.VALUE",
				0.00001,
				true,
				true,
				true,
				null,
				null
		));


		section = new HashMap<>();
		config.add(section);
		section.put("header", "SERVICE");
		section.put("show", false);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
				"OPERATOR.XOVER",
				"org.neat4j.neat.core.xover.NEATCrossover",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"OPERATOR.FUNCTION",
				"org.neat4j.neat.core.fitness.MSENEATFitnessFunction",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"OPERATOR.PSELECTOR",
				"org.neat4j.neat.core.pselectors.TournamentSelector",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"OPERATOR.MUTATOR",
				"org.neat4j.neat.core.mutators.NEATMutator",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"NATURAL.ORDER.STRATEGY",
				"true",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"LEARNABLE",
				"org.neat4j.neat.nn.core.learning.GALearnable",
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"AI.TYPE",
				"GA",
				false,
				false,
				true,
				null,
				null
		));

		section = new HashMap<>();
		config.add(section);
		section.put("header", "NODE.COUNTERS");
		section.put("show", false);
		params = new ArrayList<>();
		section.put("params", params);

		params.add(new GUIAIConfig(
				"INPUT.NODES",
				0,
				false,
				false,
				true,
				null,
				null
		));

		params.add(new GUIAIConfig(
				"OUTPUT.NODES",
				0,
				false,
				false,
				true,
				null,
				null
		));



		return config;
	}
}
