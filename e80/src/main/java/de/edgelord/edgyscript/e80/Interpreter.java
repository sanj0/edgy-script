package de.edgelord.edgyscript.e80;

import java.io.FileNotFoundException;

public class Interpreter {

    private static FunctionProvider sdk;

    static {
        try {
            sdk = Class.forName("de.edgelord.edgyscript.sdk.EdgySDK").asSubclass(FunctionProvider.class).newInstance();
        } catch (Exception e) {
            System.err.println("The edgy-sdk was not found. Either the given path does not point to it, or it isn't located in " + System.getProperty("user.home") + "/.edgy-script/bin/sdk/sdk.jar");
            e.printStackTrace();
        }
    }

    public static int lineNumber = 0;

    public static int interpretRun(ScriptFile script) throws FileNotFoundException {
        String[] lines = script.getLines();
        Lexer lexer = new Lexer(script);

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
                ScriptLine scriptLine = lexer.lexLine(i);
                execLine(scriptLine, script);
            }
        }

        return 0;
    }

    private static Variable execLine(ScriptLine line, ScriptFile context) {
        return sdk.function(line.getFunctionName(), line.getArgs(), context);
    }

    public static Variable eval(String line, ScriptFile context) {
        return execLine(Lexer.lexLine(line, context), context);
    }
}
