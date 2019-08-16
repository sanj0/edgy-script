package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

/**
 * Provides functions:
 *
 * - join (alias merge)
 *     - joins all given variables to one and returns that
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
        return null;
    }
}
