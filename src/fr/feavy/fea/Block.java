package fr.feavy.fea;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Block {

	private ArrayList<Instruction> instructions;
	
	private String content;
	private ArrayList<Block> subBlocks;
	private int id;
	
	public Block(int id, String blockContent) {
		this.content = blockContent;
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

	public void process() {
		Block b;
		for(int i = 0; i < subBlocks.size(); i++) {
			b = subBlocks.get(i);
			content = content.replace("{"+b.getContent()+"}", "{<block_"+i+">}");
			b.process();
		}
		
		Matcher instructionMatcher = InstructionParser.getLanguagePattern().matcher(content);
		
		System.out.println("Block "+id);
		while(instructionMatcher.find()) {
			System.out.println("Instruction : "+(content.substring(instructionMatcher.start(), instructionMatcher.end())));
		}
	}
	
}
