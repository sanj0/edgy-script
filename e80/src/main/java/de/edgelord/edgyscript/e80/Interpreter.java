package de.edgelord.edgyscript.e80;

import de.edgelord.edgyscript.e80.exceptions.FunctionNotFoundException;
import de.edgelord.edgyscript.e80.main.Main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private static FunctionProvider sdk;
    private static List<String> lines = new ArrayList<>();
    private static Map<String, Integer> gotos = new HashMap<>();
    private static boolean wentTo = false;
    static {
        try {
            sdk = Class.forName("de.edgelord.edgyscript.esdk.EdgySDK").asSubclass(FunctionProvider.class).newInstance();
        } catch (Exception e) {
            System.err.println("The esdk was not found. Either the given path does not point to it, or it isn't located in " + System.getProperty("user.home") + "/.edgy-script/bin/esdk/esdk.jar");
            e.printStackTrace();
        }
    }
    public static int lineNumber = 0;

    public static int interpretRun(ScriptFile script) throws FileNotFoundException {
        String[] lines = script.getLines();

        // add needed variables
        script.getVarPool().add(new Variable("+", "+")); // addition
        script.getVarPool().add(new Variable("-", "-")); // subtraction
        script.getVarPool().add(new Variable("*", "*")); // multiplication
        script.getVarPool().add(new Variable("**", "**")); // exponentiation
        script.getVarPool().add(new Variable("/", "/")); // division
        script.getVarPool().add(new Variable("%", "%")); // modulo

        // lex lines into lexedLines
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            lineNumber++;

            // ignore comments (lines starting with either "#" or "//")
            // also, ignore blank lines
            if (!line.startsWith("#") && !line.startsWith("//") && !line.isEmpty()) {
                execLine(line, script);
            }
        }

        return 0;
    }

    public static Variable execLine(String line, ScriptFile context) {

        Variable lastReturnVal = null;

        if (line.startsWith(":")) {
            gotos.put(line.replaceFirst(":", ""), lines.size());
            return Variable.empty();
        }

        if (line.startsWith("goto ")) {
            wentTo = true;
            int index = gotos.get(line.split(" ")[1]);

            for (int i = index; i < lines.size(); i++) {
                execLine(lines.get(i), context);
            }
            wentTo = false;
            return Variable.empty();
        }

        if (!wentTo) {
            lines.add(line);
        }
        if (line.toLowerCase().startsWith("simplestringmode")) {
            switch (line.split(" " )[1]) {
                case "true":
                    Main.simpleStringMode = true;
                    break;
                case "false":
                    Main.simpleStringMode = false;
                    break;
            }
            return Variable.empty();
        } else {
            String[] parts = line.split(" and ");
            String pipedVar = null;
            boolean addedTempVarToPool = false;

            for (int j = 0; j < parts.length; j++) {
                String part = parts[j];

                if (j > 0) {
                    if (part.contains("$")) {
                        part.replaceAll("[$]", pipedVar);
                    } else {
                        part += " , " + pipedVar;
                    }
                }

                ScriptLine scriptLine = Lexer.lexLine(part, context);
                Variable returnVal = sdk.function(scriptLine.getFunctionName(), scriptLine.getArgs(), context);
                lastReturnVal = returnVal;

                if (returnVal == null) {
                    throw new FunctionNotFoundException(scriptLine.getFunctionName(), context);
                }

                if (addedTempVarToPool) {
                    context.removeVar(pipedVar);
                    addedTempVarToPool = false;
                }

                pipedVar = returnVal.getName();
                if (!context.varExists(pipedVar)) {
                    context.getVarPool().add(returnVal);
                    addedTempVarToPool = true;
                }
            }
        }
        return lastReturnVal;
    }
}
