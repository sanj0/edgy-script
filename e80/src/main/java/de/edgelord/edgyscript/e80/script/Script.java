package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Script {

    private File scriptFile;
    private List<ScriptLine> lines = new ArrayList<>();
    private Lexer lexer = new Lexer();

    public Script(String absolutePath) {
        scriptFile = new File(absolutePath);
        ScriptException.SCRIPT_PATH = absolutePath;
    }

    public void loadPreCompiled() {
        try {
            FileInputStream fileIn = new FileInputStream(scriptFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            lines = (ArrayList<ScriptLine>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * compiles the lines of the file into {@link ScriptLine}s.
     *
     * @throws FileNotFoundException when something went wrong with the file I/O
     */
    public void compile() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(scriptFile);
        boolean insideMultipleLineComment = false;
        int currentLineNumber = 0;
        StringBuilder lineBuilder = new StringBuilder();

        while (fileScanner.hasNext()) {
            currentLineNumber++;
            ScriptException.LINE_NUMBER = currentLineNumber;
            String line = fileScanner.nextLine().trim();

            if (insideMultipleLineComment) {
                if (line.startsWith("*/")) {
                    insideMultipleLineComment = false;
                }
                continue;
            } else {
                if (line.startsWith("#") || line.startsWith("//")) {
                    continue;
                } else if (line.startsWith("/*")) {
                    insideMultipleLineComment = true;
                    continue;
                }
            }

            if (line.length() > 1) {

                if (buildLine(lineBuilder, line)) {
                    List<Token> tokens = lexer.tokenize(lineBuilder.toString());
                    lines.add(new ScriptLine(tokens, currentLineNumber));
                    lineBuilder.setLength(0);
                }
            }
        }
    }

    private boolean buildLine(StringBuilder lineBuilder, String line) {
        if (line.endsWith(";")) {
            lineBuilder.append(line, 0, line.length() - 1);
            return true;
        } else {
            lineBuilder.append(line);
            return false;
        }
    }

    public void saveCompiledScript(String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(lines);
            out.close();
            fileOut.close();
            System.out.printf("Compiled script is saved in %s", path);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void run() {
        for (ScriptLine line : lines) {
            ScriptException.LINE_NUMBER = line.getLineNumber();
            Interpreter.run(line);
        }
    }

    public File getFile() {
        return scriptFile;
    }
}
