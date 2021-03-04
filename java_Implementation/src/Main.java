import java.util.List;
import java.util.Scanner;

import expressions.Environment;
import expressions.Expression;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

public class Main {
	public static void main(String[] args) throws Exception {
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
		List<Token> tokens = myLexer.lex();
		
		
		/* #################################################################### 
		 * 	PARSER: GENERATES A SYNTAX TREE LIKE EXPRESSION FROM A TOKEN LIST
		 * ####################################################################
		 **/
		
		// Create an type environment e. g. ["x":true, "y":false]
		Environment myEnvironment = new Environment();
		myEnvironment.addEnvEntry("x", false);
		
		// Create a parser
		Parser myParser = new Parser(myEnvironment);
		Expression parsedTokens = myParser.parse(tokens);
		System.out.println("\nThe parsed tokens: " + parsedTokens.toString());
				
		
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
