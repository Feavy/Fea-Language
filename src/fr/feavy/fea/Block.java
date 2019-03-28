package fr.feavy.fea;

import java.util.ArrayList;

public class Block {

	private String content;
	private ArrayList<Block> subBlocks;
	private int id;
	
	public Block(int id, String instructions) {
		this.content = instructions;
		this.subBlocks = new ArrayList<Block>();
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
	}
	
}
