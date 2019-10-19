package de.edgelord.edgyscript.e80.interpreter;

import java.util.Map;

public class LinkedValue extends Value {
    private Map<String, String> source;

    public LinkedValue(String id, Map<String, String> source, ValueType type) {
        super("", id, type);
        this.source = source;
    }

    public LinkedValue(String id, ValueType type) {
        this(id, Interpreter.MEMORY, type);
    }

    public LinkedValue(String id) {
        this(id, Interpreter.MEMORY, ValueType.AUTO);
    }

    @Override
    public String getValue() {

        if (!Interpreter.MEMORY.containsKey(getID())) {
            return getID();
        } else {
            return source.get(getID());
        }
    }

    @Override
    public void setValue(String newValue) {
        source.replace(getID(), newValue);
    }

    @Override
    public ValueType getType() {

        if (Interpreter.MEMORY.containsKey(getID())) {
            return super.getType();
        } else {
            return ValueType.NUMBER;
        }
    }
}
