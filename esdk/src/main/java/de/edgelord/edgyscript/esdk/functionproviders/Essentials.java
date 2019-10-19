package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.ScriptLine;
import de.edgelord.edgyscript.e80.Variable;

/**
 * Provides functions:
 *
 * - touch
 *     returns the first given variable
 */
public class Essentials extends FunctionProvider {
    @Override
    public Variable function(ScriptLine scriptLine, String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("touch")) {
            return variables[0];
        }

        return null;
    }
}
