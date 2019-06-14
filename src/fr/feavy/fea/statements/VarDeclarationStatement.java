package fr.feavy.fea.statements;

import fr.feavy.fea.Lexeme;

public class VarDeclarationStatement {

	private String varName;
	private String varType;
	private boolean isArray;
	private boolean isDynamicArray;
	private Lexeme arrayLength;
	
	public VarDeclarationStatement(Lexeme lexeme) {
		Lexeme parent = lexeme.getParent();
		Lexeme childs[] = lexeme.getChilds();
		if(childs[0].getName().equals("var_declaration")) {
			isArray = true;
			varName = childs[0].getChild(0).getContent();
			varType = childs[0].getChild(1).getContent();
			if(childs.length > 1 && childs[1].getName().equals("expr")) {
				arrayLength = childs[1];
				isDynamicArray = false;
			}else {
				if(parent.getName().equals("var_assignment_left") && parent.getParent().getChilds().length > 1) {
					isDynamicArray = false;
					arrayLength = new Lexeme("NUMBER", (parent.getParent().getChilds().length-1)+"");
				}else
				isDynamicArray = true;
			}
		} else {
			varName = childs[0].getContent();
			varType = childs[1].getContent();
		}
	}
	
	public String getVarName() {
		return varName;
	}

	public String getVarType() {
		return varType;
	}

	public boolean isArray() {
		return isArray;
	}

	public Lexeme getArrayLength() {
		return arrayLength;
	}
	
	public boolean isDynamicArray() {
		return isDynamicArray;
	}
	
	public void debug(int depth) {
		String spaces = "";
		for(int i = 0; i < depth*4; i++)
			spaces += " ";
		
		System.out.println(spaces+"-- Var declaration --");
		System.out.println(spaces+"Nom : "+getVarName());
		System.out.println(spaces+"Type : "+getVarType());
		System.out.println(spaces+"Tableau : "+isArray());
		if(isArray())
			if(isDynamicArray)
				System.out.println(spaces+"  Taille : dynamique");
			else {
				System.out.println(spaces+"  Taille : ");
				getArrayLength().debug(depth);
			}
		System.out.println(spaces+"---------------------");
	}
	
}
