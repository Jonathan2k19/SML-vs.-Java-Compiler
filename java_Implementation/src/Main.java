import java.util.List;
import java.util.Scanner;

import expressions.Environment;
import expressions.Expression;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

public class Main {
	private static Environment myEnvironment = new Environment();
	private static Scanner myScanner = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		/* ##########################################################
		 * 	LEXER: GENERATES A TOKEN LIST FROM THE INPUT STRING
		 * ##########################################################
		 **/		
		// Reading the input
		String inputString = new String();
		System.out.println("Enter a string to compile:");
		inputString += myScanner.nextLine();
		
		// Checking if user wants to add an environment
		System.out.println("Would you like to add an environment for identifiers (y/n)?");
		String answer = myScanner.nextLine();
		if(answer.equals("y")) {
			System.out.println("How much identifiers with corresponding values do you want to add?");
			int howOften = myScanner.nextInt();
			if(howOften > 0) {
				for (int i = 0; i < howOften; i++) {
					if (i == 0) {
						myScanner.nextLine();	// otherwise it will skip the first iteration of the loop ?!
					}
					System.out.println("Enter a identifier name");
					String name = myScanner.nextLine();
					System.out.println("Enter a value (true or false) to be assigned to this identifiers name");
					String value = myScanner.nextLine();
					if (value .equals("true")) {
						myEnvironment.addEnvEntry(name, true);
					} else if (value .equals("false")){
						myEnvironment.addEnvEntry(name, false);
					} else {
						System.out.println("Unknown value type");
					}
				}
			}else {
				System.out.println("Continue without an environment");
			}
		}else {
			System.out.println("Continue without an environment");
		}
		myScanner.close();
		
		
		// Lexing the input
		Lexer myLexer = new Lexer (inputString);
		List<Token> tokens = myLexer.lex();
		System.out.println("\nThe lexed input string:\t" + tokens);
		
		
		
		/* #################################################################### 
		 * 	PARSER: GENERATES A SYNTAX TREE LIKE EXPRESSION FROM A TOKEN LIST
		 * ####################################################################
		 **/		
		// Create a parser
		Parser myParser = new Parser(myEnvironment);
		Expression parsedTokens = myParser.parse(tokens);
		System.out.println("\nThe parsed tokens:\t" + parsedTokens.toString());
				
		
		
		
		/* #################################################################### 
		 * 	ELABORATION: use Expression class method checkType()
		 * ####################################################################
		 **/
		if(parsedTokens.checkType() == false)
			throw new Exception ("ELABORATION_ERROR: types are not correct");
		System.out.println("\nTypes are correct.");
		
		
		
		/* #################################################################### 
		 * 	EVALUATION: use Expression class method calculateValue()
		 * ####################################################################
		 **/
		System.out.println("\nThe value of the expression you entered: " + parsedTokens.calculateValue());
	}	
}
