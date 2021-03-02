/* #####################################################################
 * 	UNARY EXPRESSION: Neg (expression)
 * #####################################################################
 **/

package expressions;

import lexer.Token;
import lexer.Token.TokenType;

public class UnaryExpression extends Expression{
	private Token operator = null;
	private Expression expression = null;
	
	
	// CONSTRUCTOR
	public UnaryExpression (Token operator, Expression expression) {
		if (operator == null || expression == null)
			throw new IllegalArgumentException ("ERROR_UNARY_EXPRESSION: Operator and expression cannot be assigned to null");
		if (operator.getTokenType() != Token.TokenType.NEG)
			throw new IllegalArgumentException ("ERROR_UNARY_EXPRESSION: Operator has to be Neg");
		this.operator = operator;
		this.expression = expression;
	}
	

	// METHODS
	@Override
	public String toString() {
		return (this.operatorType().name() + " (" + this.expression.toString() + ")");
	}

	@Override
	public boolean calculateValue() {
		return (!this.expression.calculateValue());	// Negation is the only possible unary expression
	}

	@Override
	public TokenType operatorType() {
		return (this.operator.getTokenType());
	}

}
