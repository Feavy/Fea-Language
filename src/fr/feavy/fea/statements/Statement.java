package fr.feavy.fea.statements;

import java.util.HashMap;
import java.util.Map;

import fr.feavy.fea.Lexeme;

public class Statement {

	private Lexeme lexeme;
	private Lexeme parent;
	private Lexeme childs[];

	private Statement childStatements[];
	
	protected Statement(Lexeme l, Statement childs[]) {
		this.lexeme = l;
		this.parent = l.getParent();
		this.childs = l.getChilds();
		this.childStatements = childs;
	}

	public Lexeme getLexeme() {
		return lexeme;
	}

	public Lexeme getParent() {
		return parent;
	}

	public Lexeme[] getChilds() {
		return childs;
	}
	
	public Statement[] getChildsStatements() {
		return childStatements;
	}

	public final static Map<String, Class<? extends Statement>> classes = new HashMap<>();
	static {
		classes.put("var_declaration", VarDeclarationStatement.class);
		classes.put("func_decl", FuncDeclarationStatement.class);
		classes.put("var_assignment", VarAssignmentStatement.class);
		classes.put("if_stmt", IfStatement.class);
	}
	
	public static Statement createStatement(Lexeme lexeme, Statement[] childs) {
		lexeme = lexeme.getChild(0).getChild(0);
		String name = lexeme.getName();
		if(classes.containsKey(name)) {
			try {
				return (Statement)classes.get(name).getConstructors()[0].newInstance(lexeme, childs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Statement(lexeme, new Statement[0]);
	}

	public String print(int depth) {
		String spaces = "";
		for(int i = 0; i < depth; i++)
			spaces += "    ";
		String rep = spaces+toString();
		if(childStatements.length > 0) {
			rep += "\n";
			for(Statement st : childStatements)
				rep += st.print(depth+1)+"\n";
		}
		return rep;
	}
	
	@Override
	public String toString() {
		String rep = "Statement : " + lexeme.getName();
		/*if (childStatements.length > 0) {
			rep += "Childs (" + childStatements.length + ") :\n";
			for (Statement st : childStatements) {
				if (st != null)
					rep += st.toString();
			}
		}*/
		return rep;
	}

}
