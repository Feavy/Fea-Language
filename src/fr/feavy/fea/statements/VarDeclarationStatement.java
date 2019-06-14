package fr.feavy.fea;

public class VarDeclarationStatement {

	private String varName;
	private String varType;
	private boolean isArray;
	private boolean isDynamicArray;
	private String arrayLength;
	
	public VarDeclarationStatement(Lexeme lexeme) {
		Lexeme childs[] = lexeme.getChilds();
		if(childs[0].getName().equals("var_declaration")) {
			isArray = true;
			varName = childs[0].getChild(0).getContent();
			varType = childs[0].getChild(1).getContent();
			if(childs.length > 1 && childs[1].getChild(0).getName().equals("expr")) {
				arrayLength = childs[1].getChild(0).getContent();
				isDynamicArray = false;
			}else {
				isDynamicArray = true;
			}
		}else {
			varName = childs[0].getContent();
			varType = childs[0].getContent();
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

	public String getArrayLength() {
		return arrayLength;
	}
	
}
