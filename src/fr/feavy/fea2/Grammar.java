package fr.feavy.fea2;

import java.util.LinkedHashMap;
import java.util.Map;

public class Grammar {

	private Map<String, String> words;
	
	public Grammar() {
		this(new LinkedHashMap<String, String>());
	}
	
	public Grammar(LinkedHashMap<String, String> words) {
		this.words = words;
	}
	
	public void putWord(String word, String regex) {
		words.put(word, regex);
	}
	
}
