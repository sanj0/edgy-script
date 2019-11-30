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

        if (Interpreter.SCOPE.containsKey(getID())) {
            return Interpreter.SCOPE.get(getID()).toString();
        } else if (source.containsKey(getID())) {
            return source.get(getID()).toString();
        } else {
            return getID();
        }
    }

    @Override
    public void setValue(String newValue) {
        source.replace(getID(), newValue);
    }

    @Override
    public ValueType getValueType() {

        if (Interpreter.MEMORY.containsKey(getID()) || Interpreter.SCOPE.containsKey(getID())) {
            return super.getValueType();
        } else {
            return ValueType.NUMBER;
        }
    }
}
