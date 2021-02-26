/*
 * EXAMPLE OF VALID INPUT:
 * 		"x and ((false -> true) and (not true))"
 * 		equivalent to: 	x ∧ ((false -> true) ∧ ¬true)
 * THE LEXED INPUT WOULD BE:
 * 		[ID "x", AND, LPAR, LPAR, CONSTANT False, IMP, CONSTANT True, RPAR, AND, LPAR, NEG, CONSTANT True, RPAR, RPAR]
 */

package lexer;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class Lexer {
	private String inputToLex = null;
	
	// CONSTRUCTOR
	/**
	 *@param input = the String to be lexed e. g. "x and ((false -> true) and (not true))"
	 * */
	public Lexer (String input) {
		if (input == null) {
			throw new IllegalArgumentException("Lexer: the input string to lex equals null");
		} else {
			this.inputToLex = input;
		}
	}
	
	// METHODS
	public List<Token> lex () throws Exception {
		List<Token> tokens = new ArrayList<Token>();
		//TODO: splitted by whitespace not engough -> split by "(" and ")" to !!!!
		String[] splittedInput = this.inputToLex.split("\s");	// split by whitespace
		System.out.println("SPLITTED INPUT:");
		for (int i = 0; i < splittedInput.length; i++)
			System.out.println(splittedInput[i]);
		System.out.println("\n\n");
		
		//put all patterns in array -> for each word: check if allPatterns[word] matches -> if so: tokens.add allPatterns[word]
		String[] allPatterns = new String[Token.TokenType.values().length];
		for (int i = 0; i < allPatterns.length; i++) {
			allPatterns[i] = Token.TokenType.values()[i].patternToString(Token.TokenType.values()[i]);
		}
		
		//iterate over words 
		for (int i = 0; i < splittedInput.length; i++) {
			for (int j = 0; j < allPatterns.length; j++) {
				Pattern pattern = Pattern.compile(allPatterns[j]);
				Matcher matcher = pattern.matcher(splittedInput[i]);
				if (matcher.matches() == false) {
					throw new Exception("Lexer: the input '" + splittedInput[i] +"' cannot be matched with a token type.");
				} else {
					//adding Token to tokens list
					Token myToken = null;
					//TODO: muss hier .values() hin?
					Token.TokenType token = Token.TokenType.values()[j];
					if (token.equals(Token.TokenType.ID)) {
						//TODO: stimmen die Argumente?
						myToken = new Token (token, token.name());
					}else {
						myToken = new Token (token);
					}
					tokens.add(myToken);
				}
			}
		}
		return tokens;
	}
	
	public static void main(String[] args) {
		Lexer myLexer = new Lexer ("x and ((false -> true) and (not true))");
		try {
			myLexer.lex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
//	String[] seperatedIn = in.split("\s");
//	System.out.println("SEPERATED IN:");
//	for (int i = 0; i < seperatedIn.length; i++)
//		System.out.println(seperatedIn[i]);
//	System.out.println("\n\n");
//	for (String substr : seperatedIn) {
//		Matcher m = p.matcher(substr);
//		if (m.matches() == false) {
//			System.out.println(substr+" was not a match");
//		} else {
//			System.out.println(substr+" was a match");
//			System.out.println(m.toString());
//		}
//	}
	
	
	
	
}
