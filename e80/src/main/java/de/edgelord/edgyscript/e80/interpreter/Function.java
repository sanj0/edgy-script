package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.ArrayList;
import java.util.List;

public class Function {

    private String name;
    private List<ScriptLine> content;
    private List<String> requiredArgs;

    public Function(String name, List<ScriptLine> content, List<String> requiredArgs) {
        this.content = content;
        this.requiredArgs = requiredArgs;
        this.name = name;
    }

    public Function(String name, List<ScriptLine> content) {
        this(name, content, new ArrayList<>());
    }

    public Value invoke(ArgumentList args) {

        Interpreter.INSIDE_FUNCTION = true;
        if (args.size() != requiredArgs.size()) {
            throw new ScriptException("function " + name + " expects " + requiredArgs.size() + " arguments but got " + args.size());
        }

        for (int i = 0; i < args.size(); i++) {
            Interpreter.SCOPE.put(requiredArgs.get(i), args.get(i).getValue());
        }

        for (ScriptLine line : content) {
            ScriptException.LINE_NUMBER = line.getLineNumber();
            Interpreter.run(line);

            if (Interpreter.NEXT_FUNCTION_RETURN_VAL != null) {
                Value returnVal = Interpreter.NEXT_FUNCTION_RETURN_VAL;
                Interpreter.NEXT_FUNCTION_RETURN_VAL = null;
                Interpreter.INSIDE_FUNCTION = false;
                return returnVal;
            }
        }

        Interpreter.INSIDE_FUNCTION = false;
        return new DirectValue("null");
    }

    /**
     * Gets {@link #name}.
     *
     * @return the value of {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets {@link #name}.
     *
     * @param name the new value of {@link #name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets {@link #content}.
     *
     * @return the value of {@link #content}
     */
    public List<ScriptLine> getContent() {
        return content;
    }

    /**
     * Sets {@link #content}.
     *
     * @param content the new value of {@link #content}
     */
    public void setContent(List<ScriptLine> content) {
        this.content = content;
    }

    /**
     * Gets {@link #requiredArgs}.
     *
     * @return the value of {@link #requiredArgs}
     */
    public List<String> getRequiredArgs() {
        return requiredArgs;
    }

    /**
     * Sets {@link #requiredArgs}.
     *
     * @param requiredArgs the new value of {@link #requiredArgs}
     */
    public void setRequiredArgs(List<String> requiredArgs) {
        this.requiredArgs = requiredArgs;
    }
}
