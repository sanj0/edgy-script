package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
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
 * -equalsIgnoreCase (alias equalIgnoreCase)
 *     - reutns true if the first given var is equal to the second given, ignoring case. returns false if not.
 */
public class StringUtils extends FunctionProvider {
    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

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
        return null;
    }
}
