package de.edgyscript.sdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import java.util.Scanner;

/**
 * Provided functions:
 *
 * - write (alias print, out)
 *     writes the first argument to the standard output. returns the written string
 *
 * - writeln (alias writeLine, println, printLine, outln, outLine)
 *     writes the first argument to the standard output and creates a newline. returns the written string
 *
 * - read (alias input, readLine)
 *     writes the first arg and assigns the given input (after ENTER was pressed) to the second arg. returns the var
 *
 * Example usage:
 *
 * write "hello, world!"
 * writeln "Hello World!"
 *
 * create name
 * writeln "What's your name?"
 * readLine and set name
 */
public class StdIO extends FunctionProvider {

    private static final Scanner inputScanner = new Scanner(System.in);

    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {
        if (name.equals("write") || name.equals("print") || name.equals("out")) {
            System.out.print(variables[0].getString());
            return variables[0];
        }

        if (name.equals("writeln") || name.equals("writeLine") || name.equals("println") || name.equals("printLine") || name.equals("outln") || name.equals("outLine")) {
            System.out.println(variables[0].getString());
            return variables[0];
        }

        if (name.equals("read") || name.equals("input") || name.equals("readLine")) {
            System.out.print(variables[0].getString());
            variables[1].setValue(inputScanner.nextLine());
            return variables[1];
        }

        return null;
    }
}
