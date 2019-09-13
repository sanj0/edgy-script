package de.edgelord.edgyscript.e80.interpreter;

public interface Token {

    String getID();
    String getValue();
    void setValue(String newValue);

    static Token getToken(String id, String value) {
        return new DummyToken(id, value);
    }
}
