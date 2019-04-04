package fr.feavy.fea2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;

public class Grammar {

	private Map<String, String> symbols;
	private Map<String, String> terminalSymbols;
	
	private List<String> keywords;
	
	public Grammar() {
		this(new LinkedHashMap<String, String>(), new LinkedHashMap<String, String>());
	}
	
	public Grammar(LinkedHashMap<String, String> symbols, LinkedHashMap<String, String> terminalSymbols) {
		this.symbols = symbols;
		this.terminalSymbols = terminalSymbols;
		this.keywords = new ArrayList<String>();
		autoExtractKeywords();
	}
	
	public void autoExtractKeywords() {
		Pattern keywordPattern = Pattern.compile("[a-z_]+");
		Matcher keywordMatcher;
		
		for(String regex : symbols.values()) {
			keywordMatcher = keywordPattern.matcher(regex);
			while(keywordMatcher.find()) {
				String current = keywordMatcher.group();
				if(!symbols.keySet().contains(current))
					keywords.add(keywordMatcher.group());
			}
		}
	}
	public void putSymbol(String symbol, String regex) {
		symbols.put(symbol, regex);
	}
	
	public void putTerminalSymbol(String symbol, String regex) {
		terminalSymbols.put(symbol.toUpperCase(), regex);
	}
	
	public String toString() {
		Iterator<Entry<String, String>> symbolsIterator = symbols.entrySet().iterator();
		Iterator<Entry<String, String>> terminalSymbolsIterator = terminalSymbols.entrySet().iterator();
		Entry<String, String> e;
		
		StringBuilder rep = new StringBuilder();

		rep.append("-- Keywords --\n");
		for(String keyword : keywords)
			rep.append(keyword+"\n");
		
		rep.append("-- Symbols --\n");
		while(symbolsIterator.hasNext()) {
			e = symbolsIterator.next();
			rep.append(e.getKey()+": "+e.getValue()+"\n");
		}
		
		rep.append("-- Terminal Symbols --\n");
		while(terminalSymbolsIterator.hasNext()) {
			e = terminalSymbolsIterator.next();
			rep.append(e.getKey()+": "+e.getValue()+"\n");
		}
		
		return rep.toString();
	}
	
	private String parseConstants(String input) {
		Iterator<Entry<String, String>> terminalSymbolsIterator = terminalSymbols.entrySet().iterator();
		Entry<String, String> e;
		
		
		Matcher currentMatcher;
		String current;
		
		while(terminalSymbolsIterator.hasNext()) {
			e = terminalSymbolsIterator.next();
			currentMatcher = Pattern.compile(e.getValue()).matcher(input);
			while(currentMatcher.find()) {
				current = currentMatcher.group();
				if(!keywords.contains(current)) {
					System.out.println("replace : "+current);
					input = input.replaceFirst(current, e.getKey());
					// Problème : remplace le premier 'fl' trouvé
				}
			}
		}
		System.out.println(input);
		return input;
	}
	
	private Pair<Boolean, String> parseCode(String input) {
		boolean replaced = false;
		String newCode = input;
		replaced = false;
		Matcher currentMatcher;
		for(String word : symbols.keySet()) {
			currentMatcher = Pattern.compile(symbols.get(word)).matcher(input);
			while(currentMatcher.find()) {
				newCode = newCode.replace(currentMatcher.group(), "<"+word+">");
				replaced = true;
				
			}
			input = newCode;
		}
		return new Pair<>(replaced, input);
	}
	
	public boolean isValid(String input) {
		Matcher currentMatcher;
		
		input = parseConstants(input);
		
		boolean replaced = false;
		Pair<Boolean, String> parseReply;
		
		do {
			parseReply = parseCode(input);
			replaced = parseReply.getKey();
			input = parseReply.getValue();
			System.out.println(input);
		}while(replaced);
		
		int length = 0;
		
		currentMatcher = Pattern.compile("<stmt>").matcher(input);
		while(currentMatcher.find())
			length += currentMatcher.end()-currentMatcher.start();
		
		System.out.println("Code final : "+input);
		return input.length() == length;
	}
	
}
