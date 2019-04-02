package fr.feavy.fea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Block {

	private Map<String, Variable> vars;
	
	private ArrayList<Instruction> instructions;
	private ArrayList<Instruction> wrongInstructions;
	
	private String content;
	private ArrayList<Block> subBlocks;
	private int id;
	
	private boolean valid;
	
	public static final Pattern blockPattern = Pattern.compile("(?<blockName>[a-z :\\(\\)]+)(\\((?<blockParams>.*)\\))?\\{<[a-z]+_(?<blockId>[0-9]+)>\\}");
	
	public Block(int id, String blockContent) {
		this.valid = false;
		this.content = blockContent;
		this.vars = new HashMap<String, Variable>();
		this.subBlocks = new ArrayList<Block>();
		this.instructions = new ArrayList<Instruction>();
		this.wrongInstructions = new ArrayList<Instruction>();
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

	public void process(Map<String, Variable> superVariables) {
		if(superVariables != null) {
			vars.putAll(superVariables);
		}
		
		this.valid = true;
		
		Block b;
		for(int i = 0; i < subBlocks.size(); i++) {
			b = subBlocks.get(i);
			content = content.replace("{"+b.getContent()+"}", "{<block_"+i+">}");
		}
		
		Matcher instructionMatcher = InstructionParser.getLanguagePattern().matcher(content);
		
		Instruction currentInstruction;
		
		String currentInstructionStr;
		
		Matcher blockMatcher;
		
		int blockId;
		
		while(instructionMatcher.find()) {
			currentInstructionStr = content.substring(instructionMatcher.start(), instructionMatcher.end());
			blockMatcher = blockPattern.matcher(currentInstructionStr);
			
			if(blockMatcher.find()) {
				blockId = Integer.parseInt(blockMatcher.group("blockId"));
				System.out.println("Block : "+blockMatcher.group("blockName"));
				/*System.out.println("Current instruction : "+currentInstructionStr);
				System.out.println("Block name : "+blockMatcher.group("blockName"));
				System.out.println("Block params : "+blockMatcher.group("blockParams"));
				System.out.println("Block Id : "+blockId);*/
				currentInstruction = new Instruction(currentInstructionStr);
				b = subBlocks.get(blockId);
				b.process(vars);
				if(!b.isValid()) {
					valid = false;
					wrongInstructions.addAll(b.getWrongInstructions());
				}
			}else if(instructionMatcher.group("varName") != null) {
				currentInstruction = new VarDeclInstruction(currentInstructionStr);
				Variable v = ((VarDeclInstruction)currentInstruction).getVariable();
				
				if(currentInstruction.isValid()) {
					if(!vars.containsKey(v.getName()))
						vars.put(v.getName(), v);
					else {
						valid = false;
						wrongInstructions.add(currentInstruction);
						System.out.println("Var name already used");
					}
				}
			}else if(instructionMatcher.group("varName2") != null) {
				String varName = instructionMatcher.group("varName2");
				String newValue = instructionMatcher.group("newValue");
				currentInstruction = new Instruction(currentInstructionStr);
			}else
				currentInstruction = new Instruction(currentInstructionStr);
			instructions.add(currentInstruction);
			if(!currentInstruction.isValid()) {
				valid = false;
				wrongInstructions.add(currentInstruction);
			}
		}
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public ArrayList<Instruction> getWrongInstructions() {
		return wrongInstructions;
	}
	
	public String transpile() {
		for(Instruction i : instructions)
			System.out.println(i.getContent());
		return null;
	}
	
}
