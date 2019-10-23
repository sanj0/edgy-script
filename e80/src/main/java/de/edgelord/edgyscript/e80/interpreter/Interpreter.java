package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.ValueToken;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.*;

public class Interpreter {

    private static final List<String> KEYWORDS = new ArrayList<>();
    private static final List<String> OPERATORS = new ArrayList<>();
    public static final SimpleBindings MEMORY = new SimpleBindings();

    public static final ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    public static NativeProvider ESDK;

    static {
        try {
            ESDK = Class.forName("de.edgelord.edgyscript.esdk.ESDK").asSubclass(NativeProvider.class).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        jsEngine.setBindings(MEMORY, ScriptContext.ENGINE_SCOPE);

        KEYWORDS.add("and");
        KEYWORDS.add("var");
        KEYWORDS.add("{");
        KEYWORDS.add("}");
        KEYWORDS.add("(");
        KEYWORDS.add(")");
        KEYWORDS.add("if");
        KEYWORDS.add("else");
        KEYWORDS.add("while");
        KEYWORDS.add("for");
        KEYWORDS.add("goto");
        KEYWORDS.add("label");

        OPERATORS.add("+");
        OPERATORS.add("-");
        OPERATORS.add("*");
        OPERATORS.add("/");
        OPERATORS.add("%");
        OPERATORS.add("=");
        OPERATORS.add("==");
        OPERATORS.add("!=");
        OPERATORS.add("<");
        OPERATORS.add(">");
        OPERATORS.add(">=");
        OPERATORS.add(">=");
        OPERATORS.add("||");
        OPERATORS.add("&&");
    }

    public static Value run(ScriptLine line) {
        return eval(line.getFunctionName(), line.getArgs());
    }

    public static Value eval(List<Token> tokens, boolean... spaceSeparatorMode) {
        Token function = tokens.get(0);
        List<Token> args = tokens.subList(1, tokens.size());

        return eval(function, Tokenizer.evaluateTokens(args, spaceSeparatorMode));
    }

    public static Value eval(Token function, List<Value> args) {

        String functionName = function.getValue();

        if (args.size() == 0) {
            return runFunction(functionName, args);
        } else if (isKeyWord(functionName.toLowerCase())) {

            switch (functionName.toLowerCase()) {
                case "var":
                    String varName = args.get(0).getValue();
                    String varValue = "";

                    if (args.size() > 1 && args.get(1).getValue().equals("=")) {
                        varValue = getPartialValue(args, 2, args.size()).getValue();
                    }

                    MEMORY.put(varName, varValue);
                    return new LinkedValue(varName);
            }
            return null;
        } else if (args.get(0).getValue().equalsIgnoreCase("=")) {
            Value newVal = getPartialValue(args, 1, args.size());
            String varName = function.getID();

            MEMORY.replace(varName, newVal.getValue());
            return new DirectValue(newVal.getValue());
        } else {
            return runFunction(functionName, args);
        }
    }

    private static Value runFunction(String functionName, List<Value> args) {
        Value returnVal = ESDK.function(functionName, args);
        if (returnVal == null) {
            throw new RuntimeException("Function " + functionName + " does not exist!");
        } else {
            return returnVal;
        }
    }

    public static Value getPartialValue(List<Value> values, int startIndex, int endIndex) {

        List<Value> sublist = values.subList(startIndex, endIndex);
        List<Token> tokens = new ArrayList<>();

        for (Value v : sublist) {
            tokens.add(new ValueToken(v.getValue(), v.getValueType()));
        }

        return Tokenizer.evaluateSingle(tokens);
    }

    public static boolean isKeyWord(String s) {
        return KEYWORDS.contains(s);
    }

    public static boolean isOperator(String s) {
        return OPERATORS.contains(s);
    }

    public static boolean isSplitChar(String s) {
        if (s.length() > 1) {
            return false;
        } else {
            return isSplitChar(s.charAt(0));
        }
    }
    public static boolean isSplitChar(char c) {
        return c == ',';
    }
}
