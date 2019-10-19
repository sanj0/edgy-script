package de.edgelord.edgyscript.e80.main;

import de.edgelord.edgyscript.e80.script.Script;

import javax.script.ScriptException;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        if (args.length >= 1) {
            Script script = new Script(args[0]);

            if (!script.getFile().getName().endsWith(".et")) {
                System.err.printf("WARNING: %s might not be an Edgy Script!\n", args[0]);
            }

            script.compile();
            script.run();
        }
    }
}
