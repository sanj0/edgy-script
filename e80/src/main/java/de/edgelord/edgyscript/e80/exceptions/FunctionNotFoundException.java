package de.edgelord.edgyscript.e80.exceptions;

import de.edgelord.edgyscript.e80.ScriptFile;

public class FunctionNotFoundException extends e80RuntimeException {

    public FunctionNotFoundException(String functionName, ScriptFile scriptFile) {
        super("Cannot find function " + functionName, scriptFile);
    }
}
