package fr.feavy.fea;

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
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		if(childs.length > 0)
			rep.append(getType()+"\n");
		else
			rep.append(getValue()+"\n");
		for(Lexeme l : childs) {
			rep.append("    "+((TerminalLexeme)l).toString());
		}
		rep.append("\n");
		return rep.toString();
	}
	
}
