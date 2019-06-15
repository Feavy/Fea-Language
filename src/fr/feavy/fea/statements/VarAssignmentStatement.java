package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;
import fr.feavy.fea.data.Type;

public class VarAssignmentStatement extends Statement{

	private Type returnType;
	private String funcName;
	
	public VarAssignmentStatement(Lexeme lexeme) {
		super(lexeme);
		Lexeme child = lexeme.getChild(0);
		System.out.println("--= Var assignment =--");
		this.funcName = child.getContent();
		//this.returnType = new Type(child.get, isArray)
		lexeme.getChild(0).debug(0);
		
	}
	
}
