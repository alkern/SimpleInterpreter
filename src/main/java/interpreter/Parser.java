package interpreter;

import java.text.ParseException;

import interpreter.ast.Ast;
import interpreter.ast.BinaryOperationNode;
import interpreter.ast.IntegerLeaf;
import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.EndOfFileToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.RoundClosingBracketToken;
import interpreter.tokens.RoundOpeningBracketToken;
import interpreter.tokens.Token;
import interpreter.tokens.TokenList;

public class Parser {

	private TokenList tokens;
	private Token currentToken;

	public Ast parse(TokenList tokens) throws ParseException {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		try {
			Ast result = expr();
			if (currentToken instanceof EndOfFileToken) {
				return result;
			}
			throw new Exception("Expected: EndOfFile Found: " + currentToken);
		} catch (Exception e) {
			throw new ParseException("Failure at position " + tokens.getPosition() + " " + e.getMessage(),
					tokens.getPosition());
		}
	}

	private boolean eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = tokens.getNextToken();
			return true;
		} else {
			return false;
		}
	}

	private Ast expr() throws ParseException {
		Ast result = term();
		while (isExprOperator(currentToken.getValue())) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			result = new BinaryOperationNode(result, op, term());
		}

		return result;
	}

	private boolean isExprOperator(String value) {
		return (value != null && (value.equals("+") || value.equals("-")));
	}

	private Ast term() throws ParseException {
		Ast result = factor();
		while (isTermOperator(currentToken.getValue())) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			result = new BinaryOperationNode(result, op, factor());
		}

		return result;
	}

	private boolean isTermOperator(String value) {
		return (value != null && (value.equals("*") || value.equals("/")));
	}

	private Ast factor() throws ParseException {
		Ast result;
		Token temp = currentToken;
		if (this.eat(IntegerToken.TOKEN_TYPE)) {
			result = new IntegerLeaf(Integer.parseInt(temp.getValue()));
			return result;
		} else if (this.eat(RoundOpeningBracketToken.TOKEN_TYPE)) {
			result = expr();
			if (!this.eat(RoundClosingBracketToken.TOKEN_TYPE)) {
				throw new ParseException(
						"Error at Position " + tokens.getPosition() + ". Missing round closing bracket!",
						tokens.getPosition());
			}
			return result;
		}
		throw new ParseException("Error at Position " + tokens.getPosition(), tokens.getPosition());
	}

}
