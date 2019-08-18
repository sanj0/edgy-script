package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.*;

import javax.script.ScriptException;
import java.util.function.BooleanSupplier;

/**
 * Provides functions:
 *
 * - if
 *     - if the first arg is true, the second arg will be executed as a separate line. If it is false and the third arg is "else", the
 *       forth is executed as a separate line. returns the first arg
 *
 * Example usage:
 *
 * # with direct boolean
 * createset condition true
 *
 * if condition "writeLine \"It is true :'D\"" else "writeLine \"It is false :(\""
 *
 * # with evaluated js condition
 *
 * if
 */
public class Structures extends FunctionProvider {
    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("if")) {
            Variable condition = variables[0];
            boolean returnVal;

            if (condition.getString().equalsIgnoreCase("true")) {
                returnVal = executeIf(true, variables, scriptFile);
            } else if (condition.getString().equalsIgnoreCase("false") && variables.length >= 4) {
                returnVal = executeIf(false, variables, scriptFile);
            } else {
                returnVal = executeIf(Interpreter.execLine(variables[0].getString(), scriptFile, false).getBoolean(), variables, scriptFile);
            }

            return new Variable(scriptFile.nextTempvar(), String.valueOf(returnVal));
        }

        if (name.equalsIgnoreCase("while")) {
            Variable condition = variables[0];
            EvalVariable evalCondition = null;

            if (condition instanceof EvalVariable) {
                evalCondition = (EvalVariable) condition;
            }
            while (condition.getString().equalsIgnoreCase("true")) {
                if (evalCondition != null) {
                    evalCondition.reEval();
                }

                Interpreter.execLine(variables[1].getString(), scriptFile, false);
            }
        }
        return null;
    }

    private boolean executeIf(boolean condition, Variable[] args, ScriptFile context) {
        if (condition) {
            Interpreter.execLine(args[1].getString(), context, false);
        } else if (args.length > 2) {
            String line;
            if (args[2].getString().equalsIgnoreCase("else")) {
                line = args[3].getString();
            } else {
                line = args[2].getString();
            }
            Interpreter.execLine(line, context, false);
        }

        return condition;
    }
}
