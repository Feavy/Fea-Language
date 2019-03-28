package fr.feavy.fea;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionParser {

	private static final String INSTRUCTION = "(<VARIABLE_DEFINITION>);";
	
	private static final Map<String, String> WORDS = new HashMap<String, String>();
	
	// Variables
	
	static {
		WORDS.put("VARIABLE_DEFINITION", "<VARIABLE_NAME>:<TYPE>(=(?<value>.+?))?");
		WORDS.put("TYPE", "(?<type>(int|string|int|float|double|boolean))(\\[(?<length>[0-9]*)\\])?");
		WORDS.put("VARIABLE_NAME", "(?<varName>[a-z0-9_]+)");
	}
	
	//
	
	private static Pattern subWordPattern = Pattern.compile("[^?]?<(?<word>[A-Z_]+)>");
	
	private static Pattern LANGUAGE_PATTERN;
	
	static {
		LANGUAGE_PATTERN = Pattern.compile(processWord(INSTRUCTION));
	}
	
	private static String processWord(String word) {
		Matcher matcher = subWordPattern.matcher(word);
		while(matcher.find()) {
			String subWord = matcher.group("word");
			if(WORDS.containsKey(subWord)) {
				word = word.replace("<"+subWord+">", processWord(WORDS.get(subWord)));
			}else {
				System.err.println("Error : word "+subWord+" doesn't exist.");
			}
		}
		return word;
	}
	
	public static Pattern getLanguagePattern() {
		return LANGUAGE_PATTERN;
	}
	
}
