package de.edgelord.edgyscript.e80.exceptions;

import de.edgelord.edgyscript.e80.ScriptFile;

public class VarNotFoundException extends e80RuntimeException {

    public VarNotFoundException(String var, ScriptFile scriptFile) {
        super("Cannot find variable " + var, scriptFile);
    }
}
