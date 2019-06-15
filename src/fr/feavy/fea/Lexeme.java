package fr.feavy.fea;

import fr.feavy.fea.statements.VarDeclarationStatement;

public class Lexeme {

	private String content;
	private String name;
	private Lexeme childs[];
	private Lexeme parent;
		
	public Lexeme(String name, String content, Lexeme ...childs) {
		this.name = name;
		this.childs = childs;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean isTerminal() {
		return childs.length == 0;
	}
	
	public void setChilds(Lexeme[] childs) {
		this.childs = childs;
	}
	
	public Lexeme getChild(int index) {
		return childs[index];
	}
	
	public Lexeme[] getChilds() {
		return childs;
	}
	
	public void setParent(Lexeme parent) {
		this.parent = parent;
	}
	
	public Lexeme getParent() {
		return parent;
	}
	
	public void debug(int depth) {
		String spaces = "";
		for(int i = 0; i < depth*4; i++)
			spaces += " ";
		System.out.println(spaces + name + (isTerminal() ? " ("+content+")" : ""));
		
		for(Lexeme c : childs) {
			c.debug(depth+1);
		}
	}
	
	/*@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		if(childs.length > 0)
			rep.append(name+"("+content+")"+"\n");
		else
			rep.append(content+"\n");
		int i = 0;
		for(Lexeme l : childs) {
			rep.append(i+":    "+l.toString());
			i++;
		}
		return rep.toString();
	}*/
}
