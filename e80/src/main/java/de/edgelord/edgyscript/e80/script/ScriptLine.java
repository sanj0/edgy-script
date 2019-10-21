package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;

import java.io.Serializable;
import java.util.List;

/**
 * Stores a classical script line, meaning a function call with corresponding args.
 */
public class ScriptLine implements Serializable {

    private Token function;
    private List<Value> args;

    public ScriptLine(Token function, List<Value> args) {
        this.function = function;
        this.args = args;
    }

    /**
     * Gets {@link #function}.
     *
     * @return the value of {@link #function}
     */
    public Token getFunctionName() {
        return function;
    }

    /**
     * Sets {@link #function}.
     *
     * @param functionName the new value of {@link #function}
     */
    public void setFunctionName(Token functionName) {
        this.function = functionName;
    }

    /**
     * Gets {@link #args}.
     *
     * @return the value of {@link #args}
     */
    public List<Value> getArgs() {
        return args;
    }

    /**
     * Sets {@link #args}.
     *
     * @param args the new value of {@link #args}
     */
    public void setArgs(List<Value> args) {
        this.args = args;
    }
}
