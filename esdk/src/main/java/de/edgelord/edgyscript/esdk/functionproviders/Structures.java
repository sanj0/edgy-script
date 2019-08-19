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
            Variable condition = variables[0];
            boolean returnVal;

            if (condition.getString().trim().equalsIgnoreCase("true")) {
                returnVal = executeIf(true, line, scriptFile);
            } else if (condition.getString().trim().equalsIgnoreCase("false") && variables.length >= 4) {
                returnVal = executeIf(false, line, scriptFile);
            } else {
                System.out.println("okay");
                returnVal = executeIf(Interpreter.execLine(variables[0].getString(), scriptFile, false).getBoolean(), line, scriptFile);
            }

            return new Variable(scriptFile.nextTempvar(), String.valueOf(returnVal));
        }

        if (name.equalsIgnoreCase("else")) {
            if (executeNextElse) {
                line.runSublines(scriptFile);
            }

            executeNextElse = !executeNextElse;
            return new Variable(scriptFile.nextTempvar(), String.valueOf(!executeNextElse));
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

    private boolean executeIf(boolean condition, ScriptLine line, ScriptFile context) {
        if (condition) {
            line.runSublines(context);
        } else {
            executeNextElse = true;
        }

        return condition;
    }
}
