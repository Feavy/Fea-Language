package fr.feavy.fea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.feavy.fea.statements.Statement;

public class Code {

	private Map<String, ArrayList<Lexeme>> lexemes;
	private Set<Lexeme> terminalLexemes;
	private String content;

	public Code(String content) {
		this.lexemes = new HashMap<String, ArrayList<Lexeme>>();
		this.terminalLexemes = new HashSet<>();
		this.content = content.replaceAll(">", "§").replaceAll("<", "\\$");
	}

	public String getContent() {
		return content;
	}

	public void addLexeme(String word, Lexeme l) {
		if (!lexemes.containsKey(word))
			lexemes.put(word, new ArrayList<Lexeme>());
		lexemes.get(word).add(l);
	}

	public void addTerminalLexeme(Lexeme l) {
		this.terminalLexemes.add(l);
	}
	
	public Lexeme[] getTerminalLexemes() {
		return terminalLexemes.toArray(new Lexeme[0]);
	}
	
	public Lexeme getLexeme(String str) {
		Matcher lexemeStringMatcher = Pattern.compile("([A-Za-z_]+)_([0-9]+)").matcher(str);
		if(lexemeStringMatcher.find()) {
			if(lexemes.containsKey(lexemeStringMatcher.group(1))) {
				Lexeme l =  lexemes.get(lexemeStringMatcher.group(1)).get(Integer.parseInt(lexemeStringMatcher.group(2)));
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

	public Statement[] processLexemes(Lexeme l) {
		Matcher lexemeStringMatcher = Pattern.compile("<([A-Za-z_]+_[0-9]+)>|([A-Z]+_[0-9]+)").matcher(l.getContent());
		
		ArrayList<Lexeme> childs = new ArrayList<>();
		
		Lexeme current;
		Statement currentStmts[] = new Statement[0];
		
		Statement stmt;
		ArrayList<Statement> stmts = new ArrayList<>();
		while(lexemeStringMatcher.find()) {
			if(lexemeStringMatcher.group(1) != null) {
				current = getLexeme(lexemeStringMatcher.group(1));
				currentStmts = processLexemes(current);
				childs.add(current);
				
				if(currentStmts != null)
					for(Statement st : currentStmts)
						stmts.add(st);
			}else if(lexemeStringMatcher.group(2) != null) {
				current = getLexeme(lexemeStringMatcher.group(2));
				 processLexemes(current);
				childs.add(current);
			}
		}
		
		for(Lexeme child : childs)
			child.setParent(l);
		l.setChilds(childs.toArray(new Lexeme[0]));
		
		if(l.getName().equals("stmt")) {
			if(stmts != null) {
				stmt = Statement.createStatement(l, stmts.toArray(new Statement[0]));
				stmts.clear();
			}else {
				stmt = Statement.createStatement(l, new Statement[0]);
			}
			stmts.add(stmt);
		} else if(currentStmts == null || currentStmts.length == 0){
				stmts = null;
		}
		
		if(stmts != null)
			return stmts.toArray(new Statement[0]);
		else
			return null;
	}
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		rep.append("-- Lexemes --\n");

		Iterator<Entry<String, ArrayList<Lexeme>>> lexemeIterators = lexemes.entrySet().iterator();
		Entry<String, ArrayList<Lexeme>> currentLexemes;

		Lexeme l;
		int max;

		while (lexemeIterators.hasNext()) {
			currentLexemes = lexemeIterators.next();
			max = currentLexemes.getValue().size();
			for (int i = 0; i < max; i++) {
				l = currentLexemes.getValue().get(i);
				rep.append(l.getName() + "_" + i + " --> " + l.getContent() + "\n");
			}

		}

		return rep.toString();
	}

}
