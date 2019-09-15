package de.edgelord.edgyscript.e80.interpreter.token.tokens;

import de.edgelord.edgyscript.e80.interpreter.token.Token;

import java.util.Map;

public class LinkedToken implements Token {

    private String id;
    private Map<String, String> source;

    public LinkedToken(String id, Map<String, String> source) {
        this.id = id;
        this.source = source;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getValue() {
        return source.get(id);
    }

    @Override
    public void setValue(String newValue) {
        source.replace(id, newValue);
    }
}
