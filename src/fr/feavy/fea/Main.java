package fr.feavy.fea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.feavy.fea.statements.Statement;

public class Main {

	public static Grammar grammar = new Grammar();

	static {
		grammar.putSymbol("stmt", "<compound_stmt>|<simple_stmt>");

		grammar.putSymbol("compound_stmt", "<if_stmt>|<while_stmt>|<func_decl>");
		grammar.putSymbol("if_head", "if\\(<expr>\\)");
		grammar.putSymbol("if_stmt", "<if_head>\\{(<stmt>)*\\}(else\\{(<stmt>)*\\})?");
		grammar.putSymbol("while_stmt", "while\\{((<stmt>)*)\\}");

		grammar.putSymbol("simple_stmt", "<var_assignment>;|<var_declaration>;");

		grammar.putSymbol("type", "(string|boolean|int|float)(\\[\\])?");
		grammar.putSymbol("var_assignment_left", "(<var_declaration>|NAME)=");
		grammar.putSymbol("var_assignment", "<var_assignment_left>(<expr>|\\[(<expr>(,<expr>)*)?\\])");
		grammar.putSymbol("var_declaration", "NAME:<type>|<var_declaration>\\[<expr>\\]");

		grammar.putSymbol("func_signature", "NAME\\((NAME:<type>(,NAME:<type>)*)?\\):<type>");
		
		grammar.putSymbol("func_decl", "<func_signature>\\{(<stmt>)*\\}");

		grammar.putSymbol("operator", "\\+|-|\\*");
		grammar.putSymbol("expr", "true|false|NAME|STRING|NUMBER|(<expr>(<operator><expr>)+)|\\(<expr>\\)");

		grammar.putTerminalSymbol("STRING", "\"[^\"]*\"|\'[^\']*\'"); // "test" ou 'test'
		grammar.putTerminalSymbol("NAME", "[a-zA-Z_][a-zA-Z0-9_]*"); // abc78f774_
		grammar.putTerminalSymbol("NUMBER", "[0-9]+(\\.[0-9]+)?"); // 9564.135

		grammar.autoExtractKeywords();
	}

	public static void main(String[] args) {

		try {
			Code code = new Code(loadCodeFromFile("/main.fea"));
			boolean valid = grammar.isValid(code);
			System.out.println(code.getContent());
			System.out.println("Valide : " + valid);
			System.out.println(grammar.toString());

			if (valid) {

				Statement[] rep = code.processLexemes(code.getCodeLexeme());
				
				//code.getCodeLexeme().debug(0);
								
				System.out.println(rep[0]);
				/*
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String line;
				while (!(line = reader.readLine()).equals("end")) {
					try {
						code.getLexeme(line).debug(0);
						// System.out.println((code.getLexeme(line)).toString());
					} catch (Exception e) {
						System.err.println("Inexistant");
					}
				}
				reader.close();*/

			}else {
				System.err.println("Code invalide.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * System.out.println(grammar.toString()); System.out.println();
		 * System.out.println("Code valide : "+grammar.isValid(block));
		 */

		// 1 - Remplacer les strings par STRING, noms par NAME, nombres par NUMBER...
		// 2 - Exécuter la grammaire en boucle tant qu'il y a des transformations.
	}

	public static String loadCodeFromFile(String path) throws IOException {
		StringBuilder code = new StringBuilder();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(path)));
		while ((line = reader.readLine()) != null) {
			if (!line.replaceAll("\\s", "").startsWith("//"))
				code.append(line);
		}
		reader.close();
		return code.toString().replaceAll("/\\**.\\*/", "");
	}

}
