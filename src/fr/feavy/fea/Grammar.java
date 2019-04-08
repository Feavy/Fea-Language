package fr.feavy.fea;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	private Code currentCode;

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

		for (String regex : symbols.values()) {
			keywordMatcher = keywordPattern.matcher(regex);
			while (keywordMatcher.find()) {
				String current = keywordMatcher.group();
				if (!keywordMatcher.group().equals("_") && !symbols.keySet().contains(current.substring(0, current.length()-1))) {
					keywords.add(keywordMatcher.group());
				}
			}
		}
	}

	public void putSymbol(String symbol, String regex) {
		regex = regex.replaceAll("<([a-z_]+)>", "<$1_[0-9]+>").replaceAll("[A-Z]+", "($0_[0-9]+)");
		symbols.put(symbol, regex);
	}

	public void putTerminalSymbol(String symbol, String regex) {
		terminalSymbols.put(symbol.toUpperCase(), regex);
	}

	public String toString() {
		Iterator<Entry<String, String>> symbolsIterator = symbols.entrySet().iterator();
		Iterator<Entry<String, String>> terminalSymbolsIterator = terminalSymbols.entrySet().iterator();
		Iterator<Entry<String, ArrayList<Lexeme>>> lexemeIterators;
		Entry<String, ArrayList<Lexeme>> currentLexemes;
		Entry<String, String> e;

		StringBuilder rep = new StringBuilder();

		rep.append("-- Keywords --\n");
		for (String keyword : keywords)
			rep.append(keyword + "\n");

		rep.append("-- Symbols --\n");
		while (symbolsIterator.hasNext()) {
			e = symbolsIterator.next();
			rep.append(e.getKey() + ": " + e.getValue() + "\n");
		}

		rep.append("-- Terminal Symbols --\n");
		while (terminalSymbolsIterator.hasNext()) {
			e = terminalSymbolsIterator.next();
			rep.append(e.getKey() + ": " + e.getValue() + "\n");
		}
		return rep.toString();
	}

	private String parseConstants(String input) {
		Iterator<Entry<String, String>> terminalSymbolsIterator = terminalSymbols.entrySet().iterator();
		Entry<String, String> e;

		Matcher currentMatcher;
		String current;
		
		Map<String, ArrayList<String>> constants = new HashMap<String, ArrayList<String>>();

		while (terminalSymbolsIterator.hasNext()) {
			e = terminalSymbolsIterator.next();
			constants.put(e.getKey(), new ArrayList<String>());
			currentMatcher = Pattern.compile(e.getValue()).matcher(input);
			int offset = 0;
			while (currentMatcher.find()) {
				current = currentMatcher.group();
				if (!keywords.contains(current)) {
					System.out.println("replace : " + current);
					constants.get(e.getKey()).add(current);
					currentCode.addLexeme(e.getKey(), new Lexeme(e.getKey(), current));
					input = input.substring(0, currentMatcher.start() + offset) + "<"+e.getKey().toLowerCase()+">"
							+ input.substring(currentMatcher.end() + offset);
					offset += e.getKey().length()+2 - (currentMatcher.end() - currentMatcher.start());
				}
			}
		}
		
		Iterator<Entry<String, ArrayList<String>>> it = constants.entrySet().iterator();
		
		System.out.println(input);
		
		Entry<String, ArrayList<String>> e2;
		String word;
		
		while(it.hasNext()) {
			e2 = it.next();
			word = e2.getKey();
			for(int i = 0; i < e2.getValue().size(); i++) {
				input = input.replaceFirst("<"+word.toLowerCase()+">", word+"_"+i);
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
		int index;
		for (String word : symbols.keySet()) {
			currentMatcher = Pattern.compile(symbols.get(word)).matcher(input);
			int offset = 0;
			while (currentMatcher.find()) {
				index = currentCode.getLexemeIndex(word);
				newCode = newCode.substring(0, currentMatcher.start()+offset)+"<" + word + "_"+index+">"+newCode.substring(currentMatcher.end()+offset);
				
				offset += ("<" + word + "_"+index+">").length() - (currentMatcher.end() - currentMatcher.start());
				
				currentCode.addLexeme(word, new Lexeme(word, currentMatcher.group()));
				replaced = true;

			}
			input = newCode;
		}
		return new Pair<>(replaced, input);
	}

	public boolean isValid(Code code) {
		this.currentCode = code;
		String input = code.getContent();
		
		Matcher currentMatcher;

		input = parseConstants(input);
		
		input = input.replaceAll("\\s", "");

		boolean replaced = false;
		Pair<Boolean, String> parseReply;

		do {
			parseReply = parseCode(input);
			replaced = parseReply.getKey();
			input = parseReply.getValue();
			System.out.println(input);
		} while (replaced);

		int length = 0;

		currentMatcher = Pattern.compile("<stmt_[0-9]+>").matcher(input);
		while (currentMatcher.find())
			length += currentMatcher.end() - currentMatcher.start();

		System.out.println("Code final : " + input);
		return input.length() == length;
	}

}
