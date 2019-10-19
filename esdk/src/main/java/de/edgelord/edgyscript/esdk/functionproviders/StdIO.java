package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.ScriptLine;
import de.edgelord.edgyscript.e80.Variable;

import java.util.Scanner;

/**
 * Provided functions:
 *
 * - write (alias print, out)
 *     writes the first argument to the standard output. returns the written string
 *
 * - writeln (alias writeLine, println, printLine, outln, outLine)
 *     writes all given arguments merged to the standard output and creates a newline. returns the written string
 *
 * - read (alias input, getinput, readLine)
 *     if there is an arg given, it writes its value out to teh screen and then waits for user input. returns the input
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
    public Variable function(ScriptLine line, String name, Variable[] variables, ScriptFile scriptFile) {
        if (name.equalsIgnoreCase("write") || name.equalsIgnoreCase("print") || name.equalsIgnoreCase("out")) {
            String s = getMergedArgs(variables);
            System.out.print(s);
            return new Variable(scriptFile.nextTempvar(), s);
        }

        if (name.equalsIgnoreCase("writeln") || name.equalsIgnoreCase("writeLine") || name.equalsIgnoreCase("println") || name.equalsIgnoreCase("printLine") || name.equalsIgnoreCase("outln") || name.equalsIgnoreCase("outLine")) {
            String s = getMergedArgs(variables);
            System.out.println(s);
            return new Variable(scriptFile.nextTempvar(), s);
        }

        if (name.equalsIgnoreCase("input") || name.equalsIgnoreCase("read") || name.equalsIgnoreCase("getinput") || name.equalsIgnoreCase("readLine")) {
            if (variables.length > 0) {
                System.out.print(variables[0].getString());
            }
            return new Variable(scriptFile.nextTempvar(), inputScanner.nextLine());
        }

        return null;
    }

    private static String getMergedArgs(Variable[] args) {
        StringBuilder mergedArgs = new StringBuilder();

        for (Variable arg : args) {
            mergedArgs.append(arg.getString());
        }

        return mergedArgs.toString();
    }
}
