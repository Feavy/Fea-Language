package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;
import fr.feavy.fea.data.Type;

public class FuncDeclarationStatement extends Statement{

	private Type returnType;
	private String funcName;
	
	public FuncDeclarationStatement(Lexeme lexeme) {
		super(lexeme);
		Lexeme child = lexeme.getChild(0);
		System.out.println("--= Func declaration =--");
		this.funcName = child.getContent();
		//this.returnType = new Type(child.get, isArray)
		lexeme.getChild(0).debug(0);
		
	}
	
}
