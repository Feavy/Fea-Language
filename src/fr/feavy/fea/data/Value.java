package fr.feavy.fea.data;

import fr.feavy.fea.Lexeme;

public class Value {

	private Lexeme expr;
	
	private String type = "undefined";
	private boolean isArray = false;
	private int arrayLength = -1;
	
	public Value(Lexeme expr) {
		this.expr = expr;
		if(expr.getContent().startsWith("[")) {
			isArray = true;
			arrayLength = expr.getChilds().length;
		}else {
			isArray = false;
		}
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	public int getArrayLength() {
		return arrayLength;
	}
	
	@Override
	public String toString() {
		String rep = "";
		if(isArray) {
			rep += "array("+arrayLength+")"+expr.getContent();
		}else {
			rep = expr.getContent();
		}
		return rep;
	}
	
}
