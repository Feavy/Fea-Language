package fr.feavy.fea;

import java.util.HashMap;
import java.util.Map;

public class TypeMatches {

	public final static Map<String, String> typeMatches = new HashMap<>();
	
	static {
		typeMatches.put("boolean", "true|false");
		typeMatches.put("int", "[0-9]+");
		typeMatches.put("float|double", "[0-9]+(.[0-9]+)?");
	}
	
}
