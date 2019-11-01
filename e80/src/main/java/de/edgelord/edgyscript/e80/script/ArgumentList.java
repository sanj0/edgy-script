package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Value;

import java.util.ArrayList;
import java.util.List;

public class ArgumentList extends ArrayList<Value> {

    private String function;

    public ArgumentList(List<Value> args, String function) {
        super(args);
        this.function = function;
    }

    @Override
    public Value get(int index) {

        if (index >= size()) {
            throw new ScriptException("Function " + function + " requires at least " + (index + 1) + " arguments!");
        }
        return super.get(index);
    }
}
