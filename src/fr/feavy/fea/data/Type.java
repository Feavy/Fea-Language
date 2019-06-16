package fr.feavy.fea.data;

import fr.feavy.fea.Lexeme;

public class Type {

	private String type;
	private boolean isArray;
	private boolean isDynamicArray;
	private Lexeme arrayLength;
	
	public Type(Lexeme lexeme) {
		if(lexeme.getContent().endsWith("]")) {
			isArray = true;
			isDynamicArray = true;
			type = lexeme.getContent().replace("[]", "");
		}else {
			isArray = false;
			type = lexeme.getContent();
		}
	}
	
	public Type(String type, boolean isArray) {
		this.type = type;
		this.isArray = isArray;
		if(isArray)
			this.isDynamicArray = true;
		this.arrayLength = null;
	}
	
	public Type(String type, boolean isArray, Lexeme arraySize) {
		this.type = type;
		this.isArray = isArray;
		this.isDynamicArray = false;
		this.arrayLength = arraySize;
	}
	
	@Override
	public String toString() {
		String rep = type;
		if(isArray) {
			rep += "[";
			if(!isDynamicArray)
				rep += arrayLength.getContent();
			rep += "]";
		}
		return rep;
	}
	
	public String getType() {
		return type;
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
	
}
