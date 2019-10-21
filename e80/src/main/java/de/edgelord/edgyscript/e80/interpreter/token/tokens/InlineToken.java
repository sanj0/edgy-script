package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.Value;

public class InlineToken extends Value {

    private static final Lexer lexer = new Lexer();

    public InlineToken(String value, ValueType valueType) {
        super(value, String.valueOf(value.hashCode()), valueType);
    }

    @Override
    public String getValue() {
        return Interpreter.eval(lexer.tokenize(value)).getValue();
    }

    @Override
    public ValueType getType() {
        return ValueType.AUTO;
    }

    @Override
    public Value toValue() {
        return this;
    }
}
