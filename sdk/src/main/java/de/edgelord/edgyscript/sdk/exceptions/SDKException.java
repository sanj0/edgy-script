package de.edgelord.edgyscript.sdk.exceptions;

import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;

public class SDKException extends RuntimeException {
    public SDKException(String message, ScriptFile scriptFile) {
        super(scriptFile.getPath() + ":" + Interpreter.lineNumber + " : " + message);
    }
}
