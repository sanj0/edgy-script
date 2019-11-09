package de.edgelord.edgyscript.e80.interpreter;

public class DirectValue extends Value {

    public DirectValue(String value) {
        this(value, ValueType.AUTO);
    }

    public DirectValue(String value, ValueType valueType) {
        super(value, String.valueOf(value.hashCode()), valueType);
    }

    @Override
    public boolean isEqualSign() {
        return getValue().equals("=");
    }

    @Override
    public boolean isBracket() {
        return getValue().equals("(") || getValue().equals(")");
    }
}
