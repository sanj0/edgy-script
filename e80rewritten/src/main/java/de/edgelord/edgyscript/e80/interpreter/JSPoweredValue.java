package de.edgelord.edgyscript.e80.interpreter;

import javax.script.ScriptException;

public class JSPoweredValue extends Value {

    public JSPoweredValue(String value) {
        super(value, String.valueOf(value.hashCode()));
    }

    @Override
    public String getValue() {
        try {
            return Interpreter.jsEngine.eval(value).toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return null;
    }
}
