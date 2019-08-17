package de.edgelord.edgyscript.e80;

public class EvalVariable extends Variable {

    private ScriptFile context;
    private String line;

    public EvalVariable(String name, String line, ScriptFile context) {
        super(name, Interpreter.execLine(line, context, false).getString());
        this.context = context;
        this.line = line;
    }

    public void reEval() {
        setValue(Interpreter.execLine(line, context, false).getString());
    }
}
