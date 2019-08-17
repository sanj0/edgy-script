package de.edgelord.edgyscript.e80;

import de.edgelord.edgyscript.e80.exceptions.VarNotFoundException;
import de.edgelord.edgyscript.e80.main.Main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private ScriptFile scriptFile;
    private String[] lines;

    public Lexer(ScriptFile scriptFile) throws FileNotFoundException {
        this.scriptFile = scriptFile;
        this.lines = scriptFile.getLines();
    }

    public static ScriptLine lexLine(String line, ScriptFile context) {
        String[] parts = line.split(" ", 2);

        // the function name is the first "word" of the line
        String functionName = parts[0];

        return new ScriptLine(functionName, parseVariables(parts[1], context, functionName.equals("create")).toArray(new Variable[0]));
    }

    public ScriptLine lexLine(int lineNumber) {
        String line = lines[lineNumber];

        return lexLine(line, scriptFile);
    }

    /**
     * Creates a list of {@link Variable}s from the given args.g
     * Example: parseVariables("314, \"hello, world\", myVar")
     *
     * @param args the args to convert to a Variable list
     * @return the list of variables read from the given function args
     */
    public static List<Variable> parseVariables(String args, ScriptFile scriptFile, boolean isCreateFunction) {

        char[] chars = args.toCharArray();

        List<Variable> vars = new ArrayList<>();

        StringBuilder currentToken = new StringBuilder();
        boolean valueMode;

        for (int i = 0; i < chars.length; i++) {
            char character = chars[i];

            // if the char is a , or a space, continue
            if (character == ' ' || character == ',') {
                continue;
            }

            // < -> start of a co-line
            if (character == '<') {
                boolean ignoreNextChar = false;
                StringBuilder subLine = new StringBuilder();
                character = chars[++i];
                int requiredClosedDiamonds = 1;
                while (requiredClosedDiamonds != 0 || ignoreNextChar) {
                    if (character == '\\') {
                        ignoreNextChar = true;
                        character = chars[++i];
                        continue;
                    }

                    if (character == '<') {
                        requiredClosedDiamonds++;
                    } else if (character == '>') {
                        requiredClosedDiamonds--;
                        if (requiredClosedDiamonds == 0) {
                            continue;
                        }
                    }
                    ignoreNextChar = false;
                    subLine.append(character);
                    character = chars[++i];
                }
                EvalVariable var = new EvalVariable(scriptFile.nextTempvar(), subLine.toString(), scriptFile);
                vars.add(var);
                continue;
            }

            // first case: " -> string
            if (character == '"') {
                valueMode = true;
                boolean ignoreNextChar = false;
                character = chars[++i];
                while (character != '"' || ignoreNextChar) {
                    if (character == '\\') {
                        ignoreNextChar = true;
                        character = chars[++i];
                        continue;
                    }
                    ignoreNextChar = false;
                    currentToken.append(character);
                    character = chars[++i];
                }

                // second case: digit (0-9) or . -> number
            } else if (Character.isDigit(character) || character == '.') {
                valueMode = true;
                i = getToken(chars, currentToken, i, character);

                // last case: a variable name
            } else {
                valueMode = false;
                i = getToken(chars, currentToken, i, character);
            }

            if (valueMode) {
                vars.add(new Variable(scriptFile.nextTempvar(), currentToken.toString()));
            } else {
                String varName = currentToken.toString();
                if (scriptFile.varExists(varName)) {
                    vars.add(scriptFile.getVar(varName));
                } else if (Main.simpleStringMode || isCreateFunction) {
                    vars.add(new Variable(scriptFile.nextTempvar(), varName));
                } else {
                    throw new VarNotFoundException(varName, scriptFile);
                }
            }

            currentToken = new StringBuilder();
        }

        return vars;
    }

    private static int getToken(char[] chars, StringBuilder currentToken, int i, char character) {
        while (character != ',' && character != ' ' && i + 1 < chars.length) {
            currentToken.append(character);
            character = chars[++i];
        }

        if (i + 1 == chars.length) {
            currentToken.append(chars[i]);
        }
        return i;
    }
}
