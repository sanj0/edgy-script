package de.edgelord.edgyscript.e80.main;

import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        ScriptFile scriptFile = new ScriptFile(new File(args[0]));
        Interpreter.interpretRun(scriptFile);
    }
}
