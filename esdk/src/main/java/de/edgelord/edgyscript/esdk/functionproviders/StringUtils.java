package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.ScriptLine;
import de.edgelord.edgyscript.e80.Variable;

/**
 * Provides functions:
 *
 * - join (alias merge)
 *     - joins all given variables to one and returns that
 *
 * - equals (alias equal)
 *     - returns true if the first given var is equal to the second given, returns false if not
 *
 * - equalsIgnoreCase (alias equalIgnoreCase)
 *     - returns true if the first given var is equal to the second given, ignoring case. returns false if not.
 *
 * - substring
 *     - returns a substring of the first given variable beginning at the second given variable and ending at the third given variable
 *
 * - length (alias len)
 *     returns the length of the first given variable's value (floating point (.) in numbers also count +1)
 *
 * - removeChar (alias removeCharAt)
 *     removes from the first given variable the character at the given index (starting at 0) and returns it
 */
public class StringUtils extends FunctionProvider {
    @Override
    public Variable function(ScriptLine line, String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("join") || name.equalsIgnoreCase("merge")) {
            StringBuilder result = new StringBuilder();
            for (Variable variable : variables) {
                result.append(variable.getString());
            }

            return new Variable(scriptFile.nextTempvar(), result.toString());
        }

        if (name.equalsIgnoreCase("equals") || name.equalsIgnoreCase("equal")) {
            if (variables[0].getString().equals(variables[1].getString())) {
                return new Variable(scriptFile.nextTempvar(), "true");
            } else {
                return new Variable(scriptFile.nextTempvar(), "false");
            }
        }

        if (name.equalsIgnoreCase("equalsIgnoreCase") || name.equalsIgnoreCase("equalIgnoreCase")) {
            if (variables[0].getString().equalsIgnoreCase(variables[1].getString())) {
                return new Variable(scriptFile.nextTempvar(), "true");
            } else {
                return new Variable(scriptFile.nextTempvar(), "false");
            }
        }

        if (name.equalsIgnoreCase("substring")) {
            return new Variable(scriptFile.nextTempvar(), variables[0].getString().substring(variables[1].getInteger(), variables[2].getInteger()));
        }

        if (name.equalsIgnoreCase("length") || name.equalsIgnoreCase("len")) {
            return new Variable(scriptFile.nextTempvar(), variables[0].getString());
        }

        if (name.equalsIgnoreCase("removeChar") || name.equalsIgnoreCase("removeCharAt")) {
            StringBuilder var = new StringBuilder(variables[0].getString());
            var.deleteCharAt(variables[1].getInteger());

            variables[0].setValue(var.toString());
            return new Variable(scriptFile.nextTempvar(), var.toString());
        }
        return null;
    }
}
