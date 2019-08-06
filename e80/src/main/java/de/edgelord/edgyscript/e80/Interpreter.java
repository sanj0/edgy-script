package de.edgelord.edgyscript.e80;

import java.io.FileNotFoundException;

public class Interpreter {

    private static FunctionProvider sdk;

    static {
        try {
            sdk = Class.forName("EdgySDK").asSubclass(FunctionProvider.class).newInstance();
        } catch (Exception e) {
            System.err.println("The edgy-sdk was not found. Either the given path does not point to it, or it isn't located in " + System.getProperty("user.home") + "/.edgy-script/bin/sdk/sdk.jar");
            e.printStackTrace();
        }
    }

    public static int lineNumber = 0;

    public static int interpretRun(ScriptFile script) throws FileNotFoundException {
        String[] lines = script.getLines();
        Lexer lexer = new Lexer(script);

        // lex lines into lexedLines
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            lineNumber++;

            // ignore comments (lines starting with either "#" or "//")
            // also, ignore blank lines
            if (!line.startsWith("#") && !line.startsWith("//") && !line.isEmpty()) {
                ScriptLine scriptLine = lexer.lexLine(i);
                sdk.function(scriptLine.getFunctionName(), scriptLine.getArgs(), script);
            }
        }

        return 0;
    }
}
