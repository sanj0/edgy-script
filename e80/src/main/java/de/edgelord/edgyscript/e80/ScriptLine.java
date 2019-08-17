package de.edgelord.edgyscript.e80;

public class ScriptLine {

    private String functionName;
    private Variable[] args;

    public ScriptLine(String functionName, Variable[] args) {
        this.functionName = functionName;
        this.args = args;
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
