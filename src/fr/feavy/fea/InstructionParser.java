package fr.feavy.fea;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionParser {

	private static final String INSTRUCTION = "(<VARIABLE_DEFINITION>|<VARIABLE_ASSIGNATION>);|(<BLOCK>)";

	private static final Map<String, String> WORDS = new HashMap<String, String>();
	
	private static final String LANGUAGE = "simple_stmt|compound_stmt";
	
	static {
		WORDS.put("simple_stmt", "(var_def|var_assignation|func_call);");
	}
	
	// value -> literal|var|func_call
	// var -> [a-z]+
	// func_call -> [a-z]+\\((value(,value)*)?\\)
	
	// Blocks

	static {
		WORDS.put("BLOCK", "(?<blockName>[^\\{;]*)\\{<block_[0-9]+>\\}");
	}

	//

	// Variables

	static {
		WORDS.put("VARIABLE_DEFINITION", "<VARIABLE_NAME>:<TYPE>(=(?<value>.+?))?");
		WORDS.put("TYPE", "(?<type>(byte|char|int|long|string|float|double|boolean))(\\[(?<length>[0-9]*)\\])?");
		WORDS.put("VARIABLE_NAME", "(?<varName>[a-z0-9_]+)");

		WORDS.put("VARIABLE_ASSIGNATION", "(?<varName2>[a-z0-9_]+)=(?<newValue>.+)");
	}

	//

	// TODO :
	// Ne pas faire les groupes de captures dans la regex du langage -> faire des
	// regex avec groupes pour chaque sous partie (declaration, assignation,
	// block...) qui seront appliquées au content directement dans le constructeur,
	// les attributs de l'objet seront modifiés en fonction des groupes trouvé.
	
	// La regex du langage n'est là qu'en tant qu'analyse lexicale (mots corrects),
	// l'analyse grammaticale (phrases correctes) doit se faire dans le constructeur
	// des objets Instruction.

	private static Pattern subWordPattern = Pattern.compile("[^?]?<(?<word>[A-Z_]+)>");

	private static Pattern LANGUAGE_PATTERN;

	static {
		LANGUAGE_PATTERN = Pattern.compile(processWord(INSTRUCTION));
	}

	private static String processWord(String word) {
		Matcher matcher = subWordPattern.matcher(word);
		while (matcher.find()) {
			String subWord = matcher.group("word");
			if (WORDS.containsKey(subWord)) {
				word = word.replace("<" + subWord + ">", processWord(WORDS.get(subWord)));
			} else {
				System.err.println("Error : word " + subWord + " doesn't exist.");
			}
		}
		return word;
	}

	public static Pattern getLanguagePattern() {
		return LANGUAGE_PATTERN;
	}

}
