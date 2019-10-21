package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;

public class InlineToken extends ValueToken {

    private static final Lexer lexer = new Lexer();

    public InlineToken(String value, ValueType valueType) {
        super(value, valueType);
    }

    @Override
    public String getValue() {
        return Interpreter.eval(lexer.tokenize(value)).getValue();
    }
}
