package fr.feavy.fea2;

public class TerminalLexeme extends Lexeme{

	private String value;
	
	public TerminalLexeme(String name, String value) {
		super(name, new Lexeme[0]);
		this.value = value;
	}
	
	public String getType() {
		return getName();
	}
	
	public String getValue() {
		return value;
	}
	
}
