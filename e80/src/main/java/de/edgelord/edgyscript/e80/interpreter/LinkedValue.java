package de.edgelord.edgyscript.e80.interpreter;

import javax.script.Bindings;

public class LinkedValue extends Value {
    private Bindings source;

    public LinkedValue(String id, Bindings source, ValueType type) {
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
    public boolean isEqualSign() {
        return false;
    }

    @Override
    public boolean isBracket() {
        return false;
    }

    @Override
    public String getValue() {

        if (!Interpreter.MEMORY.containsKey(getID())) {
            return getID();
        } else {
            return source.get(getID()).toString();
        }
    }

    @Override
    public void setValue(String newValue) {
        source.replace(getID(), newValue);
    }

    @Override
    public ValueType getValueType() {

        if (Interpreter.MEMORY.containsKey(getID())) {
            return super.getValueType();
        } else {
            return ValueType.NUMBER;
        }
    }
}
