package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Value;

import java.util.ArrayList;
import java.util.LinkedList;
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
            throw new ScriptException("Function " + function + " requires at least " + (index + 1) + " argument(s)!");
        }
        return super.get(index);
    }

    public List<String> toStringList() {
        List<String> list = new LinkedList<>();

        for (Value v : this) {
            list.add(v.getValue());
        }

        return list;
    }
}
