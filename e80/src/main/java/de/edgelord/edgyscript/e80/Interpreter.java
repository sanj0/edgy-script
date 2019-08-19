package de.edgelord.edgyscript.e80;

import de.edgelord.edgyscript.e80.exceptions.FunctionNotFoundException;
import de.edgelord.edgyscript.e80.exceptions.e80RuntimeException;
import de.edgelord.edgyscript.e80.main.Main;

import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {

    private static FunctionProvider sdk;
    private static List<String> lines = new ArrayList<>();
    private static Map<String, Integer> gotos = new HashMap<>();

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
        //script.getVarPool().add(new Variable("**", "**")); // exponentiation
        script.getVarPool().add(new Variable("/", "/")); // division
        script.getVarPool().add(new Variable("%", "%")); // modulo

        // lex lines into lexedLines
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            lineNumber++;

            // ignore comments (lines starting with either "#" or "//")
            // also, ignore blank lines
            if (!line.startsWith("#") && !line.startsWith("//") && !line.isEmpty()) {
                line = prepareLine(line);

                ArrayList<String> sublines = new ArrayList<>();
                // if the line ends with a {, that means sublines are coming
                if (line.endsWith("{")) {
                    line = line.substring(0, line.length() - 1);
                    for (; !lines[++i].endsWith("}");) {
                        sublines.add(prepareLine(lines[i]));
                    }
                }
                execLine(line, script, true, sublines.toArray(new String[0]));
            }
        }

        return 0;
    }

    public static String prepareLine(String prePreparedLine) {
        prePreparedLine = prePreparedLine.trim();
        if (prePreparedLine.endsWith(";")) {
            prePreparedLine = prePreparedLine.substring(0, prePreparedLine.length() - 1);
        }

        return prePreparedLine;
    }

    public static Variable execLine(String line, ScriptFile context, boolean addToScript, String... sublines) {

        try {
            Variable lastReturnVal = null;

            if (line.startsWith(":")) {
                gotos.put(line.replaceFirst(":", ""), lines.size());
                return Variable.empty();
            }

            if (line.startsWith("goto ")) {
                int index = gotos.get(line.split(" ")[1]);

                for (int i = index; i < lines.size(); i++) {
                    execLine(lines.get(i), context, addToScript);
                }
                return Variable.empty();
            }


            if (addToScript) {
                lines.add(line);
            }

            if (line.toLowerCase().startsWith("simplestringmode")) {
                switch (line.split(" ")[1]) {
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
                    scriptLine.addSublines(sublines);
                    Variable returnVal = sdk.function(scriptLine, scriptLine.getFunctionName(), scriptLine.getArgs(), context);
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
        } catch (Exception e) {
            System.err.println(new e80RuntimeException("Error", context).getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
