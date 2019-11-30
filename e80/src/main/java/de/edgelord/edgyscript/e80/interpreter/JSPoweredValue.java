package de.edgelord.edgyscript.e80.interpreter;

import javax.script.ScriptException;
import java.util.List;

public class JSPoweredValue extends Value {

    private List<Value> values;

    public JSPoweredValue(List<Value> values) {
        super("", "js-powered-value");
        this.values = values;
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
        try {
            return Interpreter.jsEngine.eval(buildJS()).toString();
        } catch (ScriptException e) {
            throw new de.edgelord.edgyscript.e80.script.ScriptException(e.getMessage());
        }
    }

    private String buildJS() {
        StringBuilder command = new StringBuilder();
        for (Value value : values) {
            String v = value.getValueForJS();
            command.append(v.equals("") ? "\"\"" : v);
        }

        return command.toString();
    }
}
