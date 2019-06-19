package fr.feavy.fea.statements;

import java.util.ArrayList;

import fr.feavy.fea.Lexeme;

public class IfStatement extends Statement{

	private ArrayList<Statement> ifStmts;
	private ArrayList<Statement> elseStmts;
	
	private Lexeme test;
	
	public IfStatement(Lexeme lexeme, Statement childStmts[]) {
		super(lexeme, childStmts);
	
		this.ifStmts = new ArrayList<>();
		this.elseStmts = new ArrayList<>();
		
		if(lexeme.getChild(0).getName().equals("if_else")) {
			test = lexeme.getChild(0).getChild(0).getChild(0).getChild(0);
			int ifStmtsAmount = lexeme.getChild(0).getChild(0).getChilds().length-1;
			int i;
			for(i = 0; i < ifStmtsAmount; i++)
				ifStmts.add(childStmts[i]);
			for(; i < childStmts.length; i++)
				elseStmts.add(childStmts[i]);
		}else {
			test = lexeme.getChild(0).getChild(0);
			for(int i = 0; i < childStmts.length; i++)
				ifStmts.add(childStmts[i]);
		}
	}
	
	
	@Override
	public String print(int depth) {
		return this.print(depth, true);
	}
	
	public String print(int depth, boolean startSpaces) {
		String spaces = "";
		for(int i = 0; i < depth; i++)
			spaces += "    ";
		String rep = ((startSpaces) ? spaces : "") + toString();
		if(getChildsStatements().length > 0) {
			rep += "\n";
			for(Statement st : ifStmts)
				rep += st.print(depth+1)+"\n";
			rep += spaces+"Else";
			if(elseStmts.size() == 1 && elseStmts.get(0) instanceof IfStatement) {
				rep += " "+((IfStatement)elseStmts.get(0)).print(depth, false)+"\n";
			} else {
				rep += "\n";
				for(Statement st : elseStmts)
					rep += st.print(depth+1)+"\n";
			}
		}
		return rep;
	}
	
	
	@Override
	public String toString() {
		return "If "+test.getContent();
	}
	
}
