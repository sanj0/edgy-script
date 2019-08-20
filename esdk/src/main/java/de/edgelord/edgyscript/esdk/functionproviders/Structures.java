package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.*;

/**
 * Provides functions:
 *
 * - if
 *     - if the given boolean is true, executes all given sublines (inside { })
 * - else
 *     - if the last if-call was negative, executes all given sublines
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

    private boolean executeNextElse = false;
    @Override
    public Variable function(ScriptLine line, String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("if")) {
            return new Variable(scriptFile.nextTempvar(), String.valueOf(doIf(variables[0], line, scriptFile)));
        }

        if (name.equalsIgnoreCase("else")) {

            if (variables.length > 1) {
                if (variables[0].getString().equalsIgnoreCase("if")) {
                    if (executeNextElse) {
                        doIf(variables[1], line, scriptFile);
                    }
                    executeNextElse = false;
                }
            } else if (executeNextElse) {
                line.runSublines(scriptFile);
                executeNextElse = false;
                return new Variable(scriptFile.nextTempvar(), "true");
            }

            return new Variable(scriptFile.nextTempvar(), "false");
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

                line.runSublines(scriptFile);
            }
        }
        return null;
    }

    private boolean doIf(Variable condition, ScriptLine line, ScriptFile scriptFile) {
        boolean returnVal;

        if (condition.getString().trim().equalsIgnoreCase("true")) {
            returnVal = executeIf(true, line, scriptFile);
        } else if (condition.getString().trim().equalsIgnoreCase("false")) {
            returnVal = executeIf(false, line, scriptFile);
        } else {
            returnVal = executeIf(Interpreter.execLine(condition.getString(), scriptFile, false).getBoolean(), line, scriptFile);
        }

        executeNextElse = !returnVal;
        return returnVal;
    }

    private boolean executeIf(boolean condition, ScriptLine line, ScriptFile context) {
        if (condition) {
            line.runSublines(context);
        }

        return condition;
    }
}
