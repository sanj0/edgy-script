package de.edgelord.edgyscript.e80.main;

import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static boolean simpleStringMode = true;

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length > 0) {
            ScriptFile scriptFile = new ScriptFile(new File(args[0]));
            Interpreter.interpretRun(scriptFile);
        } else {
            System.out.println("Edgy-Script interactive mode. Type \"verbose:returnval\" to see the return value for every line. \nType \"exit\" to exit the program.");
            System.out.println("\n");
            System.out.print(">>>");

            ScriptFile context = ScriptFile.dummy();
            Scanner inputScanner = new Scanner(System.in);
            String line = inputScanner.nextLine();
            boolean verboseReturnVal = false;

            while (!line.equals("exit")) {
                if (line.toLowerCase().equals("verbose:returnval")) {
                    System.out.println("verbose set to returnval. Use \"verbose:none\" to disable.");
                    verboseReturnVal = true;
                } else if (line.toLowerCase().equals("verbose:none")) {
                    System.out.println("verbose set to none. Use \"verbose:returnval\" to switch.");
                    verboseReturnVal = true;
                } else {

                    Variable returnval = Interpreter.execLine(line, context);

                    if (verboseReturnVal) {
                        System.out.println("returnval: " + returnval.getName() + " : " + returnval.getString());
                    }
                }
                System.out.print(">>>");
                line = inputScanner.nextLine();
            }
        }
    }
}
