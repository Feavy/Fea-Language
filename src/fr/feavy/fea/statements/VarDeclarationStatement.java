package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;
import fr.feavy.fea.data.Type;

public class VarDeclarationStatement extends Statement{

	private String varName;
	private Type varType;
	
	public VarDeclarationStatement(Lexeme lexeme, Statement stmts[]) {
		super(lexeme, new Statement[0]);
		String varType;
		Lexeme childs[] = getChilds();
		if(childs[0].getName().equals("var_declaration")) {
			// Array = true
			varName = childs[0].getChild(0).getContent();
			varType = childs[0].getChild(1).getContent();
			if(childs.length > 1 && childs[1].getName().equals("expr")) {
				this.varType = new Type(varType, true, childs[1]);
			}else {
				// Dynamic = true
				this.varType = new Type(varType, true);
			}
		} else {
			varName = childs[0].getContent();
			varType = childs[1].getContent();
			this.varType = new Type(childs[1]);
		}
	}
	
	@Override
	public String toString() {
		return "Declaration : "+this.varName+" is "+this.varType;
	}
	
	public String getVarName() {
		return varName;
	}

	public Type getVarType() {
		return varType;
	}
	
}
