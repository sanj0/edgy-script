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

        String[] sublineArray = sublines.toArray(new String[0]);
        for (int i = 0; i < sublines.size(); i++) {
            String line = sublines.get(i);

            String[] newSubLines = new String[0];
            if (line.trim().endsWith("{")) {
                line = line.substring(0, line.length() - 1);
                i++;
                newSubLines = Interpreter.getSubLines(sublineArray, i);
                i += Interpreter.lastSubLineCount - 1;
            }
            Interpreter.execLine(line, context, false, newSubLines);
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
