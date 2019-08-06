package de.edgelord.edgyscript.e80.exceptions;

import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;

public class e80RuntimeException extends RuntimeException {
    public e80RuntimeException(String message, ScriptFile scriptFile) {
        super(message + " in file " + scriptFile.getPath() + " line " + Interpreter.lineNumber);
    }
}
