package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.ValueToken;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.*;

public class Interpreter {

    private static final List<String> KEYWORDS = new ArrayList<>();
    public static final List<String> SPECIAL_BUILTINS = new ArrayList<>();
    private static final List<String> OPERATORS = new ArrayList<>();
    public static final SimpleBindings MEMORY = new SimpleBindings();
    public static final SimpleBindings SCOPE = new SimpleBindings();
    public static final List<Function> FUNCTIONS = new ArrayList<>();

    public static boolean INSIDE_FUNCTION = false;
    public static Value NEXT_FUNCTION_RETURN_VAL = null;
    public static boolean INSIDE_LOOP = false;
    public static boolean CONTINUE_LOOP = false;
    public static boolean BREAK_LOOP  = false;

    public static final ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static final Lexer LEXER = new Lexer();

    private static Map<Integer, Boolean> IF_HISTORY = new HashMap<>();

    public static NativeProvider ESDK;

    static {
        try {
            ESDK = Class.forName("de.edgelord.edgyscript.esdk.ESDK").asSubclass(NativeProvider.class).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //jsEngine.setBindings(MEMORY, ScriptContext.ENGINE_SCOPE);

        KEYWORDS.add("var");
        KEYWORDS.add("(");
        KEYWORDS.add(")");
        KEYWORDS.add("{");
        KEYWORDS.add("}");
        KEYWORDS.add("if");
        KEYWORDS.add("else");
        KEYWORDS.add("ifelse");
        KEYWORDS.add("elif");
        KEYWORDS.add("while");
        KEYWORDS.add("for");
        KEYWORDS.add("use");
        SPECIAL_BUILTINS.add("use");
        KEYWORDS.add("import");
        SPECIAL_BUILTINS.add("import");

        KEYWORDS.add("def");
        SPECIAL_BUILTINS.add("def");
        KEYWORDS.add("define");
        SPECIAL_BUILTINS.add("define");
        KEYWORDS.add("func");
        SPECIAL_BUILTINS.add("func");
        KEYWORDS.add("function");
        SPECIAL_BUILTINS.add("function");

        OPERATORS.add("+");
        OPERATORS.add("-");
        OPERATORS.add("*");
        OPERATORS.add("/");
        OPERATORS.add("%");
        OPERATORS.add("=");
        OPERATORS.add("==");
        OPERATORS.add("!=");
        OPERATORS.add("!");
        OPERATORS.add("<");
        OPERATORS.add(">");
        OPERATORS.add(">=");
        OPERATORS.add(">=");
        OPERATORS.add("||");
        OPERATORS.add("|");
        OPERATORS.add("&&");
        OPERATORS.add("&");
    }

    public static Value run(ScriptLine line) {
        return eval(line);
    }

    public static Value run(String line, int lineNumber) {
        List<Token> tokens = LEXER.tokenize(line);
        return run(new ScriptLine(tokens, lineNumber));
    }

    public static Value eval(List<Token> tokens, boolean spaceSeparatorMode) {
        Token function = tokens.get(0);
        List<Token> args = tokens.subList(1, tokens.size());

        return eval(new ScriptLine(function, new ArgumentList(Tokenizer.evaluateTokens(args, spaceSeparatorMode), function.getValue())));
    }

    public static Value eval(ScriptLine line) {

        if (INSIDE_FUNCTION && NEXT_FUNCTION_RETURN_VAL != null) {
            return new DirectValue("null");
        }

        if (INSIDE_LOOP && (CONTINUE_LOOP || BREAK_LOOP)) {
            return new DirectValue("null");
        }

        Token function = line.getFunctionName();
        String functionName = function.getValue();
        ArgumentList args = new ArgumentList(line.getArgs(), functionName);

        if (INSIDE_FUNCTION && functionName.equalsIgnoreCase("return")) {

            if (args.size() > 1) {
                throw new ScriptException("returns statements expects a maximum of one argument but got " + args.size());
            } else if (args.size() == 0) {
                NEXT_FUNCTION_RETURN_VAL = new DirectValue("null");
            } else {
                NEXT_FUNCTION_RETURN_VAL = args.get(0);
            }

            if (INSIDE_LOOP) {
                BREAK_LOOP = true;
            }
            return new DirectValue("null");
        }

        if (INSIDE_LOOP) {
            if (functionName.equalsIgnoreCase("break")) {
                if (args.size() != 0) {
                    throw new ScriptException("break statements expects no arguments but got " + args.size());
                }
                BREAK_LOOP = true;
                return new DirectValue("null");
            } else if (functionName.equalsIgnoreCase("continue")) {
                if (args.size() != 0) {
                    throw new ScriptException("continue statements expects no arguments but got " + args.size());
                }
                CONTINUE_LOOP = true;
                return new DirectValue("null");
            }
        }

        if (isKeyWord(functionName.toLowerCase())) {
            switch (functionName.toLowerCase()) {
                case "var":
                    String varName = args.get(0).getValue();
                    String varValue = "";

                    if (args.size() > 1 && args.get(1).isEqualSign()) {
                        varValue = getPartialValue(args, 2, args.size()).getValue();
                    }

                    MEMORY.put(varName, varValue);
                    return new LinkedValue(varName);

                case "if":
                    boolean condition = args.get(0).getBoolean();

                    if (condition) {
                        line.runSubLines();
                    }

                    IF_HISTORY.put(line.getIndentionLevel(), condition);
                    return new DirectValue(String.valueOf(condition));

                case "else":
                    boolean cnd = !IF_HISTORY.getOrDefault(line.getIndentionLevel(), true);
                    if (cnd) {
                        line.runSubLines();
                    }
                    return new DirectValue(String.valueOf(cnd));

                case "elif":
                case "elseif":
                    boolean cndForElse = !IF_HISTORY.getOrDefault(line.getIndentionLevel(), true);
                    boolean elseifExecuted = false;
                    if (cndForElse && args.get(0).getBoolean()) {
                        elseifExecuted = true;
                        line.runSubLines();
                    }
                    return new DirectValue(String.valueOf(elseifExecuted));

                case "while":
                    INSIDE_LOOP = true;
                    while (args.get(0).getBoolean()) {
                        line.runSubLines();
                        if (BREAK_LOOP) {
                            break;
                        }
                        CONTINUE_LOOP = false;
                    }
                    CONTINUE_LOOP = false;
                    BREAK_LOOP = false;
                    INSIDE_LOOP = false;
                    return new DirectValue(String.valueOf(true));

                default:
                    return new DirectValue("null");
            }
        } else if (args.size() == 0) {
            return runFunction(functionName, args);
        } else if (args.get(0).isEqualSign()) {
            Value newVal = getPartialValue(args, 1, args.size());
            String varName = function.getID();

            MEMORY.put(varName, newVal.getValue());
            return new DirectValue(newVal.getValue());
        } else {
            return runFunction(functionName, args);
        }
    }

    public static Value runFunction(String functionName, ArgumentList args) {

        Function fn = getFunction(functionName);
        if (fn != null) {
            return fn.invoke(args);
        }
        Value returnVal = ESDK.function(functionName, args);
        if (returnVal == null) {
            throw new ScriptException("unknown function " + functionName);
        } else {
            return returnVal;
        }
    }

    /**
     * Returns the function with the given name or null.
     *
     * @param name the name
     * @return teh function with the given name or null
     */
    private static Function getFunction(String name) {
        for (Function f : FUNCTIONS) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }

        return null;
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
