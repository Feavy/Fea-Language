package fr.feavy.fea;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public final static Pattern blockBoundPattern = Pattern.compile("\\{|\\}");
	
	public final static Pattern arrayValuePattern = Pattern.compile("\\[(([^,\\]]+),?)+\\]");
	public final static Pattern arraySubValuePattern = Pattern.compile("\\[?(?<value>[^,\\]]+),?");
	
	public final static String invalidVarName = "[0-9]+.*";
	
	public static void main(String[] args) throws IOException {
		
		StringBuffer buffer = new StringBuffer();
		PrintWriter writer = new PrintWriter(new File(Main.class.getResource("/output.c").getPath()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/main.fea")));
		String line = null;
		
		Pattern stringPattern = Pattern.compile(TypeMatches.typeMatches.get("string"));
		
		ArrayList<String> strings = new ArrayList<String>();
		while((line = reader.readLine()) != null) {
			strings.clear();
			Matcher stringMatcher = stringPattern.matcher(line);
			while(stringMatcher.find()) {
				strings.add(line.substring(stringMatcher.start(), stringMatcher.end()));
			}
			for(int i = 0; i < strings.size(); i++)
				line = line.replace(strings.get(i), "<string_"+i+">");
			
			line = line.trim().replaceAll("\\s", "");
			
			for(int i = 0; i < strings.size(); i++)
				line = line.replace("<string_"+i+">", strings.get(i));		// Remplacer dans le code entier au lieu de ligne par ligne ?
			
			buffer.append(line);
		}
		reader.close();
		String str = buffer.toString();
		
		Block code = processBlock(str, 0);
		
		code.process();
		
		System.out.println(code.getContent());
		
		Matcher languageMatcher = InstructionParser.getLanguagePattern().matcher(str);
		
		String name, value, type, length, value2;
		boolean valide = true;
		
		while(languageMatcher.find()) {
			valide = true;
			name = languageMatcher.group("varName");
			type = languageMatcher.group("type").toLowerCase();
			value = languageMatcher.group("value");
			length = languageMatcher.group("length");
			
			System.out.println("var name : "+name);
			System.out.print("type : "+type);
			
			if(length != null) {
				System.out.println(" array");
				if(length.length() > 0) {
					System.out.println("length : "+length);
				}else {
					System.out.println("length : variable");
				}
			}else {
				System.out.println();
			}
			if(value != null) {
				System.out.println("value : "+value);
				if(length == null) {
					if(TypeMatches.typeMatches.containsKey(type)) {
						valide = value.matches(TypeMatches.typeMatches.get(type));
					}
				}else {
					Matcher arrayValueMatcher = arrayValuePattern.matcher(value);
					if(arrayValueMatcher.matches()) {
						Matcher arraySubValueMatcher = arraySubValuePattern.matcher(value);
						while(arraySubValueMatcher.find()) {
							value2 = arraySubValueMatcher.group("value");
							if(!value2.matches(TypeMatches.typeMatches.get(type))) {
								valide = false;
								break;
							}
						}
					} else {
						valide = false;
					}
				}
			}
			if(name.matches(invalidVarName))
				valide = false;
			System.out.println("    Valide : "+valide);
			System.out.println();
		}
		
	}
	
	private static String[] extractBlocks(String str) {
		Matcher blockBoundMatcher = blockBoundPattern.matcher(str);
		
		int start = 0;
		int end = 0;
		
		int count = 0;
		
		ArrayList<String> blocks = new ArrayList<>();
		
		while(blockBoundMatcher.find()) {
			if(count == 0)
				start = blockBoundMatcher.start();
			
			if(str.substring(blockBoundMatcher.start(), blockBoundMatcher.end()).equals("{")) 
				count++;
			else
				count--;
			if(count == 0) {
				end = blockBoundMatcher.end();
				blocks.add(str.substring(start+1, end-1));
			}
		}

		return blocks.toArray(new String[blocks.size()]);
	}
	
	private static int id = 0;
	
	private static Block processBlock(String block, int depth) {
		Block b = new Block(id++, block);
		
		String[] subBlocks = extractBlocks(block);
		for(String subBlockStr : subBlocks) {
			Block subBlock = processBlock(subBlockStr, depth+1);
			b.addSubBlock(subBlock);
		}
		return b;
	}

}
