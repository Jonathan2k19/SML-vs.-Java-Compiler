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




//TODO: adjust toString() methods => looks ugly right know without any commas




package parser;

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
	/*
	 * INFO: I need these try catch blocks, otherwise it will throw an IndexOutOfBoundsException because parseNext list is empty -> return parsed stuff 
	 **/
	
	private Environment environment = null;
	
	// CONSTRUCTOR
	/**
	 *@param Environment => identifiers are assigned to boolean values e. g. env = [x:true, y:false]
	 */
	public Parser (Environment env) {
		this.environment = env;
	}
	
	// METHODS	
	public ParsedUnparsedPair arrowExp (List<Token> tokens) throws Exception {
		/*
		 * SML equivalent:
		 * arrowExp ts = (case (andExp ts) of (e, IMP :: tr) => let val (e', tr') = arrowExp tr in (Imp (e, e'), tr') end
    										| (e, tr) => (e,tr))
		 * */		
		ParsedUnparsedPair andParsed = new ParsedUnparsedPair(andExp (tokens).getParsed(), andExp (tokens).getParseNext());	// (e,tr)
		Expression left = andParsed.getParsed();	//e
		Expression right = null;
		try {
			if (andParsed.getParseNext().get(0).getTokenType() == Token.TokenType.IMP) {	// (e, IMP :: tr)
				// with IMP
				List<Token> withoutIMP = andParsed.getParseNext().subList(1, andParsed.getParseNext().size());
				ParsedUnparsedPair arrowParsed = arrowExp (withoutIMP);	// (e',tr')
				right = arrowParsed.getParsed();
				// return (Imp(e,e'),tr')
				ParsedUnparsedPair impExpWithRest = new ParsedUnparsedPair(new BinaryExpression(new Token (Token.TokenType.IMP), left, right), arrowParsed.getParseNext());
				return impExpWithRest;
			}else {
				// no IMP as next token -> return (e,tr)
				return andParsed;
			}
		} catch (IndexOutOfBoundsException e){
			return andParsed;
		}
	}
	
	
	public ParsedUnparsedPair andExp (List<Token> tokens) throws Exception {
		/*SML equivalent:
		 * andExp ts = andExpHelp (notExp ts)
		 **/
		ParsedUnparsedPair notParsed = new ParsedUnparsedPair(notExp(tokens).getParsed(),notExp(tokens).getParseNext());
		return andExpHelp(notParsed);		
	}
	
	
	public ParsedUnparsedPair andExpHelp (ParsedUnparsedPair inputPair) throws Exception {
		/*SML equivalent:
		 * andExpHelp (e, AND :: tr) = let val (e', tr') = notExp tr in andExpHelp (And (e,e'), tr') end
		 | andExpHelp (e,tr) = (e,tr)
		 **/
		try {
			if (inputPair.getParseNext().get(0).getTokenType() == Token.TokenType.AND) {	// (e, AND :: tr)
				List<Token> withoutAND = inputPair.getParseNext().subList(1, inputPair.getParseNext().size());
				ParsedUnparsedPair notParsed = notExp (withoutAND);	// (e',tr')
				// return andExpHelp (And(e,e'),tr')
				Expression left = inputPair.getParsed();	// e
				Expression right = notParsed.getParsed();	// e'
				Expression andExpression = new BinaryExpression (new Token (Token.TokenType.AND), left, right);	// And(e,e')
				return andExpHelp (new ParsedUnparsedPair(andExpression, notParsed.getParseNext()));
			}else {
				return inputPair;
			}
		} catch (IndexOutOfBoundsException e) {
			return inputPair;
		}
	}
	
	
	public ParsedUnparsedPair notExp (List<Token> tokens) throws Exception {
		/*SML equivalent:
		 * notExp (NEG :: tr) = let val (e, ts) = primitives tr in (Neg e, ts) end 
    	 | notExp ts = primitives ts
		 **/
		try {
			if(tokens.get(0).getTokenType() == Token.TokenType.NEG) {
				ParsedUnparsedPair primitivesParsed = primitives(tokens.subList(1, tokens.size()));	// (e, ts)
				Expression e = primitivesParsed.getParsed();	// e
				Expression negExpression = new UnaryExpression(new Token(Token.TokenType.NEG), e);
				ParsedUnparsedPair negExpWithRest = new ParsedUnparsedPair(negExpression, primitivesParsed.getParseNext());	// (Neg e, ts)
				return negExpWithRest;
			} else {
				return primitives(tokens);
			}
		} catch (IndexOutOfBoundsException e) {
			return primitives(tokens);
		}
	}
	
	
	public ParsedUnparsedPair primitives (List<Token> tokens) throws Exception {
		/*SML equivalence:
		 * primitives (CONSTANT (c) :: tr) = (case c of True => (Constant True, tr) | False => (Constant False, tr) | _ => raise Error "parser: unknown constant")
    	 | primitives (ID i :: tr) = (Id i, tr)
		 | primitives (LPAR :: tr) = (case (arrowExp tr) of (e, RPAR :: tr') => (e, tr') | _ => raise Error "parser: missing right parenthesis")
		 **/
		List<Token> withoutFirstToken = tokens.subList(1, tokens.size());												// tr
		Token firstToken = tokens.get(0);
		if (firstToken.getTokenType() == Token.TokenType.CONSTANT) {
			System.out.println("Parsed Constant");
			if (firstToken.getTokenValue().equals("true")) {
				Expression constantExp = new PrimitiveExpression(new Token (Token.TokenType.CONSTANT, "true"), null);		// (Constant:true)
				ParsedUnparsedPair constantExpWithRest = new ParsedUnparsedPair(constantExp, withoutFirstToken);			// (Constant:true, tr)
				return constantExpWithRest;
			} else if (firstToken.getTokenValue().equals("false")) {
				Expression constantExp = new PrimitiveExpression(new Token (Token.TokenType.CONSTANT, "false"), null);		// (Constant:false)
				ParsedUnparsedPair constantExpWithRest = new ParsedUnparsedPair(constantExp, withoutFirstToken);			// (Constant:false, tr)
				return constantExpWithRest;
			} else {
				throw new Exception ("ERROR_PARSING: constant has value \"true\" or \"false\"");
			}
		} else if (firstToken.getTokenType() == Token.TokenType.ID && firstToken.getTokenValue() != null){				// TODO: maybe test if tokenValue is not empty
			System.out.println("Parsed ID: " + firstToken);
			//Token idToken = new Token (Token.TokenType.ID, firstToken.getTokenValue());
			Expression idExp = new PrimitiveExpression(firstToken, this.environment);									// ID:i
			ParsedUnparsedPair idWithRest = new ParsedUnparsedPair(idExp, withoutFirstToken);							// (ID:i, tr)
			return idWithRest;
		} else if (firstToken.getTokenType() == Token.TokenType.LPAR){
			System.out.println("Parsed LPAR");
			try {	
				if (arrowExp(withoutFirstToken).getParseNext().get(0).getTokenType() == Token.TokenType.RPAR) {
					System.out.println("rpar: " + arrowExp(withoutFirstToken).getParseNext().get(0).getTokenType());
					List<Token> arrParseNext = arrowExp(withoutFirstToken).getParseNext();									// RPAR::tr'
					ParsedUnparsedPair arrowParsed = new ParsedUnparsedPair(arrowExp(withoutFirstToken).getParsed(), arrParseNext.subList(1, arrParseNext.size()));// (e, tr')
					return arrowParsed;
				}else {
					throw new Exception ("ERROR_PARSING: missing right parenthesis");
				}
			} catch (IndexOutOfBoundsException e) {
				throw new Exception ("ERROR_PARSING: missing right parenthesis"); 
			}
		} else {
			System.out.println("\nDEBUGGING:");
//			System.out.println(firstToken.getTokenValue());
			System.out.println("First token: " + firstToken + ", after that: " + withoutFirstToken);
			throw new Exception("ERROR_PARSING: unknown pattern");
		}
	}
	
	
	/**
	 *@return if successfull: parsed input gets returned, else: input that could not get parsed gets returned 
	 */
	public Expression parse (List<Token> tokens) throws Exception {
		/*SML equivalence:
		 * parse ts = case arrowExp ts of (e, nil) => e | _ => raise Error "something could not be parsed"
		 **/
		if (tokens == null)
			throw new IllegalArgumentException("ERROR_PARSING: no tokens to parse");
		Expression parsed = arrowExp (tokens).getParsed();			// e
		List<Token> restList = arrowExp (tokens).getParseNext();	// if null -> success, else rest of tokens that could not be parsed
	
		if (parsed != null && restList.isEmpty()) {
			System.out.println("\n\nSuccess. Parsed: " + parsed.toString());
			return parsed;
		}else {
			throw new Exception ("ERROR_PARSING: the token list could not be parsed entirely!\nThis is the parsed part: " + parsed.toString());
		}
	}
}










