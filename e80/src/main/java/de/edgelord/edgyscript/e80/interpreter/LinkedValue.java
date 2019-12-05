package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.script.ScriptException;

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
            String val = Interpreter.SCOPE.get(getID()).toString();
            if (val == null) {
                throw new ScriptException("variable value is NULL");
            } else {
                return val;
            }
        } else if (source.containsKey(getID())) {
            String val = source.get(getID()).toString();
            if (val == null) {
                throw new ScriptException("variable value is NULL");
            } else {
                return val;
            }
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
