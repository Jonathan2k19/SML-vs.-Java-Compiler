import java.util.List;
import java.util.Scanner;

import lexer.Lexer;
import lexer.Token;

public class Main {
	public static void main(String[] args) {
		/* ##########################################################
		 * 	LEXER: GENERATES A TOKEN LIST FROM THE INPUT STRING
		 * ##########################################################
		 **/
		
		// Reading the input
		Scanner myScanner = new Scanner(System.in);
		String inputString = new String();
		System.out.println("Enter a string to lex:");
		inputString += myScanner.nextLine();
		myScanner.close();
		
		// Lexing the input
		Lexer myLexer = new Lexer (inputString);
		try {
			List<Token> tokens = myLexer.lex();
			for (Token token : tokens) {
				System.out.println(token);
			}
		} catch (Exception e) {
			System.out.println("ERROR_LEXING: It seems like you entered something illegal.\n"
					+ "-->EXAMPLE of valid input: x and ((false -> true) and (not true))");
		}
		
		
		/* #################################################################### 
		 * 	PARSER: GENERATES A SYNTAX TREE LIKE EXPRESSION FROM A TOKEN LIST
		 * ####################################################################
		 **/
		
		//TODO: check expressions, environment
		
		
		/* #################################################################### 
		 * 	ELABORATION: TODO: use expression class to check types
		 * ####################################################################
		 **/
		
		/* #################################################################### 
		 * 	EVALUATION: TODO: use expression class method calculateValue()
		 * ####################################################################
		 **/
	}	
}
