package fr.feavy.fea2;

public class Lexeme {

	private String name;
	private Lexeme childs[];
		
	public Lexeme(String name, Lexeme ...childs) {
		this.name = name;
		this.childs = childs;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isTerminal() {
		return childs.length == 0;
	}
	
	public Lexeme getChild(int index) {
		return childs[index];
	}
	
}
