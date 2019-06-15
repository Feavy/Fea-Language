package fr.feavy.fea.data;

import fr.feavy.fea.Lexeme;

public class Type {

	private String type;
	private boolean isArray;
	private boolean isDynamicArray;
	private Lexeme arrayLength;
	
	public Type(String type, boolean isArray) {
		this.type = type;
		this.isArray = isArray;
		this.isDynamicArray = true;
		this.arrayLength = null;
	}
	
	public Type(String type, boolean isArray, Lexeme arraySize) {
		this.type = type;
		this.isArray = isArray;
		this.isDynamicArray = false;
		this.arrayLength = arraySize;
	}


	
	public String getVarType() {
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
