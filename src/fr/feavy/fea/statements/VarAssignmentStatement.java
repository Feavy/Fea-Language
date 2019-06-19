package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;
import fr.feavy.fea.data.Type;
import fr.feavy.fea.data.Value;

public class VarAssignmentStatement extends Statement{

	private VarDeclarationStatement declarationStmt;
	private boolean isDeclaration = false;
	
	private String varName;
	
	private Value newValue;
	
	public VarAssignmentStatement(Lexeme lexeme, Statement stmts[]) {
		super(lexeme, new Statement[0]);
		Lexeme child = lexeme.getChild(0);
		this.varName = child.getContent();
				
		if(child.getChild(0).getName().equals("var_declaration")) {
			this.isDeclaration = true;
			this.declarationStmt = new VarDeclarationStatement(child.getChild(0), null);
			this.varName = declarationStmt.getVarName();
		}else {
			this.isDeclaration = false;
			this.varName = child.getChild(0).getContent();
		}
		this.newValue = new Value(lexeme.getChild(1));
		
	}
	
	@Override
	public String toString() {
		String rep;
		if(isDeclaration)
			rep = "Declaration : "+varName+" is "+declarationStmt.getVarType();
		else	
			rep = "Assignment  : "+varName;
		rep += " = "+newValue;
		return rep;
	}
	
	public boolean isDeclaration() {
		return isDeclaration;
	}
	
}
