package de.edgyscript.sdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

public class EdgySDK extends FunctionProvider {

    private static final Variables VARIABLES = new Variables();
    private static final StdIO STDIO = new StdIO();

    @Override
    public Variable function(String s, Variable[] variables, ScriptFile scriptFile) {
        Variable var;

        var = VARIABLES.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = STDIO.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        return null;
    }
}
