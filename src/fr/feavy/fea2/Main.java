package fr.feavy.fea2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	
	public static String block = "if(true){while{a:string=bonjour+test;a=autre;a=\"other\";nb:int=5;}}else{}while{}";

	public static Grammar grammar = new Grammar();
	
	static {
		grammar.putSymbol("stmt", "<compound_stmt>|<simple_stmt>");
		
		grammar.putSymbol("compound_stmt", "<if_stmt>|<while_stmt>|<method_decl>");
		grammar.putSymbol("if_stmt", "if\\(<expr>\\)\\{(<stmt>)*\\}(else\\{(<stmt>)*\\})?");
		grammar.putSymbol("while_stmt", "while\\{((<stmt>)*)\\}");
		
		grammar.putSymbol("simple_stmt", "<var_assignment>|<var_declaration>;");
		
		grammar.putSymbol("type", "string|boolean|int|float");
		grammar.putSymbol("var_assignment_left", "(<var_declaration>|NAME)=");
		grammar.putSymbol("var_assignment", "<var_assignment_left>(<expr>|\\[(<expr>(,<expr>)*)?\\]);");
		grammar.putSymbol("var_declaration", "NAME:<type>|<var_declaration>\\[<expr>\\]");
		
		grammar.putSymbol("method_signature", "NAME\\((NAME:<type>(,NAME:<type>)*)?\\):<type>");
		grammar.putSymbol("method_decl", "<method_signature>\\{(<stmt>)*\\}");
		
		grammar.putSymbol("expr", "true|false|NAME|STRING|NUMBER|(<expr>(\\+<expr>)+)");
		
		grammar.putTerminalSymbol("STRING", "\"[^\"]*\"");
		grammar.putTerminalSymbol("NAME", "[a-zA-Z_][a-zA-Z0-9_]*");
		grammar.putTerminalSymbol("NUMBER", "[0-9]+(\\.[0-9]+)?");
		
		grammar.autoExtractKeywords();
	}
	
	public static void main(String[] args) {
			
		try {
			Code code = new Code(loadCodeFromFile("/main.fea"));
			System.out.println(code.getContent());
			System.out.println("Valide : "+grammar.isValid(code));
			System.out.println(grammar.toString());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while(!(line = reader.readLine()).equals("end")) {
				System.out.println(((TerminalLexeme)code.getLexeme(line)).getValue());
			}
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*System.out.println(grammar.toString());
		System.out.println();
		System.out.println("Code valide : "+grammar.isValid(block));*/
		
		// 1 - Remplacer les strings par STRING, noms par NAME, nombres par NUMBER...
		// 2 - Exécuter la grammaire en boucle tant qu'il y a des transformations.
	}

	public static String loadCodeFromFile(String path) throws IOException {
		StringBuilder code = new StringBuilder();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(path)));
		while((line = reader.readLine()) != null) {
			if(!line.replaceAll("\\s", "").startsWith("//"))
				code.append(line);
		}
		reader.close();
		return code.toString().replaceAll("/\\**.\\*/", "");
	}
	
}
