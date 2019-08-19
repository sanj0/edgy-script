package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.ScriptLine;
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
    private static final StringUtils STRING_UTILS = new StringUtils();

    @Override
    public Variable function(ScriptLine line, String s, Variable[] variables, ScriptFile scriptFile) {
        Variable var;

        var = VARIABLES.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = STDIO.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = MATH.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = NATIVE_EXEC.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = SYSTEM_PROVIDER.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = ARRAYS.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = STRUCTURES.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        var = STRING_UTILS.function(line, s, variables, scriptFile);
        if (var != null) {
            return var;
        }

        return null;
    }
}
