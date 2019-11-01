package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.List;

public class InlineToken extends Value {

    private static final Lexer lexer = new Lexer();

    public InlineToken(String value, ValueType valueType) {
        super(value, String.valueOf(value.hashCode()), valueType);
        String[] parts = value.split(":", 2);

        if (parts[0].equals("")) {
            setValueType(ValueType.NUMBER);
        } else {
            setValueType(Token.ValueType.getType(parts[0]));
        }
        setValue(parts[1]);
    }

    @Override
    public boolean isEqualSign() {
        return false;
    }

    @Override
    public String getValue() {

        List<Token> tokens = lexer.tokenize(value);
        return Interpreter.run(new ScriptLine(tokens)).getValue();
    }

    @Override
    public Value toValue() {
        return this;
    }
}
