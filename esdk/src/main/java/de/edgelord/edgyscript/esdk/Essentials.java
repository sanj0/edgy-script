package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.*;
import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.List;
import java.util.stream.Collectors;

public class Essentials implements NativeProvider {

    private static final Lexer LEXER = new Lexer();

    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "ev":
            case "eval":
            case "evaluate":

                List<Token> tokens = LEXER.tokenize(args.get(0).getValue());
                List<Value> values = Tokenizer.evaluateTokens(tokens, false);

                String functionName = tokens.get(0).getValue();
                ScriptLine evalLine = new ScriptLine(tokens.get(0), new ArgumentList(values.subList(1, values.size()), functionName));

                if (evalLine.getArgs().size() == 0) {
                    if (evalLine.getFunctionName() instanceof DirectValue) {
                        if (Interpreter.MEMORY.containsKey(functionName)) {
                            return new DirectValue(Interpreter.MEMORY.get(functionName).toString());
                        }
                    } else {
                        return new DirectValue(functionName);
                    }
                }

                Value value = Interpreter.runFunction(functionName, new ArgumentList(evalLine.getArgs(), functionName), evalLine);

                if (value == null) {

                    StringBuilder lineBuilder = new StringBuilder();

                    for (Value val : values) {
                        lineBuilder.append(val.getValueForJS()).append(" ");
                    }
                    try {
                        return new DirectValue(Interpreter.jsEngine.eval(lineBuilder.toString()).toString());
                    } catch (javax.script.ScriptException e) {
                        e.printStackTrace();
                    }
                } else {
                    return value;
                }

            case "deletescopevar":
            case "purgescopevar":
            case "delscopevar":
            case "rmscopevar":
            case "removescopevar":
                String varName = args.get(0).getValue();
                Interpreter.SCOPE.remove(varName);
                return new DirectValue(varName);

            case "deletevar":
            case "purgevar":
            case "delvar":
            case "vardel":
            case "removevar":
            case "rmvar":
                if (!(args.get(0) instanceof LinkedValue)) {
                    throw new ScriptException(function + " expects a variable!");
                }
                String var = args.get(0).getID();
                Interpreter.MEMORY.remove(var);
                return new DirectValue(var);
        }
        return null;
    }
}
