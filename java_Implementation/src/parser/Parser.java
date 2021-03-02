/* ###############################################################################
 * 	PARSER: Takes a token list and converts it into a syntax tree like expression
 * 
 * Adding a helper syntax categorie so that I can use recursive descent parsing:
 *  arrowExp    ::= andExp {"->" arrowExp}
 *  andExp      ::= notExp andExpHelp
 *  andExpHelp  ::= {'∧' notExp andExpHelp}
 *  notExp      ::= {'¬'} primitives
 *  primitives  ::= Id | Constant | "(" arrowExp ")"
 *  
 * EXAMPLE: 
 * 	- input: [ID:x, AND, LPAR, LPAR, CONSTANT:false, IMP, CONSTANT:true, RPAR, AND, LPAR, NEG, CONSTANT:true, RPAR, RPAR]
 *  - output: AND (ID:x, AND (IMP (CONSTANT:false, CONSTANT:true), NEG (CONSTANT:true)))
 * ###############################################################################
 **/


//TODO: Info: Klammern fallen beim parsen weg -> keine expression dafür

package parser;

import java.util.ArrayList;
import java.util.List;
import lexer.Token;

/*
 *	Generates a pair of already parsed expression and the rest of the token list to be parsed next 
 **/
class ParsedUnparsedPair {
	private String parsed = null;
	private List<Token> parseNext = null;
	
	protected ParsedUnparsedPair (String parsed, List<Token> parseNext) {
		if (parsed == null || parseNext == null)
			throw new IllegalArgumentException("ERROR_PARSING: input of parsedUnparsedPair cannot be null");
		this.parsed = parsed;
		this.parseNext = parseNext;
	}
	
	protected String getParsed () {
		return this.parsed;
	}
	
	protected List<Token> getParseNext () {
		return this.parseNext;
	}
}



public class Parser {
	// CONSTRUCTOR
	public Parser () {
		
	}
	
	// METHODS
	public ParsedUnparsedPair arrowExp (List<Token> tokens) {
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
		
		//TODO: implement arrowExp
		
		
		ParsedUnparsedPair pair = new ParsedUnparsedPair(parsed, parseNext);
		return pair;
	}
	
	
	public ParsedUnparsedPair andExp (List<Token> tokens) {
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
		
		//TODO: implement andExp
		
		ParsedUnparsedPair pair = new ParsedUnparsedPair(parsed, parseNext);
		return pair;
	}
	
	
	public ParsedUnparsedPair andExpHelp (ParsedUnparsedPair inputPair) {
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
		
		//TODO: implement andExpHelp
		
		ParsedUnparsedPair pair = new ParsedUnparsedPair(parsed, parseNext);
		return pair;
	}
	
	
	public ParsedUnparsedPair notExp (List<Token> tokens) {
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
		
		//TODO: implement notExp
		
		ParsedUnparsedPair pair = new ParsedUnparsedPair(parsed, parseNext);
		return pair;
	}
	
	
	public ParsedUnparsedPair primitives (List<Token> tokens) {
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
		
		//TODO: implement primitives
		
		ParsedUnparsedPair pair = new ParsedUnparsedPair(parsed, parseNext);
		return pair;
	}
	
	
	public String parse (List<Token> tokens) throws Exception {
		if (tokens == null)
			throw new IllegalArgumentException("ERROR_PARSING: no tokens to parse");
		String parsed = new String();
		List<Token> parseNext = new ArrayList<Token>();
	
		parsed = arrowExp (tokens).getParsed();
		parseNext = arrowExp (tokens).getParseNext();
		
		if (parsed != null && parseNext.isEmpty()) {
			System.out.println("Everything was 'parsable'.");
			return parsed;
		}else {
			throw new Exception ("ERROR_PARSING: the token list could not be parsed entirely!\nThis is the parsed part: " + parsed);
		}
	}
}










