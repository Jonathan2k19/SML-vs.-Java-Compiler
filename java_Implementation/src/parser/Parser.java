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
 *  ---> creating one method for each syntax categorie
 *  
 * EXAMPLE: 
 * 	- input: [ID:x, AND, LPAR, LPAR, CONSTANT:false, IMP, CONSTANT:true, RPAR, AND, LPAR, NEG, CONSTANT:true, RPAR, RPAR]
 *  - output: AND (ID:x, AND (IMP (CONSTANT:false, CONSTANT:true), NEG (CONSTANT:true)))
 * ###############################################################################
 **/



package parser;

import java.util.ArrayList;
import java.util.List;
import lexer.Token;
import expressions.*;



/**
 *@return	Generates a pair of an already parsed expression and the rest of the token list to be parsed after that 
 */
class ParsedUnparsedPair {
	private Expression parsed = null;
	private List<Token> parseNext = null;
	
	protected ParsedUnparsedPair (Expression parsed, List<Token> parseNext) {
		if (parsed == null || parseNext == null)
			throw new IllegalArgumentException("ERROR_PARSING: input of parsedUnparsedPair cannot be null");
		this.parsed = parsed;
		this.parseNext = parseNext;
	}
	
	protected Expression getParsed () {
		return this.parsed;
	}
	
	protected List<Token> getParseNext () {
		return this.parseNext;
	}
}



public class Parser {	
	private Environment environment = null;
	
	
	// CONSTRUCTOR
	/**
	 *@param Environment => identifiers are assigned to boolean values e. g. env = [x:true, y:false]
	 */
	public Parser (Environment env) {
		this.environment = env;
	}
	
	
	/* METHODS
	 * 
	 * - I need these try catch blocks, otherwise it will throw an IndexOutOfBoundsException because parseNext list is empty 
	 * 		--> return parsed expression in that case  
	 **/ 	
	public ParsedUnparsedPair arrowExp (List<Token> tokens) throws Exception {
		ParsedUnparsedPair andParsed = new ParsedUnparsedPair(andExp (tokens).getParsed(), andExp (tokens).getParseNext());
		Expression left = andParsed.getParsed();
		Expression right = null;
		
		// check whether the next token is of type IMP or not
		try {
			// next token is of type IMP
			if (andParsed.getParseNext().get(0).getTokenType() == Token.TokenType.IMP) {
				List<Token> withoutIMP = andParsed.getParseNext().subList(1, andParsed.getParseNext().size());
				ParsedUnparsedPair arrowParsed = arrowExp (withoutIMP);
				right = arrowParsed.getParsed();
				Expression implicationExp = new BinaryExpression(new Token (Token.TokenType.IMP), left, right);
				ParsedUnparsedPair impExpWithRest = new ParsedUnparsedPair(implicationExp, arrowParsed.getParseNext());
				return impExpWithRest;
				
			// next token is not of type IMP
			}else {
				return andParsed;
			}
		} catch (IndexOutOfBoundsException e){
			// there is no token to parse next -> return already parsed expression
			return andParsed;
		}
	}
	
	
	public ParsedUnparsedPair andExp (List<Token> tokens) throws Exception {
		// passing tokens to helper method after parsing with notExp
		ParsedUnparsedPair notParsed = new ParsedUnparsedPair(notExp(tokens).getParsed(),notExp(tokens).getParseNext());
		return andExpHelp(notParsed);		
	}
	
	
	public ParsedUnparsedPair andExpHelp (ParsedUnparsedPair inputPair) throws Exception {
		try {
			// next token is of type AND
			if (inputPair.getParseNext().get(0).getTokenType() == Token.TokenType.AND) {
				List<Token> withoutAND = inputPair.getParseNext().subList(1, inputPair.getParseNext().size());
				ParsedUnparsedPair notParsed = notExp (withoutAND);
				Expression left = inputPair.getParsed();
				Expression right = notParsed.getParsed();
				Expression andExpression = new BinaryExpression (new Token (Token.TokenType.AND), left, right);
				return andExpHelp (new ParsedUnparsedPair(andExpression, notParsed.getParseNext()));
				
			// next token is not of type AND
			}else {
				return inputPair;
			}
		} catch (IndexOutOfBoundsException e) {
			// there is no next token to be parsed -> return already parsed expression
			return inputPair;
		}
	}
	
	
	public ParsedUnparsedPair notExp (List<Token> tokens) throws Exception {
		try {
			// next token is of type NEG
			if(tokens.get(0).getTokenType() == Token.TokenType.NEG) {
				ParsedUnparsedPair primitivesParsed = primitives(tokens.subList(1, tokens.size()));
				Expression e = primitivesParsed.getParsed();
				Expression negExpression = new UnaryExpression(new Token(Token.TokenType.NEG), e);
				ParsedUnparsedPair negExpWithRest = new ParsedUnparsedPair(negExpression, primitivesParsed.getParseNext());
				return negExpWithRest;
			
			// next token is not of type NEG
			} else {
				return primitives(tokens);
			}
		} catch (IndexOutOfBoundsException e) {
			return primitives(tokens);
		}
	}
	
	
	public ParsedUnparsedPair primitives (List<Token> tokens) throws Exception {
		// the token list with all elements but the first token
		List<Token> withoutFirstToken = new ArrayList<Token>();
		try {
			withoutFirstToken = tokens.subList(1, tokens.size());
		} catch (IllegalArgumentException e) {
			// the only legal tokens (true|false) that generate a token list with size 1 are parsed correctly 
			//  --> this has to be an unknown pattern
			throw new Exception ("ERROR_PARSING: unknown pattern");
		}
		
		// the next token to be parsed
		Token firstToken = tokens.get(0);
		
		
		// check if next token is of type CONSTANT(true|false)
		if (firstToken.getTokenType() == Token.TokenType.CONSTANT) {
			// token is CONSTANT:true
			if (firstToken.getTokenValue().equals("true")) {
				Expression constantExp = new PrimitiveExpression(new Token (Token.TokenType.CONSTANT, "true"), null);
				ParsedUnparsedPair constantExpWithRest = new ParsedUnparsedPair(constantExp, withoutFirstToken);
				return constantExpWithRest;
			// token is CONSTANT:false
			} else if (firstToken.getTokenValue().equals("false")) {
				Expression constantExp = new PrimitiveExpression(new Token (Token.TokenType.CONSTANT, "false"), null);
				ParsedUnparsedPair constantExpWithRest = new ParsedUnparsedPair(constantExp, withoutFirstToken);
				return constantExpWithRest;
			// I should never get in that case
			} else {
				throw new Exception ("ERROR_PARSING: Constant has to have one of the following values: \"true\" | \"false\"");
			}
			
		// check if next token is of type ID (string)
		} else if (firstToken.getTokenType() == Token.TokenType.ID && firstToken.getTokenValue() != null){	
			Expression idExp = new PrimitiveExpression(firstToken, this.environment);				
			ParsedUnparsedPair idWithRest = new ParsedUnparsedPair(idExp, withoutFirstToken);
			return idWithRest;
		
		// check if next token is of type LPAR -> if so: check if there is a closing parenthesis afterwards
		} else if (firstToken.getTokenType() == Token.TokenType.LPAR){
			try {	
				if (arrowExp(withoutFirstToken).getParseNext().get(0).getTokenType() == Token.TokenType.RPAR) {
					System.out.println("rpar: " + arrowExp(withoutFirstToken).getParseNext().get(0).getTokenType());
					List<Token> arrParseNext = arrowExp(withoutFirstToken).getParseNext();								
					ParsedUnparsedPair arrowParsed = new ParsedUnparsedPair(arrowExp(withoutFirstToken).getParsed(), 
							arrParseNext.subList(1, arrParseNext.size()));												
					return arrowParsed;
				}else {
					throw new Exception ("ERROR_PARSING: missing right parenthesis");
				}
			} catch (IndexOutOfBoundsException e) {
				throw new Exception ("ERROR_PARSING: missing right parenthesis"); 
			}
			
		// unknown pattern (not an Id|Constant|Parenthesis)
		} else {
			throw new Exception("ERROR_PARSING: unknown pattern");
		}
	}
	
	
	/**
	 *@return if successful: parsed input, else: already parsed input and input that could not get parsed
	 */
	public Expression parse (List<Token> tokens) throws Exception {
		// check if everything could be parsed
		if (tokens == null)
			throw new IllegalArgumentException("ERROR_PARSING: no tokens to parse");
		Expression parsed = arrowExp (tokens).getParsed();
		List<Token> restList = arrowExp (tokens).getParseNext();	// if null -> success, else: contains the unparsed tokens
	
		// output parsed input or throw exception
		if (parsed != null && restList.isEmpty()) {
			return parsed;
		}else {
			throw new Exception ("ERROR_PARSING: the token list could not be parsed entirely!\nThis is the parsed part: " + parsed.toString() 
			+ "\nThis is the part that could not get parsed: " + restList);
		}
	}
}










