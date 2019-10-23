package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;

public class InlineToken extends Value {

    private static final Lexer lexer = new Lexer();

    public InlineToken(String value, ValueType valueType) {
        super(value, String.valueOf(value.hashCode()), valueType);
        String[] parts = value.split(":", 2);
        setValueType(Token.ValueType.getType(parts[0]));
        setValue(parts[1]);
    }

    @Override
    public String getValue() {
        return Interpreter.eval(lexer.tokenize(value)).getValue();
    }

    @Override
    public Value toValue() {
        return this;
    }
}
