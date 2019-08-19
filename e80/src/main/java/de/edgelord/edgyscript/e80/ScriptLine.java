package de.edgelord.edgyscript.e80;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptLine {

    private String functionName;
    private Variable[] args;

    private List<String> sublines = new ArrayList<>();

    public ScriptLine(String functionName, Variable[] args) {
        this.functionName = functionName;
        this.args = args;
    }

    public void addSubline(String line) {
        sublines.add(line);
    }

    public void addSublines(String[] line) {
        sublines.addAll(Arrays.asList(line));
    }

    public void runSublines(ScriptFile context) {
        for (String line : sublines) {
            Interpreter.execLine(line, context, false);
        }
    }

    public void refresh() {
        for (Variable var : args) {
            if (var instanceof EvalVariable) {
                EvalVariable evalVariable = (EvalVariable) var;
                evalVariable.reEval();
            }
        }
    }

    /**
     * Gets {@link #functionName}.
     *
     * @return the value of {@link #functionName}
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Gets {@link #args}.
     *
     * @return the value of {@link #args}
     */
    public Variable[] getArgs() {
        return args;
    }
}
