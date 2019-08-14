package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;
import de.edgelord.edgyscript.esdk.functionproviders.*;
import de.edgelord.edgyscript.esdk.functionproviders.Math;

public class EdgySDK extends FunctionProvider {

    private static final Variables VARIABLES = new Variables();
    private static final StdIO STDIO = new StdIO();
    private static final NativeExec NATIVE_EXEC = new NativeExec();
    private static final Math MATH = new Math();
    private static final SystemProvider SYSTEM_PROVIDER = new SystemProvider();
    private static final Arrays ARRAYS = new Arrays();
    private static final Structures STRUCTURES = new Structures();

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

        var = MATH.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = NATIVE_EXEC.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = SYSTEM_PROVIDER.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = ARRAYS.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = STRUCTURES.function(s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        return null;
    }
}
