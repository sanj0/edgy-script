package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Tokenizer;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores a classical script line, meaning a function call with corresponding args.
 */
public class ScriptLine implements Serializable {

    private Token function;
    private List<Value> args;
    private List<ScriptLine> subLines;
    private ScriptLine directParent = null;
    private int lineNumber = -1;

    public ScriptLine(Token function, List<Value> args, List<ScriptLine> subLines) {
        this.function = function;
        this.args = args;
        this.subLines = subLines;
    }

    public ScriptLine(Token function, ArgumentList args) {
        this(function, args, new ArrayList<>());
    }

    public ScriptLine(Token function, List<Value> args, List<ScriptLine> subLines, int lineNumber) {
        this(function, args, subLines);
        this.lineNumber = lineNumber;
    }

    public ScriptLine(List<Token> tokens) {
        Token functionName = tokens.get(0);

        if (tokens.size() == 1) {
            this.function = functionName;
            this.args = new LinkedList<>();
        } else {
            this.function = functionName;
            this.args = Tokenizer.evaluateTokens(tokens.subList(1, tokens.size()),
                    functionName.getValue().equalsIgnoreCase("use") || functionName.getValue().equalsIgnoreCase("import") || tokens.get(1).isEqualSign());
        }
        subLines = new ArrayList<>();
    }

    public ScriptLine (List<Token> tokens, int lineNumber) {
        this(tokens);
        this.lineNumber = lineNumber;
    }

    /**
     * Runs all {@link #subLines} using {@link Interpreter#run(ScriptLine)}.
     */
    public void runSubLines() {
        for (ScriptLine line : subLines) {
            Interpreter.run(line);
        }
    }

    public void addSubline(ScriptLine line) {
        subLines.add(line);
    }

    public ScriptLine getSubline(int index) {
        return subLines.get(index);
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

    /**
     * Gets {@link #subLines}.
     *
     * @return the value of {@link #subLines}
     */
    public List<ScriptLine> getSubLines() {
        return subLines;
    }

    /**
     * Sets {@link #subLines}.
     *
     * @param subLines the new value of {@link #subLines}
     */
    public void setSubLines(List<ScriptLine> subLines) {
        this.subLines = subLines;
    }

    /**
     * Gets {@link #lineNumber}.
     *
     * @return the value of {@link #lineNumber}
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets {@link #lineNumber}.
     *
     * @param lineNumber the new value of {@link #lineNumber}
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Gets {@link #directParent}.
     *
     * @return the value of {@link #directParent}
     */
    public ScriptLine getDirectParent() {
        return directParent;
    }

    /**
     * Sets {@link #directParent}.
     *
     * @param directParent the new value of {@link #directParent}
     */
    public void setDirectParent(ScriptLine directParent) {
        this.directParent = directParent;
    }
}
