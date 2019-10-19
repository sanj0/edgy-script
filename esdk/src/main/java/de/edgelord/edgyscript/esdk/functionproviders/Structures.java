package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.*;

import java.util.Objects;

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

            while (getBooleanFromArg(condition, scriptFile)) {
                line.runSublines(scriptFile);
            }
            return Variable.empty();
        }
        return null;
    }

    public static boolean getBooleanFromArg(Variable var, ScriptFile context) {
        if (var.getString().trim().equalsIgnoreCase("true")) {
            return true;
        } else if (var.getString().trim().equalsIgnoreCase("false")) {
            return false;
        } else {
            return Objects.requireNonNull(Interpreter.execLine(var.getString(), context, false)).getBoolean();
        }
    }

    private boolean doIf(Variable condition, ScriptLine line, ScriptFile scriptFile) {
        boolean returnVal = executeIf(getBooleanFromArg(condition, scriptFile), line, scriptFile);
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
