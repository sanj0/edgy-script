package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.token.Token;

public class ValueToken implements Token {

    private String value;
    private String id;

    public ValueToken(String value) {
        this.value = value;
        id = String.valueOf(value.hashCode());
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
}
