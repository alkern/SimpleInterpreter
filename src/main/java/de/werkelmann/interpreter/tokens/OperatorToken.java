package de.werkelmann.interpreter.tokens;

public class OperatorToken extends Token {

	public static final String TOKEN_TYPE = "Operator";

	public OperatorToken(Character value) {
		this(String.valueOf(value));
	}

	public OperatorToken(String value) {
		super(value);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
