/* ###################################################################################################################
 * 	LEXER: GENERATES A TOKEN LIST FROM THE INPUT STRING
 * ###################################################################################################################
 * 
 * EXAMPLE OF VALID INPUT:
 * 		"x and ((false -> true) and (not true))"
 * 		equivalent to: 	x ∧ ((false -> true) ∧ ¬true)
 * THE LEXED INPUT WOULD BE:
 * 		[ID:x, AND, LPAR, LPAR, CONSTANT:false, IMP, CONSTANT:true, RPAR, AND, LPAR, NEG, CONSTANT:true, RPAR, RPAR]
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
			throw new IllegalArgumentException("ERROR_LEXING: the input string to lex equals null");
		} else {
			this.inputToLex = input;
		}
	}
	
	// METHODS
	public List<Token> lex () throws Exception {
		List<Token> tokens = new ArrayList<Token>();
		
		// Split input by "(", ")", whitespace (using Lookbehind/Lookahead regex)
		String[] splittedInput = this.inputToLex.split("\\(|\\s+|(?<=\\))|(?=\\))");

		// For each word: check if allPatterns[word] matches -> if true: tokens.add(word)
		String[] allPatterns = new String[Token.TokenType.values().length];
		for (int i = 0; i < allPatterns.length; i++) {
			allPatterns[i] = Token.TokenType.values()[i].patternToString(Token.TokenType.values()[i]);
		}
		
		// Iterate over words 
		for (int i = 0; i < splittedInput.length; i++) {
			boolean foundMatch = false;
			// Iterate over all pattern for each word (I know, not very efficient but works for now)
			for (int j = 0; j < allPatterns.length; j++) {
				Pattern pattern = Pattern.compile(allPatterns[j]);
				Matcher matcher = pattern.matcher(splittedInput[i]);
				if (matcher.matches()) {
					foundMatch = true;
					// Generate Token and adding it to tokens list
					Token myToken = null;
					Token.TokenType token = Token.TokenType.values()[j];
					if (token.equals(Token.TokenType.ID) || token.equals(Token.TokenType.CONSTANT)) {
						myToken = new Token (token, splittedInput[i]);
					}else {
						myToken = new Token (token);
					}
					tokens.add(myToken);
					break;	// splittedInput[i] might be matched several times if I do not break out of for-loop
				}
			}
			// No match
			if (foundMatch == false) {
				throw new Exception("ERROR_LEXING: The input '" + splittedInput[i] +"' cannot be matched with any token type.");
			}
		}
		return tokens;
	}
}
