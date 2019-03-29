package fr.feavy.fea;

public class Variable {

	private String name, type;
	private String[] values;
	
	boolean isArray;
	
	public Variable(String name, String type, String value) {
		this(name, type, new String[]{value});
		this.isArray = false;
	}
	
	public Variable(String name, String type, String[] values) {
		this.name = name;
		this.type = type;
		this.values = values;
		this.isArray = true;
	}
	
	public String getName() {
		return name;
	}
	
}
