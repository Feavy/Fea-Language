package fr.feavy.fea.statements;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.feavy.fea.Lexeme;

public class Statement {

	private Lexeme lexeme;
	private Lexeme parent;
	private Lexeme childs[];

	private Statement childStatements[];

	protected Statement(Lexeme l) {
		this.lexeme = l;
		this.parent = l.getParent();
		this.childs = l.getChilds();
		this.childStatements = new Statement[0];
	}

	public void setChildStatements(Statement statements[]) {
		this.childStatements = statements;
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
	}
	
	public static Statement createStatement(Lexeme lexeme) {
		lexeme = lexeme.getChild(0).getChild(0);
		String name = lexeme.getName();
		if(classes.containsKey(name)) {
			try {
				return (Statement)classes.get(name).getConstructors()[0].newInstance(lexeme);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Statement(lexeme);
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
