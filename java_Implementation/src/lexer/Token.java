/* ####################################################
 * 	THIS CLASS PROVIDES TOKENS FOR THE LEXER CLASS
 * ####################################################
 **/

package lexer;

import java.util.regex.*;

public class Token {
	protected TokenType type = null;
	protected String value = null;		// only != null if TokenType == identifier or constant
	
	// CONSTRUCTORS
	protected Token (TokenType type) {
		this.type = type;
	}
	
	protected Token (TokenType type, String name) {
		this.type = type;
		if (name != null) {
			this.value = name;
		} else {
			throw new IllegalArgumentException("ERROR_LEXING: Token of type ID/CONSTANT must have a value.");
		}
	}
	
	// METHODS
	@Override
	public String toString() {
		if (this.value == null) {
			return this.type.name();
		}else {
			return (this.type.name() + ":" + this.value);
		}
	}
	
	enum TokenType {		
		/* DIFFERENT TOKEN TYPES AND THEIR REGULAR EXPRESSIONS
		 * --> regex information: (https://medium.com/factory-mind/regex-tutorial-a-simple-cheatsheet-by-examples-649dc1c3f285)	
		 */
		LPAR("\\("),						
		RPAR("\\)"),						
		NEG("^not$"),					// "¬" --> let's use "not" in input (easier to type)
		AND("^and$"),					// "∧" --> let's use "and" in input (easier to type)
		IMP("^->$"),						
		CONSTANT("^true$|^false$"),		// either "true" or "false"
		ID("^[a-zA-Z]+$");				// some identifier (String) --> must not be "not" or "and" (keywords) --> check AND and NEG before ID
		
		
		// CONSTRUCTOR
		private Pattern pattern;
		
		TokenType(String regex) {
			this.pattern = Pattern.compile(regex);
		}

		
		// METHODS
		/**
		 *@return pattern of regex of TokenType e. g.: AND => ^and$
		 **/
		protected Pattern getPattern () {
			return this.pattern;
		}
		
		/**
		 *@return pattern string => Matcher uses that string to determine if this Token exists in the input 
		 **/
		protected String patternToString (TokenType token) {
			return ("|(?<" + token.name() +  ">" + token.getPattern()+")");
		}
	}
}
