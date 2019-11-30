package de.edgelord.edgyscript.e80.script;

import java.io.PrintStream;

public class ScriptException extends RuntimeException {

    public static int LINE_NUMBER = 0;
    public static String SCRIPT_PATH = "";

    public ScriptException(String message) {
        super("An error occurred in " + SCRIPT_PATH + ":" + LINE_NUMBER + " " + message.split("\\r?\\n")[0]);
    }

    @Override
    public void printStackTrace(PrintStream out) {
        out.println(getMessage());
    }
}
