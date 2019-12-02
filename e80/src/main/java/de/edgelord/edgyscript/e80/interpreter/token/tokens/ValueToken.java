package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;

public class ValueToken extends Token {

    protected String value;
    protected String id;

    public ValueToken(String value, ValueType valueType) {
        super(valueType);
        this.value = value;
        this.id = String.valueOf(value.hashCode());
    }

    @Override
    public boolean isEqualSign() {
        return value.equals("=");
    }

    @Override
    public boolean isBracket() {
        return value.equals("(") || value.equals(")");
    }

    @Override
    public boolean isDelimiterSymbol() {
        return value.equals(",") || value.equals(";");
    }

    @Override
    public String getID() {
        return id;
    }

    protected void setID(String newID) {
        this.id = newID;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String newValue) {
        this.value = newValue;
    }

    @Override
    public Value toValue() {
        return new DirectValue(getValue(), getValueType());
    }
}
