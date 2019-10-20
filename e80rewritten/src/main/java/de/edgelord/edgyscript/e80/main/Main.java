package de.edgelord.edgyscript.e80.main;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.script.Script;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Map<String, String> mappedArgs = mapArgs(args);

        if (mappedArgs.containsKey("sdk")) {
            Interpreter.ESDK = Class.forName(mappedArgs.get("sdk")).asSubclass(NativeProvider.class).newInstance();
        }

        if (args.length >= 1) {
            Script script = new Script(args[0]);

            if (!script.getFile().getName().endsWith(".et")) {
                System.err.printf("WARNING: %s might not be an Edgy Script!\n", args[0]);
            }

            script.compile();
            script.run();
        }
    }

    private static Map<String, String> mapArgs(String[] args) {

        Map<String, String> mappedArgs = new HashMap<>();
        String key = "";
        boolean nextIsValue = false;
        boolean nextIsPath = true;

        for (String s : args) {

            if (nextIsPath) {
                nextIsPath = false;
                continue;
            }
            if (s.startsWith("-") && !nextIsValue) {
                key = s.replaceFirst("-", "");
                nextIsValue = true;
            } else {
                mappedArgs.put(key, s);
                nextIsValue = false;
            }
        }
        return mappedArgs;
    }
}
