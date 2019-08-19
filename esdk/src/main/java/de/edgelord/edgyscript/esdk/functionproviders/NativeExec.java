package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.*;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides functions:
 *
 * - js (alisa javascript) evaluates the given js script and evaluates  it, returning its output/value <br>
 *
 * - feedjs (alias jsfeed) <br>
 *     - feeds the first given variable into the js engine and adds it to the {@link #jsFedVars list} <p>
 *
 * - exec (alias eval, do, execute)
 *     - executes the given line of Edgy Script code. returns the variable returned by the line
 *
 * Example usage: <br>
 * # do a simple math calculation, which could also be done using {@code math} <br>
 * createset number1 2 <br>
 * createset number2 4 <br>
 * feedjs number1
 * feedjs number2
 * js "number1 + number2"
 *
 * # the do (exec) command adds all given variables together
 * do writeLine hello
 */
public class NativeExec extends FunctionProvider {

    private static ScriptEngine javaScriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static ScriptContext javaScriptContext = javaScriptEngine.getContext();
    private static StringWriter javaScriptWriter = new StringWriter();
    static {
        javaScriptContext.setWriter(javaScriptWriter);
    }

    public static List<Variable> jsFedVars = new LinkedList<>();

    @Override
    public Variable function(ScriptLine line, String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("exec") || name.equalsIgnoreCase("eval")
                || name.equalsIgnoreCase("do") || name.equalsIgnoreCase("execute")) {
            StringBuilder cmdLine = new StringBuilder();
            for (Variable var : variables) {
                cmdLine.append(var.getString());
            }

            return Interpreter.execLine(cmdLine.toString(), scriptFile, false);
        }

        if (name.equalsIgnoreCase("js") || name.equalsIgnoreCase("javascript")) {

            StringBuilder command = new StringBuilder();
            for (Variable var : variables) {
                command.append(var.getString());
            }

            Object evalValue = null;
            try {
                evalValue = getJavaScriptEngine().eval(command.toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }

            if (evalValue == null) {
                return Variable.empty();
            } else {
                return new Variable(scriptFile.nextTempvar(), evalValue.toString());
            }
        }

        if (name.equalsIgnoreCase("feedjs") || name.equalsIgnoreCase("jsfeed")) {
            Variable var = variables[0];

            if (var.getString().equals("")) {
                try {
                    getJavaScriptEngine().eval("var " + var.getName());
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getJavaScriptEngine().eval("var " + var.getName() + " = " + var.getValueForJS());
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
            jsFedVars.add(var);
            return var;
        }
        return null;
    }

    /**
     * Gets {@link #javaScriptEngine}.
     *
     * @return the value of {@link #javaScriptEngine}
     */
    public static ScriptEngine getJavaScriptEngine() {
        return javaScriptEngine;
    }

    public static String getJavaScriptOutput() {
        String output = javaScriptWriter.toString();
        javaScriptWriter.getBuffer().setLength(0);
        javaScriptWriter.getBuffer().trimToSize();

        return output;
    }
}
