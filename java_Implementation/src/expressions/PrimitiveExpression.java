/* ##############################################################################
 * 	PRIMITIVE EXPRESSIONS:
 * 		-> CONSTANT (true|false)
 * 		-> ID (nameOfIdentifier)
 * ##############################################################################
 **/

package expressions;

import lexer.Token;
import lexer.Token.TokenType;

public class PrimitiveExpression extends Expression{
	private Token operator = null;					// CONSTANT (true/false) or ID (name)
	private Environment env = new Environment();	// stores value of ID (name)
	
	
	// CONSTRUCTOR
	/**
	 *@param operator (id or constant) and environment (if operator is id) 
	 */
	public PrimitiveExpression (Token operator, Environment env) {
		if (operator == null)
			throw new IllegalArgumentException ("ERROR_PRIMITIVE_EXPRESSION: operator cannot be null");
		if (operator.getTokenType() != Token.TokenType.CONSTANT || operator.getTokenType() != Token.TokenType.ID)
			throw new IllegalArgumentException ("ERROR_PRIMITIVE_EXPRESSION: primitive operator has to be CONSTANT or ID");
		this.operator = operator;
		if (this.operatorType() == Token.TokenType.ID){
			if (env == null) {
				throw new IllegalArgumentException ("ERROR_PRIMITIVE_EXPRESSION: if operator is ID then you have to pass environment");
			} else {
				this.env = env;
			}
		}
	}
	
	
	// METHODS
	@Override
	public String toString() {
		// id and constant have values != null
		return (this.operatorType().name() + ":" + this.operator.getTokenValue());
	}

	@Override
	public boolean calculateValue() {
		if (this.operatorType() == Token.TokenType.CONSTANT) {
			if (this.operator.getTokenValue() == "true") {
				return true;
			} else {
				return false;
			}
		} else {
			// Identifier
			return (this.env.lookupId(this.operator.getTokenValue()));
		}
	}

	@Override
	public TokenType operatorType() {
		return (this.operator.getTokenType());
	}

}
