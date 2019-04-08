package fr.feavy.fea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Code {

	private Map<String, ArrayList<Lexeme>> lexemes;
	private String content;

	public Code(String content) {
		this.lexemes = new HashMap<String, ArrayList<Lexeme>>();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void addLexeme(String word, Lexeme l) {
		if (!lexemes.containsKey(word))
			lexemes.put(word, new ArrayList<Lexeme>());
		lexemes.get(word).add(l);
	}

	public Lexeme getLexeme(String str) {
		Matcher lexemeStringMatcher = Pattern.compile("([A-Za-z_]+)_([0-9]+)").matcher(str);
		if(lexemeStringMatcher.find()) {
			if(lexemes.containsKey(lexemeStringMatcher.group(1))) {
				TerminalLexeme l =  (TerminalLexeme)lexemes.get(lexemeStringMatcher.group(1)).get(Integer.parseInt(lexemeStringMatcher.group(2)));
				if(l.getValue().matches("<([A-Za-z_]+)_([0-9]+)>"))
					return getLexeme(l.getValue());
				return l;
			}
		}
		return null;
	}
	
	public int getLexemeIndex(String word) {
		if(!lexemes.containsKey(word))
			return 0;
		return lexemes.get(word).size();
	}

	public void processLexemes(TerminalLexeme l) {
		Matcher lexemeStringMatcher = Pattern.compile("<([A-Za-z_]+_[0-9]+)>|([A-Z]+_[0-9]+)").matcher(l.getValue());
		ArrayList<Lexeme> childs = new ArrayList<>();
		TerminalLexeme current;
		while(lexemeStringMatcher.find()) {
			if(lexemeStringMatcher.group(1) != null) {
				current = (TerminalLexeme)getLexeme(lexemeStringMatcher.group(1));
				processLexemes(current);
				childs.add(current);
			}else if(lexemeStringMatcher.group(2) != null) {
				current = (TerminalLexeme)getLexeme(lexemeStringMatcher.group(2));
				processLexemes(current);
				childs.add(current);
			}
		}
		l.childs = childs.toArray(new Lexeme[0]);
	}
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		rep.append("-- Lexemes --\n");

		Iterator<Entry<String, ArrayList<Lexeme>>> lexemeIterators = lexemes.entrySet().iterator();
		Entry<String, ArrayList<Lexeme>> currentLexemes;

		Lexeme l;
		TerminalLexeme tl;
		int max;

		while (lexemeIterators.hasNext()) {
			currentLexemes = lexemeIterators.next();
			max = currentLexemes.getValue().size();
			for (int i = 0; i < max; i++) {
				l = currentLexemes.getValue().get(i);
				if (l instanceof TerminalLexeme) {
					tl = (TerminalLexeme) l;
					rep.append(tl.getName() + "_" + i + " --> " + tl.getValue() + "\n");
				} else {
					rep.append(l.getName() + " --> " + l.toString() + "\n");
				}
			}

		}

		return rep.toString();
	}

}
