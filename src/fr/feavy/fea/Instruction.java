package fr.feavy.fea;

public class Instruction {

	private String content;
	protected boolean valid;
	
	public Instruction(String content) {
		this.content = content;
		this.valid = true;
	}
	
	public String getContent() { return content; }
	
	public boolean isValid() { return valid; }
	
}
