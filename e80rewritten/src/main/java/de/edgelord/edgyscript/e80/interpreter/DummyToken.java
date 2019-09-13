package de.edgelord.edgyscript.e80.interpreter;

public class DummyToken implements Token {

    private String id;
    private String value;

    public DummyToken(String id, String value) {
        this.id = id;
        this.value = value;
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
}
