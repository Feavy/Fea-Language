package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;
import fr.feavy.fea.data.Type;

public class FuncDeclarationStatement extends Statement{

	private Type type;
	private String name;
	
	public FuncDeclarationStatement(Lexeme lexeme) {
		super(lexeme);
		Lexeme child = lexeme.getChild(0);
		this.name = child.getChild(0).getContent();
		if(child.getChilds().length > 1)
			this.type = new Type(child.getChild(1));
		else
			this.type = new Type("void", false);
	}
	
	@Override
	public String toString() {
		return "Function "+name+" -> "+this.type;
	}
	
}
