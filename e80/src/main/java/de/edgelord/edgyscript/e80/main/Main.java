package de.edgelord.edgyscript.e80.main;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.Script;
import de.edgelord.edgyscript.e80.script.ScriptException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Map<String, String> mappedArgs;

        if (args.length < 1 || args[0].startsWith("-")) {
            mappedArgs = mapArgs(args, false);
            processArgs(mappedArgs);
            launchInteractiveMode();
        } else {
            mappedArgs = mapArgs(args, true);
            processArgs(mappedArgs);
            launchScriptMode(args[0]);
        }
    }

    private static void launchScriptMode(String path) {
        Script script = new Script(path);

        if (!script.getFile().getName().endsWith(".et")) {
            System.err.printf("WARNING: %s might not be an Edgy Script!\n", path);
        }

        try {
            script.compile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        script.run();
    }

    private static void launchInteractiveMode() {
        ScriptException.SCRIPT_PATH = "INTERACTIVE_MODE";
        System.out.println("Launching Edgy Script Interactive Mode. Type \"exit\" to quit.");
        Scanner inputScanner = new Scanner(System.in);
        String currentLine;

        while (true) {
            System.out.println(">>> ");
            currentLine = inputScanner.nextLine();

            if (currentLine.equalsIgnoreCase("exit")) {
                break;
            }

            Value returnVal = Interpreter.run(currentLine, 0);
            System.out.println("\nreturned: " + returnVal.getValue());
        }
    }

    private static void processArgs(Map<String, String> args) {
        if (args.containsKey("sdk")) {
            try {
                Interpreter.ESDK = Class.forName(args.get("sdk")).asSubclass(NativeProvider.class).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, String> mapArgs(String[] args, boolean pathIncluded) {

        Map<String, String> mappedArgs = new HashMap<>();
        String key = "";
        boolean nextIsValue = false;
        boolean nextIsPath = pathIncluded;

        for (String s : args) {

            if (nextIsPath) {
                nextIsPath = false;
                continue;
            }
            if (s.startsWith("-")) {
                key = s.replaceFirst("-", "");
                nextIsValue = true;
            } else if (nextIsValue){
                mappedArgs.put(key, s);
                nextIsValue = false;
            }
        }

        if (nextIsValue) {
            mappedArgs.put(key, "");
        }
        return mappedArgs;
    }
}
