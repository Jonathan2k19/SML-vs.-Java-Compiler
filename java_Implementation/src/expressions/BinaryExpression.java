/* ###############################################################
 * 	BINARY EXPRESSIONS: AND/IMP (leftExpression, rightExpression)
 * ###############################################################
 **/

package expressions;

import lexer.Token;
import lexer.Token.TokenType;

public class BinaryExpression extends Expression{
	private Token operator = null;	// AND or IMP
	private Expression left = null;	// expressionLeft
	private Expression right = null;// expressionRight
	
	
	// CONSTRUCTOR
	public BinaryExpression (Token operator, Expression left, Expression right) {
		if (operator == null || left == null || right == null)
			throw new IllegalArgumentException ("ERROR_BINARY_EXPRESSION: BinaryExpression takes 3 arguments, all of which are != null");
		if (operator.getTokenType() != Token.TokenType.AND && operator.getTokenType() != Token.TokenType.IMP)
			throw new IllegalArgumentException ("ERROR_BINARY_EXPRESSION: Operator of binary expression has to be AND or IMP");
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	

	// METHODS
	@Override
	public String toString() {
		return (this.operatorType().name() + " (" + this.left.toString() + this.right.toString() + ")");
	}

	
	@Override
	public boolean calculateValue() {
		if (this.operatorType() == Token.TokenType.AND) {
			return (this.left.calculateValue() && this.right.calculateValue());
		} else {
			// implication is always true if left expression is false, otherwise it's true if both expressions are true
			if (this.left.calculateValue() == false)
				return true;
			return (this.right.calculateValue());
		}
	}

	
	@Override
	public TokenType operatorType() {
		return this.operator.getTokenType();
	}

}
