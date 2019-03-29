package fr.feavy.fea;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Block {

	private ArrayList<Variable> vars;
	
	private ArrayList<Instruction> instructions;
	
	private String content;
	private ArrayList<Block> subBlocks;
	private int id;
	
	public Block(int id, String blockContent) {
		this.content = blockContent;
		this.vars = new ArrayList<Variable>();
		this.subBlocks = new ArrayList<Block>();
		this.instructions = new ArrayList<Instruction>();
		this.id = id;
	}
	
	public Block getBlock(int id) {
		return subBlocks.get(id);
	}
	
	public int getId() {
		return id;
	}
	
	public void addSubBlock(Block b) {
		subBlocks.add(b);
	}
	
	public String getContent() {
		return content;
	}

	public boolean process() {
		boolean validBlock = true;
		
		boolean validSubBlock = true;
		
		Block b;
		for(int i = 0; i < subBlocks.size(); i++) {
			b = subBlocks.get(i);
			content = content.replace("{"+b.getContent()+"}", "{<block_"+i+">}");
			validSubBlock=b.process();
			if(!validSubBlock)
				validBlock = false;
		}
		
		Matcher instructionMatcher = InstructionParser.getLanguagePattern().matcher(content);
		
		Instruction current;
		
		while(instructionMatcher.find()) {
			if(instructionMatcher.group("varName") != null) {
				current = new VarDeclInstruction(content.substring(instructionMatcher.start(), instructionMatcher.end()));
			}else
				current = new Instruction(content.substring(instructionMatcher.start(), instructionMatcher.end()));
			instructions.add(current);
			if(!current.valid)
				validBlock = false;
		}
		
		return validBlock;
	}
	
}
