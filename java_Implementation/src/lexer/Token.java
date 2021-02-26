package lexer;

import java.util.regex.*;

public class Token {
	protected TokenType type = null;
	protected String value = null;		// only != null if identifier
	
	// CONSTRUCTORS
		// for all TokenTypes but ID (these TokenTypes do not need a value)
	protected Token (TokenType type) {
		this.type = type;
	}
	
		//for TokenType of ID
	protected Token (TokenType type, String name) {
		this.type = type;
		if (name != null) {
			this.value = name;
		} else {
			throw new IllegalArgumentException("Lexer: Token of type ID has to get a name!");
		}
	}
	
	enum TokenType {		
		/* DIFFERENT TOKEN TYPES AND THEIR REGULAR EXPRESSIONS
		 * --> regex information: (https://medium.com/factory-mind/regex-tutorial-a-simple-cheatsheet-by-examples-649dc1c3f285)	
		 */
		LPAR("^($"),						
		RPAR("^)$"),						
		NEG("^not$"),					// "¬" --> let's use "not" in input
		AND("^and$"),					// "∧" --> let's use "and" in input
		IMP("^->$"),						
		TRUE("^true$"),					
		FALSE("^false$"),				
		//TODO: is CONSTANT correct?
		CONSTANT("^true$|^false$"),		// either "true" or "false"
		ID("^[a-zA-Z]+$");				// some identifier (String) --> must not be "not" or "and" (keywords)
		
		
		// CONSTRUCTOR
		private Pattern pattern;
		
		TokenType(String regex) {
			this.pattern = Pattern.compile(regex);
		}

		
		// METHODS
		/**
		 *@return pattern of regex of TokenKind (e. g.: AND => (^and$))
		 **/
		protected Pattern getPattern () {
			return this.pattern;
		}
		
		/**
		 *@return pattern string => Matcher uses that string to determine id this Token exists in the input 
		 **/
		protected String patternToString (TokenType token) {
			return ("|(?<" + token.name() +  ">" + token.getPattern());
		}
	}
}
