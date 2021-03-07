//TODO: adjust toString() methods => looks ugly right know without any commas


package expressions;

import lexer.Token;

public abstract class Expression {
	/**
	 *@return string to represent the expression:
	 *	And (exp1, exp2)
	 *	Imp (exp1, exp2)
	 *	Neg (exp)
	 *	Constant:true|false
	 *	Id:name 
	 */
	@Override 
	public abstract String toString();
	

	/**
	 *@return the value represented by the expression
	 */
	public abstract boolean calculateValue();
	
	
	/**
	 *@return the TokenType of the operator 
	 */
	public abstract Token.TokenType operatorType();
		
	
	/**
	 *@return true if the types are correct (always has to be boolean => check if true|false), false otherwise 
	 */
	public abstract boolean checkType();
}
