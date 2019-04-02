package fr.feavy.fea2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;

public class Main {
		
	public static Map<String, String> blocks = new LinkedHashMap<String, String>();
	
	public final static String KEY_WORDS = "if|else|while|string";
	
	static {
		blocks.put("<stmt>", "<compound_stmt>|<simple_stmt>");
		
		blocks.put("<compound_stmt>", "<if_stmt>|<while_stmt>");
		blocks.put("<if_stmt>", "if\\{(<stmt>)*\\}(else\\{(<stmt>)*\\})?");
		blocks.put("<while_stmt>", "while\\{((<stmt>)*)\\}");
		
		blocks.put("<simple_stmt>", "(<var_assignment>|<var_declaration>|line);");
		
		blocks.put("<type>", "string|boolean");
		blocks.put("<var_assignment_left>", "((<var_declaration>|NAME)=)");
		blocks.put("<var_assignment>", "(<var_assignment_left><expr>)");
		blocks.put("<var_declaration>", "(<expr>:<type>)");
		blocks.put("<expr>", "(NAME|(<expr>(\\+<expr>)+))");
	}
	
	public static Set<String> WORDS = blocks.keySet();
	
	public static String block = "if{while{a:string=bonjour+test;a=autre;}}else{}while{}";
		
	public static String method = "NAME(NAME, NAME(NAME, NAME))";
	
	public static void main(String[] args) {
		
		//blocks.replace("<var_declaration>", blocks.get("<var_declaration>").replaceAll("NAME", NAME));
		//blocks.replace("<expr>", blocks.get("<expr>").replaceAll("NAME", NAME));
		
		
		// 1 - Remplacer les strings par STRING, nombres par NUMBER...
		// 2 - remplacer noms par NAME
		// 3 - Exécuter la grammaire une fois
		// 4 - Exécuter la grammaire en boucle tant que des <expr> sont transformés
		// 5 - Exécuter la grammaire en boucle tant qu'il y a des transformations.
		
		Matcher currentMatcher;
			
		block = transformConstants(block);
		block = parseCode(block).getValue();
		block = parseExpressions(block);
		
		boolean replaced = false;
		Pair<Boolean, String> parseReply;
		
		do {
			parseReply = parseCode(block);
			replaced = parseReply.getKey();
			block = parseReply.getValue();
			System.out.println(block);
		}while(replaced);
		
		int length = 0;
		
		currentMatcher = Pattern.compile("<stmt>").matcher(block);
		while(currentMatcher.find())
			length += currentMatcher.end()-currentMatcher.start();
		
		System.out.println("Code final : "+block);
		if(block.length() == length) {
			System.out.println("==> Valide");
		}else {
			System.err.println("!!> Invalide");
		}
		
	}
	
	public final static String NAME = "[a-z][a-z0-9_]*";
	public final static String STRING = "\"[^\"]*\"";
	public final static String NUMBER = "[0-9]+(\\.[0-9]+)?";
	
	public static String transformConstants(String code) {
		code = code.replaceAll(STRING, "STRING").replaceAll(NUMBER, "NUMBER");
		Matcher nameMatcher = Pattern.compile(NAME).matcher(code);
		String newCode = code;
		String currentName;
		while(nameMatcher.find()) {
			currentName = code.substring(nameMatcher.start(), nameMatcher.end());
			if(!currentName.matches(KEY_WORDS))
				newCode = newCode.replaceFirst(currentName, "NAME");
		}
		return newCode;
	}
	
	public static String parseExpressions(String code) {
		String newCode = code;
		boolean replaced = false;
		String currentExpr;
		do {
			replaced = false;
			Matcher exprMatcher = Pattern.compile(blocks.get("<expr>")).matcher(code);
			while(exprMatcher.find()) {
				currentExpr = code.substring(exprMatcher.start(), exprMatcher.end());
				newCode = newCode.replace(currentExpr, "<expr>");
				replaced = true;
			}
			code = newCode;
		}while(replaced);
		return code;
	}
	
	public static Pair<Boolean, String> parseCode(String code) {
		boolean replaced = false;
		String newCode = code;
		replaced = false;
		Matcher currentMatcher;
		for(String word : WORDS) {
			currentMatcher = Pattern.compile(blocks.get(word)).matcher(newCode);
			while(currentMatcher.find()) {
				String c = block.substring(currentMatcher.start(), currentMatcher.end());
				newCode = newCode.replace(c, word);
				replaced = true;
				
			}
			code = newCode;
		}
		return new Pair<>(replaced, code);
	}

}
