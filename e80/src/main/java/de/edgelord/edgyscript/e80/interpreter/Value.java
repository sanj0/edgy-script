package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.interpreter.token.Token;

public abstract class Value extends Token {

    protected String value;
    protected String id;

    public Value(String value, String id, ValueType valueType) {
        super(valueType);
        this.value = value;
        this.id = id;
    }

    public Value(String value, String id) {
        this(value, id, ValueType.AUTO);
    }

    @Override
    public String getID() {
        return id;
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
        return this;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
