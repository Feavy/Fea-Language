package fr.feavy.fea;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public final static Pattern blockBoundPattern = Pattern.compile("\\{|\\}");
	
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
			if(line.startsWith("//"))
				continue;
			for(int i = 0; i < strings.size(); i++)
				line = line.replace("<string_"+i+">", strings.get(i));		// Remplacer dans le code entier au lieu de ligne par ligne ?
			
			buffer.append(line);
		}
		reader.close();
		String str = buffer.toString();
		str = str.replaceAll("\\/\\*.\\*\\/", "");
		
		Block code = processBlock(str, 0);
		code.process(null);
		
		boolean valid = code.isValid();
		
		System.out.println("Code valide : "+valid);
		
		if(valid)
			code.transpile();
		else {
			List<Instruction> wrongInstructions = code.getWrongInstructions();
			for(Instruction i : wrongInstructions)
				System.out.println("Mauvaise instruction : "+i.getContent());
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
