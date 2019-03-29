package fr.feavy.fea;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarDeclInstruction extends Instruction{

	public final static Pattern arrayValuePattern = Pattern.compile("\\[(([^,\\]]+),?)+\\]");
	public final static Pattern arraySubValuePattern = Pattern.compile("\\[?(?<value>[^,\\]]+),?");
	
	public final static String invalidVarName = "[0-9]+.*";
	
	private Variable variable;
	
	private String name, value, type;
	private int length;
	private boolean isArray;
	private ArrayList<String> arrayValues;
	
	public VarDeclInstruction(String content) {
		super(content);
	
		this.valid = true;
		isArray = false;
		name = "";
		value = null;
		type = "";
		
		Matcher languageMatcher = InstructionParser.getLanguagePattern().matcher(content);
		
		if(languageMatcher.find()) {
			name = languageMatcher.group("varName");
			String length = languageMatcher.group("length");
			value = languageMatcher.group("value");
			type = languageMatcher.group("type");
			
			if(length != null) {
				isArray = true;
				if(length.length() > 0)
					this.length = Integer.parseInt(length);
				else
					this.length = -1;
			}
			
			if(value != null) {
				if(length == null) {
					if(TypeMatches.typeMatches.containsKey(type)) {
						valid = value.matches(TypeMatches.typeMatches.get(type));
					}
				}else {
					Matcher arrayValueMatcher = arrayValuePattern.matcher(value);
					if(arrayValueMatcher.matches()) {
						String value2;
						arrayValues = new ArrayList<>();
						Matcher arraySubValueMatcher = arraySubValuePattern.matcher(value);
						while(arraySubValueMatcher.find()) {
							value2 = arraySubValueMatcher.group("value");
							if(!value2.matches(TypeMatches.typeMatches.get(type))) {
								valid = false;
								break;
							} else {
								arrayValues.add(value2);
							}
						}
					} else {
						valid = false;
					}
				}
			}
			if(name.matches(invalidVarName))
				valid = false;
		}
		if(valid)
			variable = new Variable(name, type, value);
	}
	
	public Variable getVariable() {
		return variable;
	}
	
}
