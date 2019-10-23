package de.edgelord.edgyscript.e80.script;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.Tokenizer;
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

        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine().trim();

            if (line.startsWith("#") || line.startsWith("//")) {
                continue;
            }

            if (line.trim().length() > 1) {

                List<Token> tokens = lexer.tokenize(line);
                Token functionName = tokens.get(0);
                lines.add(new ScriptLine(functionName, Tokenizer.evaluateTokens(tokens.subList(1, tokens.size()), Interpreter.isKeyWord(functionName.getValue()) || tokens.get(1).getValue().equals("="))));
            }
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
            Interpreter.run(line);
        }
    }

    public File getFile() {
        return scriptFile;
    }
}