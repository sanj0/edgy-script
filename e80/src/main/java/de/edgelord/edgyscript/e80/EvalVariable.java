package de.edgelord.edgyscript.e80;

public class EvalVariable extends Variable {

    private ScriptFile context;
    private String line;

    public EvalVariable(String name, String line, ScriptFile context) {
        super(name, null);
        this.context = context;
        this.line = line;

        reEval();
    }

    private void reEval() {
        setValue(Interpreter.execLine(line, context, false).getString());
    }

    @Override
    public boolean isNumber() {
        reEval();
        return super.isNumber();
    }

    @Override
    public boolean isBoolean() {
        reEval();
        return super.isBoolean();
    }

    @Override
    public boolean isMathOperator() {
        reEval();
        return super.isMathOperator();
    }

    @Override
    public String getValueForJS() {
        reEval();
        return super.getValueForJS();
    }

    @Override
    public float getFloat() {
        reEval();
        return super.getFloat();
    }

    @Override
    public float getNumber() {
        reEval();
        return super.getNumber();
    }

    @Override
    public int getInteger() {
        reEval();
        return super.getInteger();
    }

    @Override
    public String getString() {
        reEval();
        return super.getString();
    }

    @Override
    public boolean getBoolean() {
        reEval();
        return super.getBoolean();
    }
}
